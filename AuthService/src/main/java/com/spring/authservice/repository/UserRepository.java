package com.spring.authservice.repository;

import com.spring.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    // ;)
}
