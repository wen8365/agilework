package com.agilework.sims.service;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.Course;
import com.agilework.sims.entity.StudentCourseRelationship;
import com.agilework.sims.entity.User;
import com.agilework.sims.repository.CourseRepository;
import com.agilework.sims.repository.StuCourseRelationshipRepository;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
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
            return new ArrayList<>();
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
            return new ArrayList<>();
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
            return course1 == null;
        }
    }
    public boolean addCourses(List<Course>list){
        List<Course>result=courseRepository.saveAll(list);
        return result.size() == list.size();
    }
    public boolean changeCourseStatues(String courseNo,int published){
        int res=courseRepository.changeCourseStatus(courseNo,published);
        return res > 0;
    }
    public boolean addElectCourseRecord(List<StudentCourseRelationship>list){
        List<StudentCourseRelationship> res=stuCourseRelationshipRepository.saveAll(list);
        return res.size() == list.size();
    }

    public boolean deleteCourseRecords(String studentNo,List<String>courseNos){
        int res=stuCourseRelationshipRepository.deleteCourseRecords(studentNo,courseNos);
        return res > 0;
    }
    public Page<Course> queryCourseRecords(String studentNo, int currentPage , int pageSize){
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Specification<StudentCourseRelationship> specification = createSpecification(studentNo);
        Page<StudentCourseRelationship> pages = stuCourseRelationshipRepository.findAll(specification, pageable);
        SLogger.info(TAG, "course records query SUCCESS, size=" + pages.getSize()
                + ", page=" + pages.getNumber() + "/" + pages.getTotalPages());

        List<String> courseNos = new ArrayList<>();
        for (StudentCourseRelationship sc : pages) {
            courseNos.add(sc.getCourseNo());
        }
        List<Course> courses = courseRepository.findAllById(courseNos);
        return new PageImpl<>(courses, pageable, pages.getTotalElements());
    }

    private Specification<StudentCourseRelationship> createSpecification(final String studentNo) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Path<String> sno = root.get("studentNo");
            return criteriaBuilder.equal(sno, studentNo);
        };
    }
}
