package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.MsgReadService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReadController extends RBaseController {

    private final MsgReadService msgReadService;

    @Autowired
    public ReadController(MsgReadService msgReadService) {
        this.msgReadService = msgReadService;
    }

    @GetMapping("/folder")
    public R<List<FolderVO>> getFolders(@RequestParam(value = "emailAddress") String emailAddress) {
        EmailAccount account = getCurrentAccount(emailAddress);
        List<FolderVO> res = msgReadService.getFolders(account);
        return success(res);
    }

    @GetMapping("/{folderId}/list")
    public R<List<CheckMessageVo>> getMessageList(@PathVariable(value = "folderId") String folderId, @RequestParam(value = "emailAddress") String emailAddress) {
        EmailAccount account = getCurrentAccount(emailAddress);
        List<CheckMessageVo> res = msgReadService.fetchMessagesInFolder(account, EncodeUtil.toUrl(folderId));
        return success(res);
    }

    @GetMapping("/{folderId}/message/{messageUid}")
    public R<DetailMessageVo> getMessageDetail(@PathVariable(value = "folderId") String folderId,
                                               @PathVariable(value = "messageUid") Long messageUid,
                                               @RequestParam(value = "emailAddress") String emailAddress) {

        EmailAccount account = getCurrentAccount(emailAddress);
        DetailMessageVo res = msgReadService.getMessageInFolder(account, messageUid, EncodeUtil.toUrl(folderId));
        return success(res);
    }


}

