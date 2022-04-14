package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.MsgModifyService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 邮件操作Controller
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
@RestController
@Slf4j
public class ModifyController extends RBaseController {

    private final MsgModifyService msgModifyService;

    @Autowired
    public ModifyController(MsgModifyService messageModifyService) {
        this.msgModifyService = messageModifyService;
    }

    @DeleteMapping("/{folderId}/messages")
    public R<FolderVO> deleteMessages(
            @PathVariable("folderId") String folderId,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("msgIds") List<Long> messageIds) {

        EmailAccount account = getCurrentAccount(emailAddress);
        FolderVO res = msgModifyService.deleteMessages(account, EncodeUtil.toUrl(folderId), messageIds);
        return success(res);
    }
}
