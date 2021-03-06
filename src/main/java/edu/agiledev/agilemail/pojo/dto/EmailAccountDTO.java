package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;

/**
 * 邮箱账户DTO
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
