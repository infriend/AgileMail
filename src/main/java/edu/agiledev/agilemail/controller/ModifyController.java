package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.dto.MsgIdsDTO;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.vo.FolderVO;
import edu.agiledev.agilemail.service.MsgModifyService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 邮件操作请求Controller
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

    @PutMapping(path = "/{folderId}/messages/seen/{seen}")
    public R<String> setMessagesSeen(
            @PathVariable("folderId") String folderId,
            @PathVariable("seen") boolean seen,
            @RequestBody MsgIdsDTO msgIdsDTO) {

        EmailAccount account = getCurrentAccount(msgIdsDTO.getEmailAddress());
        msgModifyService.setMessagesSeen(account, EncodeUtil.toUrl(folderId), msgIdsDTO.getMsgIds(), seen);
        return success();
    }

    @PutMapping(path = "/{folderId}/messages/flagged/{flagged}")
    public R<String> setMessagesFlagged(
            @PathVariable("folderId") String folderId,
            @PathVariable("flagged") boolean flagged,
            @RequestBody MsgIdsDTO msgIdsDTO) {

        EmailAccount account = getCurrentAccount(msgIdsDTO.getEmailAddress());
        msgModifyService.setMessagesFlagged(account, EncodeUtil.toUrl(folderId), msgIdsDTO.getMsgIds(), flagged);
        return success();
    }

    @PutMapping(path = "/{folderId}/messages/folder/{toFolderId}")
    public R<FolderVO> moveMessages(
            @PathVariable("folderId") String fromFolderId,
            @PathVariable("toFolderId") String toFolderId,
            @RequestBody MsgIdsDTO msgIdsDTO) {

        EmailAccount account = getCurrentAccount(msgIdsDTO.getEmailAddress());
        FolderVO res = msgModifyService.moveMessages(account, EncodeUtil.toUrl(fromFolderId), EncodeUtil.toUrl(toFolderId), msgIdsDTO.getMsgIds());
        return success(res);
    }

    @PutMapping("/{folderId}/messages/trash")
    public R<FolderVO> setMessagesTrash(
            @PathVariable("folderId") String folderId,
            @RequestBody MsgIdsDTO msgIdsDTO) {

        EmailAccount account = getCurrentAccount(msgIdsDTO.getEmailAddress());
        FolderVO res = msgModifyService.setMessagesTrash(account, EncodeUtil.toUrl(folderId), msgIdsDTO.getMsgIds());
        return success(res);
    }

    @DeleteMapping("/{folderId}/messages")
    public R<FolderVO> deleteMessages(
            @PathVariable("folderId") String folderId,
            @RequestBody MsgIdsDTO msgIdsDTO) {

        EmailAccount account = getCurrentAccount(msgIdsDTO.getEmailAddress());
        FolderVO res = msgModifyService.deleteMessages(account, EncodeUtil.toUrl(folderId), msgIdsDTO.getMsgIds());
        return success(res);
    }
}
