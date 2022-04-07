DROP TABLE IF EXISTS t_course_schedule;
CREATE TABLE t_course_schedule (
dayOfWeek INTEGER NOT NULL COMMENT '星期几', 
lessonNo INTEGER NOT NULL COMMENT '第x节课',
courseNo varchar(20) NOT NULL COMMENT '课程号',
address varchar(64) NOT NULL COMMENT '上课地点',
startWeek INTEGER NOT NULL COMMENT '课程开始的周号',
endWeek INTEGER NOT NULL COMMENT '课程结束的周号',
PRIMARY KEY (dayOfWeek, lessonNo, courseNo),
FOREIGN KEY (courseNo) REFERENCES t_course(courseNo),
CONSTRAINT CHECK (dayOfWeek IN (1, 2, 3, 4, 5, 6,7)) 
);