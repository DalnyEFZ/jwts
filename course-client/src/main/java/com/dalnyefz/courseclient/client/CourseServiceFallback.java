package com.dalnyefz.courseclient.client;

import com.dalnyefz.courseclient.api.CourseRemoteClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 课程服务熔断回退实现
 * 当 course-service 不可用时返回兜底数据
 */
@Slf4j
@Component
public class CourseServiceFallback implements CourseRemoteClient {

    @Override
    public Map<String, Object> listCourses() {
        log.warn("触发熔断: listCourses - course-service 不可用");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "课程服务暂时不可用，请稍后重试");
        resp.put("data", null);
        return resp;
    }

    @Override
    public Map<String, Object> getCourse(Long id) {
        log.warn("触发熔断: getCourse, id={}", id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "课程服务暂时不可用，请稍后重试");
        resp.put("data", null);
        return resp;
    }

    @Override
    public Map<String, Object> getSchedule(Long studentId) {
        log.warn("触发熔断: getSchedule, studentId={}", studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "课程服务暂时不可用，无法获取课表");
        resp.put("data", null);
        return resp;
    }

    @Override
    public Map<String, Object> getTranscript(Long studentId) {
        log.warn("触发熔断: getTranscript, studentId={}", studentId);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "课程服务暂时不可用，无法获取成绩单");
        resp.put("data", null);
        return resp;
    }
}
