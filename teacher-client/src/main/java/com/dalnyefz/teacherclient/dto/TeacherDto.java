package com.dalnyefz.teacherclient.dto;

import lombok.Data;

@Data
public class TeacherDto {
    private Long id;
    private String teacherNo;
    private String name;
    private String title;
    private String department;
}
