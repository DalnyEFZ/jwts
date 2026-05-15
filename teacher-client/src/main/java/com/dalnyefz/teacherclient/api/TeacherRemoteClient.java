package com.dalnyefz.teacherclient.api;

import com.dalnyefz.teacherclient.dto.TeacherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "teacher-service")
public interface TeacherRemoteClient {
    @GetMapping("/teachers/internal/{id}")
    TeacherDto getTeacherById(@PathVariable("id") Long id);

    @GetMapping("/teachers/{id}")
    Map<String, Object> getTeacher(@PathVariable("id") Long id);

    @GetMapping("/teachers")
    Map<String, Object> listTeachers();
}
