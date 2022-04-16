package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.security.model.Credentials;
import edu.agiledev.agilemail.pojo.model.EmailAccount;

import java.util.List;

/**
 * 账户服务相关接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public interface AccountService {
    /**
     * 检查账户并且生成证书
     */
    void checkAccount(EmailAccount account);

    /**
     * 向数据库中注册新用户，返回证书
     *
     * @param account 已经验证后的账户
     * @return 用于本应用的证书
     */
    Credentials registerUser(EmailAccount account);

    //获得用户通讯录
    List<Addressbook> getContacts(String userId);

}
