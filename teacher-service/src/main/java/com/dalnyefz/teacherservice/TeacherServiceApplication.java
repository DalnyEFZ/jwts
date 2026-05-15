package com.dalnyefz.teacherservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TeacherServiceApplication {

    // 启动教师管理微服务
    public static void main(String[] args) {
        SpringApplication.run(TeacherServiceApplication.class, args);
    }
}
