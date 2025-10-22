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
            userRepository.findAll().forEach(System.out::println);
        };
    }
}