
package edu.agiledev.agilemail.pojo.message;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 邮件收件人模型
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
@Data
@AllArgsConstructor
public class Recipient implements Serializable {

    private static final long serialVersionUID = -1389623045340754035L;

    private String type; //To, Cc, Bcc
    private String address;


}
