package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.pojo.model.EmailAccount;

/**
 * Description of interface
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public interface AccountService {
    //检查账户并且生成证书
    void checkAccount(EmailAccount account);

    //向数据库中注册新用户
    Credentials registerUser(EmailAccount account);

    //
}
