package edu.agiledev.agilemail.service.impl;

import edu.agiledev.agilemail.config.ApplicationConfiguration;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.mappers.AddressbookMapper;
import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.AccountService;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.service.SmtpService;
import edu.agiledev.agilemail.utils.EncryptionUtil;
import edu.agiledev.agilemail.utils.SnowFlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static edu.agiledev.agilemail.exception.AuthenticationException.Type.BLACKLISTED;

/**
 * 账户服务实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final ImapService imapService;
    private final SmtpService smtpService;
    private final ApplicationConfiguration appConfig;
    private final AccountMapper accountMapper;
    private final SnowFlakeIdGenerator idGenerator;
    private final EncryptionUtil encryptionUtil;
    private final ExecutorService checkExecutor;

    @Autowired
    AddressbookMapper addressbookMapper;

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
        this.checkExecutor = Executors.newFixedThreadPool(2);
    }


    public void checkAccount(EmailAccount account) {
        Runnable imapCheckR = () -> imapService.checkAccount(account);
        Future imapCheckF = checkExecutor.submit(imapCheckR);
        Runnable smtpCheckR = () -> smtpService.checkAccount(account);
        Future smtpCheckF = checkExecutor.submit(smtpCheckR);


        try {
            checkHost(account);
//            imapService.checkAccount(account);
//            smtpService.checkAccount(account);
            imapCheckF.get();
            smtpCheckF.get();
        } catch (AuthenticationException e) {
            throw e;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new BaseException(ReturnCode.CHECKING_ERROR, "发生运行错误");
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

    @Override
    public List<Addressbook> getContacts(String userId) {
        return addressbookMapper.getContactByUserId(userId);
    }

    private void checkHost(EmailAccount account) {
        if (appConfig.getHostBlackList().contains(account.getDomain())) {
            throw new AuthenticationException(BLACKLISTED);
        }
    }


}
