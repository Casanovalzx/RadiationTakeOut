package org.example.radiation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class RadiationApplication {
    public static void main(String[] args){
        SpringApplication.run(RadiationApplication.class, args);
        log.info("项目启动成功...");
    }
}
