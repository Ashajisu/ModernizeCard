-- we don't know how to generate root <with-no-name> (class Root) :(

create table assets
(
    depart_name                                     varchar(100) null,
    asset_name                                      varchar(100) null,
    asset_code                                      varchar(10)  not null
        primary key,
    depart_code                                     varchar(10)  not null,
    `depart_name,asset_name,asset_code,depart_code` varchar(50)  null
);

create table categorys
(
    id    int auto_increment
        primary key,
    cat_a varchar(20) not null,
    part  varchar(20) null,
    cat_b varchar(20) null
);

create table kookmin_card
(
    id                    bigint auto_increment
        primary key,
    transaction_date      datetime             null,
    used_card             varchar(100)         null,
    usage_type            varchar(100)         null,
    merchant_name         varchar(255)         null,
    amount                bigint               null,
    benefit_type          varchar(100)         null,
    purchase_type         varchar(50)          null,
    benefit_amount        bigint               null,
    currency              bigint               null,
    balance_after_deposit bigint               null,
    payment_date          datetime             null,
    deleted               tinyint(1) default 0 not null
)
    charset = utf8mb4;

create table samsung_card
(
    id                    bigint auto_increment
        primary key,
    transaction_date      datetime             null,
    used_card             varchar(100)         null,
    usage_type            varchar(100)         null,
    merchant_name         varchar(255)         null,
    amount                bigint               null,
    installment_total     bigint               null,
    benefit_type          varchar(100)         null,
    purchase_type         varchar(50)          null,
    benefit_amount        bigint               null,
    installment_months    int                  null,
    installment_number    int                  null,
    currency              bigint               null,
    balance_after_deposit bigint               null,
    payment_date          datetime             null,
    deleted               tinyint(1) default 0 not null
)
    charset = utf8mb4;

create table schedule
(
    id        bigint               not null
        primary key,
    memo      varchar(255)         null,
    day       bigint               null,
    tag       varchar(255)         null,
    boldtext  tinyint(1) default 0 not null,
    textcolor varchar(255)         null
);

create table shinhan_card
(
    id               int auto_increment
        primary key,
    transaction_date datetime             not null,
    card_type        varchar(20)          null,
    used_card        varchar(50)          null,
    merchant_name    varchar(100)         null,
    approval_number  varchar(30)          null,
    amount           bigint               null,
    purchase_type    varchar(20)          null,
    usage_type       varchar(20)          not null,
    currency         bigint     default 0 not null,
    payment_date     datetime             null,
    deleted          tinyint(1) default 0 not null comment '삭제여부'
)
    charset = utf8mb4;

create table stats_procedure
(
    id    bigint       not null
        primary key,
    title varchar(255) null,
    stat1 varchar(100) null,
    stat2 varchar(100) null,
    stat3 varchar(100) null,
    stat4 varchar(100) null,
    stat5 varchar(100) null,
    stat6 varchar(100) null
);

create table tmp_chit
(
    id           int auto_increment
        primary key,
    category     varchar(20)    null,
    pay_method   varchar(10)    null,
    trans_date   date           null,
    trans_amount decimal(10, 2) null,
    detail       text           null,
    constraint tmp_chit_FK
        foreign key (pay_method) references assets (asset_code)
);

create table users
(
    empno    bigint auto_increment
        primary key,
    usertype varchar(255) null,
    name     varchar(255) null,
    email    varchar(100) not null,
    password varchar(100) not null,
    deptcode bigint       null,
    position varchar(50)  null,
    constraint email
        unique (email)
)
    charset = utf8mb4;

create definer = arisys@`%` view unified_card_view as
select 'SAMSUNG'                                     AS `card_company`,
       `hab2`.`samsung_card`.`id`                    AS `id`,
       `hab2`.`samsung_card`.`transaction_date`      AS `transaction_date`,
       `hab2`.`samsung_card`.`usage_type`            AS `usage_type`,
       `hab2`.`samsung_card`.`merchant_name`         AS `merchant_name`,
       `hab2`.`samsung_card`.`purchase_type`         AS `purchase_type`,
       coalesce(`hab2`.`samsung_card`.`currency`, 0) AS `currency`
from `hab2`.`samsung_card`
where `hab2`.`samsung_card`.`deleted` = 0
union all
select 'SHINHAN'                                     AS `card_company`,
       `hab2`.`shinhan_card`.`id`                    AS `id`,
       `hab2`.`shinhan_card`.`transaction_date`      AS `transaction_date`,
       `hab2`.`shinhan_card`.`usage_type`            AS `usage_type`,
       `hab2`.`shinhan_card`.`merchant_name`         AS `merchant_name`,
       `hab2`.`shinhan_card`.`purchase_type`         AS `purchase_type`,
       coalesce(`hab2`.`shinhan_card`.`currency`, 0) AS `currency`
from `hab2`.`shinhan_card`
where `hab2`.`shinhan_card`.`deleted` = 0
  and `hab2`.`shinhan_card`.`purchase_type` = '결제확정';

CREATE DEFINER=`hab2`@`%` PROCEDURE `hab2`.`getMonthlyStatsAll`()
BEGIN
    DECLARE v_now DATE;
    DECLARE v_month_now VARCHAR(7);
    DECLARE v_month_before VARCHAR(7);
    DECLARE v_month_before2 VARCHAR(7);
    DECLARE v_year_before VARCHAR(7);
    DECLARE v_month_now_key VARCHAR(20);
    DECLARE v_month_before_key VARCHAR(20);
    DECLARE v_month_before_key2 VARCHAR(20);
    DECLARE v_year_before_key VARCHAR(20);

    -- 날짜 계산
    SET v_now = CURDATE();
    SET v_month_now = DATE_FORMAT(v_now, '%Y-%m');
    SET v_month_before = DATE_FORMAT(DATE_SUB(v_now, INTERVAL 1 MONTH), '%Y-%m');
    SET v_month_before2 = DATE_FORMAT(DATE_SUB(v_now, INTERVAL 2 MONTH), '%Y-%m');
    SET v_year_before = DATE_FORMAT(DATE_SUB(v_now, INTERVAL 1 YEAR), '%Y-%m');

    -- 화면과 동일한 key 형식: 'August 2025'
    SET v_month_now_key = DATE_FORMAT(v_now, '%M %Y');
    SET v_month_before_key = DATE_FORMAT(DATE_SUB(v_now, INTERVAL 1 MONTH), '%M %Y');
    SET v_month_before_key2 = DATE_FORMAT(DATE_SUB(v_now, INTERVAL 2 MONTH), '%M %Y');
    SET v_year_before_key = DATE_FORMAT(DATE_SUB(v_now, INTERVAL 1 YEAR), '%M %Y');

    -- JSON 반환
SELECT JSON_OBJECT(
               v_month_now_key, (SELECT JSON_ARRAYAGG(JSON_OBJECT(
                'label', t.usage_type,
                'cost', t.total_cost,
                'ratio', ROUND(t.total_cost / NULLIF(m.total_month, 0) * 100)
                                                      ))
                                 FROM (SELECT usage_type, SUM(currency) AS total_cost
                                       FROM unified_card_view
                                       WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_month_now
                                       GROUP BY usage_type) t
                                          CROSS JOIN (SELECT SUM(currency) AS total_month
                                                      FROM unified_card_view
                                                      WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_month_now) m),
               v_month_before_key, (SELECT JSON_ARRAYAGG(JSON_OBJECT(
                'label', t.usage_type,
                'cost', t.total_cost,
                'ratio', ROUND(t.total_cost / NULLIF(m.total_month, 0) * 100)
                                                         ))
                                    FROM (SELECT usage_type, SUM(currency) AS total_cost
                                          FROM unified_card_view
                                          WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_month_before
                                          GROUP BY usage_type) t
                                             CROSS JOIN (SELECT SUM(currency) AS total_month
                                                         FROM unified_card_view
                                                         WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_month_before) m),
               v_month_before_key2, (SELECT JSON_ARRAYAGG(JSON_OBJECT(
                'label', t.usage_type,
                'cost', t.total_cost,
                'ratio', ROUND(t.total_cost / NULLIF(m.total_month, 0) * 100)
                                                          ))
                                     FROM (SELECT usage_type, SUM(currency) AS total_cost
                                           FROM unified_card_view
                                           WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_month_before2
                                           GROUP BY usage_type) t
                                              CROSS JOIN (SELECT SUM(currency) AS total_month
                                                          FROM unified_card_view
                                                          WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_month_before2) m),
               v_year_before_key, (SELECT JSON_ARRAYAGG(JSON_OBJECT(
                'label', t.usage_type,
                'cost', t.total_cost,
                'ratio', ROUND(t.total_cost / NULLIF(m.total_month, 0) * 100)
                                                        ))
                                   FROM (SELECT usage_type, SUM(currency) AS total_cost
                                         FROM unified_card_view
                                         WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_year_before
                                         GROUP BY usage_type) t
                                            CROSS JOIN (SELECT SUM(currency) AS total_month
                                                        FROM unified_card_view
                                                        WHERE DATE_FORMAT(transaction_date, '%Y-%m') = v_year_before) m)
       ) AS stateData;

END;

create
definer = arisys@`%` procedure getSHUsageTypeCurrencyStats()
BEGIN
    SET @rownum := 0;
SELECT (@rownum := @rownum + 1) AS id, title,
       COALESCE(stat1,0) as stat1,
       COALESCE(stat2,0) as stat2,
       COALESCE(stat3,0) as stat3,
       COALESCE(stat4,0) as stat4,
       COALESCE(stat5,0) as stat5,
       COALESCE(stat6,0) as stat6
FROM (
         SELECT
             usage_type AS title,
             SUM(amount) AS stat1,
             -(SUM(amount) - SUM(CASE WHEN purchase_type = '결제확정' THEN currency ELSE 0 END)) AS stat2,
             SUM(CASE WHEN purchase_type = '결제확정' THEN currency ELSE 0 END) AS stat3,
             SUM(CASE WHEN purchase_type = '정산' THEN currency ELSE 0 END) AS stat4,
             SUM(CASE WHEN purchase_type = '예정' THEN currency ELSE 0 END) AS stat5,
             SUM(currency) AS stat6
         FROM shinhan_card, (SELECT @rownum := 0) r
         WHERE usage_type != '승인취소'
         GROUP BY usage_type

         UNION ALL

         SELECT
             '합계' AS title,
             SUM(amount) AS stat1,
             -(SUM(amount) - SUM(CASE WHEN purchase_type = '결제확정' THEN currency ELSE 0 END)) AS stat2,
             SUM(CASE WHEN purchase_type = '결제확정' THEN currency ELSE 0 END) AS stat3,
             SUM(CASE WHEN purchase_type = '정산' THEN currency ELSE 0 END) AS stat4,
             SUM(CASE WHEN purchase_type = '예정' THEN currency ELSE 0 END) AS stat5,
             SUM(currency) AS stat6
         FROM shinhan_card
         WHERE usage_type != '승인취소') t
ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END;

create
definer = arisys@`%` procedure getSSUsageTypeCurrencyStats()
BEGIN
    SET @rownum := 0;
SELECT (@rownum := @rownum + 1) AS id, title,
       COALESCE(stat1,0) as stat1,
       COALESCE(stat2,0) as stat2,
       COALESCE(stat3,0) as stat3,
       COALESCE(stat4,0) as stat4,
       0 as stat5,
       0 as stat6
FROM (SELECT usage_type                 AS title,
             SUM(amount)                AS stat1, #이용금액
          SUM(benefit_amount)        AS stat2, #할인
                 SUM(currency)              AS stat3, #거래통화
                 SUM(balance_after_deposit) AS stat4  #정산후
      FROM samsung_card
      WHERE usage_type IS NOT NULL
      GROUP BY usage_type

      UNION ALL

      SELECT
          '합계'                       AS title,
          SUM(amount)                AS stat1,
          SUM(benefit_amount)        AS stat2,
          SUM(currency)              AS stat3,
          SUM(balance_after_deposit) AS stat4
      FROM samsung_card
      WHERE usage_type IS NOT NULL)t
ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END;

create
definer = arisys@`%` procedure getSSUsageTypeCurrencyStatsDate(IN start_date date, IN end_date date, IN pay_date date)
BEGIN
    SET @rownum := 0;
SELECT (@rownum := @rownum + 1) AS id, title,
       COALESCE(stat1,0) as stat1,
       COALESCE(stat2,0) as stat2,
       COALESCE(stat3,0) as stat3,
       COALESCE(stat4,0) as stat4,
       0 as stat5,
       0 as stat6
FROM (SELECT usage_type                 AS title,
             SUM(amount)                AS stat1, #이용금액
          SUM(benefit_amount)        AS stat2, #할인
                 SUM(currency)              AS stat3, #거래통화
                 SUM(balance_after_deposit) AS stat4  #정산후
      FROM samsung_card
      WHERE usage_type IS NOT NULL
        and ((start_date IS NULL OR end_date IS NULL) OR ( transaction_date between start_date and end_date))
        and ((pay_date IS NULL) OR (payment_date = pay_date))
      GROUP BY usage_type

      UNION ALL

      SELECT
          '합계'                       AS title,
          SUM(amount)                AS stat1,
          SUM(benefit_amount)        AS stat2,
          SUM(currency)              AS stat3,
          SUM(balance_after_deposit) AS stat4
      FROM samsung_card
      WHERE usage_type IS NOT NULL
        and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))
        and ((pay_date IS NULL) OR (payment_date = pay_date))
     )t
ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END;

create
definer = arisys@`%` procedure getKBUsageTypeCurrencyStatsDate(IN start_date date, IN end_date date, IN pay_date date)
BEGIN
    SET @rownum := 0;
SELECT (@rownum := @rownum + 1) AS id, title,
       COALESCE(stat1,0) as stat1,
       COALESCE(stat2,0) as stat2,
       COALESCE(stat3,0) as stat3,
       0 as stat4,
       0 as stat5,
       0 as stat6
FROM (SELECT usage_type                 AS title,
             SUM(amount)                AS stat1, #이용금액
          SUM(benefit_amount)        AS stat2, #할인
                 SUM(currency)              AS stat3 #거래통화
      FROM kookmin_card
      WHERE usage_type IS NOT NULL
        and ((start_date IS NULL OR end_date IS NULL) OR ( transaction_date between start_date and end_date))
        and ((pay_date IS NULL) OR (payment_date = pay_date))
      GROUP BY usage_type

      UNION ALL

      SELECT
          '합계'                       AS title,
          SUM(amount)                AS stat1,
          SUM(benefit_amount)        AS stat2,
          SUM(currency)              AS stat3
      FROM kookmin_card
      WHERE usage_type IS NOT NULL
        and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))
        and ((pay_date IS NULL) OR (payment_date = pay_date))
     )t
ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END;

create
definer = arisys@`%` procedure getAllUsageTypeCurrencyStatsDate(IN start_date date, IN end_date date)
BEGIN
SELECT (@rownum := @rownum + 1) AS id,
       title,
       COALESCE(stat1, 0)       as stat1,
       COALESCE(stat2, 0)       as stat2,
       COALESCE(stat3, 0)       as stat3,
       COALESCE(stat4, 0)       as stat4,
       0                        as stat5,
       0                        as stat6
FROM (SELECT usage_type    AS title,
             SUM(currency) AS stat1, #합계
          SUM(CASE WHEN card_company = 'SAMSUNG' THEN currency ELSE 0 END) AS stat2,
                 SUM(CASE WHEN card_company = 'KOOKMIN' THEN currency ELSE 0 END) AS stat3,
                 SUM(CASE WHEN card_company = 'SHINHAN' THEN currency ELSE 0 END) AS stat4
      FROM unified_card_view
      WHERE usage_type IS NOT NULL
        and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))
      GROUP BY usage_type

      UNION ALL

      SELECT '합계'          AS title,
          SUM(currency) AS stat1,
          SUM(CASE WHEN card_company = 'SAMSUNG' THEN currency ELSE 0 END) AS stat2,
          SUM(CASE WHEN card_company = 'KOOKMIN' THEN currency ELSE 0 END) AS stat3,
          SUM(CASE WHEN card_company = 'SHINHAN' THEN currency ELSE 0 END) AS stat4
      FROM unified_card_view
      WHERE usage_type IS NOT NULL
        and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date)) )t
ORDER BY
    CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
SET @rownum := 0;
END;



