package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.EmailAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Folder;
import javax.mail.MessagingException;

import java.util.List;

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

    @Test
    void getFolders() throws MessagingException {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        List<Folder> f = imapService.getFolders(testAccount);
        for(Folder fd:f)
            System.out.println(">> "+fd.getName());
    }
}