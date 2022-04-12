package com.agilework.sims.util;

import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.entity.Student;
import com.agilework.sims.entity.StudentV;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentConverter {
    public static Student convert2Student(StudentInfo info) {
        Student student = new Student();
        student.setStudentNo(info.getStudentNo());
        student.setStudentName(info.getStudentName());
        student.setSex(info.getSex());
        student.setMajor(info.getMajor());
        student.setGrade(info.getGrade());
        student.setClazz(info.getClazz());
        student.setPassword(info.getPassword());
        return student;
    }

    public static StudentInfo convertToStudentInfo(StudentV student) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setStudentNo(student.getStudentNo());
        studentInfo.setStudentName(student.getStudentName());
        studentInfo.setSex(student.getSex());
        studentInfo.setMajor(student.getMajor());
        studentInfo.setGrade(student.getGrade());
        studentInfo.setClazz(student.getClazz());
        return studentInfo;
    }
}
