package com.agilework.sims.repository;

import com.agilework.sims.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserNoAndPassword(String id, String password);
}
