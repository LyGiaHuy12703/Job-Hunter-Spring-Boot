package com.ctu.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.service.UserService;
import com.ctu.jobhunter.service.error.IdInvalidException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    private final UserService userService;
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> fetchAllUsers() {
        List<User> users = userService.fetchAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }    
    @GetMapping("/test")
    public String getMethodName() {
        if(true)
            throw new IdInvalidException("helloasdaksnd");
        return "test controller";
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable("id") String id) throws IdInvalidException{
        if (id.equals("hello")) {
            System.out.println("Throwing IdInvalidException...");
            throw new IdInvalidException("id không hợp lệ");
        }
        return ResponseEntity.noContent().build();
    }
    
    
    
}
