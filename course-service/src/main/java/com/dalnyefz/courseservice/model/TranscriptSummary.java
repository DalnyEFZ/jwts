package com.dalnyefz.courseservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TranscriptSummary {
    private Long studentId;
    private Integer totalCourses;
    private Integer passedCourses;
    private Integer totalCredits;
    private Double averageScore;
}
