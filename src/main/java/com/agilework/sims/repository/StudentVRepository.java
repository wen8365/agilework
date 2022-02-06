package com.agilework.sims.repository;

import com.agilework.sims.entity.StudentV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentVRepository extends JpaRepository<StudentV, String>, JpaSpecificationExecutor<StudentV> {
    StudentV findByStudentNo(String studentNo);
}
