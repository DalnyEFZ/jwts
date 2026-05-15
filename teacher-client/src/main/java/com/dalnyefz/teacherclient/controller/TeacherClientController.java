package com.dalnyefz.teacherclient.controller;

import com.dalnyefz.teacherclient.api.TeacherRemoteClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/teacher-client")
public class TeacherClientController {

    private final TeacherRemoteClient teacherRemoteClient;

    public TeacherClientController(TeacherRemoteClient teacherRemoteClient) {
        this.teacherRemoteClient = teacherRemoteClient;
    }

    @GetMapping("/teachers/{id}")
    public Map<String, Object> getTeacher(@PathVariable Long id) {
        return teacherRemoteClient.getTeacher(id);
    }

    @GetMapping("/teachers")
    public Map<String, Object> listTeachers() {
        return teacherRemoteClient.listTeachers();
    }
}
