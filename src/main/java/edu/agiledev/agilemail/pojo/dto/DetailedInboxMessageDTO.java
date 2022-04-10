package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;

@Data
public class DetailedInboxMessageDTO {
    private String emailAddress;
    private Long msgUID;
    private String folderId;
}
