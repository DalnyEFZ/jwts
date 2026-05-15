package com.dalnyefz.studentservice.controller;

import com.dalnyefz.studentclient.dto.StudentDto;
import com.dalnyefz.studentservice.common.ApiResponse;
import com.dalnyefz.studentservice.model.Student;
import com.dalnyefz.studentservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ApiResponse<Student> register(@RequestBody Student student) {
        try {
            Student result = studentService.register(student);
            result.setPassword(null);
            return ApiResponse.ok(result);
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<Student> login(@RequestBody Map<String, String> body) {
        try {
            return ApiResponse.ok(studentService.login(body.get("studentNo"), body.get("password")));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Student> get(@PathVariable Long id) throws InterruptedException {
        try {
            // 测试熔断机制
            Thread.sleep(2000);
            // 测试负载均衡
            log.info("-------------OK /students/{id}--------------------");
            return ApiResponse.ok(studentService.getById(id));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Student>> list() {
        return ApiResponse.ok(studentService.list());
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable Long id, @RequestBody Student student) {
        try {
            return ApiResponse.ok(studentService.updateProfile(id, student));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/internal/{id}")
    public StudentDto internal(@PathVariable Long id) {
        Student s = studentService.getById(id);
        StudentDto dto = new StudentDto();
        dto.setId(s.getId());
        dto.setStudentNo(s.getStudentNo());
        dto.setName(s.getName());
        dto.setMajor(s.getMajor());
        dto.setGrade(s.getGrade());
        return dto;
    }
}
