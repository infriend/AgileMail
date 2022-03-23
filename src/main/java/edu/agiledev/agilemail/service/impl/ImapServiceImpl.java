package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.message.AMessage;
import edu.agiledev.agilemail.pojo.message.Attachment;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.pojo.model.SupportDomain;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.MessageReadService;
import edu.agiledev.agilemail.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.agiledev.agilemail.exception.AuthenticationException.Type.IMAP;
import static edu.agiledev.agilemail.utils.MessageUtil.*;
import static javax.mail.Folder.READ_ONLY;

/**
 * imap服务实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Service
@Slf4j
public class ImapServiceImpl implements ImapService {
    private MailReceiver receiver;

    private IMAPStore imapStore;
    private final MessageReadService messageReadService;
    private final MailSSLSocketFactory mailSSLSocketFactory;


    static final String IMAP_CAPABILITY_CONDSTORE = "CONDSTORE";

    private static Map<String, SupportDomain> domainMap = new HashMap();

    static {
        SupportDomain _163 = new SupportDomain("INBOX", "已发送", "草稿箱", "已删除", "垃圾箱");
        domainMap.put("163.com", _163);
    }

    @Autowired
    public ImapServiceImpl(MessageReadService messageReadService, MailSSLSocketFactory mailSSLSocketFactory) {
        this.messageReadService = messageReadService;
        this.mailSSLSocketFactory = mailSSLSocketFactory;
    }

    @Override
    public void checkAccount(EmailAccount account) {
        try {
            getImapStore(account).getDefaultFolder();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new AuthenticationException(IMAP);
        }
    }

    @Override
    public List<FolderVO> getFolders(EmailAccount account) {
        IMAPStore store = getImapStore(account);
        SupportDomain domainInfo = domainMap.get(account.getDomain());
        try {
            IMAPFolder root = (IMAPFolder) store.getDefaultFolder();
            List<AFolder> folders = Stream.of(root.list())
                    .map(IMAPFolder.class::cast)
                    .map(mf -> AFolder.from(mf, true))
                    .sorted(Comparator.comparing(AFolder::getName))
                    .collect(Collectors.toList());
            //找出INBOX
            folders.stream().filter(o -> o.getName().contains(domainInfo.getInbox())).forEach(f -> f.setCategory(FolderCategory.INBOX));

            return folders.stream().map(FolderVO::from).collect(Collectors.toList());
//            return folders;
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_FOLDER_ERROR, "imap: 读取文件夹失败", e);
        }
    }

    @Override
    public List<CheckMessageVo> getDefaultFolderMessages(EmailAccount account, FolderCategory folderName) {
        IMAPStore store = getImapStore(account);
        SupportDomain domainInfo = domainMap.get(account.getDomain());
        IMAPFolder folder;
        try {
            switch (folderName) {
                case INBOX:
                    folder = (IMAPFolder) store.getFolder(domainInfo.getInbox());
                    break;
                case SENT:
                    folder = (IMAPFolder) store.getFolder(domainInfo.getSent());
                    break;
                case DRAFTS:
                    folder = (IMAPFolder) store.getFolder(domainInfo.getDraft());
                    break;
                case TRASH:
                    folder = (IMAPFolder) store.getFolder(domainInfo.getTrash());
                    break;
                case JUNK:
                    folder = (IMAPFolder) store.getFolder(domainInfo.getJunk());
                    break;

                default:
                    throw new BaseException("非默认文件夹!");
            }
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_FOLDER_ERROR, "imap: 文件夹读取失败", e);
        }


        List<AMessage> messages;
        try {
            messages = getAllMessagesInFolder(folder);
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, "imap: 邮件读取失败", e);
        }

        List<CheckMessageVo> checkMessageVos = new ArrayList<>();

        for (AMessage message : messages) {
            Date datetime = Date.from(message.getReceivedDate().toInstant());

            CheckMessageVo messageVo = new CheckMessageVo();
            messageVo.setFrom(message.getFrom());
            messageVo.setReplyTo(message.getReplyTo());
            messageVo.setRecipients(message.getRecipients());
            messageVo.setSubject(message.getSubject());

            messageVo.setDatetime(datetime);

            messageVo.setFlagged(message.getFlagged());
            messageVo.setRecent(message.getRecent());
            messageVo.setSeen(message.getSeen());
            messageVo.setDeleted(message.getDeleted());

            messageVo.setFromEmailAccount(account.getUsername());
            checkMessageVos.add(messageVo);
        }

        try {
            folder.close();
            store.close();
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_CONNECTION_ERROR, "imap: 连接错误", e);
        }

        return checkMessageVos;
    }


    @Override
    public DetailMessageVo getMessageInFolder(EmailAccount account, Long msgUID, URLName folderId) {

        try {
            IMAPStore store = getImapStore(account);
            IMAPFolder folder = (IMAPFolder) store.getFolder(folderId);
            folder.open(Folder.READ_ONLY);
            IMAPMessage imapMessage = (IMAPMessage) folder.getMessageByUID(msgUID);

            if (imapMessage == null) {
                folder.close();
                throw new BaseException(ReturnCode.IMAP_MESSAGE_ERROR, "找不到邮件");
            }

            AMessage message = messageReadService.readMessageDetail(folder, imapMessage);
            DetailMessageVo res = DetailMessageVo.from(message);
            folder.close();
            store.close();
            return res;
        } catch (MessagingException | IOException e) {
//            log.error("Error loading messages for folder: " + folderId.getRef(), e);
            throw new BaseException(ReturnCode.IMAP_CONNECTION_ERROR, String.format("从文件夹%s中读取邮件时产生错误", folderId), e);
        }
    }

    @Override
    public void deleteMessage(int msgNum, String folderName) {

    }

    List<AMessage> getAllMessagesInFolder(IMAPFolder folder) throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        Integer end = folder.getMessageCount();
        List<AMessage> res = getMessages(folder, 1, end);

        return res;
    }

    List<AMessage> getMessages(IMAPFolder folder, Integer start, Integer end) throws MessagingException {

        if (!folder.isOpen()) {
            folder.open(READ_ONLY);
        }
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


    private void configReceiver(EmailAccount account) {
        receiver = new ImapMailReceiver(String.format("imap://%s:%s@imap.%s/INBOX", account.getUsername(), account.getPassword(), account.getDomain()));
    }

    IMAPStore getImapStore(EmailAccount account) {
        if (imapStore == null) {
            try {
                final Session session = Session.getInstance(initMailProperties(mailSSLSocketFactory, account.getDomain()), null);
                imapStore = (IMAPStore) session.getStore("imap");
                imapStore.connect(account.getUsername(), account.getPassword());

                HashMap IAM = new HashMap();
                //带上IMAP ID信息，由key和value组成，例如name，version，vendor，support-email等。
                IAM.put("name", "myname");
                IAM.put("version", "1.0.0");
                IAM.put("vendor", "myclient");
                imapStore.id(IAM);

                log.debug("Opened new ImapStore session");
            } catch (MessagingException e) {
                throw new BaseException(ReturnCode.IMAP_CONNECTION_ERROR, "imap: 连接失败", e);
            }
        }
        return imapStore;
    }

    String getBody(Part part) throws MessagingException, IOException {

        if (part.isMimeType("text/*")) {
            // Part是文本:
            return part.getContent().toString();
        }
        if (part.isMimeType("multipart/*")) {
            // Part是一个Multipart对象:
            Multipart multipart = (Multipart) part.getContent();
            // 循环解析每个子Part:
            for (int i = 0; i < multipart.getCount(); i++) {

                BodyPart bodyPart = multipart.getBodyPart(i);
                String body = getBody(bodyPart);
                if (!body.isEmpty()) {

                    return body;
                }
            }
        }
        return "";
    }

    String getFromAddress(MimeMessage m) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = m.getFrom();
        InternetAddress address = (InternetAddress) froms[0];
        String personal = address.getPersonal();
        String from = personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");

        return from;
    }


    /**
     * Method to resolve the proper name of a folder from an {@link URLName}.
     *
     * <p>{@link URLName#URLName(String)} truncates anything behind a # symbol and treats it as a ref.
     *
     * <p>This is a problem in {@link javax.mail.Store#getFolder(URLName)} as any folder with a # in its name
     * will not be loaded properly.
     *
     * <p>This method can be used to resolve the complete folder name before passing it to the
     * {@link javax.mail.Store#getFolder(String)} method so the proper folder gets loaded.
     *
     * @param folder URLName to get a safe proper name from
     * @return the name of the folder including the ref if applicable
     */
    public static String getFileWithRef(@NonNull URLName folder) {
        return String.format("%s%s", folder.getFile(), folder.getRef() == null ? "" : "#" + folder.getRef());
    }

    private static Properties initMailProperties(MailSSLSocketFactory mailSSLSocketFactory, String domain) {
        final Properties prop = new Properties();
        switch (domain) {
            case "gmail.com":
                prop.put("mail.imap.ssl.enable", true);
                prop.put("mail.imap.connectiontimeout", 5000);
                prop.put("mail.imap.connectionpooltimeout", 5000);
                prop.put("mail.imap.ssl.socketFactory", mailSSLSocketFactory);
                prop.put("mail.imap.starttls.enable", true);
                prop.put("mail.imap.starttls.required", false);
                prop.put("mail.imaps.socketFactory", mailSSLSocketFactory);
                prop.put("mail.imaps.socketFactory.fallback", false);
                prop.put("mail.imaps.ssl.socketFactory", mailSSLSocketFactory);
                prop.put("mail.store.protocol", "imap");
                prop.put("mail.imap.port", "993");
                prop.put("mail.imap.host", "imap.gmail.com");
                break;
            case "163.com":
                prop.setProperty("mail.store.protocol", "imap");
                prop.setProperty("mail.imap.host", "imap.163.com");
                prop.setProperty("mail.imap.port", "143");
                break;
            default:
                throw new BaseException("未识别的邮箱！");
        }

        return prop;
    }


}
