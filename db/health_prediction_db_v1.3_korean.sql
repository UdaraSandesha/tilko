CREATE DATABASE  IF NOT EXISTS `health_prediction_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `health_prediction_db`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: health_prediction_db
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `disease`
--

DROP TABLE IF EXISTS `disease`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disease` (
  `disease_id` int unsigned NOT NULL AUTO_INCREMENT,
  `disease_name` varchar(100) NOT NULL,
  `cause` varchar(500) DEFAULT NULL,
  `recommended_food` varchar(500) DEFAULT NULL,
  `caution_food` varchar(500) DEFAULT NULL,
  `recommendation_reason` varchar(500) DEFAULT NULL,
  `be_careful_reason` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`disease_id`),
  UNIQUE KEY `disease_UNIQUE` (`disease_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disease`
--

LOCK TABLES `disease` WRITE;
/*!40000 ALTER TABLE `disease` DISABLE KEYS */;
INSERT INTO `disease` VALUES (1,'고혈압','나트륨 과잉 섭취시 혈관 내 삼투압이 상승하여 혈액량이 증가해 혈압 상승 및 고혈압 발생','바나나, 아보카도, 토마토, 당근, 콩, 고구마 등','염장굴비, 자반생선, 마른멸치, 오징어채, 피클 등','칼륨이 많이 함유되어 있는 식품은 나트륨의 배설을 도와줍니다.','염분의 주성분인 나트륨은 세포외액의 가장 중요한 성분으로 혈중 나트륨 농도가 높으면 삼투압 현상에 의해 혈액량 증가로 혈압 상승에 기여합니다. 따라서, 나트륨이 많이 함유되어 있는 식품은 자제하는 것이 좋습니다.'),(2,'당뇨','췌장이 충분한 인슐린을 만들어 내지 못하거나 인슐린에 적절하게 반응하지 못함','양배추, 토마토, 버섯, 미역 등','케이크, 꿀, 과일통조림, 과일청 등','당지수가 낮은 식품은 식후혈당 변화가 적습니다.','당지수가 70 이상인 식품은 당질의 흡수속도가 빨라 혈압을 급격히 상승시킬수 있습니다. 따라서, 당지수가 높은 식품은 자제하는 것이 좋습니다.'),(3,'빈혈','적혈구 수 및 헤모글루빈 수치가 떨어지고 철 겹핍으로 인한 빈혈 발생','육류, 생선, 시금치 등','커피, 녹차, 탄산음료, 알코올 등','철이 충분히 함유된 식품은 철 결핍성으로 인한 빈혈 개선에 도움을 줍니다.','탄닌이 많이 함유되어 있는 식품은 철과 결합하여 철의 흡수를 저해합니다. 따라서, 식사 도중이나 식사 전후에는 가급적 자제하는 것이 좋습니다. 또한, 알코올은 혈액을 만드는 데 필요한 엽산, 비타민 B12의 흡수를 방해하므로 자제하는 것이 좋습니다.'),(4,'허혈성심장질환','심장에 혈액을 공급해주는 관상동맥이 좁아지거나 막히게 되어 심장근육에 혈액 공급이 부족하여 발생','연어, 고등어구이, 조기구이 등','새우, 계란 노른자, 삼겹살, 감자튀김 등','오메가-3 지방산은 주로 생선 기름에 많이 포함되어 있으며, 오메가-3는 혈소판 응집과 염증반응을 감소시키고 혈중 중성지방 농도를 감소시킵니다.','포화지방과 트랜스지방이 많이 함유된 식품은 LDL 수치가 높아 혈관을 막게합니다. 따라서, 포화지방 및 콜레스테롤이 많이 함유된 식품은 자제하는 것이 좋습니다.'),(5,'뇌혈관질환','뇌의 혈액을 공급하고 있는 혈관이 막히거나 터져 뇌 손상 유발. 고혈압 환자들에게 많이 발생','바나나, 아보카도, 토마토, 당근, 콩, 고구마 등','염장굴비, 자반생선, 마른멸치, 오징어채, 피클 등','나트륨을 과잉 섭취할 경우 혈압을 증가시켜 혈관이 찢어질 수 있음으로 나트륨을 배출하는 데 효과적인 칼륨이 많이 함유되어 있는 식품을 섭취하는 것이 좋습니다.','염분의 주성분인 나트륨은 세포외액의 가장 중요한 성분으로 혈중 나트륨 농도가 높으면 삼투압 현상에 의해 혈액량 증가로 혈압 상승에 기여합니다. 따라서, 나트륨이 많이 함유되어 있는 식품은 자제하는 것이 좋습니다.'),(6,'녹내장','시신경이 손상되어 시야 결손이 나타나는 질환. 주로 안압이 상승하여 발생','라즈베리, 아로니아, 가지, 아사이베리 등','커피, 초콜릿, 홍차 등','안토시아닌이 풍부한 음식은 항산화제로서의 가치가 있어 녹내장 발생의 요인인 \"산화 스트레스\"를 줄여줍니다. ','카페인이 많은 음식을 섭취할 경우 안압이 상승하여 녹내장 발병 위험이 높아지므로, 카페인이 많이 함유되어 있는 식품은 자제하는 것이 좋습니다.'),(7,'당뇨망막변증','당뇨병으로 인해 혈관이 약해지고 혈액 속의 성분이 혈관을 빠져나가 망막에 쌓여 발생','양배추, 토마토, 버섯, 미역 등','케이크, 꿀, 과일통조림, 과일청 등','당지수가 낮은 식품은 식후혈당 변화가 적습니다.','본 질환은 당뇨병에 의해 발병됩니다. 따라서, 당지수가 높은 식품은 당질의 흡수속도가 빨라 혈압을 상승시키므로 자제하는 것이 좋습니다.');
/*!40000 ALTER TABLE `disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_checkup_item`
--

DROP TABLE IF EXISTS `health_checkup_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_checkup_item` (
  `checkup_item_id` int NOT NULL AUTO_INCREMENT,
  `checkup_item` varchar(100) NOT NULL,
  `short_name` varchar(10) DEFAULT NULL,
  `related_health_function` varchar(100) DEFAULT NULL,
  `food_ingredients` text,
  `effect_of_food` text,
  PRIMARY KEY (`checkup_item_id`),
  UNIQUE KEY `checkup_item_UNIQUE` (`checkup_item`),
  UNIQUE KEY `short_name_UNIQUE` (`short_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_checkup_item`
--

LOCK TABLES `health_checkup_item` WRITE;
/*!40000 ALTER TABLE `health_checkup_item` DISABLE KEYS */;
INSERT INTO `health_checkup_item` VALUES (1,'수축기혈압','sbp','혈압조절','코엔자임Q10',NULL),(2,'이완기혈압','dbp','혈압조절','코엔자임Q10',NULL),(3,'키','height','체지방감소','가르시니아캄보지아 추출물,공액리놀레산,녹차추출물,키토산/키토올리고당',NULL),(4,'몸무게','weight','체지방감소','가르시니아캄보지아 추출물,공액리놀레산,녹차추출물,키토산/키토올리고당',NULL),(5,'BMI','bmi','체지방감소','가르시니아캄보지아 추출물,공액리놀레산,녹차추출물,키토산/키토올리고당',NULL),(6,'허리둘레','waist','체지방감소','가르시니아캄보지아 추출물,공액리놀레산,녹차추출물,키토산/키토올리고당',NULL),(7,'혈색소','hgb',NULL,NULL,NULL),(8,'공복혈당','fbs','혈당조절','구아검/구아검가수분해물,구아바잎 추출물,귀리식이섬유,난소화성말토덱스트린,달맞이꽃종자 추출물,대두식이섬유,밀식이섬유,바나나잎 추출물,옥수수겨식이섬유,이눌린/치커리추출물,호로파종자식이섬유',NULL),(9,'총콜레스테롤','tc','혈중콜레스테롤개선','감마리놀렌산 함유 유지,구아검/구아검가수분해물,귀리식이섬유,글루코만난(곤약 곤약만난),녹차추출물,대두단백,대두식이섬유,레시틴,마늘,스피루리나,식물스테롤/식물스테롤에스테르,옥수수겨식이섬유,이눌린/치커리추출물,차전자피식이섬유,클로렐라,키토산/키토올리고당,홍국',NULL),(10,'중성지방','tg','혈중중성지방개선','EPA 및 DHA함유 유지,난소화성말토덱스트린',NULL),(11,'HDL','hdl','혈행개선','EPA 및 DHA 함유유지,감마리놀렌산 함유 유지,영지버섯자실체 추출물,은행잎 추출물,홍삼',NULL),(12,'LDL','ldl','혈행개선','EPA 및 DHA 함유유지,감마리놀렌산 함유 유지,영지버섯자실체 추출물,은행잎 추출물,홍삼',NULL),(13,'AST','ast','간건강','밀크씨슬 추출물',NULL),(14,'ALT','alt','간건강','밀크씨슬 추출물',NULL),(15,'감마GTP','rgtp','간건강','밀크씨슬 추출물',NULL),(16,'혈청크레아티닌','cr','요로건강',NULL,'요로감염의 주된 원인균인 E. coli의 요로 상피에의 부착을 방지하고, E. coli의 소변 배출을 촉진하여 박테리아들이 요로를 군락화 하는 것을 막는데 도움을 줄 수 있습니다.');
/*!40000 ALTER TABLE `health_checkup_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_checkup_result`
--

DROP TABLE IF EXISTS `health_checkup_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_checkup_result` (
  `checkup_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int unsigned NOT NULL,
  `checkup_date` date NOT NULL,
  PRIMARY KEY (`checkup_id`),
  UNIQUE KEY `user_health_checkup_date_unique` (`user_id`,`checkup_date`),
  KEY `user_health_checkup_result_idx` (`user_id`),
  CONSTRAINT `user_health_checkup_result` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_checkup_result`
--

LOCK TABLES `health_checkup_result` WRITE;
/*!40000 ALTER TABLE `health_checkup_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_checkup_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `health_checkup_value`
--

DROP TABLE IF EXISTS `health_checkup_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `health_checkup_value` (
  `checkup_id` int NOT NULL,
  `checkup_item` varchar(100) NOT NULL,
  `value` decimal(7,3) DEFAULT NULL,
  KEY `health_checkup_value_idx` (`checkup_id`),
  KEY `health_checkup_item_health_checkup_value_idx` (`checkup_item`),
  CONSTRAINT `health_checkup_item_health_checkup_value` FOREIGN KEY (`checkup_item`) REFERENCES `health_checkup_item` (`checkup_item`),
  CONSTRAINT `health_checkup_result_health_checkup_value` FOREIGN KEY (`checkup_id`) REFERENCES `health_checkup_result` (`checkup_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `health_checkup_value`
--

LOCK TABLES `health_checkup_value` WRITE;
/*!40000 ALTER TABLE `health_checkup_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `health_checkup_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile_no` varchar(50) DEFAULT NULL,
  `password` varchar(512) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `height` decimal(7,3) DEFAULT NULL,
  `weight` decimal(7,3) DEFAULT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `profile_image` longtext,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-05 23:22:34
