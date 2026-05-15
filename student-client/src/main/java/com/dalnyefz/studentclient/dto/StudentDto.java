package com.dalnyefz.studentclient.dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String studentNo;
    private String name;
    private String major;
    private Integer grade;
}
