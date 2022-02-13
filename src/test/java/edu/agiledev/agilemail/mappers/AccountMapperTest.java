package edu.agiledev.agilemail.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AccountMapperTest {

    @Autowired
    AccountMapper accountMapper;

    @Test
    @Transactional
    void insertUser() {
        int row = accountMapper.insertUser("1529072975784054786");
        assertThat(row).isEqualTo(1);
    }

    @Test
    @Transactional
    void insertAccount() {
        int row = accountMapper.insertAccount("1529078053739827211", "mapper@test.com", "mapperpassword", "test.com");
        assertThat(row).isEqualTo(1);
    }

    @Test
    @Transactional
    void relateAccount() {
        int row = accountMapper.relateAccount("1529235917448024072", "1529078053739827202");
        assertThat(row).isEqualTo(1);
    }
}