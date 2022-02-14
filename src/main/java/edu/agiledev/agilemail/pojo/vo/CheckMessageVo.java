package edu.agiledev.agilemail.pojo.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CheckMessageVo {
    private String from;
    private String subject;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    private String fromEmailAccount;
    private int state;
}
