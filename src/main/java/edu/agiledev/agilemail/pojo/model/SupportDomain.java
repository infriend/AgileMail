package edu.agiledev.agilemail.pojo.model;

import lombok.Getter;

/**
 * Domain文件夹描述类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/19
 */
@Getter
public class SupportDomain {
    private final String inbox;
    private final String sent;
    private final String draft;
    private final String trash;
    private final String junk;

    public SupportDomain(String inbox, String sent, String draft, String trash, String junk) {
        this.inbox = inbox;
        this.sent = sent;
        this.draft = draft;
        this.trash = trash;
        this.junk = junk;
    }
}
