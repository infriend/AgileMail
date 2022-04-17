package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.dto.EmailAccountDTO;
import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.dto.AccountDTO;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.AccountService;
import edu.agiledev.agilemail.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 账户相关请求Controller
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@RestController
@Slf4j
public class AccountController extends RBaseController {

    private final AccountService accountService;

    private final TokenProvider tokenProvider;

    private final EncryptionUtil encryptionUtil;

    @Autowired
    public AccountController(AccountService accountService, TokenProvider tokenProvider, EncryptionUtil encryptionUtil) {
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
        this.encryptionUtil = encryptionUtil;
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody AccountDTO accountDTO) {
//        EmailAccount account = new EmailAccount();
//        account.setUsername(accountDTO.getUsername());
//        account.setPassword(accountDTO.getPassword());
//        account.setDomain(accountDTO.getDomain() == null ? getDomain(accountDTO.getUsername()) : accountDTO.getDomain());

        Credentials credentials = authenticate(accountDTO.getUsername(), accountDTO.getPassword());
        if (credentials == null) {
            throw new BaseException(ReturnCode.LOGIN_FAILED, "用户名或者密码错误");
        }
        String token = tokenProvider.generateToken(credentials);
        return success(token);
    }


    @PostMapping("/account/email")
    public R<String> relateAccountEmail(@RequestBody EmailAccountDTO emailAccountDTO) {
        EmailAccount emailAccount = new EmailAccount();
        emailAccount.setUsername(emailAccountDTO.getEmailAddress());
        emailAccount.setPassword(emailAccountDTO.getPassword());
        emailAccount.setDomain(emailAccountDTO.getDomain());
        boolean success = accountService.checkAccount(emailAccount);
        if (success) {
            String userId = getCurrentUserId();
            success = accountService.addEmailAccount(userId, emailAccount);
        }
        return success ? success() : error();
    }

    @DeleteMapping("/account/email")
    public R<String> deRelateAccountEmail(@RequestBody String emailAddress) {
        String userId = getCurrentUserId();
        boolean success = accountService.deleteEmailAccount(userId, emailAddress);
        return success ? success() : error();
    }

    @GetMapping("/isOnline")
    public R<String> isOnline() {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        return success(String.format("Hello, %s", credentials.getUserId()));
    }

    @GetMapping("/contact")
    public R<List<Addressbook>> getContacts() {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        return success(accountService.getContacts(userId));
    }

    private Credentials authenticate(String username, String password) {
//        accountService.checkAccount(account);
        Credentials credentials = accountService.loginUser(username, password);
        if (credentials != null) {
            SecurityContextHolder.getContext().setAuthentication(credentials);
        }
        return credentials;
    }

    private String getDomain(String emailAddress) {
        return emailAddress.substring(emailAddress.indexOf('@') + 1);
    }

}
