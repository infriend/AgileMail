package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ImapService {
    void checkAccount(EmailAccount account);

    /**
     * Get all folders of the email account.
     * @param account
     * @return
     * @throws MessagingException
     */
    List<Folder> getFolders(EmailAccount account) throws MessagingException;

    /**
     * Get messages from the email inbox.
     * @param account
     * @return
     * @throws MessagingException
     */
    List<CheckMessageVo> getInboxMessages(EmailAccount account) throws MessagingException, UnsupportedEncodingException;
}
