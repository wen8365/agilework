package com.agilework.sims.service;

import com.agilework.sims.dto.GradeInfo;
import com.agilework.sims.dto.GradeSummary;
import com.agilework.sims.entity.Grade;
import com.agilework.sims.entity.GradeV;
import com.agilework.sims.repository.GradeRepository;
import com.agilework.sims.repository.GradeVRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.GradeConverter;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {
    private static final String TAG = "GradeService";

    private final GradeRepository gradeRepository;
    private final GradeVRepository gradeVRepository;

    public ErrorCode importGrades(List<Grade> grades) {
        List<Grade> result = gradeRepository.saveAll(grades);
        SLogger.info(TAG, "import student complete, total=" + grades.size() + ", result=" + result.size());
        return ErrorCode.NORMAL;
    }

    public Tuple<ErrorCode, List<GradeSummary>> queryStudentGrades(String studentNo,
                                                                   List<String> courseNoList) {
        List<GradeV> gradeList = gradeVRepository.findByStudentNoAndCourseNoIn(studentNo, courseNoList);
        SLogger.info(TAG, "query student grades complete, " +
                "courses=" + courseNoList.size() + ", grades=" + gradeList.size());

        List<GradeSummary> gradeSummaries = new ArrayList<>();
        for (GradeV gradeV : gradeList) {
            GradeSummary summary = new GradeSummary();
            summary.setCourseNo(gradeV.getCourseNo());
            summary.setGrade(gradeV.getFinalGrade());
            gradeSummaries.add(summary);
        }
        return new Tuple<>(ErrorCode.NORMAL, gradeSummaries);
    }

    public Tuple<ErrorCode, List<GradeInfo>> queryGradeDetails(String courseNo) {
        List<Grade> grades = gradeRepository.findByCourseNo(courseNo);
        SLogger.info(TAG, "query course grade details complete, grades=" + grades.size());

        List<GradeInfo> gradeInfoList = new ArrayList<>();
        for (Grade grade : grades) {
            GradeInfo info = GradeConverter.convert2GradeInfo(grade);
            gradeInfoList.add(info);
        }
        return new Tuple<>(ErrorCode.NORMAL, gradeInfoList);
    }
}
