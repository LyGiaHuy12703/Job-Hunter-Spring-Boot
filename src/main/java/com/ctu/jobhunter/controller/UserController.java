package com.ctu.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.dto.users.ResponseUserDTO;
import com.ctu.jobhunter.service.UserService;
import com.ctu.jobhunter.utils.anotation.ApiMessage;
import com.turkraft.springfilter.boot.Filter;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    @ApiMessage("Create user success")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody User request) {
        ResponseUserDTO res = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/users")
    @ApiMessage("Fetch all users")
    public ResponseEntity<ResultPaginationDTO> fetchAllUsers(
            @Filter Specification<User> spec,
            Pageable pageable) {
        ResultPaginationDTO users = userService.fetchAll(spec, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetch users by id")
    public ResponseEntity<ResponseUserDTO> fetchUsersById(@PathVariable("id") String id) {
        ResponseUserDTO res = userService.fetchUserById(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/users/{id}")
    @ApiMessage("Update user successful")
    public ResponseEntity<ResponseUserDTO> updateUserById(@PathVariable("id") String id, @RequestBody User request) {
        ResponseUserDTO res = userService.updateUserById(id, request);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Deleted user successful")
    public ResponseEntity<Void> deleteUsers(@PathVariable("id") String id) throws IdInvalidException {
        userService.handleDeleteUsers(id);
        return ResponseEntity.noContent().build();
    }

}
