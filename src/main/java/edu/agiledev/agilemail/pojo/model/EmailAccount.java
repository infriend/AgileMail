package edu.agiledev.agilemail.pojo.model;

import lombok.Data;

/**
 * 邮箱账户信息
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/8
 */
@Data
public class EmailAccount {
    private String id;
    private String username;
    private String password;
    private String domain;

    public EmailAccount() {

    }

    public EmailAccount(String username, String password, String domain) {
        setUsername(username);
        setPassword(password);
        setDomain(domain);
    }
}
