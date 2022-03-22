package edu.agiledev.agilemail.pojo.vo;

import edu.agiledev.agilemail.pojo.message.Recipient;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CheckMessageVo {
    private List<String> from;
    private List<String> replyTo;
    private List<Recipient> recipients;
    private String subject;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date datetime;

    private boolean flagged;
    private boolean seen;
    private boolean recent;
    private boolean deleted;

    private String fromEmailAccount;

}
