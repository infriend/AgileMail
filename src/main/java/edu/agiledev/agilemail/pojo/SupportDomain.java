package edu.agiledev.agilemail.pojo;

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
    private final String outbox;
    private final String draft;
    private final String garbage;

    public SupportDomain(String inbox, String outbox, String draft, String garbage) {
        this.inbox = inbox;
        this.outbox = outbox;
        this.draft = draft;
        this.garbage = garbage;
    }
}
