package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;

@Data
public class DetailedInboxMessageDto {
    private String emailAddress;
    private int msgNum;
    private String inboxName;
}
