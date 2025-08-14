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

create table sam_sung_card
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

create table shin_han_card
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

create definer = arisys@`%` trigger trg_generate_seq_num
    before insert
    on tmp_chit
    for each row
BEGIN
    DECLARE count_today INT;

SELECT COUNT(*) + 1 INTO count_today
FROM tmp_chit
WHERE trans_date = NEW.trans_date;

SET NEW.seq = CONCAT(DATE_FORMAT(NEW.trans_date, '%Y-%m-%d'), '_', LPAD(count_today,5,'0'));
END;

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
         FROM shin_han_card, (SELECT @rownum := 0) r
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
         FROM shin_han_card
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
      FROM sam_sung_card
      WHERE usage_type IS NOT NULL
      GROUP BY usage_type

      UNION ALL

      SELECT
          '합계'                       AS title,
          SUM(amount)                AS stat1,
          SUM(benefit_amount)        AS stat2,
          SUM(currency)              AS stat3,
          SUM(balance_after_deposit) AS stat4
      FROM sam_sung_card
      WHERE usage_type IS NOT NULL)t
ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END;

