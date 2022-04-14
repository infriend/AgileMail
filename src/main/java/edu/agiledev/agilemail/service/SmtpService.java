package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.EmailAccount;

import java.io.File;

public interface SmtpService {
    void checkAccount(EmailAccount account);

    void sendMessage(String emailAddress, String subject, String content,
                     String toUser, String ccUser, String bccUser);
}
