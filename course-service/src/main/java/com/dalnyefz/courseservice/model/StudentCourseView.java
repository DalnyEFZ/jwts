package com.dalnyefz.courseservice.model;

import lombok.Data;

@Data
public class StudentCourseView {
    private Long enrollmentId;
    private Long studentId;
    private Long courseId;
    private String courseCode;
    private String courseName;
    private Integer credit;
    private String scheduleTime;
    private String location;
    private Double score;
    private String status;
}
