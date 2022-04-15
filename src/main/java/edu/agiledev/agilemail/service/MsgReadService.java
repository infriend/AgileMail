package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;

import javax.mail.BodyPart;
import javax.mail.URLName;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * 邮件读取服务接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
public interface MsgReadService {

    /**
     * 获取账户下所有的文件夹
     */
    List<FolderVO> getFolders(EmailAccount account);

    /**
     * 读取文件夹中的邮件信息
     */
    List<CheckMessageVo> fetchMessagesInFolder(EmailAccount account, URLName folderId);

    /**
     * 读取邮件详细信息
     */
    DetailMessageVo readMessage(EmailAccount account, Long msgUid, URLName folderId);

    /**
     * 读取邮件附件，写入输出流中，返回contentType
     */
    String readAttachment(EmailAccount account, URLName folderId, Long msgUid, String aid, boolean isContentId, OutputStream outputStream);

    /**
     * 下载邮件，返回用于response的Content-Disposition header
     */
    String downloadMessage(EmailAccount account, URLName folderId, Long msgUid, OutputStream outputStream);
}
