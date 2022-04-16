package edu.agiledev.agilemail.pojo.model;

import lombok.Data;

@Data
public class Addressbook {
    private String contactEmail;

    private String id;

    public Addressbook(String userId, String mailId) {
        this.contactEmail = mailId;
        this.id = userId;
    }
}