package com.agilework.sims.repository;

import com.agilework.sims.entity.CourseScheduler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchedulerRepository extends JpaRepository<CourseScheduler, CourseScheduler.PK> {
    @Query("select cs from CourseScheduler cs where cs.pk.courseNo in (?1)")
    List<CourseScheduler> findByCourseNoIn(List<String> courseNoList);
}
