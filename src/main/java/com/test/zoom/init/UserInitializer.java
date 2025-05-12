package com.test.zoom.init;

import com.test.zoom.entity.Auth;
import com.test.zoom.entity.User;
import com.test.zoom.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserInitializer {

    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("info@wrappixel.com").isEmpty()) {
                User user = new User();
                user.setUsername("info@wrappixel.com");
                user.setPassword(passwordEncoder.encode("admin123")); // 원본 비밀번호를 해싱
                user.setAuthName(Auth.USER);
                userRepository.save(user);
            }
        };
    }
}