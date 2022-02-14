package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.R;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.ImapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImapController extends RBaseController{
    @Autowired
    ImapService imapService;

    @GetMapping("/inbox/list")
    public R<CheckMessageVo> getInboxList(@RequestBody String emailAddress){
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        return success();
    }

}

