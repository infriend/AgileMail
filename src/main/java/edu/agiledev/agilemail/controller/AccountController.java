package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.dto.EmailAccountDTO;
import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public AccountController(AccountService accountService, TokenProvider tokenProvider) {
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody EmailAccountDTO accountDTO) {
        EmailAccount account = new EmailAccount();
        account.setUsername(accountDTO.getEmailAddress());
        account.setPassword(accountDTO.getPassword());
        account.setDomain(accountDTO.getDomain() == null ? getDomain(accountDTO.getEmailAddress()) : accountDTO.getDomain());

        Credentials credentials = authenticate(account);
        String token = tokenProvider.generateToken(credentials);
        return success(token);
    }

    @GetMapping("/isOnline")
    public R<String> isOnline() {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        return success(String.format("Hello, %s", credentials.getUserId()));
    }

    @GetMapping("/contact")
    public R<List<Addressbook>> getContacts(){
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        return success(accountService.getContacts(userId));
    }

    private Credentials authenticate(EmailAccount account) {
        accountService.checkAccount(account);
        Credentials credentials = accountService.registerUser(account);
        if (credentials != null) {
            SecurityContextHolder.getContext().setAuthentication(credentials);
        }
        return credentials;
    }

    private String getDomain(String emailAddress) {
        return emailAddress.substring(emailAddress.indexOf('@') + 1);
    }

}
