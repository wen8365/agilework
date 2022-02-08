package com.agilework.sims.repository;

import com.agilework.sims.entity.StudentCourseRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StuCourseRelationshipRepository extends
        JpaRepository<StudentCourseRelationship,String> {
    @Modifying
    @Transactional
    @Query("delete from StudentCourseRelationship sc where " +
            "sc.studentNo=?1 and sc.courseNo in (?2)")
    public int deleteCourseRecords(String studentNo, List<String>courseNos);
    @Query("select sc.courseNo from StudentCourseRelationship sc where sc.studentNo=:studentNo")
    public List<String> findByStudentNo(String studentNo);
}
