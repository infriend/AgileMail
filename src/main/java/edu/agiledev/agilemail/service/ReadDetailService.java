package edu.agiledev.agilemail.service;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import edu.agiledev.agilemail.pojo.message.AMessage;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Description of the class
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
public interface ReadDetailService {


    /**
     * 从给定{@link IMAPMessage}中读取邮件详情，映射到{@link AMessage}中，包括读取content
     *
     * @param folder      邮件所在的文件夹，要求是打开的
     * @param imapMessage 需要读取内容的邮件
     */
    AMessage readMessageDetail(IMAPFolder folder, IMAPMessage imapMessage) throws MessagingException, IOException;

}
