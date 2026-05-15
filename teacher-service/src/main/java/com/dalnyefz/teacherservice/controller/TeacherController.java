package com.dalnyefz.teacherservice.controller;

import com.dalnyefz.teacherclient.dto.TeacherDto;
import com.dalnyefz.teacherservice.common.ApiResponse;
import com.dalnyefz.teacherservice.model.Teacher;
import com.dalnyefz.teacherservice.service.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ApiResponse<Teacher> add(@RequestBody Teacher teacher) {
        return ApiResponse.ok(teacherService.add(teacher));
    }

    @GetMapping("/{id}")
    public ApiResponse<Teacher> get(@PathVariable Long id) {
        try {
            return ApiResponse.ok(teacherService.getById(id));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Teacher>> list() {
        return ApiResponse.ok(teacherService.list());
    }

    @PutMapping("/{id}")
    public ApiResponse<Teacher> update(@PathVariable Long id, @RequestBody Teacher teacher) {
        try {
            return ApiResponse.ok(teacherService.update(id, teacher));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> delete(@PathVariable Long id) {
        try {
            teacherService.delete(id);
            return ApiResponse.ok("鍒犻櫎鎴愬姛");
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/internal/{id}")
    public TeacherDto internal(@PathVariable Long id) {
        Teacher t = teacherService.getById(id);
        TeacherDto dto = new TeacherDto();
        dto.setId(t.getId());
        dto.setTeacherNo(t.getTeacherNo());
        dto.setName(t.getName());
        dto.setTitle(t.getTitle());
        dto.setDepartment(t.getDepartment());
        return dto;
    }
}
