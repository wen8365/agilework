package com.agilework.sims.service;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.StudentCourseRelationship;
import com.agilework.sims.entity.User;
import com.agilework.sims.repository.CourseRepository;
import com.agilework.sims.repository.StuCourseRelationshipRepository;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private static final String TAG = "CourseService";
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private StuCourseRelationshipRepository stuCourseRelationshipRepository;

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

    public boolean deleteCourseByCourseNo(String courseNo){
        Course course=courseRepository.findByCourseNo(courseNo);
        if(course==null){
            SLogger.error(TAG, "courseNo invalid, courseNo=" + courseNo);
            return false;
        }else{
            SLogger.info(TAG,"start delete course,courseNo="+courseNo);
            courseRepository.deleteCourseByCourseNo(courseNo);
            Course course1=courseRepository.findByCourseNo(courseNo);
            if(course1==null){
                return true;
            }else{
                return false;
            }
        }
    }
    public boolean addCourses(List<Course>list){
        List<Course>result=courseRepository.saveAll(list);
        if(result.size()==list.size()){
            return true;
        }else{
            return false;
        }
    }
    public boolean changeCourseStatues(String courseNo,int published){
        int res=courseRepository.changeCourseStatus(courseNo,published);
        if(res>0){
            return true;
        }else{
            return false;
        }
    }
    public boolean addElectCourseRecord(List<StudentCourseRelationship>list){
        List res=stuCourseRelationshipRepository.saveAll(list);
        if(res.size()==list.size()){
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteCourseRecords(String studentNo,List<String>courseNos){
        int res=stuCourseRelationshipRepository.deleteCourseRecords(studentNo,courseNos);
        if(res>0){
            return true;
        }else{
            return false;
        }
    }
    public List<Course>queryCourseRecords(String studentNo){
        List<String>courseNos=stuCourseRelationshipRepository.findByStudentNo(studentNo);
        List<Course>res=new ArrayList<>();
        for(String courseNo:courseNos){
            Course course=courseRepository.findByCourseNo(courseNo);
            res.add(course);
        }
        return res;
    }
}
