package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;
import edu.agiledev.agilemail.pojo.vo.FolderVO;

import javax.mail.URLName;
import java.util.List;

/**
 * 邮件修改接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
public interface MsgModifyService {


    FolderVO deleteMessages(EmailAccount account, URLName folderId, List<Long> messageIds);

}
