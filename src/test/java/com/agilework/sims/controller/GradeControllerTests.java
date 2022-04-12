package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.GradeInfo;
import com.agilework.sims.entity.Grade;
import com.agilework.sims.entity.GradeV;
import com.agilework.sims.entity.User;
import com.agilework.sims.repository.GradeRepository;
import com.agilework.sims.repository.GradeVRepository;
import com.agilework.sims.service.GradeService;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.vo.BaseResp;
import com.agilework.sims.vo.GradeDetailsQueryResp;
import com.agilework.sims.vo.GradesImportReq;
import com.agilework.sims.vo.GradesQueryResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GradeControllerTests {
    @InjectMocks
    private GradeController gradeController;
    @InjectMocks
    private GradeService gradeService;
    @Mock
    private SessionService sessionService;
    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private GradeVRepository gradeVRepository;

    @Before
    public void init() {
        ReflectionTestUtils.setField(gradeController, "gradeService", gradeService);
        ReflectionTestUtils.setField(gradeController, "roleTeacher", 1);
        ReflectionTestUtils.setField(gradeController, "roleStudent", 0);

        ReflectionTestUtils.setField(gradeService, "gradeRepository", gradeRepository);
        ReflectionTestUtils.setField(gradeService, "gradeVRepository", gradeVRepository);
    }

    @Test
    public void testGradesImport() {
        User user = new User();
        user.setUserNo("S0022320100");
        user.setRole(1);
        Session session = new Session("57c7395e-34c6-45ed-af18-0c8cb31874bb", user);
        Mockito.when(sessionService.getSession(Mockito.anyString())).thenReturn(session);

        List<GradeInfo> grades = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GradeInfo info = new GradeInfo();
            info.setExamTime(new Date(Instant.now().toEpochMilli()));
            info.setStudentNo("S002232000" + i);
            info.setUGrade(80);
            info.setURatio(35);
            info.setWGrade(0);
            info.setWRatio(0);
            info.setExpGrade(90);
            info.setExpRatio(35);
            info.setExaGrade(70);
            info.setExaRatio(30);
            grades.add(info);
        }
        GradesImportReq req = new GradesImportReq();
        req.setCourseNo("1001");
        req.setGrades(grades);

        BaseResp resp = gradeController.gradesImport(req, Mockito.anyString());
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
    }

    @Test
    public void testGradesImportWithoutPermission() {
        User user = new User();
        Session session = new Session("", user);
        Mockito.when(sessionService.getSession(Mockito.anyString())).thenReturn(session);

        BaseResp resp = gradeController.gradesImport(new GradesImportReq(), Mockito.anyString());
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.GRADE_IMPORT_PERMISSION_DENIED.getCode());
    }

    @Test
    public void testGradesQuery(){
        List<GradeV> gradeList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GradeV gradeV = new GradeV();
            gradeV.setCourseNo("100" + i);
            gradeV.setStudentNo("S002132000" + i);
            gradeV.setFinalGrade(60.0 + Math.pow(i, 2));
        }
        Mockito.when(gradeVRepository.findByStudentNoAndCourseNoIn(Mockito.anyString(), Mockito.anyList()))
                .thenReturn(gradeList);

        List<String> courseNoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            courseNoList.add("100" + i);
        }
        GradesQueryResp resp = gradeController.gradesQuery("S0022320009", courseNoList);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
        Assertions.assertEquals(resp.getGradeSummaries().size(), gradeList.size());
    }

    @Test
    public void testGradesQueryWithoutCourseNo(){
        GradesQueryResp resp = gradeController.gradesQuery("S0022320009", new ArrayList<>());
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.GRADE_QUERY_NONE_COURSE_SPECIFIC.getCode());
        Assertions.assertNull(resp.getGradeSummaries());
    }

    @Test
    public void testGradeDetailQuery() {
        User user = new User();
        user.setUserNo("S0022320005");
        user.setRole(1);
        Session session = new Session("86a7f45d-a879-42c6-b0b5-bc23d8b1ef18", user);
        Mockito.when(sessionService.getSession(Mockito.anyString())).thenReturn(session);

        List<Grade> grades = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Grade grade = new Grade();
            grade.setExamTime(new Date(Instant.now().toEpochMilli()));
            grade.setStudentNo("S002232010" + i);
            grade.setUGrade(80);
            grade.setURatio(35);
            grade.setWGrade(0);
            grade.setWRatio(0);
            grade.setExpGrade(90);
            grade.setExpRatio(35);
            grade.setExaGrade(70);
            grade.setExaRatio(30);
            grades.add(grade);
        }
        Mockito.when(gradeRepository.findByCourseNo(Mockito.anyString())).thenReturn(grades);

        GradeDetailsQueryResp resp = gradeController.gradeDetailQuery("1003",
                "86a7f45d-a879-42c6-b0b5-bc23d8b1ef18");
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
        Assertions.assertEquals(resp.getGradeDetailList().size(), grades.size());
    }

    @Test
    public void testGradeDetailQueryWithoutPermission() {
        User user = new User();
        Session session = new Session("", user);
        Mockito.when(sessionService.getSession(Mockito.anyString())).thenReturn(session);

        BaseResp resp = gradeController.gradeDetailQuery("1007",
                "81c7f3b7-de0d-44e5-8555-0a5f7b22beb9");
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.GRADE_QUERY_PERMISSION_DENIED.getCode());
    }
}
