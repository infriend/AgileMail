package edu.agiledev.agilemail.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description of class
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

    private int credentialsDurationMinutes;
    private int credentialsRefreshBeforeDurationMinutes;


    @Value("${config.connect.timeout}")
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    @Value("${dev.security.encryption}")
    public void setEncryptionPassword(String password) {
        encryptionPassword = password;
    }

    @Value("${dev.security.credentials.duration}")
    public void setCredentialsDurationMinutes(int minutes) {
        credentialsDurationMinutes = minutes;
    }

    @Value("${dev.security.credentials.refresh}")
    public void setCredentialsRefreshBeforeDurationMinutes(int minutes) {
        credentialsRefreshBeforeDurationMinutes = minutes;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public String getEncryptionPassword() {
        return encryptionPassword;
    }

    public int getCredentialsDurationMinutes() {
        return credentialsDurationMinutes;
    }

    public int getCredentialsRefreshBeforeDurationMinutes() {
        return credentialsRefreshBeforeDurationMinutes;
    }

}
