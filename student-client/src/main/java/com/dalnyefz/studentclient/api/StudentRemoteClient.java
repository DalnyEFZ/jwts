package com.dalnyefz.studentclient.api;

import com.dalnyefz.studentclient.client.StudentServiceFallback;
import com.dalnyefz.studentclient.dto.StudentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "student-service", fallback = StudentServiceFallback.class)
public interface StudentRemoteClient {
    @GetMapping("/students/internal/{id}")
    StudentDto getStudentById(@PathVariable("id") Long id);

    @GetMapping("/students/{id}")
    Map<String, Object> getStudent(@PathVariable("id") Long id);

    @GetMapping("/students")
    Map<String, Object> listStudents();
}
