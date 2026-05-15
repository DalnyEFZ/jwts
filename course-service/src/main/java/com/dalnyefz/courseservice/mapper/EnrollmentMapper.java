package com.dalnyefz.courseservice.mapper;

import com.dalnyefz.courseservice.model.Enrollment;
import com.dalnyefz.courseservice.model.StudentCourseView;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EnrollmentMapper {

    @Insert("INSERT INTO t_enrollment(student_id,course_id,score,status) VALUES(#{studentId},#{courseId},#{score},#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Enrollment enrollment);

    @Select("SELECT * FROM t_enrollment WHERE student_id=#{studentId} AND course_id=#{courseId} LIMIT 1")
    Enrollment findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Delete("DELETE FROM t_enrollment WHERE student_id=#{studentId} AND course_id=#{courseId}")
    int deleteByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Update("UPDATE t_enrollment SET score=#{score},status=#{status},updated_at=NOW() WHERE student_id=#{studentId} AND course_id=#{courseId}")
    int updateScoreAndStatus(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("score") Double score, @Param("status") String status);

    @Select("SELECT e.id AS enrollment_id,e.student_id,e.course_id,e.score,e.status,c.course_code,c.course_name,c.credit,c.schedule_time,c.location FROM t_enrollment e JOIN t_course c ON e.course_id=c.id WHERE e.student_id=#{studentId} ORDER BY c.id")
    List<StudentCourseView> findCoursesByStudent(Long studentId);
}
