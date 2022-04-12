package com.agilework.sims.repository;

import com.agilework.sims.entity.GradeV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GradeVRepository extends JpaRepository<GradeV, GradeV.PK> {

    @Query("select g from GradeV g where g.pk.studentNo = ?1 and g.pk.courseNo in (?2)")
    List<GradeV> findByStudentNoAndCourseNoIn(String studentNo, List<String> courseNoList);
}
