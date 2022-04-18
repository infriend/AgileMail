package edu.agiledev.agilemail.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮箱信息VO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/18
 */
@Data
@AllArgsConstructor
public class EmailInfoVO {
    String emailAddress;
    String domain;
}
