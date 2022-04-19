package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.agiledev.agilemail.exception.AuthenticationException.Type.IMAP;

/**
 * imap服务类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Service
@Slf4j
public class ImapService {
    private MailReceiver receiver;

    private IMAPStore imapStore;
    private final MailSSLSocketFactory mailSSLSocketFactory;


    static final String IMAP_CAPABILITY_CONDSTORE = "CONDSTORE";


    @Autowired
    public ImapService(MailSSLSocketFactory mailSSLSocketFactory) {
        this.mailSSLSocketFactory = mailSSLSocketFactory;
    }

    /**
     * 检查账户是否可连通imap服务器
     */
    public void checkAccount(EmailAccount account) {
        try {
            getImapStore(account).getDefaultFolder();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new AuthenticationException(IMAP);
        }
    }


    /**
     * 获取账户的{@link IMAPStore} 对象
     *
     * @return 已连通的Store对象
     */
    public IMAPStore getImapStore(EmailAccount account) {
        if (imapStore == null || !imapStore.isConnected()) {
            newImapStore(account);
        }
        return imapStore;
    }

    public IMAPStore newImapStore(EmailAccount account) {
        try {
            if (imapStore != null && imapStore.isConnected()) {
                imapStore.close();
            }
            final Session session = Session.getInstance(initMailProperties(mailSSLSocketFactory, account.getDomain()), null);
            imapStore = (IMAPStore) session.getStore("imap");
            imapStore.connect(account.getAddress(), account.getPassword());

            HashMap IAM = new HashMap();
            //带上IMAP ID信息，由key和value组成，例如name，version，vendor，support-email等。
            IAM.put("name", "njuagile");
            IAM.put("version", "1.0.0");
            IAM.put("vendor", "test_client");
            imapStore.id(IAM);

            log.debug("Opened new ImapStore session");
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_CONNECTION_ERROR, "imap: 连接失败", e);
        }
        return imapStore;
    }


    /**
     * 返回存在并且关闭的folder
     */
    public IMAPFolder getFolder(IMAPStore store, URLName folderId) {
        return getFolder(store, folderId.getFile());
    }

    public IMAPFolder getFolder(IMAPStore store, String folderName) {
        try {
            IMAPFolder folder = (IMAPFolder) store.getFolder(folderName);
            if (!folder.exists()) {
                throw new BaseException(ReturnCode.IMAP_FOLDER_ERROR, String.format("imap: 文件夹 %s 不存在", folderName));
            }
            return folder;
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_FOLDER_ERROR, "imap: 文件夹读取失败", e);
        }
    }

    public void testNewDomainFolders(EmailAccount account) {
        IMAPStore store = getImapStore(account);
        try {
            IMAPFolder root = (IMAPFolder) store.getDefaultFolder();
            List<AFolder> folders = Stream.of(root.list())
                    .map(IMAPFolder.class::cast)
                    .map(mf -> AFolder.from(mf, true))
                    .sorted(Comparator.comparing(AFolder::getName))
                    .collect(Collectors.toList());

            for (AFolder fd : folders) {
                log.info((String.format("sub:%s\tdate:%s\tfrom:%s", fd.getFolderId(), fd.getName(), fd.getCategory(), fd.getFullURL(), fd.getUIDValidity())));
            }

//            return folders;
        } catch (MessagingException e) {
            throw new BaseException(ReturnCode.IMAP_FOLDER_ERROR, "imap: 读取文件夹失败", e);
        }
    }

    public static Message[] getMassagesByUid(IMAPFolder folder, List<Long> msgUids) throws MessagingException {
        Message[] res = Stream.of(folder.getMessagesByUID(msgUids.stream().mapToLong(Long::longValue).toArray()))
                .filter(Objects::nonNull)
                .toArray(javax.mail.Message[]::new);
        return res;
    }


    private void configReceiver(EmailAccount account) {
        receiver = new ImapMailReceiver(String.format("imap://%s:%s@imap.%s/INBOX", account.getAddress(), account.getPassword(), account.getDomain()));
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
            case "qq.com":
                prop.put("mail.imap.ssl.enable", true);
                prop.setProperty("mail.store.protocol", "imap");
                prop.setProperty("mail.imap.host", "imap.qq.com");
                prop.setProperty("mail.imap.port", "993");
                break;
            case "smail.nju.edu.cn":
                prop.put("mail.imap.ssl.enable", true);
                prop.setProperty("mail.store.protocol", "imap");
                prop.setProperty("mail.imap.host", "imap.exmail.qq.com");
                prop.setProperty("mail.imap.port", "993");
                break;
            default:
                throw new BaseException("未识别的邮箱！");
        }

        return prop;
    }


}
