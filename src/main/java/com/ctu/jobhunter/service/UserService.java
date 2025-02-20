package com.ctu.jobhunter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User createUser(User request){
        User user = User.builder()
            .email("admin@gmail.com")
            .name("admin")
            .password("123")
            .build();
        return userRepository.save(user);
    }

    public List<User> fetchAll() {
        return userRepository.findAll();
    }
}
