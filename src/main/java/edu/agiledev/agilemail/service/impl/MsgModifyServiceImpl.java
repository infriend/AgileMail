package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.pojo.model.SupportDomain;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.MsgModifyService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import edu.agiledev.agilemail.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.mail.Folder.READ_ONLY;
import static javax.mail.Folder.READ_WRITE;
import static edu.agiledev.agilemail.service.ImapService.getMassagesByUid;

/**
 * MessageModifyService实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
@Service
@Slf4j
public class MsgModifyServiceImpl implements MsgModifyService {

    ImapService imapService;

    Map<String, SupportDomain> domainMap;

    @Autowired
    public MsgModifyServiceImpl(ImapService imapService, Map<String, SupportDomain> domainMap) {
        this.imapService = imapService;
        this.domainMap = domainMap;
    }

    @Override
    public FolderVO moveMessages(EmailAccount account, URLName fromFolderId, URLName toFolderId, List<Long> msgUids) {
        IMAPStore store = imapService.getImapStore(account);


        try {
            final IMAPFolder fromFolder = imapService.getFolder(store, fromFolderId);
            final IMAPFolder toFolder = imapService.getFolder(store, toFolderId);

//            toFolder.open(READ_ONLY);
//            long toFolderNextUID = toFolder.getUIDNext();
//            toFolder.close(false);

            fromFolder.open(READ_WRITE);

            //复制到新文件夹，删除原有邮件
            final Message[] messagesToMove = Stream.of(getMassagesByUid(fromFolder, msgUids))
                    .filter(m -> !m.isExpunged())
                    .toArray(Message[]::new);
            if (messagesToMove.length > 0) {
                fromFolder.copyMessages(messagesToMove, toFolder);
                for (Message m : messagesToMove) {
                    m.setFlag(Flags.Flag.DELETED, true);
                }
            }

            fromFolder.close(true);

            //读取新文件夹的邮件
//            toFolder.open(READ_ONLY);
            //复制需要时间，可能需要等待
//            Message[] newMessages;
//            int retries = 5;
//            final long sleepTimeMillis = 100L;
//            while ((newMessages = toFolder.getMessagesByUID(toFolderNextUID, UIDFolder.LASTUID)).length == 0
//                    && retries-- > 0) {
//                Thread.sleep(sleepTimeMillis);
//            }
//            MessageUtil.envelopeFetch(toFolder, newMessages);

            final long sleepTimeMillis = 100L;
            Thread.sleep(sleepTimeMillis);

            fromFolder.open(READ_ONLY);
            FolderVO res = FolderVO.from(AFolder.from(fromFolder, true));
            fromFolder.close(true);
            return res;

        } catch (InterruptedException | MessagingException ex) {
            Thread.currentThread().interrupt();
            throw new BaseException(ReturnCode.MOVE_MESSAGE_ERROR, String.format("从%s向%s移动邮件时发生错误", fromFolderId, toFolderId), ex);
        }
    }

    @Override
    public FolderVO setMessagesTrash(EmailAccount account, URLName folderId, List<Long> msgUids) {
        IMAPStore store = imapService.getImapStore(account);
        URLName trashId;

        try {
            //本质是移动到trash文件夹，需要应用中有对应domain的配置信息
            final Folder trashFolder = store.getFolder(domainMap.get(account.getDomain()).getTrash());
            trashId = trashFolder.getURLName();
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.DELETE_MESSAGE_ERROR, "删除邮件时出错", e);
        }

        FolderVO res = moveMessages(account, folderId, trashId, msgUids);
        return res;
    }


    @Override
    public FolderVO deleteMessages(EmailAccount account, URLName folderId, List<Long> msgUids) {
        IMAPStore store = imapService.getImapStore(account);

        try (final IMAPFolder folder = imapService.getFolder(store, folderId)) {
            folder.open(READ_WRITE);
            Message[] messages = folder.getMessagesByUID(msgUids.stream().mapToLong(Long::longValue).toArray());
            folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
            folder.expunge(messages);
            return FolderVO.from(AFolder.from(folder, true));
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.DELETE_MESSAGE_ERROR, "删除邮件时出错", e);
        }
    }
}
