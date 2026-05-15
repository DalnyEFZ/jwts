package com.dalnyefz.studentclient.api;

import com.dalnyefz.studentclient.client.StudentServiceFallback;
import com.dalnyefz.studentclient.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

// Feign 客户端：声明 student-service 的远程调用接口
@FeignClient(name = "student-service", fallback = StudentServiceFallback.class)
public interface StudentRemoteClient {
    // 内部校验接口，供其他微服务按 ID 获取学生简要信息
    @GetMapping("/students/internal/{id}")
    StudentDto getStudentById(@PathVariable("id") Long id);

    // 外部查询单个学生
    @GetMapping("/students/{id}")
    Map<String, Object> getStudent(@PathVariable("id") Long id);

    // 外部查询学生列表
    @GetMapping("/students")
    Map<String, Object> listStudents();
}
