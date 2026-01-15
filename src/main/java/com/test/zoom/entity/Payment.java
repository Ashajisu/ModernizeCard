package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(name = "name", nullable = false)
    private String title;
    @Column(name = "memo")
    private String subtitle;
    @Column(name = "rank", nullable = false)
    private String rank;
    @Column(name = "bgcolor", nullable = false)
    private String bgcolor;
    @Column(name = "disable")
    private String disable;
    @Column(name = "img")
    private String img = "";
}
