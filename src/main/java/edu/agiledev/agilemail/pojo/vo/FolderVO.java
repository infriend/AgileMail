package edu.agiledev.agilemail.pojo.vo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.agiledev.agilemail.pojo.message.AFolder;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @JsonIgnore
    private String fullName;
    @JsonIgnore
    private String fullURL;

    private int total;
    private int recent;
    private int unread;
    private int deleted;
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
        res.setTotal(folder.getMessageCount());
        res.setRecent(folder.getRecentCount());
        res.setUnread(folder.getUnreadCount());
        res.setDeleted(folder.getDeletedCount());

        res.children = Arrays.stream(folder.getChildren()).map(FolderVO::from).collect(Collectors.toList());

        return res;
    }
}
