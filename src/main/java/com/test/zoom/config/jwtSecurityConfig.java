package com.test.zoom.config;

import com.test.zoom.jwt.JwtAuthorizationFilter;
import com.test.zoom.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class jwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void configure(HttpSecurity http) {
        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtUtils);
        http.addFilter(jwtAuthorizationFilter);
    }
}
