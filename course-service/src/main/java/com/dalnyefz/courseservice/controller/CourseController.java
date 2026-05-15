package com.dalnyefz.courseservice.controller;

import com.dalnyefz.courseservice.common.ApiResponse;
import com.dalnyefz.courseservice.model.Course;
import com.dalnyefz.courseservice.model.StudentCourseView;
import com.dalnyefz.courseservice.model.TranscriptSummary;
import com.dalnyefz.courseservice.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
// 课程管理接口：课程维护、选课退课、成绩与成绩单
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ApiResponse<Course> addCourse(@RequestBody Course course) {
        try {
            return ApiResponse.ok(courseService.addCourse(course));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        try {
            return ApiResponse.ok(courseService.updateCourse(id, course));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ApiResponse.ok("删除成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> getCourse(@PathVariable Long id) {
        try {
            return ApiResponse.ok(courseService.getCourse(id));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Course>> listCourse() {
        return ApiResponse.ok(courseService.listCourse());
    }

    @PostMapping("/select")
    public ApiResponse<String> selectCourse(@RequestBody Map<String, Long> body) {
        try {
            courseService.selectCourse(body.get("studentId"), body.get("courseId"));
            return ApiResponse.ok("选课成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/drop")
    public ApiResponse<String> dropCourse(@RequestBody Map<String, Long> body) {
        try {
            courseService.dropCourse(body.get("studentId"), body.get("courseId"));
            return ApiResponse.ok("退课成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @PostMapping("/score")
    public ApiResponse<String> score(@RequestBody Map<String, Object> body) {
        try {
            Long studentId = Long.valueOf(body.get("studentId").toString());
            Long courseId = Long.valueOf(body.get("courseId").toString());
            Double score = Double.valueOf(body.get("score").toString());
            courseService.recordScore(studentId, courseId, score);
            return ApiResponse.ok("成绩录入成功");
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/schedule/{studentId}")
    public ApiResponse<List<StudentCourseView>> schedule(@PathVariable Long studentId) {
        try {
            return ApiResponse.ok(courseService.studentSchedule(studentId));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }

    @GetMapping("/transcript/{studentId}")
    public ApiResponse<TranscriptSummary> transcript(@PathVariable Long studentId) {
        try {
            return ApiResponse.ok(courseService.transcript(studentId));
        } catch (IllegalArgumentException e) {
            return ApiResponse.fail(e.getMessage());
        }
    }
}
