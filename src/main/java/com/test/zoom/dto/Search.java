package com.test.zoom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.zoom.service.MultiDateDeserializer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
}
