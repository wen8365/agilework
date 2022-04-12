DROP TABLE IF EXISTS t_course_schedule;
CREATE TABLE t_course_schedule (
dayOfWeek INTEGER NOT NULL COMMENT '星期几', 
lessonNo INTEGER NOT NULL COMMENT '第x节课',
courseNo VARCHAR(20) NOT NULL COMMENT '课程号',
address VARCHAR(64) NOT NULL COMMENT '上课地点',
startWeek INTEGER NOT NULL COMMENT '课程开始的周号',
endWeek INTEGER NOT NULL COMMENT '课程结束的周号',
PRIMARY KEY (dayOfWeek, lessonNo, courseNo),
FOREIGN KEY (courseNo) REFERENCES t_course(courseNo),
CONSTRAINT CHECK (dayOfWeek IN (1, 2, 3, 4, 5, 6,7))
);

DROP TABLE IF EXISTS t_course_grade;
CREATE TABLE t_course_grade (
examTime DATETIME NOT NULL,
courseNo VARCHAR(20) NOT NULL COMMENT '课程号',
studentNo VARCHAR(20) NOT NULL COMMENT '学号',
uGrade DOUBLE COMMENT' 平时成绩',
uRatio INTEGER COMMENT '平时成绩占比',
wGrade DOUBLE COMMENT '作业成绩',
wRatio INTEGER COMMENT '作业成绩占比',
expGrade DOUBLE COMMENT '实验成绩',
expRatio INTEGER COMMENT '实验成绩占比',
exaGrade DOUBLE COMMENT '考试成绩',
exaRatio INTEGER COMMENT '考试成绩占比',
PRIMARY KEY (examTime, courseNo, studentNo),
FOREIGN KEY (courseNo) REFERENCES t_course(courseNo),
FOREIGN KEY (studentNo) REFERENCES t_student(studentNo)
);

CREATE OR REPLACE VIEW v_grade AS
SELECT courseNo, studentNo,
            MAX((uGrade * uRatio
		+ wGrade * wRatio
		+ expGrade * expRatio
		+ exaGrade * exaRatio)/ 100)
		 as finalGrade
FROM t_course_grade GROUP BY courseNo, studentNo