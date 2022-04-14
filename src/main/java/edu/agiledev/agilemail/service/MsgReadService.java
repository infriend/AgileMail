package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;

import javax.mail.URLName;
import java.util.List;

/**
 * Description of the class
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
     * TODO: add attachment, pictures.
     */
    DetailMessageVo getMessageInFolder(EmailAccount account, Long msgUid, URLName folderId);
}
