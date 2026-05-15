package com.dalnyefz.studentservice.service;

import com.dalnyefz.studentservice.mapper.StudentMapper;
import com.dalnyefz.studentservice.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Student register(Student student) {
        if (studentMapper.findByStudentNo(student.getStudentNo()) != null) {
            throw new IllegalArgumentException("注册失败");
        }
        studentMapper.insert(student);
        return student;
    }

    public Student login(String studentNo, String password) {
        Student student = studentMapper.findByStudentNo(studentNo);
        if (student == null || !student.getPassword().equals(password)) {
            throw new IllegalArgumentException("璐﹀彿鎴栧瘑鐮侀敊璇?");
        }
        student.setPassword(null);
        return student;
    }

    public Student getById(Long id) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new IllegalArgumentException("瀛︾敓涓嶅瓨鍦?");
        }
        student.setPassword(null);
        return student;
    }

    public List<Student> list() {
        List<Student> students = studentMapper.findAll();
        students.forEach(s -> s.setPassword(null));
        return students;
    }

    public Student updateProfile(Long id, Student student) {
        Student exists = studentMapper.findById(id);
        if (exists == null) {
            throw new IllegalArgumentException("瀛︾敓涓嶅瓨鍦?");
        }
        student.setId(id);
        studentMapper.updateProfile(student);
        return getById(id);
    }
}
