package com.ctu.jobhunter.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.ctu.jobhunter.dto.auth.ResponseLoginDTO;
import com.nimbusds.jose.util.Base64;

@Service
public class SecurityUtil {
    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${TIME_TO_LIFE_ACCESS_TOKEN_IN_SECONDS}")
    private int EXPIRATION_TIME_ACCESS_TOKEN;
    @Value("${TIME_TO_LIFE_REFRESH_TOKEN_IN_SECONDS}")
    private int EXPIRATION_TIME_REFRESH_TOKEN;

    // thuat toan
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createAccessToken(String email, ResponseLoginDTO.UserLogin response) {
        // demo hardcode data permission
        List<String> listAuthority = new ArrayList<String>();
        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        // Instant trong java lay thoi gian hien tai
        Instant now = Instant.now();
        // thoi gian het han
        Instant validity = now.plus(EXPIRATION_TIME_ACCESS_TOKEN, ChronoUnit.SECONDS);

        // tao body
        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuedAt(now) // thoi gian tao
                .expiresAt(validity) // thoi gian het han
                .subject(email) // ten nguoi dung
                .claim("user", response)
                .claim("permission", listAuthority)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claim)).getTokenValue();
    }

    public String createRefreshToken(String email, ResponseLoginDTO.UserLogin response) {
        // Instant trong java lay thoi gian hien tai
        Instant now = Instant.now();

        // thoi gian het han
        Instant validity = now.plus(EXPIRATION_TIME_REFRESH_TOKEN, ChronoUnit.SECONDS);

        // tao body
        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuedAt(now) // thoi gian tao
                .expiresAt(validity) // thoi gian het han
                .subject(email) // ten nguoi dung
                .claim("user", response)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claim)).getTokenValue();
    }

    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();

        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> Refresh token error " + e.getMessage());
            throw e;
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(SECRET_KEY).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                JWT_ALGORITHM.getName());
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    public static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    public static Optional<String> getCurrentUserJwt() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    // public static boolean isAuthenticated() {
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // return authentication != null
    // &&
    // getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equal);
    // }
    //
    // public static boolean hasCurrentUserAnyOfAuthorities(String... authorities){
    // Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    // return (
    // authentication != null && getAuthorities(authentication).anyMatch(authority
    // -> Arrays.asList(authorities).contains(authority));
    // );
    // }

    // public static boolean hasCurrentUserNoneOfAuthorities(String... authorities)
    // {
    // return !hasCurrentUserAnyOfAuthorities(authorities);
    // }

    // public static boolean hasCurrentUserThisAuthority(String authority) {
    // return hasCurrentUserAnyOfAuthorities(authority);
    // }

    // private static Stream<String> getAuthorities(Authentication authentication){
    // return
    // authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    // }
}
