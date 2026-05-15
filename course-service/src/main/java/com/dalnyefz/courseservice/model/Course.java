package com.dalnyefz.courseservice.model;

import lombok.Data;

@Data
public class Course {
    private Long id;
    private String courseCode;
    private String courseName;
    private Long teacherId;
    private Integer credit;
    private Integer capacity;
    private String scheduleTime;
    private String location;
    private String createdAt;
    private String updatedAt;
}
