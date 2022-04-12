package com.agilework.sims.controller;

import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.StudentCourseRelationship;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.vo.LoginReq;
import com.agilework.sims.vo.LoginResp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CourseControllerTest {
    private static final String TAG = "CourseControllerTest";
    @Autowired
    private CourseController courseController;
    @Autowired
    private LoginController loginController;
    private static final String USER_NAME_1 = "MF21320138";
    private static final String USER_NAME_2 = "MF21320137";
    private static final String P = "Abc123456=";
    private static final String CNO_1 = "202201=";
    private static final String SUCCESS = "success";
    @Test
    public void testQueryCourses(){
        //student account
        LoginReq req = new LoginReq(USER_NAME_1, P);
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        List<Course>list=courseController.queryCourses(sessionId);
        Assertions.assertEquals(list.size(),2);
        //teacher account
        LoginReq req1 = new LoginReq(USER_NAME_2, P);
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        List<Course>list1=courseController.queryCourses(sessionId1);
        Assertions.assertEquals(list1.size(),6);
    }
    @Test
    public void testFindCourse(){
        //student account
        LoginReq req = new LoginReq(USER_NAME_1, P);
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        Course course=courseController.findCourse(sessionId,"202205");
        SLogger.info(TAG, "course=" + course);

        //teacher account
        LoginReq req1 = new LoginReq(USER_NAME_2, P);
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        Course course1=courseController.findCourse(sessionId1,CNO_1);
        SLogger.info(TAG, "course=" + course1);
    }
    @Test
    public void testAddCourses(){
        List<Course>list=new ArrayList<>();
        Course course=new Course();
        course.setCourseNo("202207");
        course.setCourseCode("mat");
        course.setCourseName("math");
        course.setMajor("se");
        Course course1=new Course();
        course1.setCourseNo("202208");
        course1.setCourseCode("phs");
        course1.setCourseName("physics");
        course1.setMajor("se");
        list.add(course);
        list.add(course1);
        Map<String, Object>map=courseController.addCourses(list);
        Assertions.assertEquals(map.get(SUCCESS),true);
    }
    @Test
    public void testChangeCourseStats(){
        LoginReq req = new LoginReq(USER_NAME_1, P);
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        SLogger.info(TAG, "sessionId=" + sessionId);
        Map<String, Object>map=courseController.changeCourseStatus(CNO_1,1);
        Assertions.assertEquals(map.get(SUCCESS),true);

    }
    @Test
    public void testInsertCourseRecord(){
        List<StudentCourseRelationship>list=new ArrayList<>();
        StudentCourseRelationship sc1=new StudentCourseRelationship();
        sc1.setCourseNo("212201");
        sc1.setStudentNo(USER_NAME_1);
        sc1.setElectiveTime(new Date());
        StudentCourseRelationship sc2=new StudentCourseRelationship();
        sc2.setCourseNo("212205");
        sc2.setStudentNo(USER_NAME_1);
        sc2.setElectiveTime(new Date());
        list.add(sc1);
        list.add(sc2);
        Map<String, Object>map=courseController.addCourseRecords(list);
        Assertions.assertEquals(map.get(SUCCESS),true);
    }
    @Test
    public void testQueryCourseRecord(){
        LoginReq req = new LoginReq(USER_NAME_1, P);
        LoginResp resp = loginController.login(req);
        Page<Course> res=courseController.queryCourseRecords(USER_NAME_1, 0, 10);
        Assertions.assertEquals(res.getTotalElements(),2);
    }
    @Test
    public void testUpdateCourse(){
        LoginReq req1 = new LoginReq(USER_NAME_2, P);
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        Course course1=courseController.findCourse(sessionId1,CNO_1);
        course1.setCourseName("software engineering");
        course1.setMajor("se");
        Course course=courseController.updateCourse(course1);
        Assertions.assertEquals(course.getCourseName(),"software engineering");
        Assertions.assertEquals(course.getMajor(),"se");
    }
    @Test
    public void testDeleteCourse(){
        Map<String, Object>map=courseController.deleteCourse("202208");
        Assertions.assertEquals(map.get(SUCCESS),true);
    }
}