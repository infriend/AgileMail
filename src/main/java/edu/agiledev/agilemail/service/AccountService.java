package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.pojo.EmailAccount;

/**
 * Description of interface
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public interface AccountService {
    //检查账户并且生成证书
    Credentials checkAccount(EmailAccount account);

    //向数据库中注册新用户
    void registerUser(EmailAccount account);
}
