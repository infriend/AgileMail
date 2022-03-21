package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.DefaultFolder;
import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
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

    @Test
    void getMessagesInFolder() throws MessagingException, IOException {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        List<CheckMessageVo> messages = imapService.getDefaultFolderMessages(testAccount, DefaultFolder.INBOX);
        for (int i = 0, n = messages.size(); i < n; i++) {
            System.out.println(messages.get(i).getFrom());
        }
    }

    @Test
    void getMessage() throws MessagingException, IOException {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        DetailMessageVo m = imapService.getMessageInFolder(testAccount, 1, "INBOX");
        System.out.println(m.getFrom());
    }
}