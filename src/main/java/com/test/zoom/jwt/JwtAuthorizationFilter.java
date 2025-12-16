package com.test.zoom.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 예외 경로 설정 ("/auth/login" 경로는 필터링하지 않음)
        String path = request.getServletPath();
        boolean loginPath = "/auth/login".equals(path);
        boolean notFilteringPath = "/.well-known/appspecific/com.chrome.devtools.json".equals(path);
        return loginPath || notFilteringPath;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (shouldNotFilter(request)) {filterChain.doFilter(request, response); return;}
        String token = "";
        try {
            token = getJwtFromCookie(request);
            if (StringUtils.isEmpty(token)) {
                token = getTokenFromRequest(request);
            }
            // 인증 후 : JWT가 유효하다면 CustomUserDetails 를 principal 로 넣고 SecurityContext에 저장
            if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
                Authentication authentication = jwtUtils.getAuthoritiesFromToken(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("JWT 검증 실패: {}", e.getMessage());
        } finally {
            filterChain.doFilter(request, response); // 다음 필터로 전달
        }
    }

    /** 요청헤더 에서 token 추출 **/
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtUtils.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtUtils.BEARER_TOKEN_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 이후의 토큰 값 추출
        }
        return null;
    }
    /** 쿠키에서 token 추출 **/
    private String getJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("token".equals(cookie.getName())) {
                //log.debug("getJwtFromCookie : {}", cookie.getValue());
                return cookie.getValue();
            }else {
                //log.debug("token in Cookie : null");
            }
        }
        return null;
    }
}
