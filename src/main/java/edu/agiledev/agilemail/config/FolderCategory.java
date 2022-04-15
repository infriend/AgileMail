package edu.agiledev.agilemail.config;

/**
 * 文件夹类别
 * 分为收件箱，发件箱，草稿箱，已删除，垃圾箱和其他邮箱
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/19
 */
public enum FolderCategory {
    INBOX("INBOX"), SENT("\\Sent"), DRAFTS("\\Drafts"), TRASH("\\Trash"), JUNK("\\Junk"), OTHER("Other");

    private final String label;

    public String getLabel() {
        return label;
    }

    private FolderCategory(String label) {
        this.label = label;
    }

    public static FolderCategory[] defaultCategory = new FolderCategory[]{
            SENT, DRAFTS, TRASH, JUNK
    };


}
