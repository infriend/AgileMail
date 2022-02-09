package edu.agiledev.agilemail.security;

import edu.agiledev.agilemail.pojo.EmailAccount;
import edu.agiledev.agilemail.security.model.Credentials;

/**
 * 通信token的工具类，负责验证和生成token
 * TODO：实现接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/8
 */
public interface TokenProvider {

    boolean validateToken(String token);

    Credentials getCredentials(String token);

    String generateToken(Credentials credentials);

    String refreshToken(String token);

}
