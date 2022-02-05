package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.StudentCourseRelationship;
import com.agilework.sims.entity.User;
import com.agilework.sims.repository.CourseRepository;
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
    @Autowired
    private CourseRepository courseRepository;
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
    public List<Course> queryCourses(@RequestHeader("sessionId") @NonNull String sessionId){
        return courseService.queryCourse(sessionId);
    }
    @GetMapping("findCourse")
    @ResponseBody
    public Course findCourse(@RequestHeader("sessionId") @NonNull String sessionId, String courseNo){
        Session session = sessionService.getSession(sessionId);
        User user=session.getUser();
        if(user.getRole()==0){
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

    @PostMapping("/changeCourseStatues")
    @ResponseBody
    public Map<String,Object>changeCourseStatus(@NonNull String courseNo,int published){
        boolean flag=courseService.changeCourseStatues(courseNo,published);
        String message=flag?"修改成功":"修改失败";
        return resultMap(flag,message);
    }
    @PostMapping("/insertStuCourseRecords")
    @ResponseBody
    public Map<String,Object>addCourseRecords(@NonNull List<StudentCourseRelationship>list){
        boolean flag=courseService.addElectCourseRecord(list);
        String message=flag?"插入成功":"插入失败";
        return resultMap(flag,message);
    }

    @PostMapping("/deleteStuCourseRecords")
    @ResponseBody
    public Map<String,Object>deleteCourseRecords(@RequestHeader("sessionId")String sessionId,
                                                 @NonNull List<String>courseNos){
        Session session = sessionService.getSession(sessionId);
        User user=session.getUser();
        String studentNo=user.getUserNo();
        boolean flag=courseService.deleteCourseRecords(studentNo,courseNos);
        String message=flag?"删除成功":"删除失败";
        return resultMap(flag,message);
    }
    @GetMapping("/queryCourseRecords")
    @ResponseBody
    public List<Course>queryCourseRecords(@RequestHeader("sessionId")String sessionId){
        Session session = sessionService.getSession(sessionId);
        User user=session.getUser();
        String studentNo=user.getUserNo();
        List<Course>res=courseService.queryCourseRecords(studentNo);
        return res;
    }

    @PostMapping("/updateCourse")
    @ResponseBody
    public Course updateCourse(Course course){
        Course res=courseRepository.save(course);
        return res;
    }

    @PostMapping("/deleteCourseByCourseNo")
    @ResponseBody
    public Map<String,Object>deleteCourse(String courseNo){
        boolean flag=courseService.deleteCourseByCourseNo(courseNo);
        String message=flag?"删除成功":"删除失败";
        return resultMap(flag,message);
    }

}

