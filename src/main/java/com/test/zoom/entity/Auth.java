package com.test.zoom.entity;

import jakarta.persistence.Convert;
import org.springframework.security.core.GrantedAuthority;

@Convert(converter = AuthConverter.class)
public enum Auth implements GrantedAuthority {
    USER, ADMIN, LOCK, GUEST;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
