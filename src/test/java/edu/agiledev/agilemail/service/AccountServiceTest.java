package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.TestConfiguration;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    TestConfiguration testConfig;

    @Test
    void checkAccount() {
        EmailAccount account = testConfig.getTestEmailAccount();
        accountService.checkAccount(account);
    }

    @Test
    void registerUser() {
    }
}