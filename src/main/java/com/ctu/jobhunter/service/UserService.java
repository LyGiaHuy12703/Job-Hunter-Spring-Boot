package com.ctu.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.Company;
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
    private final CompanyService companyService;

    public UserService(CompanyService companyService, UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.companyService = companyService;
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

        ResponseUserDTO res = userMapper.toResponseUserDTO(user);

        // check company
        if (request.getCompany() != null) {
            Company companyOptional = companyService.findById(request.getCompany().getId());
            if (companyOptional != null) {
                ResponseUserDTO.Company com = userMapper.toResponseUserCompany(companyOptional);
                user.setCompany(companyOptional);
                res.setCompany(com);
            }
        }
        userRepository.save(user);
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

        // Ánh xạ User -> ResponseUserDTO đảm bảo đầy đủ dữ liệu
        List<ResponseUserDTO> users = pageUser.getContent().stream()
                .map(user -> new ResponseUserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getGender(),
                        user.getAddress(),
                        user.getAge(),
                        user.getCreatedAt(),
                        user.getUpdatedAt(),
                        user.getCompany() != null
                                ? new ResponseUserDTO.Company(user.getCompany().getId(), user.getCompany().getName())
                                : null))
                .collect(Collectors.toList());

        return ResultPaginationDTO.builder()
                .meta(meta)
                .result(users)
                .build();
    }

    public ResponseUserDTO fetchUserById(String id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new IdInvalidException("User với id " + id + " không tồn tại");
        }
        return convertToResponseUserDTO(user);
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
        if (request.getCompany() != null) {
            if (companyService.findById(request.getCompany().getId()) != null) {
                users.setCompany(request.getCompany());
            }
        }

        userRepository.save(users);
        return convertToResponseUserDTO(users);
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username).orElse(null);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            userRepository.save(currentUser);
        }
    }

    public User getUserByEmailAndRefreshToken(String refresh_token, String email) {
        return userRepository.findByEmailAndRefreshToken(email, refresh_token);
    }

    public ResponseUserDTO convertToResponseUserDTO(User user) {
        ResponseUserDTO res = userMapper.toResponseUserDTO(user);

        if (user.getCompany() != null) {
            ResponseUserDTO.Company com = userMapper.toResponseUserCompany(user.getCompany());
            res.setCompany(com);
        }
        return res;
    }

}
