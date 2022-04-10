package edu.agiledev.agilemail.service.impl;

import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.service.SmtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
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

    private final JavaMailSender mailSender;
    private final MailSSLSocketFactory mailSSLSocketFactory;

    private Session session;
    private Transport smtpTransport;

    @Autowired
    public SmtpServiceImpl(JavaMailSender mailSender, MailSSLSocketFactory mailSSLSocketFactory) {
        this.mailSender = mailSender;
        this.mailSSLSocketFactory = mailSSLSocketFactory;
    }

    public void checkAccount(EmailAccount account) {
        try {
            getSmtpTransport(account);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new AuthenticationException(SMTP);
        }
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
