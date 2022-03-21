package edu.agiledev.agilemail.mappers;

import edu.agiledev.agilemail.pojo.EmailAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    int insertUser(@Param("userId") String userId);

    int insertAccount(@Param("accountId") String accountId,
                      @Param("username") String username,
                      @Param("password") String password,
                      @Param("domain") String domain);

    int relateAccount(@Param("userId") String userId, @Param("accountId") String accountId);

    EmailAccount getUserFirstEmailAccount(@Param("userId") String userId);

    EmailAccount getUserEmailAccount(@Param("userId") String userId, @Param("emailAddress") String emailAddress);


}
