package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.dto.ContactsDTO;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.EmailInfoVO;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.utils.EncryptionUtil;
import edu.agiledev.agilemail.utils.SnowFlakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    EncryptionUtil encryptionUtil;

    @Autowired
    SnowFlakeIdGenerator idGenerator;

    @Autowired
    TestConfiguration testConfig;

    @Test
    void loginUser() {
        String username = "user1";
        String correctPassword = "123456";
        Credentials credentials = accountService.loginUser(username, correctPassword);
        assertThat(credentials).isNotNull();
        String wrongPassword = "wrong";
        credentials = accountService.loginUser(username, wrongPassword);
        assertThat(credentials).isNull();
    }

    @Test
    void checkAccount() {
        EmailAccount account = testConfig.getTestEmailAccount();
        boolean success = accountService.checkAccount(account);
        assertThat(success).isTrue();
    }


    @Test
    void getAccountEmailList() {
        String userId = "1529235917448024071";
        List<EmailInfoVO> emailInfoList = accountService.getAccountEmailList(userId);
        assertThat(emailInfoList.size()).isEqualTo(1);
        assertThat(emailInfoList.get(0).getEmailAddress()).isEqualTo("sqltest1@test.com");
    }

    @Test
    @Transactional
    void addEmailAccount() {
        String userId = "1529235917448024072";
        EmailAccount newAccount = new EmailAccount();
        String emailId = "1552273494702690304";
        newAccount.setId(emailId);
        newAccount.setAddress("new@example.com");
        newAccount.setPassword("some password");
        newAccount.setDomain("example.com");
        boolean success = accountService.addEmailAccount(userId, newAccount);
        assertThat(success).isTrue();
    }

    @Test
    @Transactional
    void deleteEmailAccount() {
        String userId = "1529235917448024071";
        String emailAddress = "sqltest1@test.com";
        boolean success = accountService.deleteEmailAccount(userId, emailAddress);
        assertThat(success).isTrue();
    }

    @Test
    void getContacts() {

    }

    @Test
    void addContacts(){
        ContactsDTO contactsDTO = new ContactsDTO();
        contactsDTO.setContactEmail("695520903@qq.com");
        contactsDTO.setName("aaa");
        accountService.addContacts("1529235917448024071", contactsDTO);
    }

    @Test
    void deleteContacts(){
        accountService.deleteContacts("1552430393121181696");
    }
}