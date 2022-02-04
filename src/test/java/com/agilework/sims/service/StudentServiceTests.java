package com.agilework.sims.service;

import com.agilework.sims.dto.StudentInfo;
import com.agilework.sims.entity.Student;
import com.agilework.sims.entity.StudentV;
import com.agilework.sims.repository.FastSaveRepositoryImpl;
import com.agilework.sims.repository.StudentRepository;
import com.agilework.sims.repository.StudentVRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.Tuple;
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
public class StudentServiceTests {

    @InjectMocks
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private FastSaveRepositoryImpl<Student> saveService;
    @Mock
    private StudentVRepository studentVRepository;

    private List<StudentInfo> studentInfoList;
    private static final int normalStatus = 0;
    private static final int delStatus = 2;

    @Before
    public void init() {
        StudentInfo si1 = new StudentInfo();
        si1.setStudentNo("S0022320001");
        si1.setStudentName("张一");
        si1.setPassword("#0xF4CBE001");
        si1.setSex("女");
        si1.setMajor("SE");
        si1.setGrade(2022);
        si1.setClazz(1);

        StudentInfo si2 = new StudentInfo();
        si2.setStudentNo("S0022320011");
        si2.setStudentName("张一一");
        si2.setPassword("#0xF4CBE011");
        si2.setSex("女");
        si2.setMajor("CS");
        si2.setGrade(2021);
        si2.setClazz(5);

        StudentInfo si3 = new StudentInfo();
        si3.setStudentNo("S0022320033");
        si3.setStudentName("张三三");
        si3.setPassword("#0xF4CBE033");
        si3.setSex("女");
        si3.setMajor("CS");
        si3.setGrade(2022);
        si3.setClazz(3);

        studentInfoList = new ArrayList<>();
        studentInfoList.add(si1);
        studentInfoList.add(si2);
        studentInfoList.add(si3);

        ReflectionTestUtils.setField(studentService, "normalStatus", normalStatus);
        ReflectionTestUtils.setField(studentService, "delStatus", delStatus);
    }

    @Test
    public void testImportStudents() {
        Mockito.when(saveService.insertAll(Mockito.anyList())).thenReturn(new ArrayList<>());
        Tuple<ErrorCode, String> result = studentService.importStudents(studentInfoList);
        Assertions.assertEquals(result.getFirst(), ErrorCode.NORMAL);
    }

    @Test
    public void testQueryStudent() {
        String studentNo = "S0022320002";

        Mockito.when(studentVRepository.findByStudentNo(studentNo)).thenReturn(null);
        StudentInfo result = studentService.queryStudent(studentNo);
        Assertions.assertNull(result);

        StudentV studentV = new StudentV();
        studentV.setStudentNo(studentNo);
        Mockito.when(studentVRepository.findByStudentNo(studentNo)).thenReturn(studentV);
        result = studentService.queryStudent(studentNo);
        Assertions.assertEquals(result.getStudentNo(), studentV.getStudentNo());
    }

    @Test
    public void testUpdateStudents() {
        Mockito.when(studentRepository.updateByStudentNoAndStatus(
                Mockito.any(Student.class), Mockito.eq(normalStatus))).thenReturn(1);
        Tuple<ErrorCode, Integer> result = studentService.updateStudents(studentInfoList);
        Assertions.assertEquals(result.getFirst(), ErrorCode.NORMAL);
        Assertions.assertEquals(result.getSecond(), studentInfoList.size());
    }

    @Test
    public void testRemoveStudents() {
        List<String> studentNoList = new ArrayList<>();
        studentNoList.add("S0022320009");
        studentNoList.add("S0022320008");
        studentNoList.add("S0022320007");

        Mockito.when(studentRepository.updateStudentStatus(
                Mockito.anyString(), Mockito.eq(delStatus))).thenReturn(1);
        Tuple<ErrorCode, Integer> result = studentService.removeStudents(studentNoList);
        Assertions.assertEquals(result.getFirst(), ErrorCode.NORMAL);
        Assertions.assertEquals(result.getSecond(), studentInfoList.size());
    }
}
