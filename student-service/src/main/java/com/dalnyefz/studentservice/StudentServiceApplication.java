package com.dalnyefz.studentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StudentServiceApplication {

    // 启动学生信息管理微服务
    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }
}
