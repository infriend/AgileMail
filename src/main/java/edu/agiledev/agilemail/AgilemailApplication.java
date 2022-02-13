package edu.agiledev.agilemail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class AgilemailApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgilemailApplication.class, args);
    }

}
