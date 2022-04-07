DROP TABLE IF EXISTS t_student;
CREATE TABLE t_student (
id BIGINT NOT NULL UNIQUE PRIMARY KEY,
studentNo VARCHAR(20) NOT NULL COMMENT '学号',
studentName VARCHAR(32) NOT NULL COMMENT '学生姓名',
password VARCHAR(256) NOT NULL COMMENT '密码',
sex VARCHAR(2) CHECK(sex='男' OR sex='女'),
major VARCHAR(32) NOT NULL COMMENT '专业名称',
grade INTEGER NOT NULL COMMENT '年级',
class INTEGER NOT NULL COMMENT '班级',
status INTEGER(2) DEFAULT 0 CHECK(status=0 OR status=1 OR status=2),
UNIQUE KEY c_uni_sno_status (studentNo, status)
);

DROP TABLE IF EXISTS t_admin;
CREATE TABLE t_admin (
id BIGINT NOT NULL UNIQUE PRIMARY KEY,
teacherNo VARCHAR(20) NOT NULL COMMENT '教工号',
teacherName VARCHAR(32) NOT NULL COMMENT '教师姓名',
password VARCHAR(256) NOT NULL COMMENT '密码',
phone VARCHAR(20) NOT NULL COMMENT '手机号',
email VARCHAR(64) NOT NULL COMMENT '邮箱',
status INTEGER(2) DEFAULT 0 CHECK(status=0 OR status=1 OR status=2),
UNIQUE KEY c_uni_tno_status (teacherNo, status)
);

DROP VIEW IF EXISTS v_user;
CREATE OR REPLACE VIEW v_user AS 
SELECT userNo, userName, password, role FROM ( 
SELECT studentNo AS userNo, studentName AS userName, password, 0 AS role FROM t_student WHERE t_student.status = 0 
UNION ALL 
SELECT teacherNo AS userNo, teacherName AS userName, password, 1 AS role FROM t_admin WHERE t_admin.status = 0
) AS users;

DROP TABLE IF EXISTS t_course_schedule;
DROP TABLE IF EXISTS t_course;
CREATE TABLE t_course (
courseNo VARCHAR(20) NOT NULL UNIQUE PRIMARY KEY COMMENT '课程号',
courseCode VARCHAR(5) NOT NULL COMMENT '课程代码',
courseName VARCHAR(32) NOT NULL COMMENT '课程名称',
major VARCHAR(20) NOT NULL COMMENT '所属专业',
published BOOLEAN NOT NULL DEFAULT FALSE COMMENT '发布状态',
examTime DATETIME COMMENT '考试时间',
examDuration INTEGER DEFAULT 0 COMMENT '考试时长（小时）',
examAddr VARCHAR(32) COMMENT '考试地点'
);

DROP TABLE IF EXISTS t_sc_relationship;
CREATE TABLE t_sc_relationship (
id BIGINT NOT NULL UNIQUE PRIMARY KEY,
studentNo VARCHAR(20) NOT NULL,
courseNo VARCHAR(20) NOT NULL,
electiveTime DATETIME NOT NULL COMMENT '选修时间'
);

DROP VIEW IF EXISTS v_student;
CREATE OR REPLACE VIEW v_student AS
SELECT studentName, studentNo, sex, major, grade, class FROM t_student WHERE status = 0; 

DROP TABLE IF EXISTS t_course_schedule;
CREATE TABLE  t_course_schedule (
weekNo INTEGER NOT NULL,
dayOfWeek INTEGER NOT NULL COMMENT 'day of week', 
course1 VARCHAR(32) COMMENT '第x节课，下同',
course2 VARCHAR(32),
course3 VARCHAR(32),
course4 VARCHAR(32),
course5 VARCHAR(32),
course6 VARCHAR(32),
course7 VARCHAR(32),
course8 VARCHAR(32),
course9 VARCHAR(32),
course10 VARCHAR(32),
course11 VARCHAR(32),
PRIMARY KEY (weekNo, dayOfWeek),
FOREIGN KEY (course1) REFERENCES t_course(courseNo),
FOREIGN KEY (course2) REFERENCES t_course(courseNo),
FOREIGN KEY (course3) REFERENCES t_course(courseNo),
FOREIGN KEY (course4) REFERENCES t_course(courseNo),
FOREIGN KEY (course5) REFERENCES t_course(courseNo),
FOREIGN KEY (course6) REFERENCES t_course(courseNo),
FOREIGN KEY (course7) REFERENCES t_course(courseNo),
FOREIGN KEY (course8) REFERENCES t_course(courseNo),
FOREIGN KEY (course9) REFERENCES t_course(courseNo),
FOREIGN KEY (course10) REFERENCES t_course(courseNo),
FOREIGN KEY (course11) REFERENCES t_course(courseNo),
CONSTRAINT CHECK (dayOfWeek IN (0, 1, 2, 3, 4, 5, 6)) 
);