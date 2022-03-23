package edu.agiledev.agilemail.pojo.vo;


import edu.agiledev.agilemail.pojo.message.AFolder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 文件夹VO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/23
 */
@Data
public class FolderVO {

    private String folderId;
    private String previousFolderId;
    private String name;
    private String category;
    private Long uid;

    private String fullName;
    private String fullURL;

    private int messageCount;
    private int newMessageCount;
    private int unreadMessageCount;
    private int deletedMessageCount;
    private List<FolderVO> children;

    public static FolderVO from(AFolder folder) {
        FolderVO res = new FolderVO();
        res.setFolderId(folder.getFolderId());
        res.setPreviousFolderId(folder.getPreviousFolderId());
        res.setName(folder.getName());
        res.setCategory(folder.getCategory().name());
        res.setUid(folder.getUIDValidity());
        res.setFullName(folder.getFullName());
        res.setFullURL(folder.getFullURL());
        res.setMessageCount(folder.getMessageCount());
        res.setNewMessageCount(folder.getNewMessageCount());
        res.setUnreadMessageCount(folder.getUnreadMessageCount());
        res.setDeletedMessageCount(folder.getDeletedMessageCount());

        res.children = Arrays.stream(folder.getChildren()).map(FolderVO::from).collect(Collectors.toList());

        return res;
    }
}
