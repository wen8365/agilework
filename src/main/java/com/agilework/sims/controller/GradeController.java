package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.GradeInfo;
import com.agilework.sims.dto.GradeSummary;
import com.agilework.sims.entity.Grade;
import com.agilework.sims.entity.User;
import com.agilework.sims.service.GradeService;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.GradeConverter;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.Tuple;
import com.agilework.sims.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GradeController {
    private static final String TAG = "GradeController";

    private final GradeService gradeService;
    private final SessionService sessionService;

    @Value("${role.teacher}")
    private int roleTeacher;
    @Value("${role.student}")
    private int roleStudent;

    @PostMapping("/gradeImport")
    public BaseResp gradesImport(@RequestBody GradesImportReq req, @RequestHeader String sessionId) {
        SLogger.info(TAG, "import grades, sessionId=" + sessionId);
        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() < roleTeacher) {
            SLogger.warn(TAG, ErrorCode.GRADE_IMPORT_PERMISSION_DENIED.getReason());
            return new StudentsImportResp(ErrorCode.GRADE_IMPORT_PERMISSION_DENIED);
        }

        List<Grade> grades = convertToGradeInfo(req.getCourseNo(), req.getGrades());
        ErrorCode errCode = gradeService.importGrades(grades);
        SLogger.info(TAG, "import grades result, errCode=" + errCode.getCode());
        return new BaseResp(errCode);
    }

    private List<Grade> convertToGradeInfo(String courseNo, List<GradeInfo> gradeInfos) {
        List<Grade> grades = new ArrayList<>();
        for (GradeInfo info : gradeInfos) {
            Grade grade = GradeConverter.convert2Grade(courseNo, info);
            grades.add(grade);
        }
        return grades;
    }

    @GetMapping("/gradesQuery")
    public GradesQueryResp gradesQuery(@RequestParam String studentNo,
                                       @RequestParam List<String> courseNoList) {
        SLogger.info(TAG, "query student grades, studentNo=" + studentNo
                + ", course.size=" + courseNoList.size());
        if (courseNoList.isEmpty()) {
            SLogger.error(TAG, ErrorCode.GRADE_QUERY_NONE_COURSE_SPECIFIC.getReason());
            return new GradesQueryResp(ErrorCode.GRADE_QUERY_NONE_COURSE_SPECIFIC);
        }
        Tuple<ErrorCode, List<GradeSummary>> tuple = gradeService.queryStudentGrades(studentNo, courseNoList);
        SLogger.info(TAG, "query student grades result, errCode=" + tuple.getFirst());
        return new GradesQueryResp(tuple.getFirst(), tuple.getSecond());
    }

    @GetMapping("/gradeDetailsQuery")
    public GradeDetailsQueryResp gradeDetailQuery(@RequestParam String courseNo,
                                                   @RequestHeader String sessionId) {
        SLogger.info(TAG, "query course grade details, courseNo=" + courseNo);
        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() < roleTeacher) {
            SLogger.warn(TAG, ErrorCode.GRADE_QUERY_PERMISSION_DENIED.getReason());
            return new GradeDetailsQueryResp(ErrorCode.GRADE_QUERY_PERMISSION_DENIED);
        }
        Tuple<ErrorCode, List<GradeInfo>> tuple = gradeService.queryGradeDetails(courseNo);
        SLogger.info(TAG, "query course grade details result, errCode=" + tuple.getFirst());

        GradeDetailsQueryResp resp = new GradeDetailsQueryResp();
        resp.setCourseNo(courseNo);
        resp.setGradeDetailList(tuple.getSecond());
        return resp;
    }
}
