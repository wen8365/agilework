package com.agilework.sims.repository;

import com.agilework.sims.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_student as s SET " +
            "s.studentName= CASE WHEN :#{#student.studentName} IS NULL THEN s.studentName ELSE :#{#student.studentName} END, " +
            "s.sex= CASE WHEN :#{#student.sex} IS NULL THEN s.sex ELSE :#{#student.sex} END, " +
            "s.major= CASE WHEN :#{#student.major} IS NULL THEN s.major ELSE :#{#student.major} END, " +
            "s.grade= CASE WHEN :#{#student.grade} IS NULL THEN s.grade ELSE :#{#student.grade} END, " +
            "s.class= CASE WHEN :#{#student.clazz} IS NULL THEN s.class ELSE :#{#student.clazz} END " +
            "WHERE s.studentNo=:#{#student.studentNo} AND s.status=:status",
            nativeQuery = true)
    int updateByStudentNoAndStatus(Student student, int status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE t_student as s SET s.status=:status " +
            "where s.studentNo=:studentNo and s.status=0",
            nativeQuery = true)
    int updateStudentStatus(String studentNo, int status);

}
