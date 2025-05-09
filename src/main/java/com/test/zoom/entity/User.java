package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

/** View 이므로 단순 조회만 가능 **/
@Entity
@Table(name = "users")
@Getter @Setter @Builder @ToString
@AllArgsConstructor
@NoArgsConstructor
@Immutable
public class User {

    @Id
    @Column(name = "empno")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동증가되는 PK
    private Long userId;

    @Column(name = "usertype")
    @Enumerated(EnumType.STRING) // ENUM 값을 문자열로 저장
    private Auth authName;

    @Column(name = "name")
    private String name;

    @Column(name = "email", length = 100, unique = true, nullable = false) // comments 조회를 위해 반드시 unique, notnull 이어야함
    private String username;

    @JsonIgnore
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "deptcode", length = 50)
    private Long dept;
    @Column(name = "position", length = 50)
    private String position;
}
