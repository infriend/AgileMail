package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.pojo.message.AFolder;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.vo.CheckMessageVo;
import edu.agiledev.agilemail.pojo.vo.DetailMessageVo;

import javax.mail.URLName;
import java.util.List;

public interface ImapService {
    void checkAccount(EmailAccount account);

    /**
     * Get all folders of the email account.
     */
    List<AFolder> getFolders(EmailAccount account);

    /**
     * Get messages from the email inbox.
     */
    List<CheckMessageVo> getDefaultFolderMessages(EmailAccount account, FolderCategory folderName);

    /**
     * Get certain specific message from the selected folder.
     * TODO: add attachment, pictures.
     */
    DetailMessageVo getMessageInFolder(EmailAccount account, Long msgUID, URLName folderId);

    void deleteMessage(int msgNum, String folderName);

}
