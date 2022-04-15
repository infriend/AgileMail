package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.utils.EncodeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MsgModifyServiceTest {

    @Autowired
    TestConfiguration testConfig;

    @Autowired
    MsgModifyService msgModifyService;

    @Autowired
    MsgReadService msgReadService;

    @Test
    void setMessagesTrash() {
        EmailAccount testAccount = testConfig.getTestEmailAccount();
        Map<String, String> folderIdMap = testConfig.getAccountFolderIdMap(testAccount);
        String inboxId = folderIdMap.get("INBOX");
        List<CheckMessageVo> messages = msgReadService.fetchMessagesInFolder(testAccount, EncodeUtil.toUrl(inboxId));
        int before = messages.size();
        Long toDelete = messages.stream().filter(o -> o.getSubject().contains("Fujian")).findAny().orElseThrow(() -> new RuntimeException("无此邮件")).getUid();

        FolderVO folder = msgModifyService.setMessagesTrash(testAccount, EncodeUtil.toUrl(inboxId), Collections.singletonList(toDelete));
        int after = folder.getTotal();
        assertThat(after).isEqualTo(before - 1);

        String trashId = folderIdMap.get("TRASH");
        messages = msgReadService.fetchMessagesInFolder(testAccount, EncodeUtil.toUrl(trashId));
        Long toRecover = messages.stream().filter(o -> o.getSubject().contains("Fujian")).findAny().orElseThrow(() -> new RuntimeException("无此邮件")).getUid();
        msgModifyService.moveMessages(testAccount, EncodeUtil.toUrl(trashId), EncodeUtil.toUrl(inboxId), Collections.singletonList(toRecover));

        System.out.println("Recover uid equals with delete uid? " + toDelete.equals(toRecover));

//        messages = msgReadService.fetchMessagesInFolder(testAccount, EncodeUtil.toUrl(inboxId));
//        int recover = messages.size();
//        assertThat(recover).isEqualTo(after + 1);

    }
}