package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.EmailAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ImapServiceTest {

    @Autowired
    ImapService imapService;

    @Autowired
    TestConfiguration testConfig;

    @Test
    void checkAccount() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        imapService.checkAccount(testAccount);
    }
}