package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.dto.EmailAddressAttachDTO;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.MsgReadService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.BodyPart;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
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
                                               @RequestBody String emailAddress) {

        EmailAccount account = getCurrentAccount(emailAddress);
        DetailMessageVo res = msgReadService.getMessageInFolder(account, messageUid, EncodeUtil.toUrl(folderId));
        return success(res);
    }

    @GetMapping(path = "/{folderId}/messages/{messageUid}/attachments/{aid}")
    public R<String> getAttachment(@PathVariable("folderId") String folderId,
                                   @PathVariable("messageUid") Long messageUid,
                                   @PathVariable("aid") String aid,
                                   @RequestBody EmailAddressAttachDTO emailAddressAttach,
                                   HttpServletResponse response) {

        EmailAccount account = getCurrentAccount(emailAddressAttach.getEmailAddress());
        final String contentType;
        try {
            contentType = msgReadService.readAttachment(account, EncodeUtil.toUrl(folderId), messageUid, aid,
                    emailAddressAttach.getIsContentId().equals(Boolean.TRUE), response.getOutputStream());
            response.setContentType(contentType);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(ReturnCode.IO_ERROR, "写入response时发生错误");
        }

        return success(null);
    }


}

