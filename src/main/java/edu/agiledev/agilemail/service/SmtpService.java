package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.EmailAccount;

import javax.mail.MessagingException;
import javax.mail.URLName;
import java.io.File;
import java.io.UnsupportedEncodingException;

public interface SmtpService {
    void checkAccount(EmailAccount account);

    void sendMessage(EmailAccount emailAccount, String subject, String content,
                     String toUser, String ccUser, String bccUser, String[] attachments);

    void saveToDraft(EmailAccount emailAccount, String subject, String content,
                     String toUser, String ccUser, String bccUser, String[] attachments) throws MessagingException, UnsupportedEncodingException;

    void replyMessage(EmailAccount emailAccount, Long msgUid, URLName folderId,
                      String subject, String content, String toUser, String ccUser,
                      String bccUser, String[] attachments, boolean replyToAll);

    void sendDraftMessage(EmailAccount emailAccount, Long msgUid, URLName folderId,
                          String subject, String content, String toUser, String ccUser,
                          String bccUser, String[] attachments);
}
