package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.entity.User;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.service.StudentService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.Tuple;
import com.agilework.sims.util.UserVerifier;
import com.agilework.sims.vo.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTests {

    @InjectMocks
    private StudentController controller;
    @Mock
    private StudentService studentService;
    @Mock
    private UserVerifier verifier;
    @Mock
    private SessionService sessionService;
    private static final int roleTeacher = 1;
    private static final int roleStudent = 0;

    private static final String SESSION_ID_1 = "717e0a1a-12f7-494f-9a31-82abe82dfe0c";
    private static final String SESSION_ID_2 = "57c7395e-34c6-45ed-af18-0c8cb31874bb";
    private static final String STUDENT_NO = "S0022320001";
    private static final String TEACHER_NO = "T00896745";
    private StudentsImportReq importReq;

    @Before
    public void init() {
        importReq = new StudentsImportReq();

        List<StudentInfo> studentInfoList = new ArrayList<>();

        StudentInfo si1 = new StudentInfo();
        si1.setOrder(0);
        si1.setStudentNo(STUDENT_NO);
        si1.setStudentName("张三三");
        si1.setPassword("#0xF4CBE001");
        si1.setSex("女");
        si1.setMajor("SE");
        si1.setGrade(2022);
        si1.setClazz(1);
        studentInfoList.add(si1);
        importReq.setStudents(studentInfoList);

        User user1 = new User();
        user1.setRole(roleStudent);
        user1.setUserNo(STUDENT_NO);
        Session session1 = new Session(SESSION_ID_1, user1);
        Mockito.when(sessionService.getSession(SESSION_ID_1)).thenReturn(session1);

        User user2 = new User();
        user2.setRole(roleTeacher);
        user2.setUserNo(TEACHER_NO);
        Session session2 = new Session(SESSION_ID_2, user2);
        Mockito.when(sessionService.getSession(SESSION_ID_2)).thenReturn(session2);

        ReflectionTestUtils.setField(controller, "roleTeacher", roleTeacher);
        ReflectionTestUtils.setField(controller, "roleStudent", roleStudent);
    }

    @Test
    public void testStudentsImport() {
        StudentsImportResp resp = controller.studentsImport(SESSION_ID_1, importReq);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.STUDENT_IMPORT_PERMISSION_DENIED.getCode());

        Mockito.when(studentService.importStudents(Mockito.anyList()))
                .thenReturn(new Tuple<>(ErrorCode.NORMAL, null));
        resp = controller.studentsImport(SESSION_ID_2, importReq);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
    }

    @Test
    public void testStudentQuery() {
        // test query another student with student identity
        StudentQueryResp resp = controller.studentQuery(SESSION_ID_1, "");
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.STUDENT_QUERY_PERMISSION_DENIED.getCode());

        // test student not exist
        Mockito.when(studentService.queryStudent(Mockito.anyString())).thenReturn(null);
        resp = controller.studentQuery(SESSION_ID_2, TEACHER_NO);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.STUDENT_QUERY_NOT_EXISTS.getCode());

        StudentInfo studentInfo = new StudentInfo();
        Mockito.when(studentService.queryStudent(Mockito.anyString())).thenReturn(studentInfo);
        resp = controller.studentQuery(SESSION_ID_1, STUDENT_NO);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
        Assertions.assertEquals(resp.getStudent(), studentInfo);
    }

    @Test
    public void testStudentUpdate() {
        StudentUpdateReq req = new StudentUpdateReq();
        req.setStudentInfo(new StudentInfo());

        BaseResp resp = controller.studentUpdate(SESSION_ID_1, req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.STUDENT_UPDATE_PERMISSION_DENIED.getCode());

        Mockito.when(studentService.updateStudent(Mockito.any())).thenReturn(ErrorCode.NORMAL);
        resp = controller.studentUpdate(SESSION_ID_2, req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
    }

    @Test
    public void testStudentRemove() {
        List<String> studentNoList = new ArrayList<>();
        studentNoList.add(STUDENT_NO);
        StudentRmReq req = new StudentRmReq();
        req.setStudentNoList(studentNoList);

        StudentsUpdateResp resp = controller.studentsRemove(SESSION_ID_1, req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.STUDENT_REMOVE_PERMISSION_DENIED.getCode());

        Mockito.when(verifier.isUserNameInvalid(Mockito.anyString())).thenReturn(false);
        Mockito.when(studentService.removeStudents(studentNoList))
                .thenReturn(new Tuple<>(ErrorCode.NORMAL, 1));
        resp = controller.studentsRemove(SESSION_ID_2, req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
        Assertions.assertEquals(resp.getRows(), 1);
    }
}
