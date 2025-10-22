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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

//spring boot의 주요원칙인 컴포넌트 기반 설정을 유지하기위해 extands WebSecurityConfigurerAdapter 구조는 deprecated 하고, @Bean 방식을 사용한다.
//Spring Security 5.7 이후로 @EnableWebSecurity 는 더 이상 필수적이지 않다.
@Slf4j
@Configuration
public class SecurityConfig {

    /** B. rest 방식으로 Controller 에서 AuthenticationProvider 를 사용하기위해 빈 등록함. **/
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //------------------------------------------------------------------------------------------------------- 빈 등록.

    private final UserRepository userRepository;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    /** 불변성을 위해 생성자 주입을 사용함. **/
    public SecurityConfig(UserRepository userRepository, JwtUtils jwtUtils, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.userRepository = userRepository;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = new JwtAuthenticationEntryPoint();
    }

    /** i-2. JPA 조회를 통해 사용자를 확인함.**/
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userRepository, passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtils jwtUtils) throws Exception {
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
                        .requestMatchers("/zoom/**").authenticated()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/check/login").permitAll()
                        .anyRequest().authenticated()
                )
        //B. rest 방식 : id/pw 로그인 후 JWT 반환 필터 & 로그인을 제외한 모든 요청에서 JWT 검증하는 필터 & 세션 미사용 :
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("http://localhost:5173");  // Vue 허용
        configuration.addAllowedOrigin("http://192.168.0.9:5173");  // Vue 허용
        configuration.addAllowedOrigin("http://ashajisu.iptime.org:5173");  // Vue 허용
        configuration.addAllowedOrigin("http://ashajisu.iptime.org:5050"); //외부 경로 허용
        configuration.addAllowedOrigin("http://ashajisu.iptime.org:9090"); //외부 경로 허용
        configuration.addAllowedOrigin("https://jisu-arisys.github.io"); //외부 경로 허용
        configuration.addAllowedOrigin("https://*.ngrok-free.dev"); //외부 경로 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
