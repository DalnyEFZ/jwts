package com.dalnyefz.courseservice.mapper;

import com.dalnyefz.courseservice.model.Course;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourseMapper {
    @Insert("INSERT INTO t_course(course_code,course_name,teacher_id,credit,capacity,schedule_time,location) VALUES(#{courseCode},#{courseName},#{teacherId},#{credit},#{capacity},#{scheduleTime},#{location})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);

    @Select("SELECT * FROM t_course WHERE id=#{id} LIMIT 1")
    Course findById(Long id);

    @Select("SELECT * FROM t_course ORDER BY id")
    List<Course> findAll();

    @Update("UPDATE t_course SET course_code=#{courseCode},course_name=#{courseName},teacher_id=#{teacherId},credit=#{credit},capacity=#{capacity},schedule_time=#{scheduleTime},location=#{location},updated_at=NOW() WHERE id=#{id}")
    int update(Course course);

    @Delete("DELETE FROM t_course WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(1) FROM t_enrollment WHERE course_id=#{courseId}")
    int enrollmentCount(Long courseId);
}
