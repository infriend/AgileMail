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
 * 邮件读取服务类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
public interface MsgReadService {

    /**
     * Get all folders of the email account.
     */
    List<FolderVO> getFolders(EmailAccount account);

    List<CheckMessageVo> fetchMessagesInFolder(EmailAccount account, URLName folderId);

    /**
     * Get certain specific message from the selected folder.
     */
    DetailMessageVo getMessageInFolder(EmailAccount account, Long msgUid, URLName folderId);

    /**
     * 读取邮件附件，写入输出流中，返回contentType
     */
    String readAttachment(EmailAccount account, URLName folderId, Long msgUid, String aid, boolean isContentId, OutputStream outputStream);
}
