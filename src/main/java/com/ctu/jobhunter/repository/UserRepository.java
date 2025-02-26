package com.ctu.jobhunter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ctu.jobhunter.domain.User;

//thêm interface JpaSpecificationExecutor để có thêm thư viện filter
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    User findByEmailAndRefreshToken(String email, String refreshToken);
}
