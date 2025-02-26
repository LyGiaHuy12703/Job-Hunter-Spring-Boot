package com.ctu.jobhunter.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.Base64;

@Service
public class SecurityUtil {
    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;
    @Value("${TIME_TO_LIFE_TOKEN_IN_SECONDS}")
    private int EXPIRATION_TIME_TOKE;

    // thuat toan
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String createToken(Authentication authentication) {
        // Instant trong java lay thoi gian hien tai
        Instant now = Instant.now();

        // thoi gian het han
        Instant validity = now.plus(EXPIRATION_TIME_TOKE, ChronoUnit.SECONDS);

        // tao body
        JwtClaimsSet claim = JwtClaimsSet.builder()
                .issuedAt(now) // thoi gian tao
                .expiresAt(validity) // thoi gian het han
                .subject(authentication.getName()) // ten nguoi dung
                .claim("Gia Huy", authentication)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claim)).getTokenValue();
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
