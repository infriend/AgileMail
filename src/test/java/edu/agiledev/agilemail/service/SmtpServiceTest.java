package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

//    @Test
//    void sendmail() {
//        smtpService.sendMessage(
//                "stalker01@163.com",
//                "testmessage",
//                "This is a test message!",
//                "infriendliu@gmail.com",
//                "stive0118@sina.com",
//                "695520903@qq.com"
//        );
//    }
}