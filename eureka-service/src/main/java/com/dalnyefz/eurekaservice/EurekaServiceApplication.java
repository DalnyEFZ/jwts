package com.dalnyefz.eurekaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {

    // 启动 Eureka 注册中心
    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApplication.class, args);
    }

}
