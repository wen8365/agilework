package com.agilework.sims.repository;

import com.agilework.sims.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,String>{
    //按课程号查询
    public Course findByCourseNo(String courseNo);
//    @Query("select c from Course c where c.courseNo=:courseNo and published=:published")
    public Course findByCourseNoAndPublished(String courseNo,int published);
    //按照发布状态查询
    public List<Course> findByPublished(int published);
    //查询全部
    @Query("select c from Course c")
    public List<Course> findAllCourses();
    //删除特定课程
    @Modifying
    @Transactional
    @Query("delete from Course c where c.courseNo=:courseNo")
    public void deleteCourseByCourseNo(String courseNo);

    @Modifying
    @Transactional
    @Query("update Course c set c.published=:published where c.courseNo=:courseNo")
    public int changeCourseStatus(String courseNo,int published);

}
