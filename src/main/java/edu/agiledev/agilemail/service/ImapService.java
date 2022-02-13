package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import edu.agiledev.agilemail.pojo.EmailAccount;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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
     * Get all messages from the selected folder.
     * @param folder
     * @return
     * @throws MessagingException
     */
    List<Message> getAllMessagesInFolder(IMAPFolder folder) throws MessagingException;
}
