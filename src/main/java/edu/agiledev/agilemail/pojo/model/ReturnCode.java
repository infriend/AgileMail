package edu.agiledev.agilemail.pojo.model;

/**
 * 返回码定义
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public enum ReturnCode {


    SUCCESS(1, "操作成功"),
    ERROR(0, "操作失败"),

    LOGIN_FAILED(1001, "登录失败"),

    IMAP_ERROR(2000, "imap错误"),
    IMAP_CONNECTION_ERROR(2001, "imap连接错误"),
    IMAP_FOLDER_ERROR(2002, "imap文件夹操作错误"),
    IMAP_MESSAGE_ERROR(2003, "imap邮件操作错误"),
    ATTACHMENT_NOT_FOUND(2004, "找不到附件"),
    MOVE_MESSAGE_ERROR(2005, "移动邮件时发生错误"),
    DELETE_MESSAGE_ERROR(2005, "删除邮件时发生错误"),
    DOWNLOAD_MESSAGE_ERROR(2006, "下载邮件时发生错误"),

    INTERNAL_ERROR(3000, "系统错误"),
    CHECKING_ERROR(3001, "检查时发生错误"),
    ADDRESS_ERROR(3002, "解析地址时发生错误"),
    IO_ERROR(3003, "IO时发生错误");
    //TODO：定义其他操作代码


    private Integer code;
    private String msg;

    ReturnCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
