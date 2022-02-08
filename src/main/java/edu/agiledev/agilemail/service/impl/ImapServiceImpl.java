package edu.agiledev.agilemail.service.impl;

import com.sun.mail.imap.IMAPStore;
import com.sun.mail.util.MailSSLSocketFactory;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.service.ImapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceiver;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.util.Properties;

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

    @Autowired
    public ImapServiceImpl(MailSSLSocketFactory mailSSLSocketFactory) {
        this.mailSSLSocketFactory = mailSSLSocketFactory;
    }

    public void checkAccount(EmailAccount account) {
        try {
            getImapStore(account).getDefaultFolder();
        } catch (MessagingException e) {
            throw new AuthenticationException(IMAP);
        }
    }


    private void configReceiver(EmailAccount account) {
        receiver = new ImapMailReceiver(String.format("imap://%s:%s@imap.%s/INBOX", account.getUsername(), account.getPassword(), account.getDomain()));
    }

    IMAPStore getImapStore(EmailAccount account) throws MessagingException {
        if (imapStore == null) {
            final Session session = Session.getInstance(initMailProperties(mailSSLSocketFactory), null);
            imapStore = (IMAPStore) session.getStore("imap");
            imapStore.connect(
                    "imap." + account.getDomain(),
                    143,
                    account.getUsername(),
                    account.getPassword());
            log.debug("Opened new ImapStore session");
        }
        return imapStore;
    }

    private static Properties initMailProperties(MailSSLSocketFactory mailSSLSocketFactory) {
        final Properties prop = new Properties();
        prop.put("mail.imap.ssl.enable", false);
        prop.put("mail.imap.connectiontimeout", 5000);
        prop.put("mail.imap.connectionpooltimeout", 5000);
        prop.put("mail.imap.ssl.socketFactory", mailSSLSocketFactory);
        prop.put("mail.imap.starttls.enable", true);
        prop.put("mail.imap.starttls.required", false);
        prop.put("mail.imaps.socketFactory", mailSSLSocketFactory);
        prop.put("mail.imaps.socketFactory.fallback", false);
        prop.put("mail.imaps.ssl.socketFactory", mailSSLSocketFactory);
        return prop;
    }


}
