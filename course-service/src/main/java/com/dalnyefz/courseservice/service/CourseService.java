package com.dalnyefz.courseservice.service;

import com.dalnyefz.courseservice.mapper.CourseMapper;
import com.dalnyefz.courseservice.mapper.EnrollmentMapper;
import com.dalnyefz.courseservice.model.Course;
import com.dalnyefz.courseservice.model.Enrollment;
import com.dalnyefz.courseservice.model.StudentCourseView;
import com.dalnyefz.courseservice.model.TranscriptSummary;
import com.dalnyefz.studentclient.api.StudentRemoteClient;
import com.dalnyefz.studentclient.dto.StudentDto;
import com.dalnyefz.teacherclient.api.TeacherRemoteClient;
import com.dalnyefz.teacherclient.dto.TeacherDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// 课程核心业务：课程维护、选退课、成绩录入、成绩单统计
public class CourseService {
    private final CourseMapper courseMapper;
    private final EnrollmentMapper enrollmentMapper;
    private final StudentRemoteClient studentRemoteClient;
    private final TeacherRemoteClient teacherRemoteClient;

    public CourseService(CourseMapper courseMapper,
                         EnrollmentMapper enrollmentMapper,
                         StudentRemoteClient studentRemoteClient,
                         TeacherRemoteClient teacherRemoteClient) {
        this.courseMapper = courseMapper;
        this.enrollmentMapper = enrollmentMapper;
        this.studentRemoteClient = studentRemoteClient;
        this.teacherRemoteClient = teacherRemoteClient;
    }

    public Course addCourse(Course course) {
        // 新增课程前先校验授课教师是否存在
        validateTeacher(course.getTeacherId());
        courseMapper.insert(course);
        return course;
    }

    public Course updateCourse(Long id, Course course) {
        getCourse(id);
        validateTeacher(course.getTeacherId());
        course.setId(id);
        courseMapper.update(course);
        return getCourse(id);
    }

    public void deleteCourse(Long id) {
        getCourse(id);
        courseMapper.deleteById(id);
    }

    public Course getCourse(Long id) {
        Course course = courseMapper.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        return course;
    }

    public List<Course> listCourse() {
        return courseMapper.findAll();
    }

    public void selectCourse(Long studentId, Long courseId) {
        validateStudent(studentId);
        Course course = getCourse(courseId);
        if (courseMapper.enrollmentCount(courseId) >= course.getCapacity()) {
            throw new IllegalArgumentException("课程容量已满");
        }
        if (enrollmentMapper.findByStudentAndCourse(studentId, courseId) != null) {
            throw new IllegalArgumentException("该课程已选");
        }
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setScore(null);
        enrollment.setStatus("SELECTED");
        enrollmentMapper.insert(enrollment);
    }

    public void dropCourse(Long studentId, Long courseId) {
        validateStudent(studentId);
        if (enrollmentMapper.deleteByStudentAndCourse(studentId, courseId) <= 0) {
            throw new IllegalArgumentException("选课记录不存在");
        }
    }

    public void recordScore(Long studentId, Long courseId, Double score) {
        validateStudent(studentId);
        getCourse(courseId);
        Enrollment enrollment = enrollmentMapper.findByStudentAndCourse(studentId, courseId);
        if (enrollment == null) {
            throw new IllegalArgumentException("选课记录不存在");
        }
        enrollmentMapper.updateScoreAndStatus(studentId, courseId, score, "COMPLETED");
    }

    public List<StudentCourseView> studentSchedule(Long studentId) {
        validateStudent(studentId);
        return enrollmentMapper.findCoursesByStudent(studentId);
    }

    public TranscriptSummary transcript(Long studentId) {
        List<StudentCourseView> list = studentSchedule(studentId);
        int total = list.size();
        int passed = 0;
        int credits = 0;
        double scoreSum = 0;
        int scoreCount = 0;
        for (StudentCourseView item : list) {
            if (item.getScore() != null) {
                scoreSum += item.getScore();
                scoreCount++;
                if (item.getScore() >= 60) {
                    passed++;
                    credits += item.getCredit();
                }
            }
        }
        double avg = scoreCount == 0 ? 0.0 : scoreSum / scoreCount;
        return new TranscriptSummary(studentId, total, passed, credits, avg);
    }

    private void validateStudent(Long studentId) {
        StudentDto student = studentRemoteClient.getStudentById(studentId);
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("学生不存在");
        }
    }

    private void validateTeacher(Long teacherId) {
        TeacherDto teacher = teacherRemoteClient.getTeacherById(teacherId);
        if (teacher == null || teacher.getId() == null) {
            throw new IllegalArgumentException("教师不存在");
        }
    }
}
