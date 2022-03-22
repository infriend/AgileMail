package edu.agiledev.agilemail.pojo.message;

import com.sun.mail.imap.IMAPFolder;
import edu.agiledev.agilemail.config.FolderCategory;
import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.utils.EncodeUtil;
import lombok.Data;

import javax.mail.MessagingException;
import javax.mail.URLName;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

import static javax.mail.Folder.HOLDS_MESSAGES;

/**
 * Folder模型
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
@Data
public class AFolder {

    private static final String ATTR_HAS_NO_CHILDREN = "\\HasNoChildren";
    private static final AFolder[] EMPTY_FOLDERS = {};

    private String folderId;
    // Used when folder renaming to store previous folderId in order to identificate in FE
    private String previousFolderId;
    private String name;
    private FolderCategory category;
    private char separator;
    private Long UIDValidity;
    private String fullName;
    private String fullURL;
    private Set<String> attributes;
    private int messageCount;
    private int newMessageCount;
    private int unreadMessageCount;
    private int deletedMessageCount;
    private AFolder[] children;


    /**
     * 将{@link IMAPFolder}映射为{@link AFolder}。
     *
     * @param mailFolder   待映射文件夹
     * @param loadChildren 是否迭代映射子文件夹
     */
    public static AFolder from(IMAPFolder mailFolder, boolean loadChildren) {
        final AFolder res;
        if (mailFolder != null) {
            res = new AFolder();
            res.setName(mailFolder.getName());
            try {
                res.setFolderId(EncodeUtil.toBase64Id(mailFolder.getURLName()));
                res.setSeparator(mailFolder.getSeparator());
                res.setFullName(mailFolder.getFullName());
                res.setFullURL(mailFolder.getURLName().toString());
                res.setAttributes(new HashSet<>(Arrays.asList(mailFolder.getAttributes())));

                //根据attribute对文件夹类型进行分类
                Stream.of(FolderCategory.defaultCategory)
                        .forEach(c ->
                                res.getAttributes().stream()
                                        .filter(o -> c.getLabel().equals(o))
                                        .findAny()
                                        .ifPresent(o -> res.setCategory(c))
                        );
                if (res.getCategory() == null) {
                    res.setCategory(FolderCategory.OTHER);
                }


                if ((mailFolder.getType() & HOLDS_MESSAGES) != 0) {
                    res.setUIDValidity(mailFolder.getUIDValidity());
                    res.setMessageCount(mailFolder.getMessageCount());
                    res.setNewMessageCount(mailFolder.getNewMessageCount());
                    res.setUnreadMessageCount(mailFolder.getUnreadMessageCount());
                    res.setDeletedMessageCount(mailFolder.getDeletedMessageCount());
                    if (res.getDeletedMessageCount() < 0)
                        res.setDeletedMessageCount(0);
                }
                if (loadChildren && !res.getAttributes().contains(ATTR_HAS_NO_CHILDREN)) {
                    res.setChildren(Stream.of(mailFolder.list())
                            .map(IMAPFolder.class::cast)
                            .map(mf -> from(mf, true))
                            .sorted(Comparator.comparing(AFolder::getName))
                            .toArray(AFolder[]::new));
                } else {
                    res.setChildren(EMPTY_FOLDERS);
                }
            } catch (MessagingException e) {
                //TODO: 错误码
                throw new BaseException("Error parsing IMAP Folder", e);
            }
        } else {
            res = null;
        }
        return res;
    }


}
