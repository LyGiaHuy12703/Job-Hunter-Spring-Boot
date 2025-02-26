package com.ctu.jobhunter.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class ResponseLoginDTO {
    private String accessToken;
    private UserLogin user;

    // inner class kiến thức java core
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class UserLogin {
        private String email;
        private String name;
        private String id;
    }
}
