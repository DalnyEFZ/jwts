package com.dalnyefz.studentclient.client;

import com.dalnyefz.studentclient.api.StudentRemoteClient;
import com.dalnyefz.studentclient.dto.StudentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.HashMap;

// Feign 熔断回退实现：当 student-service 不可用时返回兜底数据
@Slf4j
@Component
public class StudentServiceFallback implements StudentRemoteClient {
    @Override
    public StudentDto getStudentById(@PathVariable("id") Long id) {
        log.warn("触发熔断: getStudentById, id={}", id);
        return new StudentDto();
    }

    @Override
    public Map<String, Object> getStudent(@PathVariable("id") Long id) {
        log.warn("触发熔断: getStudent, id={}", id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "student-service unavailable");
        resp.put("data", null);
        return resp;
    }

    @Override
    public Map<String, Object> listStudents() {
        log.warn("触发熔断: listStudents");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "student-service unavailable");
        resp.put("data", null);
        return resp;
    }
}
