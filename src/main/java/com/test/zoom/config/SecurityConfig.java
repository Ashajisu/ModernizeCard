package com.test.zoom.config;

import com.test.zoom.jwt.*;
import com.test.zoom.repository.UserRepository;
import com.test.zoom.service.CustomUserDetailsService;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

//spring boot 수동로그인 하려면 @Bean 방식을 사용한다. 그외에는 자동주입을 생성하도록 수정했다.
//Spring Security 5.7 이후로 @EnableWebSecurity 는 더 이상 필수적이지 않다.
@Slf4j
@Configuration
public class SecurityConfig {

    /** B. rest 방식으로 Controller 에서 AuthenticationProvider 를 사용하기위해 빈 등록함. **/
    @Bean
    public AuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }
    //------------------------------------------------------------------------------------------------------- 빈 등록.
    /** 생성자 없이 자동주입된 컴포넌트 사용**/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler,
                                           JwtAuthorizationFilter jwtAuthorizationFilter, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // CORS 허용
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                // 2. H2 Console은 CSRF 보호를 비활성화해야 정상 작동합니다.
                .csrf(AbstractHttpConfigurer::disable)
                // 3. enable h2-console
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                // 4. jwt 사용시 에러핸들링
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
                )

                //1. 요청 수준에서 권한을 모델링함.
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll() // request 외 디스패처 유형 인증 불필요
                        .requestMatchers("/h2-console/**").permitAll() // 브라우저에서 사용하는 DB인 h2에 콘솔로 접근할 때 인증 불필요 (기본 경로는 /h2-console)
                        .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll() // 페이지를 대표하는 아이콘
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/check/login").permitAll()
                        .requestMatchers("/guest/**").hasAnyRole("GUEST", "USER", "ADMIN")
                        .requestMatchers("/card/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
        //B. rest 방식 : id/pw 로그인 후 JWT 반환 필터 & 로그인을 제외한 모든 요청에서 JWT 검증하는 필터 & 세션 미사용 :
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore( jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of(
                "https://ashajisu.github.io",
                "http://localhost:5173", "http:/192.168.0.4:5173"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
