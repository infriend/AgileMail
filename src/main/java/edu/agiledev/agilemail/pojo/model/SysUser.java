package edu.agiledev.agilemail.pojo.model;

import lombok.Data;

/**
    * 用户表
    */
@Data
public class SysUser {
    /**
    * 用户id
    */
    private String id;

    /**
    * 删除标识
    */
    private String delFlag;

    private String passwd;
}