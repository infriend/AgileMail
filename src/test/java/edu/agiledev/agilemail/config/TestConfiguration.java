package edu.agiledev.agilemail.config;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试相关配置
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/13
 */
@Configuration
@Getter
public class TestConfiguration {

    @Value("${test.email.username}")
    private String testEmailUsername;

    @Value("${test.email.password}")
    private String testEmailPassword;

    @Value("${test.email.domain}")
    private String testEmailDomain;

    private Map<String, Map<String, String>> accountFolderIdMap = null;

    public EmailAccount getTestEmailAccount() {
        return new EmailAccount(testEmailUsername, testEmailPassword, testEmailDomain);
    }

    public Map<String, String> getAccountFolderIdMap(EmailAccount account) {
        if (accountFolderIdMap == null) {
            accountFolderIdMap = new HashMap<>();
            Map<String, String> folderIdMap = new HashMap<>();
            folderIdMap.put("INBOX", "aW1hcDovL3N0YWxrZXIwMSU0MDE2My5jb21AaW1hcC4xNjMuY29tL0lOQk9Y");
            folderIdMap.put("SENT", "aW1hcDovL3N0YWxrZXIwMSU0MDE2My5jb21AaW1hcC4xNjMuY29tL-W3suWPkemAgQ==");
            folderIdMap.put("DRAFTS", "aW1hcDovL3N0YWxrZXIwMSU0MDE2My5jb21AaW1hcC4xNjMuY29tL-iNieeov-eusQ==");
            folderIdMap.put("TRASH", "aW1hcDovL3N0YWxrZXIwMSU0MDE2My5jb21AaW1hcC4xNjMuY29tL-W3suWIoOmZpA==");
            folderIdMap.put("JUNK", "aW1hcDovL3N0YWxrZXIwMSU0MDE2My5jb21AaW1hcC4xNjMuY29tL-Weg-WcvumCruS7tg==");

            accountFolderIdMap.put(testEmailUsername, folderIdMap);

        }

        return accountFolderIdMap.get(account.getUsername());
    }

}
