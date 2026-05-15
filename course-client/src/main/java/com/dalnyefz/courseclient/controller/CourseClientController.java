package com.dalnyefz.courseclient.controller;

import com.dalnyefz.courseclient.api.CourseRemoteClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/course-client")
public class CourseClientController {

    private final CourseRemoteClient courseRemoteClient;

    public CourseClientController(CourseRemoteClient courseRemoteClient) {
        this.courseRemoteClient = courseRemoteClient;
    }

    @GetMapping("/courses")
    public Map<String, Object> listCourses() {
        return courseRemoteClient.listCourses();
    }

    @GetMapping("/courses/{id}")
    public Map<String, Object> getCourse(@PathVariable Long id) {
        return courseRemoteClient.getCourse(id);
    }

    @GetMapping("/schedule/{studentId}")
    public Map<String, Object> getSchedule(@PathVariable Long studentId) {
        return courseRemoteClient.getSchedule(studentId);
    }

    @GetMapping("/transcript/{studentId}")
    public Map<String, Object> getTranscript(@PathVariable Long studentId) {
        return courseRemoteClient.getTranscript(studentId);
    }
}
