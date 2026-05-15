package com.dalnyefz.studentservice.controller;

import com.dalnyefz.studentclient.dto.StudentDto;
import com.dalnyefz.studentservice.common.ApiResponse;
import com.dalnyefz.studentservice.model.Student;
import com.dalnyefz.studentservice.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/students")
// 学生信息管理接口：注册、登录、查询、更新
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
            // 用于演示熔断：模拟延迟
            // 访问http://localhost:9011/student-client/students/1
            Thread.sleep(2000);
            // 用于演示负载均衡：输出日志
            // 访问http://localhost:9000/api/students/1?token=1
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
