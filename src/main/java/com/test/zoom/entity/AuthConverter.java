package com.test.zoom.entity;

import jakarta.persistence.AttributeConverter;

/** 소문자 입력값을 Enum 객체에서 인식할 수 있게하는 클래스. book.users.usertype을 대문자로 지정해 불필요 함. **/
public class AuthConverter implements AttributeConverter<Auth, String> {

    /** Auth 대문자 -> String 소문자 **/
    @Override
    public String convertToDatabaseColumn(Auth attribute) {
        return (attribute == null) ? null : attribute.name().toLowerCase();
    }

    /** String 소문자 -> Auth 대문자 **/
    @Override
    public Auth convertToEntityAttribute(String dbString) {
        if (dbString == null) return null;
        return Auth.valueOf(dbString.toUpperCase());
    }
}
