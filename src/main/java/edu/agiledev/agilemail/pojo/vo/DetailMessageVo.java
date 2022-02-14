package edu.agiledev.agilemail.pojo.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DetailMessageVo {
    private String from;
    private String to;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    private String subject;
    private String content;
}
