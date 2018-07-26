package com.bluz.demo;

import com.bluz.demo.config.DruidDataSourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ServletComponentScan
@Import(DruidDataSourceConfig.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
