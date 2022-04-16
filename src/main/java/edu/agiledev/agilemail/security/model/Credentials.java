package edu.agiledev.agilemail.security.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * 身份信息类，实现Authentication接口可在Spring，Security中使用，保存身份信息
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
public class Credentials extends AbstractAuthenticationToken {
    @Getter
    @Setter
    private String userId;

//    @Getter
//    @Setter
//    private ZonedDateTime expiryDate;

    private static final GrantedAuthority GENERAL_USER = new SimpleGrantedAuthority("GENERAL_USER");

    public Credentials() {
        super(Collections.singleton(GENERAL_USER));
    }

    public Credentials(String userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        setUserId(userId);
//        setExpiryDate(ZonedDateTime.ofInstant(expiryDate.toInstant(),
//                ZoneOffset.UTC));
    }


    @Override
    public Object getCredentials() {
        return userId;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}
