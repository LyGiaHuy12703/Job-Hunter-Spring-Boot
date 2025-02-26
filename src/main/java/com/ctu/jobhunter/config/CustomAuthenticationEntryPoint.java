package com.ctu.jobhunter.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.ctu.jobhunter.dto.api.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @component biến nó thành 1 bean
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // ghi đè mặt định spring không quăng lỗi chỉ quăng lỗi sang header

    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    // chuyển data sang object
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authenticationException) throws IOException, ServletException {
        delegate.commence(request, response, authenticationException);
        response.setContentType("application/json;charset=UTF-8"); // quang lỗi tiếng việt

        String errorMessage = Optional.ofNullable(authenticationException.getCause())
                .map(Throwable::getMessage)
                .orElse(authenticationException.getMessage());

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        res.setError(errorMessage);
        res.setMessage("Token không hợp lệ");

        mapper.writeValue(response.getWriter(), res);
    }

}
