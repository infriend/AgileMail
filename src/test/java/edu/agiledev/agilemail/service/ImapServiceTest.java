package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.List;

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
    void getFolders() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        List<AFolder> f = imapService.getFolders(testAccount);
        for (AFolder fd : f)
            System.out.println(">> " + fd.getName());
    }

    @Test
    void getMessagesInFolder() throws MessagingException, IOException {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        List<CheckMessageVo> messages = imapService.getDefaultFolderMessages(testAccount, FolderCategory.INBOX);
        for (int i = 0, n = messages.size(); i < n; i++) {
            System.out.println(messages.get(i).getFrom());
        }
    }

    @Test
    void getMessage() throws MessagingException, IOException {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
//        DetailMessageVo m = imapService.getMessageInFolder(testAccount, 1, "INBOX");
//        System.out.println(m.getFrom());
    }
}