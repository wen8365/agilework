package com.agilework.sims.repository;

import com.agilework.sims.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByTeacherNoAndStatus(String teacherNo, int status);
}
