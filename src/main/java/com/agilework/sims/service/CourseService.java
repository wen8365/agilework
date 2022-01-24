package com.agilework.sims.service;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.User;
import com.agilework.sims.repository.CourseRepository;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private static final String TAG = "CourseService";
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SessionService sessionService;

    public List<Course> queryCourse(String sessionId){
        Session session = sessionService.getSession(sessionId);
        if(session==null){
            SLogger.info(TAG, "session NOT FOUND, id=" + sessionId);
            return null;
        }
        User user=session.getUser();
        if(user.getRole()==0){
            SLogger.info(TAG, "student query courses, id=" + user.getUserNo());
            return courseRepository.findByPublished(1);
        }else if(user.getRole()==1){
            SLogger.info(TAG, "teacher query courses, id=" + user.getUserNo());
            return courseRepository.findAllCourses();
        }else{
            SLogger.error(TAG, "role invalid, id=" + sessionId);
            return null;
        }
    }
    public Course findCourseByCourseNo(String courseNo){
        return courseRepository.findByCourseNo(courseNo);
    }
    public Course findCourseByCourseNoAndPublished(String courseNo,int published){
        return courseRepository.findByCourseNoAndPublished(courseNo,published);
    }

    public String deleteCourseByCourseNo(String courseNo){
        Course course=courseRepository.findByCourseNo(courseNo);
        if(course==null){
            SLogger.error(TAG, "courseNo invalid, courseNo=" + courseNo);
            return "Course do not exists.";
        }else{
            SLogger.info(TAG,"start delete course,courseNo="+courseNo);
            courseRepository.deleteCourseByCourseNo(courseNo);
            return "Delete Success.";
        }
    }
}
