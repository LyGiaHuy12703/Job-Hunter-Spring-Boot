package com.ctu.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ctu.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
}
