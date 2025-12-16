package com.test.zoom.jwt;

import com.test.zoom.dto.LoginRequest;
import com.test.zoom.entity.CustomUserDetails;
import com.test.zoom.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/** B-2. Spring Security 내 의 필터 체인에 동작을 추가하여 인증 요청(`/auth/login`)을 컨트롤러의 엔드포인트 대신 자동으로 처리. JSON 요청을 처리하고 인증 후 토큰을 생성 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostConstruct
    public void init() {
        // 로그인 엔드포인트 설정
        log.debug("JwtAuthenticationFilter create");
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // JSON 요청 파싱
            LoginRequest loginUser = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            // 인증 전
            Authentication authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword());

            // 인증 처리
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 인증 성공 시 JWT, 사용자정보 반환
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUserForJWT();
        String token = jwtUtils.generateToken(authentication);

        // 응답생성
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        response.setContentType("application/json;charset=UTF-8");

        // 쿠키에 등록
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // https이면 true
                .path("/")
                .sameSite("Strict")
                .maxAge(60 * 60)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        new ObjectMapper().writeValue(response.getWriter(), result); //내부적으로 writer.flush() 를 이미 호출하는 함수
    }
}
