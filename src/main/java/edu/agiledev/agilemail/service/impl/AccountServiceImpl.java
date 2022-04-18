package edu.agiledev.agilemail.service.impl;

import edu.agiledev.agilemail.config.ApplicationConfiguration;
import edu.agiledev.agilemail.exception.AuthenticationException;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.mappers.AddressbookMapper;
import edu.agiledev.agilemail.mappers.SysUserMapper;
import edu.agiledev.agilemail.pojo.dto.AccountDTO;
import edu.agiledev.agilemail.pojo.model.*;
import edu.agiledev.agilemail.pojo.vo.EmailInfoVO;
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
import java.util.stream.Collectors;

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
    SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Autowired
    AddressbookMapper addressbookMapper;

    @Autowired
    SysUserMapper sysUserMapper;

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


    @Override
    @Transactional
    public Credentials loginUser(String username, String password) {
        Account user = accountMapper.searchAccount(username);
        String encryptedPassword = encryptionUtil.encrypt(password);
        if (encryptedPassword.equals(user.getPassword())) {
            Credentials credentials = new Credentials();
            credentials.setUserId(user.getId());
            return credentials;
        } else {
            return null;
        }
//        accountMapper.insertAccount(accountId, account.getUsername(), encryptedPassword, account.getDomain());
//        accountMapper.relateAccount(userId, accountId);

//        credentials.setExpiryDate(ZonedDateTime.now(ZoneOffset.UTC).plus(appConfig.getCredentialsDuration()));


    }

    @Override
    public boolean checkAccount(EmailAccount account) {
        //异步检查，提高速度
        Runnable imapCheckR = () -> imapService.checkAccount(account);
        Future imapCheckF = checkExecutor.submit(imapCheckR);
        Runnable smtpCheckR = () -> smtpService.checkAccount(account);
        Future smtpCheckF = checkExecutor.submit(smtpCheckR);


        try {
            checkHost(account);
            imapCheckF.get();
            smtpCheckF.get();
            return true;
        } catch (AuthenticationException e) {
//            throw e;
            return false;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new BaseException(ReturnCode.CHECKING_ERROR, "发生运行错误");
        }
    }

    @Override
    public List<EmailInfoVO> getAccountEmailList(String userId) {
        List<EmailAccount> emailAccountList = accountMapper.searchAccountEmailList(userId);
        final List<EmailInfoVO> res = emailAccountList.stream().map(o -> new EmailInfoVO(o.getAddress(), o.getDomain())).collect(Collectors.toList());
        return res;
    }

    @Override
    @Transactional
    public boolean addEmailAccount(String userId, EmailAccount emailAccount) {
        String emailAccountId = idGenerator.nextIdStr();
        String encryptedPassword = encryptionUtil.encrypt(emailAccount.getPassword());
        int res = accountMapper.insertEmailAccount(emailAccountId, emailAccount.getAddress(), encryptedPassword, emailAccount.getDomain());
        if (res > 0) {
            res = accountMapper.relateAccount(userId, emailAccountId);
        }
        return res > 0;
    }

    @Override
    public boolean deleteEmailAccount(String userId, String emailAddress) {
        int res = accountMapper.deRelateAccount(userId, emailAddress);
        return res > 0;
    }

    @Override
    public List<Addressbook> getContacts(String userId) {
        return addressbookMapper.getContactByUserId(userId);
    }

    @Override
    public boolean addUser(AccountDTO accountDTO) {
        Account user = accountMapper.searchAccount(accountDTO.getUsername());
        if (user != null) {
            throw new BaseException("用户名重复");
        } else {
            String id = snowFlakeIdGenerator.nextIdStr();
            SysUser sysUser = new SysUser();
            sysUser.setId(id);
            sysUser.setPasswd(encryptionUtil.encrypt(accountDTO.getPassword()));
            sysUser.setDelFlag("0");

            sysUserMapper.insert(sysUser);
        }

        return true;
    }

    private void checkHost(EmailAccount account) {
        if (appConfig.getHostBlackList().contains(account.getDomain())) {
            throw new AuthenticationException(BLACKLISTED);
        }
    }


}
