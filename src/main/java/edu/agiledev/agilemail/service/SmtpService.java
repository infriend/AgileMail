package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.EmailAccount;

public interface SmtpService {
    void checkAccount(EmailAccount account);
}
