package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;

/**
 * 邮箱登录VO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/14
 */
@Data
public class EmailAccountDTO {
    private String emailAddress;
    private String password;
    private String domain;
}
