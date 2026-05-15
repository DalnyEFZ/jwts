CREATE DATABASE IF NOT EXISTS jwts_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE jwts_db;

DROP TABLE IF EXISTS t_enrollment;
DROP TABLE IF EXISTS t_course;
DROP TABLE IF EXISTS t_teacher;
DROP TABLE IF EXISTS t_student;

CREATE TABLE t_student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_no VARCHAR(32) NOT NULL UNIQUE,
    name VARCHAR(64) NOT NULL,
    password VARCHAR(128) NOT NULL,
    gender VARCHAR(16),
    phone VARCHAR(32),
    email VARCHAR(64),
    major VARCHAR(64),
    grade INT,
    class_name VARCHAR(64),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_teacher (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    teacher_no VARCHAR(32) NOT NULL UNIQUE,
    name VARCHAR(64) NOT NULL,
    title VARCHAR(64),
    department VARCHAR(64),
    phone VARCHAR(32),
    email VARCHAR(64),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(32) NOT NULL UNIQUE,
    course_name VARCHAR(128) NOT NULL,
    teacher_id BIGINT NOT NULL,
    credit INT NOT NULL,
    capacity INT NOT NULL,
    schedule_time VARCHAR(128),
    location VARCHAR(128),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_course_teacher FOREIGN KEY (teacher_id) REFERENCES t_teacher(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE t_enrollment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    score DECIMAL(5,2) DEFAULT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'SELECTED',
    selected_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_student_course(student_id, course_id),
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES t_student(id),
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) REFERENCES t_course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO t_student(student_no, name, password, gender, phone, email, major, grade, class_name)
VALUES ('20260001', '张三', '123456', '男', '13800000001', 'zhangsan@jwts.com', '软件工程', 2026, '软工1班');

INSERT INTO t_teacher(teacher_no, name, title, department, phone, email)
VALUES ('T0001', '李老师', '副教授', '计算机学院', '13900000001', 'li@jwts.com');

INSERT INTO t_course(course_code, course_name, teacher_id, credit, capacity, schedule_time, location)
VALUES ('CS101', '云原生技术实践', 1, 3, 60, '周三 08:00-10:00', 'A201');
