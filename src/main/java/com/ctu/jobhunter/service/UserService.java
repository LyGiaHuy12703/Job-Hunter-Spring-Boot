package com.ctu.jobhunter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.dto.pagination.Meta;
import com.ctu.jobhunter.dto.pagination.ResultPaginationDTO;
import com.ctu.jobhunter.dto.users.ResponseUserDTO;
import com.ctu.jobhunter.repository.UserRepository;
import com.ctu.jobhunter.utils.Mapper.UserMapper;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public ResponseUserDTO createUser(User request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IdInvalidException("Email existed");
        }
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .address(request.getAddress())
                .age(request.getAge())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);

        ResponseUserDTO res = userMapper.toResponseUserDTO(user);
        return res;
    }

    public ResultPaginationDTO fetchAll(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = userRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pageUser.getTotalPages())
                .total(pageUser.getTotalElements())
                .build();
        List<ResponseUserDTO> users = userMapper.toListResponseUserDTO(pageUser.getContent());
        ResultPaginationDTO rs = ResultPaginationDTO.builder()
                .meta(meta)
                .result(users)
                .build();
        return rs;
    }

    public ResponseUserDTO fetchUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IdInvalidException("User với id " + id + " không tồn tại");
        }
        return userMapper.toResponseUserDTO(user);
    }

    public void handleDeleteUsers(String id) {
        User users = userRepository.findById(id).orElse(null);
        if (users == null) {
            throw new IdInvalidException("User với id " + id + " không tồn tại");
        }
        userRepository.delete(users);
    }

    public ResponseUserDTO updateUserById(String id, User request) {
        User users = userRepository.findById(id).orElse(null);
        if (users == null) {
            throw new IdInvalidException("User với id " + id + " không tồn tại");
        }
        users.setAddress(request.getAddress());
        users.setAge(request.getAge());
        users.setEmail(request.getEmail());
        users.setGender(request.getGender());
        users.setName(request.getName());
        userRepository.save(users);
        return userMapper.toResponseUserDTO(users);
    }

}
