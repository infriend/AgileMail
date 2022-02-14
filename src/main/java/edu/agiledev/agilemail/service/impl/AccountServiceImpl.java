package edu.agiledev.agilemail.service.impl;

import edu.agiledev.agilemail.config.ApplicationConfiguration;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.AccountService;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.SmtpService;
import edu.agiledev.agilemail.utils.EncryptionUtil;
import edu.agiledev.agilemail.utils.SnowFlakeIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static edu.agiledev.agilemail.exception.AuthenticationException.Type.BLACKLISTED;

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
    private final AccountMapper accountMapper;
    private final SnowFlakeIdGenerator idGenerator;
    private final EncryptionUtil encryptionUtil;


    @Autowired
    public AccountServiceImpl(ImapService imapService,
                              SmtpService smtpService,
                              ApplicationConfiguration appConfig,
                              AccountMapper accountMapper,
                              SnowFlakeIdGenerator idGenerator,
                              EncryptionUtil encryptionUtil) {
        this.imapService = imapService;
        this.smtpService = smtpService;
        this.appConfig = appConfig;
        this.accountMapper = accountMapper;
        this.idGenerator = idGenerator;
        this.encryptionUtil = encryptionUtil;
    }


    public void checkAccount(EmailAccount account) {
        try {
            checkHost(account);
            imapService.checkAccount(account);
            smtpService.checkAccount(account);
        } catch (AuthenticationException e) {
            throw e;
        }
    }

    @Override
    @Transactional
    public Credentials registerUser(EmailAccount account) {
        String userId = idGenerator.nextIdStr();
        String accountId = idGenerator.nextIdStr();
        accountMapper.insertUser(userId);
        String encryptedPassword = encryptionUtil.encrypt(account.getPassword());
        accountMapper.insertAccount(accountId, account.getUsername(), encryptedPassword, account.getDomain());
        accountMapper.relateAccount(userId, accountId);
        Credentials credentials = new Credentials();
        credentials.setUserId(userId);
//        credentials.setExpiryDate(ZonedDateTime.now(ZoneOffset.UTC).plus(appConfig.getCredentialsDuration()));
        return credentials;

    }

    private void checkHost(EmailAccount account) {
        if (appConfig.getHostBlackList().contains(account.getDomain())) {
            throw new AuthenticationException(BLACKLISTED);
        }
    }


}
