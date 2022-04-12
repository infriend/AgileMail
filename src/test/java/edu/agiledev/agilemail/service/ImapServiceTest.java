package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.SupportDomain;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.utils.EncodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<FolderVO> f = imapService.getFolders(testAccount);
        for (FolderVO fd : f)
            System.out.printf(">> %s %s %s %s %s\n", fd.getFolderId(), fd.getName(), fd.getCategory(), fd.getFullURL(), fd.getUid().toString());
    }

    @Test
    void fetchMessagesInFolder() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        Map<String, String> folderIdMap = testConfig.getAccountFolderIdMap(testAccount);
        String inboxId = folderIdMap.get("INBOX");
        List<CheckMessageVo> messages = imapService.fetchMessagesInFolder(testAccount, EncodeUtil.toUrl(inboxId));
        for (CheckMessageVo m : messages) {
            System.out.printf("sub:%s\tdate:%s\tfrom:%s\n", m.getSubject(), dateFormat().format(m.getDatetime()), m.getFrom().get(0));
        }
    }

    @Test
    void getMessageInFolder() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        Map<String, String> folderIdMap = testConfig.getAccountFolderIdMap(testAccount);
        String inboxId = folderIdMap.get("INBOX");
        List<CheckMessageVo> messages = imapService.fetchMessagesInFolder(testAccount, EncodeUtil.toUrl(inboxId));
        CheckMessageVo target = messages.get(0);
        DetailMessageVo m = imapService.getMessageInFolder(testAccount, target.getUid(), EncodeUtil.toUrl(inboxId));
        System.out.println(m.getContent());
    }


    SimpleDateFormat dateFormat() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return new SimpleDateFormat(pattern);
    }


}