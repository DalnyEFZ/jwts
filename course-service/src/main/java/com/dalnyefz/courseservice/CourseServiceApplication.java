package com.dalnyefz.courseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.dalnyefz.studentclient", "com.dalnyefz.teacherclient"})
public class CourseServiceApplication {

    // 启动课程管理微服务，并启用对 student/teacher 服务的 Feign 调用
    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class, args);
    }
}
