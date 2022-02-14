package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
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
     * @throws MessagingException,UnsupportedEncodingException
     */
    List<CheckMessageVo> getDefaultFolderMessages(EmailAccount account, String folderName)
            throws MessagingException, UnsupportedEncodingException;

    /**
     * Get certain specific message from the selected folder.
     * TODO: add attachment, pictures.
     * @param account
     * @param msgNum
     * @param folderName
     * @return
     * @throws MessagingException
     */
    DetailMessageVo getMessageInFolder(EmailAccount account, int msgNum, String folderName) throws MessagingException, IOException;

    void deleteMessage(int msgNum, String folderName);

}
