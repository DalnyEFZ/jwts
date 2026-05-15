package com.dalnyefz.teacherservice.mapper;

import com.dalnyefz.teacherservice.model.Teacher;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeacherMapper {

    @Insert("INSERT INTO t_teacher(teacher_no,name,title,department,phone,email) VALUES(#{teacherNo},#{name},#{title},#{department},#{phone},#{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Teacher teacher);

    @Select("SELECT * FROM t_teacher WHERE id = #{id} LIMIT 1")
    Teacher findById(Long id);

    @Select("SELECT * FROM t_teacher ORDER BY id")
    List<Teacher> findAll();

    @Update("UPDATE t_teacher SET name=#{name},title=#{title},department=#{department},phone=#{phone},email=#{email},updated_at=NOW() WHERE id=#{id}")
    int update(Teacher teacher);

    @Delete("DELETE FROM t_teacher WHERE id = #{id}")
    int deleteById(Long id);
}
