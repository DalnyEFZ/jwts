package com.dalnyefz.teacherclient.client;

import com.dalnyefz.teacherclient.api.TeacherRemoteClient;
import com.dalnyefz.teacherclient.dto.TeacherDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 教师服务熔断回退实现
 * 当 teacher-service 不可用时返回兜底数据
 */
@Slf4j
@Component
public class TeacherServiceFallback implements TeacherRemoteClient {

    @Override
    public TeacherDto getTeacherById(Long id) {
        log.warn("触发熔断: getTeacherById, id={}", id);
        return new TeacherDto();
    }

    @Override
    public Map<String, Object> getTeacher(Long id) {
        log.warn("触发熔断: getTeacher, id={}", id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "教师服务暂时不可用，请稍后重试");
        resp.put("data", null);
        return resp;
    }

    @Override
    public Map<String, Object> listTeachers() {
        log.warn("触发熔断: listTeachers - teacher-service 不可用");
        Map<String, Object> resp = new HashMap<>();
        resp.put("code", -1);
        resp.put("message", "教师服务暂时不可用，请稍后重试");
        resp.put("data", null);
        return resp;
    }
}
