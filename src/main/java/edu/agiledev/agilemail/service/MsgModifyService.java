package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.FolderVO;

import javax.mail.URLName;
import java.util.List;

/**
 * 邮件修改服务接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/12
 */
public interface MsgModifyService {

    /**
     * 将邮件设为已读/未读
     */
    void setMessagesSeen(EmailAccount account, URLName folderId, List<Long> msgUids, boolean seen);

    /**
     * 将邮件设为已标记/未标记
     */
    void setMessagesFlagged(EmailAccount account, URLName folderId, List<Long> msgUids, boolean seen);

    /**
     * 移动邮件到新文件夹
     *
     * @return 移动邮件后的原文件夹
     */
    FolderVO moveMessages(EmailAccount account, URLName fromFolderId, URLName toFolderId, List<Long> msgUids);

    /**
     * 将邮件送入已删除文件夹
     *
     * @return 移动邮件后的原文件夹
     */
    FolderVO setMessagesTrash(EmailAccount account, URLName folderId, List<Long> msgUids);

    /**
     * 删除邮件
     *
     * @return 删除邮件后的原文件夹
     */
    FolderVO deleteMessages(EmailAccount account, URLName folderId, List<Long> msgUids);

}
