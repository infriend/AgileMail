package edu.agiledev.agilemail.controller;

import edu.agiledev.agilemail.pojo.dto.SendInfo;
import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.service.AddressService;
import edu.agiledev.agilemail.service.FileManageService;
import edu.agiledev.agilemail.service.SmtpService;
import edu.agiledev.agilemail.utils.EncodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

@RestController
@Slf4j
public class SmtpController extends RBaseController {
    @Autowired
    private SmtpService smtpService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private FileManageService fileManageService;

    @PostMapping("/message")
    public R<String> sendMessage(@RequestBody SendInfo sendInfo) throws AddressException {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        EmailAccount emailAccount = accountMapper.getUserEmailAccount(userId, sendInfo.getFrom());

        smtpService.sendMessage(
                emailAccount,
                sendInfo.getSubject(),
                sendInfo.getContent(),
                sendInfo.getToUser(),
                sendInfo.getCcUser(),
                sendInfo.getBccUser(),
                sendInfo.getAttachments()
        );

        String toUser = sendInfo.getToUser();

        if (null != toUser && !toUser.isEmpty()) {
            InternetAddress[] internetAddressTo = InternetAddress.parse(toUser);
            for (InternetAddress address : internetAddressTo) {
                String mailId = address.getAddress();
                if (!addressService.addressIsSaved(userId, mailId)) {
                    addressService.saveAddress(userId, mailId);
                }
            }
            return success("Successfully sent");
        } else {
            return error(ReturnCode.ERROR, "To address should NOT be empty");
        }

    }


    @PostMapping("/todraft")
    public R<String> saveToDraft(@RequestBody SendInfo sendInfo) throws MessagingException, UnsupportedEncodingException {
        EmailAccount emailAccount = getCurrentAccount(sendInfo.getFrom());
        smtpService.saveToDraft(
                emailAccount,
                sendInfo.getSubject(),
                sendInfo.getContent(),
                sendInfo.getToUser(),
                sendInfo.getCcUser(),
                sendInfo.getBccUser(),
                sendInfo.getAttachments()
        );
        return success("Draft saved!");
    }

    @PostMapping("/draftemail/{folderId}/{messageUid}")
    public R<String> sendDraftMail(@RequestBody SendInfo sendInfo,
                                   @PathVariable("folderId") String folderId,
                                   @PathVariable("messageUid") Long messageUid){
        EmailAccount emailAccount = getCurrentAccount(sendInfo.getFrom());
        smtpService.sendDraftMessage(
                emailAccount,
                messageUid,
                EncodeUtil.toUrl(folderId),
                sendInfo.getSubject(),
                sendInfo.getContent(),
                sendInfo.getToUser(),
                sendInfo.getCcUser(),
                sendInfo.getBccUser(),
                sendInfo.getAttachments()
        );

        return success("successfully sent message!");
    }

    @PostMapping("{folderId}/{messageUid}/replyto/{replyToAll}")
    public R<String> replyMessage(@PathVariable("folderId") String folderId,
                                  @PathVariable("messageUid") Long messageUid,
                                  @PathVariable("replyToAll") Boolean replyToAll,
                                  @RequestBody SendInfo sendInfo) {
        EmailAccount emailAccount = getCurrentAccount(sendInfo.getFrom());

        smtpService.replyMessage(
                emailAccount,
                messageUid,
                EncodeUtil.toUrl(folderId),
                sendInfo.getSubject(),
                sendInfo.getContent(),
                sendInfo.getToUser(),
                sendInfo.getCcUser(),
                sendInfo.getBccUser(),
                sendInfo.getAttachments(),
                replyToAll.equals(Boolean.TRUE)
        );

        return success();
    }

    @PostMapping("/attachment")
    public R<String> uploadFile(HttpServletRequest request) throws IOException {

        long startTime = System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMultipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if (multipartResolver.isMultipart(request)) {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            //获取multiRequest 中所有的文件名
            Iterator<String> iter = multiRequest.getFileNames();

            while (iter.hasNext()) {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if (file != null) {
                    fileManageService.saveAttachment(file.getOriginalFilename(), file.getInputStream());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("Time: " + (endTime - startTime) + "ms");

        return success("Uploading succeeded");
    }

    @GetMapping("/delete/{filename}")
    public R<String> deleteAttachment(@PathVariable String filename) {
        boolean res = fileManageService.deleteAttachment(filename);
        if (res) {
            return success("Attachment deleted!");
        } else {
            return error(ReturnCode.ERROR, "File not exist!");
        }
    }


}
