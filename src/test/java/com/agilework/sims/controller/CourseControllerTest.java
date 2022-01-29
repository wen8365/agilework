package com.agilework.sims.controller;

import com.agilework.sims.entity.Course;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.vo.LoginReq;
import com.agilework.sims.vo.LoginResp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CourseControllerTest {
    private static final String TAG = "CourseControllerTest";
    @Autowired
    private CourseController courseController;
    @Autowired
    private LoginController loginController;
    private String password = "Abc123456=";
    @Test
    public void testQueryCourses(){
        //student account
        LoginReq req = new LoginReq("MF21320138", password);
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        List<Course>list=courseController.queryCourses(sessionId);
        Assertions.assertEquals(list.size(),2);
        //teacher account
        LoginReq req1 = new LoginReq("MF21320137", password);
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        List<Course>list1=courseController.queryCourses(sessionId1);
        Assertions.assertEquals(list1.size(),6);
    }
    @Test
    public void testFindCourse(){
        //student account
        LoginReq req = new LoginReq("MF21320138", password);
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        Course course=courseController.findCourse(sessionId,"202205");
        SLogger.info(TAG, "course=" + course);

        //teacher account
        LoginReq req1 = new LoginReq("MF21320137", password);
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        Course course1=courseController.findCourse(sessionId1,"202201");
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
        Assertions.assertEquals(map.get("success"),true);
    }
}
