package edu.agiledev.agilemail.controller;


import edu.agiledev.agilemail.pojo.R;
import edu.agiledev.agilemail.pojo.ReturnCode;

import java.util.List;

/**
 * Controller base class
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public class RBaseController {

    protected <T> R<T> success() {
        return R.success();
    }

    protected <T> R<T> success(ReturnCode returnCode, T obj) {
        return R.success(returnCode, obj);
    }

    protected <T> R<T> success(T obj) {
        return R.success(obj);
    }

    protected <T> R<T> error() {
        return R.error();
    }

    protected <T> R<T> error(ReturnCode returnCode, T obj) {
        return R.error(returnCode, obj);
    }

    protected <T> R<T> error(Integer code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    protected <T> R<T> error(ReturnCode returnCode) {
        return R.error(returnCode);
    }

}
