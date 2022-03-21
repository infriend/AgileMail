package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.config.DefaultFolder;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.SupportDomain;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.service.ImapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Stream;

import static edu.agiledev.agilemail.exception.AuthenticationException.Type.IMAP;

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
    private final MailSSLSocketFactory mailSSLSocketFactory;

    private static Map<String, SupportDomain> domainMap = new HashMap();

    static {
        SupportDomain _163 = new SupportDomain("INBOX", "已发送", "草稿箱", "已删除");
        domainMap.put("163.com", _163);
    }

    @Autowired
    public ImapServiceImpl(MailSSLSocketFactory mailSSLSocketFactory) {
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
    public List<Folder> getFolders(EmailAccount account) throws MessagingException {
        IMAPStore store = getImapStore(account);
        Folder[] f = store.getDefaultFolder().list();
        List<Folder> res = Arrays.asList(f);

        return res;
    }

    @Override
    public List<CheckMessageVo> getDefaultFolderMessages(EmailAccount account, DefaultFolder folderName) throws MessagingException, UnsupportedEncodingException {
        IMAPStore store = getImapStore(account);
        SupportDomain domainInfo = domainMap.get(account.getDomain());
        IMAPFolder folder;
        switch (folderName) {
            case INBOX:
                folder = (IMAPFolder) store.getFolder(domainInfo.getInbox());
                break;
            case OUTBOX:
                folder = (IMAPFolder) store.getFolder(domainInfo.getOutbox());
                break;
            case DRAFT:
                folder = (IMAPFolder) store.getFolder(domainInfo.getDraft());
                break;
            case GARBAGE:
                folder = (IMAPFolder) store.getFolder(domainInfo.getGarbage());
                break;

            default:
                throw new BaseException("非默认文件夹!");
        }

        List<Message> messages = getAllMessagesInFolder(folder);

        List<CheckMessageVo> checkMessageVos = new ArrayList<>();

        for (Message message : messages) {
            MimeMessage m = (MimeMessage) message;

            String from = getFromAddress(m);
            String subject = m.getSubject();
            String fromEmailAccount = account.getUsername();
            int state = 0;
            if (message.getFlags().contains(Flags.Flag.SEEN)) {
                state = 1;
            }
            Date datetime = message.getSentDate();

            CheckMessageVo messageVo = new CheckMessageVo();
            messageVo.setFrom(from);
            messageVo.setSubject(subject);
            messageVo.setDatetime(datetime);
            messageVo.setFromEmailAccount(fromEmailAccount);
            messageVo.setState(state);
            checkMessageVos.add(messageVo);
        }
        folder.close();
        store.close();

        return checkMessageVos;
    }


    @Override
    public DetailMessageVo getMessageInFolder(EmailAccount account, int msgNum, String folderName) throws MessagingException, IOException {
        IMAPStore store = getImapStore(account);
        IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);

        folder.open(Folder.READ_ONLY);
        MimeMessage m = (MimeMessage) folder.getMessage(msgNum);

        String from = getFromAddress(m);
        String subject = m.getSubject();
        String to = account.getUsername();
        Date datetime = m.getSentDate();
        String content = getBody(m);

        DetailMessageVo detailMessageVo = new DetailMessageVo();
        detailMessageVo.setFrom(from);
        detailMessageVo.setSubject(subject);
        detailMessageVo.setTo(to);
        detailMessageVo.setDatetime(datetime);
        detailMessageVo.setContent(content);

        folder.close();

        return detailMessageVo;
    }

    @Override
    public void deleteMessage(int msgNum, String folderName) {

    }


    private void configReceiver(EmailAccount account) {
        receiver = new ImapMailReceiver(String.format("imap://%s:%s@imap.%s/INBOX", account.getUsername(), account.getPassword(), account.getDomain()));
    }

    IMAPStore getImapStore(EmailAccount account) throws MessagingException {
        if (imapStore == null) {
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


    List<Message> getAllMessagesInFolder(IMAPFolder folder) throws MessagingException {
        folder.open(Folder.READ_ONLY);
        Integer end = folder.getMessageCount();
        Message[] messages = folder.getMessages(1, end);

        return Arrays.asList(messages);
    }

    String getFromAddress(MimeMessage m) throws MessagingException, UnsupportedEncodingException {
        Address[] froms = m.getFrom();
        InternetAddress address = (InternetAddress) froms[0];
        String personal = address.getPersonal();
        String from = personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + " <" + address.getAddress() + ">");

        return from;
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
