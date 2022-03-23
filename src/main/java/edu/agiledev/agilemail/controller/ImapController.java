package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.dto.DetailedInboxMessageDTO;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.ImapService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ImapController extends RBaseController {
    @Autowired
    ImapService imapService;

    @Autowired
    AccountMapper accountMapper;

    @GetMapping("/folder")
    public R<List<FolderVO>> getFolders(@RequestParam(value = "emailAddress") String emailAddress) {
        EmailAccount account = getCurrentAccount(emailAddress);
        List<FolderVO> res = imapService.getFolders(account);
        return success(res);
    }

    @GetMapping("/inbox/list")
    public R<List<CheckMessageVo>> getInboxList(@RequestParam(value = "emailAddress") String emailAddress) {
//        String emailAddress = emailAddressDto.getEmailAddress();
        EmailAccount account = getCurrentAccount(emailAddress);
        List<CheckMessageVo> res = imapService.getDefaultFolderMessages(account, FolderCategory.INBOX);
        return success(res);
    }

    @GetMapping("/inbox/detail")
    public R<DetailMessageVo> getDetailedInboxMessage(@RequestBody DetailedInboxMessageDTO detailedInboxMessageDto) {
        String emailAddress = detailedInboxMessageDto.getEmailAddress();
        EmailAccount account = getCurrentAccount(emailAddress);

        String folderId = detailedInboxMessageDto.getFolderId();
        Long msgUID = detailedInboxMessageDto.getMsgUID();

        DetailMessageVo res = imapService.getMessageInFolder(account, msgUID, EncodeUtil.toUrl(folderId));
        return success(res);
    }

    private EmailAccount getCurrentAccount(String emailAddress) {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        EmailAccount account = accountMapper.getUserEmailAccount(userId, emailAddress);
        return account;
    }

}

