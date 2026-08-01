package com.test.zoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.YearMonth;

/** 검색조건 **/
@Getter
@Setter
public class Search {

    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate; //시작일
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate; //종료일
    @JsonDeserialize(using = MultiDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate payDate; //결제일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private YearMonth transactionDate; //지출월

    public void normalizeLastMonth(){
        // 기본: 이번 달 포함 최근 1개월
        if(startDate == null || endDate == null){
            setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1));
            setEndDate(LocalDate.now());
        }
    }
}
