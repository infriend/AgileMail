package edu.agiledev.agilemail.pojo.model;

import lombok.Data;

@Data
public class Addressbook {
    private String id;

    private String name;

    private String contactEmail;

    private String userId;


    public Addressbook(String id, String name, String contactEmail, String userId) {
        this.id = id;
        this.name = name;
        this.contactEmail = contactEmail;
        this.userId = userId;
    }
}