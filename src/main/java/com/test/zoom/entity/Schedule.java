package com.test.zoom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@NoArgsConstructor
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(name = "day")
    @JsonIgnore
    private int day;

    @Column(name = "memo")
    private String subtitle;
    @Column(name = "tag")
    private String link;
    @Column(name = "textcolor")
    private String textcolor;
    @Column(name = "boldtext")
    private boolean boldtext;


    // ===== DB에 없는 필드들 =====
    @Transient
    private LocalDate date;
    @Transient
    private String title;
    @Transient
    private boolean line = true;
    @Transient
    private String url = "";


    public String getTitle() {
        date = getDate();
        if (date != null) {
            return date.getMonthValue() + "." + date.getDayOfMonth();
        }else{
            return "";
        }
    }

    /** 말일 보정 → 은행/카드사 납부일이 30일, 31일인데 2월 같은 달에는 자동으로 해당월의 말일(28일/29일) 로 처리 → 실제 금융권 로직과 일치 **/
    public LocalDate getDate() {
        if (day > 0) {
            LocalDate beforeThree = LocalDate.now().minusDays(4);
            YearMonth ym = YearMonth.from(LocalDate.now());
            LocalDate scheduleDate = LocalDate.of(ym.getYear(), ym.getMonth(), Math.min(day, ym.lengthOfMonth()));

            if (scheduleDate.isBefore(beforeThree)) {
                return scheduleDate.plusMonths(1);
            }

            return scheduleDate;
        }else{
            return null;
        }
    }
}
