package edu.agiledev.agilemail.service.impl;

import edu.agiledev.agilemail.config.ApplicationConfiguration;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.service.AccountService;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.SmtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 账户服务实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final ImapService imapService;
    private final SmtpService smtpService;
    private final ApplicationConfiguration appConfig;

    @Autowired
    public AccountServiceImpl(ImapService imapService,
                              SmtpService smtpService,
                              ApplicationConfiguration appConfig) {
        this.imapService = imapService;
        this.smtpService = smtpService;
        this.appConfig = appConfig;
    }

    public Credentials checkAccount(EmailAccount account) {
        try {
            checkHost(account);
            imapService.checkAccount(account);
            smtpService.checkAccount(account);
            return null;
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    @Override
    public void registerUser(EmailAccount account) {
        //TODO：实现注册用户罗技
    }

    private void checkHost(EmailAccount account) {
        //TODO：实现检查邮箱域名逻辑
    }


}
