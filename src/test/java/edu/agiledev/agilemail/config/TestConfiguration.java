package edu.agiledev.agilemail.config;

import edu.agiledev.agilemail.pojo.model.EmailAccount;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 测试相关配置
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/13
 */
@Configuration
@Getter
public class TestConfiguration {

    @Value("${test.email.username}")
    private String testEmailUsername;

    @Value("${test.email.password}")
    private String testEmailPassword;

    @Value("${test.email.domain}")
    private String testEmailDomain;

    public EmailAccount getTestEmailAccount() {
        return new EmailAccount(testEmailUsername, testEmailPassword, testEmailDomain);
    }

}
