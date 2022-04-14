package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.MsgModifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.URLName;
import java.util.List;

import static javax.mail.Folder.READ_WRITE;

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

    @Autowired
    public MsgModifyServiceImpl(ImapService imapService) {
        this.imapService = imapService;
    }

    @Override
    public FolderVO deleteMessages(EmailAccount account, URLName folderId, List<Long> messageIds) {
        IMAPStore store = imapService.getImapStore(account);

        try {
            final IMAPFolder folder = imapService.getFolder(store, folderId);
            folder.open(READ_WRITE);
            Message[] messages = folder.getMessagesByUID(messageIds.stream().mapToLong(Long::longValue).toArray());
            folder.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
            folder.expunge(messages);
            return FolderVO.from(AFolder.from(folder, true));
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.DELETE_MESSAGE_ERROR, "删除邮件时出错", e);
        }
    }
}
