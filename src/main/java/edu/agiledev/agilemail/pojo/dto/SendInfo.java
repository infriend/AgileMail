package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;

@Data
public class SendInfo {
    private String from;
    private String subject;
    private String content;
    private String toUser;
    private String ccUser;
    private String bccUser;
    private String[] attachments;
}
