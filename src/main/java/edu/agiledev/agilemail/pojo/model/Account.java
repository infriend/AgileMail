package edu.agiledev.agilemail.pojo.model;

import lombok.Data;

/**
 * 账户模型
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/17
 */
@Data
public class Account {
    private String id;
    private String username;
    private String password;
}
