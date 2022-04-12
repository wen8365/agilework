package com.agilework.sims.repository;

import com.agilework.sims.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Grade.PK> {
    @Query("select g from Grade g where g.pk.courseNo = ?1")
    List<Grade> findByCourseNo(String courseNo);
}
