package edu.agiledev.agilemail.service.impl;

import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.SupportDomain;
import edu.agiledev.agilemail.service.SmtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import static edu.agiledev.agilemail.exception.AuthenticationException.Type.SMTP;

/**
 * Description of class
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Service
@Slf4j
public class SmtpServiceImpl implements SmtpService {
    private JavaMailSender mailSender;

    @Autowired
    private Map<String, SupportDomain> domainMap;

    private final MailSSLSocketFactory mailSSLSocketFactory;

    private Session session;
    private Transport smtpTransport;

    @Autowired
    public SmtpServiceImpl(JavaMailSender mailSender, MailSSLSocketFactory mailSSLSocketFactory) {
        this.mailSender = mailSender;
        this.mailSSLSocketFactory = mailSSLSocketFactory;
    }


    public void sendMessage(EmailAccount emailAccount, String subject, String content,
                            String toUser, String ccUser, String bccUser, String attachments[]){
        //首先检查to的地址是否合法，接着配置mailsender，最后就发送
        JavaMailSender mailSender = getJavaMailSender(emailAccount);


        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // set from
            InternetAddress from = new InternetAddress(emailAccount.getUsername());
            message.setFrom(from);

            // set to
            if(null != toUser && !toUser.isEmpty()){
                InternetAddress[] internetAddressTo = new InternetAddress().parse(toUser);
                helper.setTo(internetAddressTo);
            }

            // set cc
            if(null != ccUser && !ccUser.isEmpty()){
                InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
                helper.setCc(internetAddressCC);
            }

            // set bcc
            if(null != bccUser && !bccUser.isEmpty()){
                InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
                helper.setBcc(internetAddressBCC);
            }

            // set date
            helper.setSentDate(new Date());

            // set subject
            helper.setSubject(subject);

            // set attachment
            if (null != attachments && attachments.length > 0){
                for (String file: attachments){
                    File temp = new File("../resources/attachments/"+ file);
                    helper.addAttachment(temp.getName(), temp);
                }
            }

/**
            for (File f: attachment){
                helper.addAttachments(f.getName(), f);
            } **/

            // set content
            helper.setText(content);

            mailSender.send(message);
            if (null != attachments && attachments.length > 0){
                for (String file: attachments){
                    File temp = new File("../resources/attachments/"+ file);
                    temp.delete();
                }
            }
        } catch (MessagingException e){
            throw new BaseException("send failed!");
        }
    }

    @Override
    public void saveToDraft(EmailAccount emailAccount, String subject, String content,
                            String toUser, String ccUser, String bccUser, String[] attachments) throws MessagingException, UnsupportedEncodingException {
        Session session = getSession(emailAccount);
        Store store = session.getStore("imap");
        store.connect("imap."+emailAccount.getDomain(), emailAccount.getUsername(), emailAccount.getPassword());

        Folder folder = (Folder) store.getFolder(domainMap.get(emailAccount.getDomain()).getDraft());

        MimeMessage message = new MimeMessage(session);

        InternetAddress from = new InternetAddress(emailAccount.getUsername());
        message.setFrom(from);

        if(null != toUser && !toUser.isEmpty()){
            InternetAddress[] internetAddressTo = new InternetAddress().parse(toUser);
            message.setRecipients(Message.RecipientType.TO, internetAddressTo);
        }

        if(null != ccUser && !ccUser.isEmpty()){
            InternetAddress[] internetAddressCC = new InternetAddress().parse(ccUser);
            message.setRecipients(Message.RecipientType.CC, internetAddressCC);
        }

        if(null != bccUser && !bccUser.isEmpty()){
            InternetAddress[] internetAddressBCC = new InternetAddress().parse(bccUser);
            message.setRecipients(Message.RecipientType.BCC, internetAddressBCC);
        }

        message.setSentDate(new Date());

        message.setSubject(subject);

        Multipart multipart = new MimeMultipart();

        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(content, "text/plain;charset=utf-8");
        multipart.addBodyPart(contentPart);

        BodyPart attachmentBodyPart = null;
        if (null != attachments && attachments.length != 0) {
            for (String f : attachments) {
                attachmentBodyPart = new MimeBodyPart();
                File file = new File("../resources/attachments/"+f);
                DataSource source = new FileDataSource(file);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(file.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
        }

        message.setContent(multipart);
        message.saveChanges();
        message.setFlag(Flags.Flag.DRAFT, true);
        MimeMessage draftMessages[] = {message};
        folder.appendMessages(draftMessages);

    }


    public void checkAccount(EmailAccount account) {
        try {
            getSmtpTransport(account);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new AuthenticationException(SMTP);
        }
    }

    private JavaMailSender getJavaMailSender(EmailAccount emailAccount)
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        String emailDomain = emailAccount.getDomain();
        Properties props = mailSender.getJavaMailProperties();
        switch (emailDomain){
            case "163.com":
                mailSender.setHost("smtp.163.com");
                mailSender.setPort(25);
                break;
            case "gmail.com":
                mailSender.setHost("smtp.gmail.com");
                mailSender.setPort(465);
                props.put("mail.smtp.ssl.enable", true);
                break;
            case "qq.com":
                mailSender.setHost("smtp.gmail.com");
                mailSender.setPort(993);
                props.put("mail.smtp.ssl.enable", true);
                break;
        }
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.default-encoding", "UTF-8");
        mailSender.setJavaMailProperties(props);

        mailSender.setUsername(emailAccount.getUsername());
        mailSender.setPassword(emailAccount.getPassword());

        return mailSender;
    }

    private Transport getSmtpTransport(EmailAccount account) throws MessagingException {
        if (smtpTransport == null) {
            smtpTransport = getSession(account).getTransport("smtp");
            final String smtpHost = "smtp." + account.getDomain();
            smtpTransport.connect(
                    smtpHost,
                    465,
                    account.getUsername(),
                    account.getPassword());
            log.debug("Opened new SMTP transport");
        }
        return smtpTransport;
    }

    private Session getSession(EmailAccount account) {
        if (session == null) {
            session = Session.getInstance(initMailProperties(mailSSLSocketFactory), null);
        }
        return session;
    }

    private static Properties initMailProperties(MailSSLSocketFactory socketFactory) {
        final Properties ret = new Properties();
        ret.put("mail.smtp.ssl.enable", true);
        ret.put("mail.smtp.connectiontimeout", 5000);
        ret.put("mail.smtp.ssl.socketFactory", socketFactory);
        ret.put("mail.smtp.starttls.enable", true);
        ret.put("mail.smtp.starttls.required", false);
        ret.put("mail.smtps.connectiontimeout", 5000);
        ret.put("mail.smtps.socketFactory", socketFactory);
        ret.put("mail.smtps.ssl.socketFactory", socketFactory);
        ret.put("mail.smtps.socketFactory.fallback", false);
        ret.put("mail.smtps.auth", true);
        return ret;
    }
}
