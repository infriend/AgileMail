package edu.agiledev.agilemail.controller;

import com.fasterxml.jackson.databind.node.POJONode;
import edu.agiledev.agilemail.mappers.AddressbookMapper;
import edu.agiledev.agilemail.pojo.dto.SendInfo;
import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.security.model.Credentials;
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
import java.util.List;

@RestController
@Slf4j
public class SmtpController extends RBaseController{
    @Autowired
    private SmtpService smtpService;

    @Autowired
    private AddressbookMapper addressbookMapper;

    @PostMapping("/attachment")
    public R<String> fileupload(HttpServletRequest request) throws IOException {

        long startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    String path="../resources/attachments"+file.getOriginalFilename();
                    //上传
                    file.transferTo(new File(path));
                }
            }
        }
        long  endTime=System.currentTimeMillis();
        System.out.println("Time: "+String.valueOf(endTime-startTime)+"ms");

        return success("upload succeed");
    }

    @PostMapping("/message")
    public R<String> sendMessage(@RequestBody SendInfo sendInfo) throws AddressException {
        EmailAccount emailAccount = getCurrentAccount(sendInfo.getFrom());
        smtpService.sendMessage(
                emailAccount,
                sendInfo.getSubject(),
                sendInfo.getContent(),
                sendInfo.getToUser(),
                sendInfo.getCcUser(),
                sendInfo.getBccUser(),
                sendInfo.getAttachments()
        );
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        String toUser = sendInfo.getToUser();

        if(null != toUser && !toUser.isEmpty()){
            InternetAddress[] internetAddressTo = new InternetAddress().parse(toUser);
            for (InternetAddress address: internetAddressTo){
                String mailId = address.getAddress();
                Addressbook addressbook = addressbookMapper.searchAddressByPrimarykey(new Addressbook(userId, mailId));
                if (addressbook == null){
                    addressbookMapper.insert(new Addressbook(userId, mailId));
                }
            }
        }


        return success("successfully send");
    }

    @GetMapping("/delete/{filename}")
    public R<String> deleteAttachment(@PathVariable String filename){
        File file = new File("../resources/attachments/"+filename);
        boolean res = file.delete();
        if (res){
            return success("attachment delete!");
        } else {
            return error(ReturnCode.ERROR, "file not exist!");
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
        return success("draft saved!");
    }

    @PostMapping("{folderId}/{messageUid}/replyto/{replyToAll}")
    public R<String> replyMessage(@PathVariable("folderID") String folderId,
                                   @PathVariable("messageUid") Long messageUid,
                                   @PathVariable("replyToAll") String toAll,
                                   @RequestBody SendInfo sendInfo){
        EmailAccount emailAccount = getCurrentAccount(sendInfo.getFrom());
        boolean replyToAll;
        if (toAll.equals("true")){
            replyToAll = true;
        } else {
            replyToAll = false;
        }
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
                replyToAll
                );

        return success();
    }


}
