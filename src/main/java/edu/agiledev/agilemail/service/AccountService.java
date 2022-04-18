package edu.agiledev.agilemail.service;

import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.pojo.vo.EmailInfoVO;
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
     * 向数据库中注册新用户，返回证书
     *
     * @param username 用户名
     * @param password 密码
     * @return 用于本应用的证书
     */
    Credentials loginUser(String username, String password);

    /**
     * 检查邮箱
     */
    boolean checkAccount(EmailAccount account);

    List<EmailInfoVO> getAccountEmailList(String userId);

    boolean addEmailAccount(String userId, EmailAccount emailAccount);

    boolean deleteEmailAccount(String userId, String emailAddress);

    //获得用户通讯录
    List<Addressbook> getContacts(String userId);

}
