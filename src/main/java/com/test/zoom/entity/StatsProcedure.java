package com.test.zoom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

/** 프로시저를 통한 데이터를 자동매핑하기 위한 집계용 엔티티, 테이블생성 필수, 생성/수정 불가능**/
@Entity
@Table(name = "stats_procedure")
@Immutable
@Data
public class StatsProcedure {

    @Id
    private Long id; // PK 필요
    private String title;   // usage_type
    private String stat1; // 결제확정
    private String stat2; // 정산
    private String stat3; // 예정
    private String stat4; // 전체 합계
    private String stat5; // 전체 합계
    private String stat6; // 전체 합계
}
