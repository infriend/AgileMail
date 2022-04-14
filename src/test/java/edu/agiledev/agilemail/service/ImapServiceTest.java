package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void testNewDomainFolders() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        imapService.testNewDomainFolders(testAccount);
    }


}