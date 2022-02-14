package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.R;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.ImapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class ImapController extends RBaseController{
    @Autowired
    ImapService imapService;

    @Autowired
    AccountMapper accountMapper;

    @GetMapping("/inbox/list")
    public R<List<CheckMessageVo>> getInboxList(@RequestBody String emailAddress) throws MessagingException, UnsupportedEncodingException {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        EmailAccount account = accountMapper.getUserEmailAccount(userId);
        return success(imapService.getDefaultFolderMessages(account, "inbox"));
    }

}

