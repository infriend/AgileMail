package edu.agiledev.agilemail.controller;


import edu.agiledev.agilemail.mappers.AccountMapper;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.security.model.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 控制器基类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public class RBaseController {

    @Autowired
    AccountMapper accountMapper;

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


    protected EmailAccount getCurrentAccount(String emailAddress) {
        Credentials credentials = (Credentials) SecurityContextHolder.getContext().getAuthentication();
        String userId = credentials.getUserId();
        EmailAccount account = accountMapper.getUserEmailAccount(userId, emailAddress);
        return account;
    }
}
