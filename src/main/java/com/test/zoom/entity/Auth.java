package com.test.zoom.entity;

import jakarta.persistence.Convert;

@Convert(converter = AuthConverter.class)
public enum Auth {
    USER, ADMIN, LOCK
}
