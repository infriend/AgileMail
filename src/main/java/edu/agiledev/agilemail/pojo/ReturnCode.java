package edu.agiledev.agilemail.pojo;

/**
 * Return code definition
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public enum ReturnCode {


    SUCCESS(1, "操作成功"),
    ERROR(0, "操作失败"),

    INTERNAL_ERROR(3000, "系统错误"),
    CHECKING_ERROR(3001, "检查时发生错误");
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
