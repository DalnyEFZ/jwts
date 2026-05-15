package com.dalnyefz.teacherservice.model;

import lombok.Data;

@Data
public class Teacher {
    private Long id;
    private String teacherNo;
    private String name;
    private String title;
    private String department;
    private String phone;
    private String email;
    private String createdAt;
    private String updatedAt;
}
