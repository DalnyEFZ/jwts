package com.dalnyefz.studentclient.client;

import com.dalnyefz.studentclient.api.StudentRemoteClient;
import com.dalnyefz.studentclient.dto.StudentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Dalny_EFZ
 * @version 1.0
 */
@Slf4j
@Component
public class StudentServiceFallback implements StudentRemoteClient {
    @Override
    public StudentDto getStudentById(@PathVariable("id") Long id) {
        log.info("getStudentById callback");
        return new StudentDto();
    }

    @Override
    public Map<String, Object> getStudent(@PathVariable("id") Long id) {
        log.info("getStudent callback");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "student-service unavailable");
        resp.put("data", null);
        return resp;
    }

    @Override
    public Map<String, Object> listStudents() {
        log.info("listStudents callback");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "student-service unavailable");
        resp.put("data", null);
        return resp;
    }
}
