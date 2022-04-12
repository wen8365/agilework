package com.agilework.sims.util;

import com.agilework.sims.dto.GradeInfo;
import com.agilework.sims.entity.Grade;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GradeConverter {
    public static Grade convert2Grade(String courseNo, GradeInfo info) {
        Grade grade = new Grade();
        grade.setExamTime(info.getExamTime());
        grade.setCourseNo(courseNo);
        grade.setStudentNo(info.getStudentNo());
        grade.setUGrade(info.getUGrade());
        grade.setURatio(info.getURatio());
        grade.setWGrade(info.getWGrade());
        grade.setWRatio(info.getWRatio());
        grade.setExpGrade(info.getExpGrade());
        grade.setExpRatio(info.getExpRatio());
        grade.setExaGrade(info.getExaGrade());
        grade.setExaRatio(info.getExaRatio());
        return grade;
    }

    public static GradeInfo convert2GradeInfo(Grade grade) {
        GradeInfo info = new GradeInfo();
        info.setExamTime(grade.getExamTime());
        info.setStudentNo(grade.getStudentNo());
        info.setUGrade(grade.getUGrade());
        info.setURatio(grade.getURatio());
        info.setWGrade(grade.getWGrade());
        info.setWRatio(grade.getWRatio());
        info.setExpGrade(grade.getExpGrade());
        info.setExpRatio(grade.getExpRatio());
        info.setExaGrade(grade.getExaGrade());
        info.setExaRatio(grade.getExaRatio());
        return info;
    }
}
