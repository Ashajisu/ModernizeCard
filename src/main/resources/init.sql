/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.14-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: hab2
-- ------------------------------------------------------
-- Server version	10.11.14-MariaDB-0ubuntu0.24.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assets`
--

DROP TABLE IF EXISTS `assets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `assets` (
  `depart_name` varchar(100) DEFAULT NULL,
  `asset_name` varchar(100) DEFAULT NULL,
  `asset_code` varchar(10) DEFAULT NULL,
  `depart_code` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categorys`
--

DROP TABLE IF EXISTS `categorys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorys` (
  `id` int(11) DEFAULT NULL,
  `cat_a` varchar(20) DEFAULT NULL,
  `part` varchar(20) DEFAULT NULL,
  `cat_b` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `kookmin_card`
--

DROP TABLE IF EXISTS `kookmin_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `kookmin_card` (
  `id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `used_card` varchar(255) DEFAULT NULL,
  `usage_type` varchar(255) DEFAULT NULL,
  `merchant_name` varchar(255) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `benefit_type` varchar(255) DEFAULT NULL,
  `purchase_type` varchar(255) DEFAULT NULL,
  `benefit_amount` bigint(20) DEFAULT NULL,
  `currency` bigint(20) DEFAULT NULL,
  `balance_after_deposit` bigint(20) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `nh_card`
--

DROP TABLE IF EXISTS `nh_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `nh_card` (
  `id` bigint(20) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `benefit_amount` bigint(20) DEFAULT NULL,
  `benefit_type` varchar(255) DEFAULT NULL,
  `currency` bigint(20) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL,
  `merchant_name` varchar(255) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `purchase_type` varchar(255) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `usage_type` varchar(255) DEFAULT NULL,
  `used_card` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint(20) DEFAULT NULL,
  `bgcolor` varchar(255) DEFAULT NULL,
  `img` varchar(255) DEFAULT NULL,
  `rank` varchar(255) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `disable` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `samsung_card`
--

DROP TABLE IF EXISTS `samsung_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `samsung_card` (
  `id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `used_card` varchar(255) DEFAULT NULL,
  `usage_type` varchar(255) DEFAULT NULL,
  `merchant_name` varchar(255) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `installment_total` bigint(20) DEFAULT NULL,
  `benefit_type` varchar(255) DEFAULT NULL,
  `purchase_type` varchar(255) DEFAULT NULL,
  `benefit_amount` bigint(20) DEFAULT NULL,
  `installment_months` int(11) DEFAULT NULL,
  `installment_number` int(11) DEFAULT NULL,
  `currency` bigint(20) DEFAULT NULL,
  `balance_after_deposit` bigint(20) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` bigint(20) DEFAULT NULL,
  `payment_type` varchar(30) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `day` bigint(20) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `boldtext` tinyint(1) DEFAULT NULL,
  `textcolor` varchar(255) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shinhan_card`
--

DROP TABLE IF EXISTS `shinhan_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `shinhan_card` (
  `id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime NOT NULL,
  `card_type` varchar(255) DEFAULT NULL,
  `used_card` varchar(255) DEFAULT NULL,
  `merchant_name` varchar(255) DEFAULT NULL,
  `approval_number` varchar(255) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `purchase_type` varchar(255) DEFAULT NULL,
  `usage_type` varchar(255) DEFAULT NULL,
  `currency` bigint(20) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stats_procedure`
--

DROP TABLE IF EXISTS `stats_procedure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `stats_procedure` (
  `id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `stat1` varchar(255) DEFAULT NULL,
  `stat2` varchar(255) DEFAULT NULL,
  `stat3` varchar(255) DEFAULT NULL,
  `stat4` varchar(255) DEFAULT NULL,
  `stat5` varchar(255) DEFAULT NULL,
  `stat6` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tmp_chit`
--

DROP TABLE IF EXISTS `tmp_chit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `tmp_chit` (
  `id` int(11) DEFAULT NULL,
  `category` varchar(20) DEFAULT NULL,
  `pay_method` varchar(10) DEFAULT NULL,
  `trans_date` date DEFAULT NULL,
  `trans_amount` decimal(10,2) DEFAULT NULL,
  `detail` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `unified_card_view`
--

DROP TABLE IF EXISTS `unified_card_view`;
/*!50001 DROP VIEW IF EXISTS `unified_card_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8mb4;
/*!50001 CREATE VIEW `unified_card_view` AS SELECT
 1 AS `card_company`,
  1 AS `id`,
  1 AS `transaction_date`,
  1 AS `usage_type`,
  1 AS `merchant_name`,
  1 AS `purchase_type`,
  1 AS `currency`,
  1 AS `payment_date` */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `empno` bigint(20) DEFAULT NULL,
  `usertype` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `deptcode` bigint(20) DEFAULT NULL,
  `position` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `woori_card`
--

DROP TABLE IF EXISTS `woori_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `woori_card` (
  `id` bigint(20) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `used_card` varchar(255) DEFAULT NULL,
  `usage_type` varchar(255) DEFAULT NULL,
  `merchant_name` varchar(255) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `installment_total` bigint(20) DEFAULT NULL,
  `benefit_type` varchar(255) DEFAULT NULL,
  `purchase_type` varchar(255) DEFAULT NULL,
  `benefit_amount` bigint(20) DEFAULT NULL,
  `installment_months` int(11) DEFAULT NULL,
  `installment_number` int(11) DEFAULT NULL,
  `currency` bigint(20) DEFAULT NULL,
  `balance_after_deposit` bigint(20) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'hab2'
--
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getAllUsageTypeCurrencyStatsDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getAllUsageTypeCurrencyStatsDate`(IN start_date date, IN end_date date)
BEGIN
    SELECT (@rownum := @rownum + 1) AS id,
           title,
           COALESCE(stat1, 0)       as stat1,
           COALESCE(stat2, 0)       as stat2,
           COALESCE(stat3, 0)       as stat3,
           COALESCE(stat4, 0)       as stat4,
           COALESCE(stat5, 0)       as stat5,
           COALESCE(stat6, 0)       as stat6
    FROM (SELECT usage_type    AS title,
                 SUM(currency) AS stat1, #합계
                 SUM(CASE WHEN card_company = 'SAMSUNG' THEN currency ELSE 0 END) AS stat2,
                 SUM(CASE WHEN card_company = 'KOOKMIN' THEN currency ELSE 0 END) AS stat3,
                 SUM(CASE WHEN card_company = 'SHINHAN' THEN currency ELSE 0 END) AS stat4,
                 SUM(CASE WHEN card_company = 'WOORI' THEN currency ELSE 0 END) AS stat5,
                 SUM(CASE WHEN card_company = 'NONGHYUP' THEN currency ELSE 0 END) AS stat6
          FROM unified_card_view
          WHERE usage_type IS NOT NULL
            and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))
          GROUP BY usage_type

          UNION ALL

          SELECT '개인합계'          AS title,
                 SUM(currency) AS stat1,
                 SUM(CASE WHEN card_company = 'SAMSUNG' THEN currency ELSE 0 END) AS stat2,
                 SUM(CASE WHEN card_company = 'KOOKMIN' THEN currency ELSE 0 END) AS stat3,
                 SUM(CASE WHEN card_company = 'SHINHAN' THEN currency ELSE 0 END) AS stat4,
                 SUM(CASE WHEN card_company = 'WOORI' THEN currency ELSE 0 END) AS stat5,
                 SUM(CASE WHEN card_company = 'NONGHYUP' THEN currency ELSE 0 END) AS stat6
          FROM unified_card_view
          WHERE ((usage_type IS NOT NULL) AND usage_type NOT IN ('송승미','엄지혜','엄지은','엄지성','임준영'))
            and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))

          UNION ALL

          SELECT '합계'          AS title,
                 SUM(currency) AS stat1,
                 SUM(CASE WHEN card_company = 'SAMSUNG' THEN currency ELSE 0 END) AS stat2,
                 SUM(CASE WHEN card_company = 'KOOKMIN' THEN currency ELSE 0 END) AS stat3,
                 SUM(CASE WHEN card_company = 'SHINHAN' THEN currency ELSE 0 END) AS stat4,
                 SUM(CASE WHEN card_company = 'WOORI' THEN currency ELSE 0 END) AS stat5,
                 SUM(CASE WHEN card_company = 'NONGHYUP' THEN currency ELSE 0 END) AS stat6
          FROM unified_card_view
          WHERE usage_type IS NOT NULL
            and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date)) )t
    ORDER BY
        CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
    SET @rownum := 0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getKBUsageTypeCurrencyStatsDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getKBUsageTypeCurrencyStatsDate`(IN start_date date, IN end_date date, IN pay_date date)
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
            and deleted = 0
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
            and deleted = 0
         )t
    ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getMonthlyStatsAll` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getMonthlyStatsAll`()
    SQL SECURITY INVOKER
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
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getNHUsageTypeCurrencyStatsDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getNHUsageTypeCurrencyStatsDate`(IN start_date date, IN end_date date, IN pay_date date)
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
          FROM nh_card
          WHERE usage_type IS NOT NULL
            and ((start_date IS NULL OR end_date IS NULL) OR ( transaction_date between start_date and end_date))
            and ((pay_date IS NULL) OR (payment_date = pay_date))
            and deleted = 0
          GROUP BY usage_type

          UNION ALL

          SELECT
              '합계'                       AS title,
              SUM(amount)                AS stat1,
              SUM(benefit_amount)        AS stat2,
              SUM(currency)              AS stat3
          FROM nh_card
          WHERE usage_type IS NOT NULL
            and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))
            and ((pay_date IS NULL) OR (payment_date = pay_date))
            and deleted = 0
         )t
    ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getSHUsageTypeCurrencyStats` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getSHUsageTypeCurrencyStats`()
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
               and deleted = 0
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
             WHERE usage_type != '승인취소'
               and deleted = 0) t
    ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getSSUsageTypeCurrencyStats` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getSSUsageTypeCurrencyStats`()
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
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getSSUsageTypeCurrencyStatsDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getSSUsageTypeCurrencyStatsDate`(IN start_date date, IN end_date date, IN pay_date date)
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
            and deleted = 0
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
            and deleted = 0
         )t
    ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'IGNORE_SPACE,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
/*!50003 DROP PROCEDURE IF EXISTS `getWOUsageTypeCurrencyStatsDate` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_general_ci */ ;
DELIMITER ;;
CREATE DEFINER=`hab2`@`%` PROCEDURE `getWOUsageTypeCurrencyStatsDate`(IN start_date date, IN end_date date, IN pay_date date)
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
          FROM woori_card
          WHERE usage_type IS NOT NULL
            and ((start_date IS NULL OR end_date IS NULL) OR ( transaction_date between start_date and end_date))
            and ((pay_date IS NULL) OR (payment_date = pay_date))
            and deleted = 0
          GROUP BY usage_type

          UNION ALL

          SELECT
              '합계'                       AS title,
              SUM(amount)                AS stat1,
              SUM(benefit_amount)        AS stat2,
              SUM(currency)              AS stat3,
              SUM(balance_after_deposit) AS stat4
          FROM woori_card
          WHERE usage_type IS NOT NULL
            and ((start_date IS NULL OR end_date IS NULL) OR (transaction_date between start_date and end_date))
            and ((pay_date IS NULL) OR (payment_date = pay_date))
            and deleted = 0
         )t
    ORDER BY CASE WHEN title = 'TOTAL' THEN 1 ELSE 0 END, title;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `unified_card_view`
--

/*!50001 DROP VIEW IF EXISTS `unified_card_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`hab2`@`%` SQL SECURITY DEFINER */
/*!50001 VIEW `unified_card_view` AS select 'KOOKMIN' AS `card_company`,`kookmin_card`.`id` AS `id`,`kookmin_card`.`transaction_date` AS `transaction_date`,`kookmin_card`.`usage_type` AS `usage_type`,`kookmin_card`.`merchant_name` AS `merchant_name`,coalesce(`kookmin_card`.`purchase_type`,'일시불') AS `purchase_type`,coalesce(`kookmin_card`.`currency`,0) AS `currency`,`kookmin_card`.`payment_date` AS `payment_date` from `kookmin_card` where `kookmin_card`.`deleted` = 0 union all select 'SAMSUNG' AS `card_company`,`samsung_card`.`id` AS `id`,`samsung_card`.`transaction_date` AS `transaction_date`,`samsung_card`.`usage_type` AS `usage_type`,`samsung_card`.`merchant_name` AS `merchant_name`,coalesce(`samsung_card`.`purchase_type`,'일시불') AS `purchase_type`,coalesce(`samsung_card`.`currency`,0) AS `currency`,`samsung_card`.`payment_date` AS `payment_date` from `samsung_card` where `samsung_card`.`deleted` = 0 union all select 'SHINHAN' AS `card_company`,`shinhan_card`.`id` AS `id`,`shinhan_card`.`transaction_date` AS `transaction_date`,case when `shinhan_card`.`usage_type` = '엄지수' then '차량' else `shinhan_card`.`usage_type` end AS `usage_type`,`shinhan_card`.`merchant_name` AS `merchant_name`,coalesce(`shinhan_card`.`purchase_type`,'일시불') AS `purchase_type`,coalesce(`shinhan_card`.`currency`,0) AS `currency`,`shinhan_card`.`payment_date` AS `payment_date` from `shinhan_card` where `shinhan_card`.`deleted` = 0 and `shinhan_card`.`purchase_type` = '결제확정' union all select 'WOORI' AS `card_company`,`woori_card`.`id` AS `id`,`woori_card`.`transaction_date` AS `transaction_date`,`woori_card`.`usage_type` AS `usage_type`,`woori_card`.`merchant_name` AS `merchant_name`,coalesce(`woori_card`.`purchase_type`,'일시불') AS `purchase_type`,coalesce(`woori_card`.`currency`,0) AS `currency`,`woori_card`.`payment_date` AS `payment_date` from `woori_card` where `woori_card`.`deleted` = 0 union all select 'NONGHYUP' AS `card_company`,`nh_card`.`id` AS `id`,`nh_card`.`transaction_date` AS `transaction_date`,`nh_card`.`usage_type` AS `usage_type`,`nh_card`.`merchant_name` AS `merchant_name`,coalesce(`nh_card`.`purchase_type`,'일시불') AS `purchase_type`,coalesce(`nh_card`.`currency`,0) AS `currency`,`nh_card`.`payment_date` AS `payment_date` from `nh_card` where `nh_card`.`deleted` = 0 */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-19 14:29:27
