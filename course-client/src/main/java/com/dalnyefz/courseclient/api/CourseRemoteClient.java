package com.dalnyefz.courseclient.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "course-service")
public interface CourseRemoteClient {

    @GetMapping("/courses")
    Map<String, Object> listCourses();

    @GetMapping("/courses/{id}")
    Map<String, Object> getCourse(@PathVariable("id") Long id);

    @GetMapping("/courses/schedule/{studentId}")
    Map<String, Object> getSchedule(@PathVariable("studentId") Long studentId);

    @GetMapping("/courses/transcript/{studentId}")
    Map<String, Object> getTranscript(@PathVariable("studentId") Long studentId);
}
