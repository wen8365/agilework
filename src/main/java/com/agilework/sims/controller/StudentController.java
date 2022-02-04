package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.entity.StudentV;
import com.agilework.sims.entity.User;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.vo.StudentsImportReq;
import com.agilework.sims.service.StudentService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.Tuple;
import com.agilework.sims.util.UserVerifier;
import com.agilework.sims.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static com.agilework.sims.util.ErrorCode.*;

@RestController
public class StudentController {
    private static final String TAG = "StudentController";

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserVerifier verifier;

    @Autowired
    private SessionService sessionService;

    @Value("${role.teacher}")
    private int roleTeacher;

    @Value("${role.student}")
    private int roleStudent;

    @PostMapping("/studentsImport")
    public StudentsImportResp studentsImport(@RequestHeader("sessionId") String sessionId,
                                             @RequestBody @NonNull StudentsImportReq req) {
        SLogger.info(TAG, "import students, sessionId=" + sessionId);
        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() < roleTeacher) {
            SLogger.warn(TAG, STUDENT_IMPORT_PERMISSION_DENIED.getReason());
            return new StudentsImportResp(STUDENT_IMPORT_PERMISSION_DENIED);
        }

        List<StudentInfo> students = req.getStudents();
        List<StudentInfo> invalids = new ArrayList<>();
        filterInvalidStudents(students, invalids, this::isStudentInvalid);

        SLogger.info(TAG, "total=" + students.size() + " ,invalid=" + invalids.size());
        Tuple<ErrorCode, String> tuple = studentService.importStudents(students);
        ErrorCode errorCode = tuple.getFirst();
        String duplicateNo = tuple.getSecond();

        StudentsImportResp resp = new StudentsImportResp();
        resp.setInvalidStudents(invalids);
        resp.setErrCode(errorCode.getCode());
        resp.setDuplicate(duplicateNo);
        return resp;
    }

    private void filterInvalidStudents(List<StudentInfo> allStudents, List<StudentInfo> filter,
                                       Function<StudentInfo, Boolean> fun) {
        Iterator<StudentInfo> iterator = allStudents.iterator();
        while (iterator.hasNext()) {
            StudentInfo student = iterator.next();
            if (fun.apply(student)) {
                filter.add(student);
                iterator.remove();
            }
        }
    }

    private boolean isStudentInvalid(StudentInfo student) {
        return student.getStudentNo() == null || verifier.isUserNameInvalid(student.getStudentNo())
                || student.getStudentName() == null || verifier.isRealNameInvalid(student.getStudentName())
                || student.getPassword() == null || verifier.isPasswordInvalid(student.getPassword())
                || student.getSex() == null || verifier.isSexInvalid(student.getSex())
                || student.getMajor() == null || verifier.isMajorInvalid(student.getMajor())
                || student.getGrade() == null || student.getGrade() <= 0
                || student.getClazz() == null || student.getClazz() <= 0;
    }

    @GetMapping("/studentQuery")
    public StudentQueryResp studentQuery(@RequestHeader("sessionId") String sessionId,
                                         @RequestParam("studentNo") @NonNull String studentNo) {
        SLogger.info(TAG, "query student, sessionId=" + sessionId);
        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() > roleStudent || (user.getRole() == roleStudent)
                && user.getUserNo().equals(studentNo)) {
            // only allow teacher update students, or students update themselves
            StudentQueryResp resp = new StudentQueryResp();
            StudentInfo studentInfo = studentService.queryStudent(studentNo);
            if (studentInfo != null) {
                resp.setStudent(studentInfo);
            } else {
                resp.setErrCode(STUDENT_QUERY_NOT_EXISTS.getCode());
            }
            return resp;
        }
        SLogger.warn(TAG, STUDENT_QUERY_PERMISSION_DENIED.getReason());
        return new StudentQueryResp(STUDENT_QUERY_PERMISSION_DENIED);
    }

    @PostMapping("/studentSelectByConditionBySearchTextByPage")
    public Page<StudentV> studentsQuery(@RequestBody StudentsQueryReq req) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setStudentNo(req.getStudentNo());
        if (!StringUtils.hasLength(studentInfo.getStudentNo()))
            studentInfo.setStudentNo(req.getSearchText());
        studentInfo.setStudentName(req.getStudentName());
        if (!StringUtils.hasLength(studentInfo.getStudentName()))
            studentInfo.setStudentName(req.getSearchText());
        studentInfo.setSex(req.getSex());
        studentInfo.setMajor(req.getMajor());
        studentInfo.setGrade(req.getGrade());
        studentInfo.setClazz(req.getClazz());
        return studentService.queryStudents(studentInfo, req.getCurPage(), req.getPageSize());
    }

    @PostMapping("/studentUpdate")
    public BaseResp studentUpdate(@RequestHeader("sessionId") String sessionId,
                                  @RequestBody @NonNull StudentUpdateReq req) {
        SLogger.info(TAG, "update student, sessionId=" + sessionId);
        StudentInfo studentInfo = req.getStudentInfo();
        if (isStudentInvalid2(studentInfo)) {
            SLogger.warn(TAG, STUDENT_UPDATE_INFO_INVALID.getReason());
            return new StudentQueryResp(STUDENT_UPDATE_INFO_INVALID);
        }

        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() > roleStudent) {
            // only allow teacher update students
            ErrorCode errorCode = studentService.updateStudent(studentInfo);
            return new StudentQueryResp(errorCode);
        }
        SLogger.warn(TAG, STUDENT_UPDATE_PERMISSION_DENIED.getReason());
        return new StudentQueryResp(STUDENT_UPDATE_PERMISSION_DENIED);
    }

    private boolean isStudentInvalid2(StudentInfo student) {
        return student.getStudentNo() == null || verifier.isUserNameInvalid(student.getStudentNo())
                || student.getStudentName() != null && verifier.isRealNameInvalid(student.getStudentName())
                || student.getPassword() != null && verifier.isPasswordInvalid(student.getPassword())
                || student.getSex() != null && verifier.isSexInvalid(student.getSex())
                || student.getMajor() != null && verifier.isMajorInvalid(student.getMajor())
                || student.getGrade() != null && student.getGrade() <= 0
                || student.getClazz() != null && student.getClazz() <= 0;
    }

    @PostMapping("/studentBatchUpdate")
    public StudentsUpdateResp studentsUpdate(@RequestHeader("sessionId") String sessionId,
                                  @RequestBody @NonNull StudentsImportReq req) {
        SLogger.info(TAG, "update multiple students, sessionId=" + sessionId);
        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() < roleTeacher) {
            SLogger.warn(TAG, STUDENT_UPDATE_PERMISSION_DENIED.getReason());
            return new StudentsUpdateResp(STUDENT_UPDATE_PERMISSION_DENIED);
        }

        List<StudentInfo> students = req.getStudents();
        List<StudentInfo> invalids = new ArrayList<>();
        filterInvalidStudents(students, invalids, this::isStudentInvalid2);

        SLogger.info(TAG, "total=" + students.size() + " ,invalid=" + invalids.size());
        Tuple<ErrorCode, Integer> tuple = studentService.updateStudents(students);
        ErrorCode errorCode = tuple.getFirst();
        int rows = tuple.getSecond();

        StudentsUpdateResp resp = new StudentsUpdateResp();
        resp.setInvalidStudents(invalids);
        resp.setErrCode(errorCode.getCode());
        resp.setRows(rows);
        return resp;
    }

    @PostMapping("/studentBatchRemove")
    public StudentsUpdateResp studentsRemove(@RequestHeader("sessionId") String sessionId,
                                             @RequestBody @NonNull StudentRmReq req) {
        SLogger.info(TAG, "remove multiple students, sessionId=" + sessionId);
        Session session = sessionService.getSession(sessionId);
        User user = session.getUser();
        if (user.getRole() < roleTeacher) {
            SLogger.warn(TAG, STUDENT_REMOVE_PERMISSION_DENIED.getReason());
            return new StudentsUpdateResp(STUDENT_REMOVE_PERMISSION_DENIED);
        }

        List<String> students = req.getStudentNoList();
        int total = students.size();
        students.removeIf(s -> verifier.isUserNameInvalid(s));

        SLogger.info(TAG, "total=" + total + " ,invalid=" + (total - students.size()));
        Tuple<ErrorCode, Integer> tuple = studentService.removeStudents(students);
        ErrorCode errorCode = tuple.getFirst();
        int rows = tuple.getSecond();

        StudentsUpdateResp resp = new StudentsUpdateResp();
        resp.setErrCode(errorCode.getCode());
        resp.setRows(rows);
        return resp;
    }
}
