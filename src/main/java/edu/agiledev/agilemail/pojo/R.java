package edu.agiledev.agilemail.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * Response wrapper
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Getter
@Setter
public class R<T> {

    private Integer code;

    private String msg;

    private T data;

    public R() {

    }

    public R(ReturnCode returnCode, T data) {
        this.code = returnCode.getCode();
        this.msg = returnCode.getMsg();
        this.data = data;
    }

    public static <T> R<T> success() {
        return new R<T>(ReturnCode.SUCCESS, null);
    }

    public static <T> R<T> success(T object) {
        return new R<T>(ReturnCode.SUCCESS, object);
    }

    public static <T> R<T> success(ReturnCode returnCode, T object) {
        return new R<T>(returnCode, object);
    }

    public static <T> R<T> error() {
        return new R<T>(ReturnCode.ERROR, null);
    }

    public static <T> R<T> error(ReturnCode returnCode) {
        return new R<T>(returnCode, null);
    }

    public static <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> error(ReturnCode returnCode, T object) {
        return new <T>R<T>(returnCode, object);
    }
}
