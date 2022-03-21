package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.config.DefaultFolder;
import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.pojo.R;
import edu.agiledev.agilemail.pojo.dto.DetailedInboxMessageDto;
import edu.agiledev.agilemail.pojo.dto.EmailAddressDto;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.ImapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class ImapController extends RBaseController {
    @Autowired
    ImapService imapService;

    @Autowired
    AccountMapper accountMapper;

    @GetMapping("/inbox/list")
    public R<List<CheckMessageVo>> getInboxList(@RequestBody EmailAddressDto emailAddressDto)
            throws MessagingException, UnsupportedEncodingException {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        String emailAddress = emailAddressDto.getEmailAddress();
        EmailAccount account = accountMapper.getUserEmailAccount(userId, emailAddress);
        return success(imapService.getDefaultFolderMessages(account, DefaultFolder.INBOX));
    }

    @GetMapping("/inbox/detail")
    public R<DetailMessageVo> getDetailedInboxMessage(@RequestBody DetailedInboxMessageDto detailedInboxMessageDto)
            throws MessagingException, IOException {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        String emailAddress = detailedInboxMessageDto.getEmailAddress();
        EmailAccount account = accountMapper.getUserEmailAccount(userId, emailAddress);

        String inboxName = detailedInboxMessageDto.getInboxName();
        int msgNum = detailedInboxMessageDto.getMsgNum();

        return success(imapService.getMessageInFolder(account, msgNum, inboxName));
    }

}

