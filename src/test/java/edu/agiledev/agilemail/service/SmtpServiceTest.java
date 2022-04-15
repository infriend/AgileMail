package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@SpringBootTest
class SmtpServiceTest {

    @Autowired
    private SmtpService smtpService;

    @Autowired
    private TestConfiguration testConfig;


    @Test
    void checkAccount() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        smtpService.checkAccount(testAccount);
    }

    @Test
    void sendmail() {
        EmailAccount emailAccount = testConfig.getTestEmailAccount();
        smtpService.sendMessage(
                emailAccount,
                "testmessage",
                "This is a test message!",
                "infriendliu@gmail.com,mg21320011@smail.nju.edu.cn",
                "695520903@qq.com",
                "stive0118@sina.com",
                null
        );
    }

    @Test
    void saveToMail() throws MessagingException, UnsupportedEncodingException {
        EmailAccount emailAccount = testConfig.getTestEmailAccount();
        smtpService.saveToDraft(
                emailAccount,
                "testmessage",
                "This is a test message2!",
                "infriendliu@gmail.com,mg21320011@smail.nju.edu.cn",
                "695520903@qq.com",
                "stive0118@sina.com",
                null
        );
    }
}