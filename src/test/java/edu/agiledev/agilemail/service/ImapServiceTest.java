package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.SupportDomain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ImapServiceTest {

    @Autowired
    ImapService imapService;

    @Autowired
    Map<String, SupportDomain> supportDomainMap;

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

    @Test
    void getNamedFolder() throws MessagingException {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        SupportDomain domain = supportDomainMap.get(testAccount.getDomain());
        IMAPStore store = imapService.getImapStore(testAccount);
        IMAPFolder folder = (IMAPFolder) store.getFolder(domain.getTrash());
        assertThat(folder.exists()).isTrue();

    }


}