package com.dalnyefz.studentservice.model;

import lombok.Data;

@Data
public class Student {
    private Long id;
    private String studentNo;
    private String name;
    private String password;
    private String gender;
    private String phone;
    private String email;
    private String major;
    private Integer grade;
    private String className;
    private String createdAt;
    private String updatedAt;
}
