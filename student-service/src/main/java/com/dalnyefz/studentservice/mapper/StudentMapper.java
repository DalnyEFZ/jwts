package com.dalnyefz.studentservice.mapper;

import com.dalnyefz.studentservice.model.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    @Insert("INSERT INTO t_student(student_no,name,password,gender,phone,email,major,grade,class_name) VALUES(#{studentNo},#{name},#{password},#{gender},#{phone},#{email},#{major},#{grade},#{className})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);

    @Select("SELECT * FROM t_student WHERE student_no = #{studentNo} LIMIT 1")
    Student findByStudentNo(String studentNo);

    @Select("SELECT * FROM t_student WHERE id = #{id} LIMIT 1")
    Student findById(Long id);

    @Select("SELECT * FROM t_student ORDER BY id")
    List<Student> findAll();

    @Update("UPDATE t_student SET name=#{name},gender=#{gender},phone=#{phone},email=#{email},major=#{major},grade=#{grade},class_name=#{className},updated_at=NOW() WHERE id=#{id}")
    int updateProfile(Student student);
}
