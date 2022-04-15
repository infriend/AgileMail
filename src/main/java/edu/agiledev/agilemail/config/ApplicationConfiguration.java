package edu.agiledev.agilemail.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量配置器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/7
 */
@Configuration
@ComponentScan("edu.agiledev.agilemail")
@Slf4j
public class ApplicationConfiguration {


    public int connectTimeout;

    private String encryptionPassword;

    private long credentialsDuration;
    private long credentialsRefreshBeforeDuration;

    private List<String> hostBlackList = new ArrayList<>();


    @Value("${config.connect.timeout}")
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    @Value("${dev.security.key}")
    public void setEncryptionKey(String password) {
        encryptionPassword = password;
    }

    @Value("${config.security.credentials.expiration}")
    public void setCredentialsExpiration(int duration) {
        credentialsDuration = duration;
    }

    @Value("${config.security.credentials.refresh}")
    public void setCredentialsRefreshBeforeDuration(int refresh) {
        credentialsRefreshBeforeDuration = refresh;
    }

    public void setHostBlackList(List<String> hostBlackList) {
        this.hostBlackList = hostBlackList;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public String getEncryptionPassword() {
        return encryptionPassword;
    }

    public Duration getCredentialsDuration() {
        return Duration.ofMillis(credentialsDuration);
    }

    public Duration getCredentialsRefreshBeforeDuration() {
        return Duration.ofMillis(credentialsRefreshBeforeDuration);
    }

    public List<String> getHostBlackList() {
        return hostBlackList;
    }
}
