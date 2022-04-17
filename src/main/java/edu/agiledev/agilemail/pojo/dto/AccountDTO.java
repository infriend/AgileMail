package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;

/**
 * 用户登录DTO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/14
 */
@Data
public class AccountDTO {
    private String username;
    private String password;
}
