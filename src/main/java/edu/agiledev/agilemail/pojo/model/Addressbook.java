package edu.agiledev.agilemail.pojo.model;

import lombok.Data;

@Data
public class Addressbook {
    private String userId;

    private String id;

    private String contactEmail;

    private String name;

    public Addressbook(String id, String userId, String contactEmail, String name) {
        this.userId = userId;
        this.id = id;
        this.contactEmail = contactEmail;
        this.name = name;
    }
}