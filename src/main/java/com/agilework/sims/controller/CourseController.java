package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.User;
import com.agilework.sims.service.CourseService;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CourseController {
    private static final String TAG = "CourseController";
    @Autowired
    private CourseService courseService;
    @Autowired
    private SessionService sessionService;
    //返回信息
    private Map<String, Object> resultMap(boolean success, String message) {
        Map<String, Object> result = new HashMap<>();
        //操作是否成功，true false
        result.put("success", success);
        result.put("message", message);
        return result;
    }
    @GetMapping("/queryCourses")
    @ResponseBody
    public List<Course> queryCourses(String sessionId){
        return courseService.queryCourse(sessionId);
    }
    @GetMapping("findCourse")
    @ResponseBody
    public Course findCourse(String sessionId,String courseNo){
        Session session = sessionService.getSession(sessionId);
        User user=session.getUser();
        if(session==null){
            SLogger.info(TAG, "session NOT FOUND, id=" + sessionId);
            return null;
        }else if(user.getRole()==0){
            SLogger.info(TAG, user.getUserNo()+"start query course with courseNo=" + courseNo);
            return courseService.findCourseByCourseNoAndPublished(courseNo,1);
        }else{
            SLogger.info(TAG, user.getUserNo()+"start query course with courseNo=" + courseNo);
            return courseService.findCourseByCourseNo(courseNo);
        }
    }
    @PostMapping("/insertCourses")
    @ResponseBody
    public Map<String, Object> addCourses(@RequestBody @NonNull List<Course>list){
        boolean flag=courseService.addCourses(list);
        String message = flag ? "插入成功" : "插入失败";
        return resultMap(flag,message);
    }

}

