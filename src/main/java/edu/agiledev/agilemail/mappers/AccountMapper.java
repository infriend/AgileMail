package edu.agiledev.agilemail.mappers;

import edu.agiledev.agilemail.pojo.model.Account;
import edu.agiledev.agilemail.pojo.model.EmailAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户相关表Mapper
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/12
 */
@Mapper
@Repository
public interface AccountMapper {

    Account searchAccount(@Param("username") String username);

    int insertUserAccount(@Param("userId") String userId, @Param("username") String username, @Param("password") String password);

    int insertEmailAccount(@Param("emailAccountId") String emailAccountId,
                           @Param("emailAddress") String emailAddress,
                           @Param("password") String password,
                           @Param("domain") String domain);

    List<EmailAccount> searchAccountEmailList(@Param("userId") String userId);

    int relateAccount(@Param("userId") String userId, @Param("emailAccountId") String emailAccountId);

    int deRelateAccount(@Param("userId") String userId, @Param("emailAddress") String emailAddress);

    int deleteEmailAccount(@Param("emailAddress") String emailAddress);

    EmailAccount getUserEmailAccount(@Param("userId") String userId, @Param("emailAddress") String emailAddress);


}
