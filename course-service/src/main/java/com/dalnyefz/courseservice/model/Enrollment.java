package com.dalnyefz.courseservice.model;

import lombok.Data;

@Data
public class Enrollment {
    private Long id;
    private Long studentId;
    private Long courseId;
    private Double score;
    private String status;
    private String selectedAt;
    private String updatedAt;
}
