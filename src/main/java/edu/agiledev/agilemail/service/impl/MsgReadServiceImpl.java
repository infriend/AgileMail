package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.pojo.model.SupportDomain;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.MsgReadService;
import edu.agiledev.agilemail.service.ReadDetailService;
import edu.agiledev.agilemail.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.mail.Folder.READ_ONLY;

/**
 * 邮件读取接口实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
@Service
@Slf4j
public class MsgReadServiceImpl implements MsgReadService {

    private final ImapService imapService;

    private final ReadDetailService readDetailService;

    private Map<String, SupportDomain> domainMap;

    @Autowired
    public MsgReadServiceImpl(ImapService imapService, ReadDetailService readDetailService, Map<String, SupportDomain> domainMap) {
        this.imapService = imapService;
        this.readDetailService = readDetailService;
        this.domainMap = domainMap;
    }

    @Override
    public List<FolderVO> getFolders(EmailAccount account) {
        IMAPStore store = imapService.getImapStore(account);
        SupportDomain domainInfo = domainMap.get(account.getDomain());
        try {
            IMAPFolder root = (IMAPFolder) store.getDefaultFolder();
            List<AFolder> folders = Stream.of(root.list())
                    .map(IMAPFolder.class::cast)
                    .map(mf -> AFolder.from(mf, true))
                    .sorted(Comparator.comparing(AFolder::getName))
                    .collect(Collectors.toList());
            //找出INBOX
//            folders.stream().filter(o -> o.getName().contains(domainInfo.getInbox())).forEach(f -> f.setCategory(FolderCategory.INBOX));
            folders = Stream.concat(folders.stream().filter(o -> o.getUIDValidity() > 0),
                            folders.stream()
                                    .filter(o -> o.getUIDValidity() <= 0)
                                    .filter(o -> o.getChildren().length > 0)
                                    .flatMap(o -> Stream.of(o.getChildren())))
                    .collect(Collectors.toList());

            //设置category
            folders.forEach(o -> setCategory(o, domainInfo));

            return folders.stream().map(FolderVO::from).collect(Collectors.toList());
//            return folders;
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_FOLDER_ERROR, "imap: 读取文件夹失败", e);
        }
    }


    @Override
    public DetailMessageVo readMessage(EmailAccount account, Long msgUid, URLName folderId) {
        IMAPStore store = imapService.getImapStore(account);

        try (IMAPFolder folder = imapService.getFolder(store, folderId)) {
            folder.open(Folder.READ_ONLY);
            IMAPMessage imapMessage = (IMAPMessage) folder.getMessageByUID(msgUid);

            if (imapMessage == null) {
                throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, "找不到邮件");
            }

            try {
                AMessage message = readDetailService.readMessageDetail(folder, imapMessage);
                DetailMessageVo res = DetailMessageVo.from(message);
                return res;
            } catch (MessagingException | IOException e) {
                throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, String.format("imap: 从文件夹%s中读取邮件失败", folderId));
            }

        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, String.format("imap: 读取文件夹%s失败", folderId), e);
        }
    }

    @Override
    public String readAttachment(EmailAccount account, URLName folderId, Long msgUid, String aid, boolean isContentId, OutputStream outputStream) {
        IMAPStore store = imapService.getImapStore(account);

        try (IMAPFolder folder = imapService.getFolder(store, folderId)) {
            if (!folder.isOpen()) {
                folder.open(READ_ONLY);
            }
            IMAPMessage imapMessage = (IMAPMessage) folder.getMessageByUID(msgUid);
            Object content = imapMessage.getContent();

            if (content instanceof Multipart) {
                final BodyPart bp = MessageUtil.extractBodyPart((Multipart) content, aid, isContentId);
                if (bp != null) {
                    bp.getDataHandler().writeTo(outputStream);
                    outputStream.flush();
                    return bp.getContentType();

                } else {
                    throw new BaseException(ReturnCode.ATTACHMENT_NOT_FOUND, "找不到附件");
                }
            } else {
                throw new BaseException(ReturnCode.ATTACHMENT_NOT_FOUND, "找不到附件");
            }
        } catch (MessagingException | IOException e) {
            throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, "获取附件时出错", e);
        }
    }

    @Override
    public String downloadMessage(EmailAccount account, URLName folderId, Long msgUid, OutputStream outputStream) {
        IMAPStore store = imapService.getImapStore(account);

        try (final IMAPFolder folder = imapService.getFolder(store, folderId)) {
            if (!folder.isOpen()) {
                folder.open(READ_ONLY);
            }
            final IMAPMessage imapMessage = (IMAPMessage) folder.getMessageByUID(msgUid);
            imapMessage.writeTo(outputStream);
            String subject = imapMessage.getSubject();
            return String.format("attachment; filename=%s.eml", URLEncoder.encode(subject.length() <= 10 ? subject : subject.substring(0, 10), "UTF-8"));

        } catch (MessagingException | IOException e) {
            throw new BaseException(ReturnCode.DOWNLOAD_MESSAGE_ERROR, "下载邮件时出错", e);
        }
    }

    @Override
    public List<CheckMessageVo> fetchMessagesInFolder(EmailAccount account, URLName folderId) {

        IMAPStore store = imapService.getImapStore(account);
        List<AMessage> messages;

        try (IMAPFolder folder = imapService.getFolder(store, folderId)) {

            folder.open(Folder.READ_ONLY);
            try {
                messages = getAllMessagesInFolder(folder);
            } catch (MessagingException e) {
                throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, "imap: 读取邮件失败");
            }

        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, String.format("imap: 读取文件夹%s失败", folderId), e);
        }

        List<CheckMessageVo> checkMessageVos = new ArrayList<>();

        for (AMessage message : messages) {
            CheckMessageVo messageVo = CheckMessageVo.from(message);
            messageVo.setFromEmailAccount(account.getAddress());
            checkMessageVos.add(messageVo);
        }

        return checkMessageVos;

    }

    /**
     * 解析文件夹中所有的邮件
     *
     * @return list of AMessage
     */
    List<AMessage> getAllMessagesInFolder(IMAPFolder folder) throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        Integer end = folder.getMessageCount();
        List<AMessage> res = getMessages(folder, 1, end);

        return res;
    }

    /**
     * 解析文件夹中指定范围内的邮件
     *
     * @return list of AMessage
     */
    List<AMessage> getMessages(IMAPFolder folder, Integer start, Integer end) throws MessagingException {

        final Message[] messages;
        if (start != null && end != null) {
            // start / end message counts may no longer match, recalculate index if necessary
            if (end > folder.getMessageCount()) {
                start = folder.getMessageCount() - (end - start);
                end = folder.getMessageCount();
            }
            messages = folder.getMessages(start < 1 ? 1 : start, end);
        } else {
            messages = folder.getMessages();
        }
        MessageUtil.envelopeFetch(folder, messages);

        return Stream.of(messages)
                .map(m -> AMessage.from(folder, (IMAPMessage) m))
                .sorted(Comparator.comparingLong(AMessage::getUid).reversed())
                .collect(Collectors.toList());
    }

    private void setCategory(AFolder folder, SupportDomain domainInfo) {
        if (folder.getName().equals(domainInfo.getInbox())) {
            folder.setCategory(FolderCategory.INBOX);
        } else if (folder.getName().equals(domainInfo.getSent())) {
            folder.setCategory(FolderCategory.SENT);
        } else if (folder.getName().equals(domainInfo.getDraft())) {
            folder.setCategory(FolderCategory.DRAFTS);
        } else if (folder.getName().equals(domainInfo.getJunk())) {
            folder.setCategory(FolderCategory.JUNK);
        } else if (folder.getName().equals(domainInfo.getTrash())) {
            folder.setCategory(FolderCategory.TRASH);
        } else {
            folder.setCategory(FolderCategory.OTHER);
        }
    }


}
