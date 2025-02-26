package com.ctu.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ctu.jobhunter.Exception.error.IdInvalidException;
import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.dto.auth.RequestLoginDTO;
import com.ctu.jobhunter.dto.auth.ResponseLoginDTO;
import com.ctu.jobhunter.repository.UserRepository;
import com.ctu.jobhunter.service.UserService;
import com.ctu.jobhunter.utils.SecurityUtil;
import com.ctu.jobhunter.utils.Mapper.UserMapper;
import com.ctu.jobhunter.utils.anotation.ApiMessage;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // đây là cách làm dependency injection
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    private final UserMapper userMapper;
    @Value("${TIME_TO_LIFE_REFRESH_TOKEN_IN_SECONDS}")
    private int EXPIRATION_TIME_REFRESH_TOKEN;

    public AuthController(
            UserMapper userMapper,
            UserService userService,
            AuthenticationManagerBuilder authenticationManageBuilder,
            SecurityUtil securityUtil) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.authenticationManagerBuilder = authenticationManageBuilder;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/")
    public String getMethodName() {
        return "new String()";
    }

    @PostMapping("/login")
    @ApiMessage("Login successful")
    public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody RequestLoginDTO request) {
        // nạp include username password vào security
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getUsername(),
                request.getPassword());
        // xác thực người dùng => cần viết hàm loadUserByUsername => nếu lỗi quăng lỗi
        // tại đây
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authToken);
        // sau khi login thi set vao securityContextHolder (có thể sử dụng sau này)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUserDB = userService.handleGetUserByUsername(request.getUsername());
        if (currentUserDB != null) {
            ResponseLoginDTO.UserLogin DTO = userMapper.toUserLoginDTO(currentUserDB);
            String refreshToken = securityUtil.createRefreshToken(request.getUsername(), DTO);
            userService.updateUserToken(refreshToken, request.getUsername());
            // set cookies
            ResponseCookie responseCookie = ResponseCookie
                    .from("refresh_token", refreshToken)
                    .httpOnly(true) // chỉ cho server sử dụng
                    .secure(true)
                    .path("/") // tất cả đều được sd cookie
                    .maxAge(EXPIRATION_TIME_REFRESH_TOKEN)// thời gian hết hạn
                    .build();

            // create a token
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .body(ResponseLoginDTO
                            .builder()
                            .accessToken(securityUtil.createAccessToken(authentication.getName(), DTO))
                            .user(DTO)
                            .build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/account")
    @ApiMessage("Fetch account")
    public ResponseEntity<ResponseLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User currentUserDB = userService.handleGetUserByUsername(email);
        if (currentUserDB != null) {
            ResponseLoginDTO.UserGetAccount DTO = userMapper.toUserGetAccount(currentUserDB);
            return ResponseEntity.ok(DTO);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/refresh")
    @ApiMessage("Refresh token success")
    public ResponseEntity<ResponseLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token) {
        if (refresh_token.equals("abc")) {
            throw new IdInvalidException("Bạn không có refresh token ở cookie");
        }
        Jwt decodedToken = securityUtil.checkValidRefreshToken(refresh_token);

        String email = decodedToken.getSubject();

        // check user by token and email
        User currentUserDB = userService.getUserByEmailAndRefreshToken(refresh_token, email);

        if (currentUserDB == null) {
            throw new IdInvalidException("Refresh token invalid");
        }

        ResponseLoginDTO.UserLogin DTO = userMapper.toUserLoginDTO(currentUserDB);
        String newRefreshToken = securityUtil.createRefreshToken(email, DTO);
        userService.updateUserToken(refresh_token, email);
        // set cookies
        ResponseCookie responseCookie = ResponseCookie
                .from("refresh_token", newRefreshToken)
                .httpOnly(true) // chỉ cho server sử dụng
                .secure(true)
                .path("/") // tất cả đều được sd cookie
                .maxAge(EXPIRATION_TIME_REFRESH_TOKEN)// thời gian hết hạn
                .build();

        // create a token
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(ResponseLoginDTO
                        .builder()
                        .accessToken(securityUtil.createAccessToken(email, DTO))
                        .user(DTO)
                        .build());
    }

    @PostMapping("logout")
    @ApiMessage("Logout successful")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("")) {
            throw new IdInvalidException("Access token không hợp lệ");
        }
        // update token = null
        userService.updateUserToken(null, email);

        // remove token in cookie
        ResponseCookie deleteCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true) // chỉ cho server sử dụng
                .secure(true)
                .path("/") // tất cả đều được sd cookie
                .maxAge(0)// hết hạn tức thì
                .build();
        return ResponseEntity.ok(null);
    }
}
