package edu.agiledev.agilemail.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//自行决定是否应用Spring上下文来测试
@SpringBootTest
class SnowFlakeIdGeneratorTest {

    @Autowired
    SnowFlakeIdGenerator snowFlakeIdGenerator;

    @Test
    void testNextId() {
//        SnowFlakeIdGenerator snowFlake = new SnowFlakeIdGenerator(2, 3);

        for (int i = 0; i < (1 << 4); i++) {
            //10进制
            System.out.println(snowFlakeIdGenerator.nextId());
        }
    }

}