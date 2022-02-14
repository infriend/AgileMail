package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.R;
import edu.agiledev.agilemail.security.TokenProvider;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Account controller
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@RestController
public class AccountController extends RBaseController {

    private final AccountService accountService;

    private final TokenProvider tokenProvider;

    @Autowired
    public AccountController(AccountService accountService, TokenProvider tokenProvider) {
        this.accountService = accountService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody EmailAccount emailAccount) {
        Credentials credentials = authenticate(emailAccount);
        String token = tokenProvider.generateToken(credentials);
        return success(token);
    }

    private Credentials authenticate(EmailAccount account) {
        accountService.checkAccount(account);
        Credentials credentials = accountService.registerUser(account);
        if (credentials != null) {
            SecurityContextHolder.getContext().setAuthentication(credentials);
        }
        return credentials;
    }

}
