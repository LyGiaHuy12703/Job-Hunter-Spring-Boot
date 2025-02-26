package com.ctu.jobhunter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.dto.auth.LoginDTO;
import com.ctu.jobhunter.dto.auth.ResponseLoginDTO;
import com.ctu.jobhunter.utils.SecurityUtil;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class AuthController {
    // đây là cách làm dependency injection
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManageBuilder, SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManageBuilder;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/")
    public String getMethodName() {
        return "new String()";
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody LoginDTO request) {
        // nạp include username password vào security
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        // sau khi login thi set vao securityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create a token
        ResponseLoginDTO DTO = ResponseLoginDTO.builder().accessToken(securityUtil.createToken(authentication)).build();
        return ResponseEntity.status(HttpStatus.OK).body(DTO);
    }
}
