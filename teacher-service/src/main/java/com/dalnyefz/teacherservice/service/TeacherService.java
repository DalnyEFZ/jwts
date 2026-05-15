package com.dalnyefz.teacherservice.service;

import com.dalnyefz.teacherservice.mapper.TeacherMapper;
import com.dalnyefz.teacherservice.model.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    public Teacher add(Teacher teacher) {
        teacherMapper.insert(teacher);
        return teacher;
    }

    public Teacher getById(Long id) {
        Teacher teacher = teacherMapper.findById(id);
        if (teacher == null) {
            throw new IllegalArgumentException("鏁欏笀涓嶅瓨鍦?");
        }
        return teacher;
    }

    public List<Teacher> list() {
        return teacherMapper.findAll();
    }

    public Teacher update(Long id, Teacher teacher) {
        getById(id);
        teacher.setId(id);
        teacherMapper.update(teacher);
        return getById(id);
    }

    public void delete(Long id) {
        getById(id);
        teacherMapper.deleteById(id);
    }
}
