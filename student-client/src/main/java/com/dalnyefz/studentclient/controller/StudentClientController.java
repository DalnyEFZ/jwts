package com.dalnyefz.studentclient.controller;

import com.dalnyefz.studentclient.api.StudentRemoteClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/student-client")
public class StudentClientController {

    private final StudentRemoteClient studentRemoteClient;

    public StudentClientController(StudentRemoteClient studentRemoteClient) {
        this.studentRemoteClient = studentRemoteClient;
    }

    @GetMapping("/students/{id}")
    public Map<String, Object> getStudent(@PathVariable Long id) {
        return studentRemoteClient.getStudent(id);
    }

    @GetMapping("/students")
    public Map<String, Object> listStudents() {
        return studentRemoteClient.listStudents();
    }
}
