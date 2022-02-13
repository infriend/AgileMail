package edu.agiledev.agilemail.security;

import edu.agiledev.agilemail.security.model.Credentials;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * jwt Token功能单元，用于生成和验证token
 */
public class CredentialsTokenProvider implements TokenProvider {

    private final Logger log = LoggerFactory.getLogger(CredentialsTokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    private static final SignatureAlgorithm ALG = SignatureAlgorithm.HS256;

    private final long expiration;

    private final long refresh;

    public CredentialsTokenProvider(long expiration, long refresh, String secretKey) {
        this.expiration = expiration;
        this.refresh = refresh;
        setKey(secretKey);
    }

    public void setKey(@NonNull String secretKey) {
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);
        key = new SecretKeySpec(secretKeyBytes, ALG.getJcaName());
    }

    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Authentication authentication) {
        Credentials credentials = (Credentials) authentication;
        String authorities = credentials.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTHORITIES_KEY, authorities);
        return doGenerateToken(claims, credentials.getUserId());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public boolean canBeRefreshed(String token) {
        if (validateToken(token)) {
            Date createdDate = getIssuedAtDateFromToken(token);
            Date now = new Date();
            return now.getTime() - createdDate.getTime() > refresh * 1000;
        }
        return false;
    }

    public String refreshAuthority(String token, Collection<String> newAuthorities) {
        String authorities = String.join(",", newAuthorities);
        final Claims claims = getAllClaimsFromToken(token);
        claims.put(AUTHORITIES_KEY, authorities);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }

    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (SignatureException e) {
            log.info("Invalid signature.");
            log.trace("Invalid signature trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;

    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        Credentials credentials = new Credentials(claims.getSubject(), authorities);
        return credentials;
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
}
