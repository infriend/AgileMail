package edu.agiledev.agilemail.mappers;

import edu.agiledev.agilemail.pojo.model.Account;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {

    @Autowired
    AccountMapper accountMapper;

    @Test
    void searchAccount() {
        Account exist = accountMapper.searchAccount("user1");
        assertThat(exist).isNotNull();
        Account notExist = accountMapper.searchAccount("nouser");
        assertThat(notExist).isNull();
    }

    @Test
    @Transactional
    void insertUserAccount() {
        int row = accountMapper.insertUserAccount("1529072975784054786", "newUser", "123456");
        assertThat(row).isEqualTo(1);
    }

    @Test
    @Transactional
    void insertEmailAccount() {
        int row = accountMapper.insertEmailAccount("1529078053739827211", "mapper@test.com", "mapperpassword", "test.com");
        assertThat(row).isEqualTo(1);
    }

    @Test
    @Transactional
    void searchAccountEmailList() {
        List<EmailAccount> res = accountMapper.searchAccountEmailList("1529235917448024071");
        assertThat(res.size()).isEqualTo(1);
        EmailAccount email = res.get(0);
        assertThat(email.getAddress()).isEqualTo("sqltest1@test.com");
    }

    @Test
    @Transactional
    void relateAccount() {
        int row = accountMapper.relateAccount("1529235917448024072", "1529078053739827202");
        assertThat(row).isEqualTo(1);
    }

    @Test
    @Transactional
    void deRelateAccount() {
        int row = accountMapper.deRelateAccount("1529235917448024071", "sqltest1@test.com");
        assertThat(row).isEqualTo(1);
        row = accountMapper.deRelateAccount("1529235917448024072", "sqltest2@test.com");
        assertThat(row).isEqualTo(0);
    }

    @Test
    @Transactional
    void deleteEmailAccount() {
        String emailAddress = "sqltest2@test.com";
        int res = accountMapper.deleteEmailAccount(emailAddress);
        assertThat(res).isEqualTo(1);
    }

    @Test
    void getUserEmailAccount() {
        String userId = "1529235917448024071";
        EmailAccount expected = new EmailAccount("sqltest1@test.com", "sqltest", "test.com");
        expected.setId("1529078053739827201");

        EmailAccount res = accountMapper.getUserEmailAccount(userId, "sqltest1@test.com");
        assertThat(res).isEqualTo(expected);
    }


}