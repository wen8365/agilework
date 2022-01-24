package com.agilework.sims.controller;

import com.agilework.sims.entity.Course;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.vo.LoginReq;
import com.agilework.sims.vo.LoginResp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseControllerTest {
    @Autowired
    private CourseController courseController;
    @Autowired
    private LoginController loginController;
    @Test
    public void testQueryCourses(){
        //student account
        LoginReq req = new LoginReq("MF21320138", "Abc123456=");
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        List<Course>list=courseController.queryCourses(sessionId);
        Assertions.assertEquals(list.size(),2);
        //teacher account
        LoginReq req1 = new LoginReq("MF21320137", "Abc123456=");
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        List<Course>list1=courseController.queryCourses(sessionId1);
        Assertions.assertEquals(list1.size(),6);
    }
    @Test
    public void testFindCourse(){
        //student account
        LoginReq req = new LoginReq("MF21320138", "Abc123456=");
        LoginResp resp = loginController.login(req);
        String sessionId=resp.getSessionId();
        Course course=courseController.findCourse(sessionId,"202205");
        System.out.println(course);

        //teacher account
        LoginReq req1 = new LoginReq("MF21320137", "Abc123456=");
        LoginResp resp1 = loginController.login(req1);
        String sessionId1=resp1.getSessionId();
        Course course1=courseController.findCourse(sessionId1,"202201");
        System.out.println(course1);

    }
}
