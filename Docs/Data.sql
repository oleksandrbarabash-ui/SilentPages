-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: library
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES (1,'Ангеліна','Підкопай','email1@gmail.com','380 50 123 4567','12345',1),(2,'Дмитро','Петрів','email2@gmail.com','380 67 987 6543','12345',1),(3,'Ольга','Коваленко','email3@gmail.com','380 93 111 2233','admin',2),(4,'Марія','Савченко','email4@gmail.com','380 93 598 2643','4273',1),(5,'Олександр','Мельник','email5@gmail.com','380 67 598 2843','73652',1),(6,'Наталія','Бондаренко','email6@gmail.com','380 67 398 7843','732488',1),(7,'Сергій','Шевченко','email7@gmail.com','380 50 378 7243','атткт247',1),(8,'Олена','Лисенко','email8@gmail.com','380 50 348 4863','5275оегоег',1),(9,'Дмитро','Петренко','email9@gmail.com','380 67 298 4843','12345',2),(10,'Вікторія','Кравченко','email10@gmail.com','380 67 298 4893','\\75373поео',2),(11,'Михайло','Кравченко','email11@gmail.com','380 67 888 4893','12345',1),(12,'Алексій','Пономаренко','email12@gmail.com','380 50 878 6693','75277пл',1),(13,'Марина','Коц','email13@gmail.com','380 67 898 4773','88837поьпо',1),(14,'Ірина','Ткаченко','email78@gmail.com','+380 (99) 337 26 77','12345',1),(18,'Hacker','Admin','hacker@test.com','777','hacked',2),(19,'ZAP','ZAP','zaproxy@example.com','9999999999','ZAP',1);
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES (1,'1984','Джордж Орвелл','Англійська',328,2,1),(2,'Майстер і Маргарита','Михайло Булгаков','Російська',480,1,2),(3,'За двома зайцями','Михайло Старицький','Українська',200,2,3),(4,'Гаррі Поттер і філософський камінь','Джоан Роулінг','Англійська',223,1,4),(5,'Гордість і упередження','Джейн Остін','Англійська',432,1,2),(6,'Портрет Доріана Грея','Оскар Вайльд','Англійська',254,1,6),(7,'Три товариші','Еріх Марія Ремарк','Німецька',416,2,2),(8,'Аліса в Країні Чудес','Льюїс Керролл','Англійська',128,2,7),(9,'Запах думки','Роберт Шеклі','Англійська',224,2,8),(10,'Тигролови','Іван Багряний','Українська',288,2,9),(11,'Сторонній','Альбер Камю','Французька',192,1,6),(12,'Кобзар','Тарас Шевченко','Українська',240,1,5),(13,'Дюна','Френк Герберт','Англійська',544,2,8),(14,'Маленький принц','Антуан де Сент-Екзюпері','Французька',96,2,10),(15,'Пригоди Тома Сойєра','Марк Твен','Англійська',224,1,9),(16,'Нічний цирк','Ерін Морґенштерн','Англійська',387,2,4),(17,'Ловець у житі','Джером Селінджер','Англійська',277,1,2),(18,'Фауст','Йоганн В. Гете','Німецька',464,1,6),(19,'Дванадцять стільців','Ільф і Петров','Російська',384,2,3),(20,'Пісня льоду й полум’я','Джордж Мартін','Англійська',694,1,4);
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `book_status`
--

LOCK TABLES `book_status` WRITE;
/*!40000 ALTER TABLE `book_status` DISABLE KEYS */;
INSERT INTO `book_status` VALUES (1,'В наявності','книга наявна в бібліотеці'),(2,'Немає в наявності','книгу вже хтось взяв');
/*!40000 ALTER TABLE `book_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `cart_book`
--

LOCK TABLES `cart_book` WRITE;
/*!40000 ALTER TABLE `cart_book` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `genre`
--

LOCK TABLES `genre` WRITE;
/*!40000 ALTER TABLE `genre` DISABLE KEYS */;
INSERT INTO `genre` VALUES (1,'Антиутопія'),(2,'Роман'),(3,'Комедія'),(4,'Фентезі'),(5,'Поезія'),(6,'Філософський роман'),(7,'Казка'),(8,'Наукова фантастика'),(9,'Пригодницький роман'),(10,'Притча');
/*!40000 ALTER TABLE `genre` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,1,1,'2025-06-09','2025-06-12',3,'2025-06-19'),(2,1,5,'2025-02-05','2025-02-07',9,'2025-06-19'),(3,2,3,'2025-06-08','2025-06-08',3,'2025-06-08'),(4,4,1,'2025-06-07','2025-06-08',10,'2025-06-08'),(5,7,1,'2025-04-24','2025-04-24',3,'2025-04-24'),(6,5,2,'2025-05-10','2025-05-11',9,'2025-05-12'),(7,5,1,'2025-06-01','2025-06-05',3,'2025-06-19'),(8,6,1,'2025-06-02','2025-06-03',3,'2025-06-03'),(9,4,5,'2025-04-01','2025-04-02',9,'2025-05-01'),(10,2,4,'2025-03-10','2025-03-12',3,'2025-04-15'),(11,1,1,'2025-06-05','2025-06-06',3,'2025-06-07'),(12,11,2,'2025-06-12','2025-06-13',3,'2025-06-19'),(13,8,2,'2025-05-05','2025-05-07',10,'2025-05-07'),(14,12,5,'2025-02-01','2025-02-05',9,'2025-03-01'),(15,13,1,'2025-06-10','2025-06-11',3,'2025-06-11'),(16,14,3,'2025-06-19','2025-06-21',NULL,'2025-06-19'),(17,1,3,'2026-05-13','2026-05-14',NULL,'2026-05-13');
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reservation_book`
--

LOCK TABLES `reservation_book` WRITE;
/*!40000 ALTER TABLE `reservation_book` DISABLE KEYS */;
INSERT INTO `reservation_book` VALUES (1,1,NULL,NULL,'2025-07-12',3),(2,3,NULL,NULL,'2025-07-08',3),(2,16,NULL,NULL,'2025-07-19',3),(3,2,'2025-02-07','2025-06-19','2025-03-07',4),(3,10,'2025-03-12','2025-04-15','2025-04-12',2),(5,6,'2025-05-12',NULL,'2025-06-11',5),(6,9,'2025-04-01','2025-05-01','2025-05-02',4),(6,17,NULL,NULL,'2026-06-11',3),(7,4,'2025-06-11',NULL,'2025-07-08',1),(8,5,'2025-04-25',NULL,'2025-05-25',1),(9,4,'2025-06-11',NULL,'2025-07-08',1),(10,5,'2025-04-25','2025-05-24','2025-05-25',4),(13,7,'2025-06-01',NULL,'2025-07-01',3),(14,8,NULL,NULL,'2025-07-03',1),(15,11,NULL,NULL,'2025-07-06',1),(16,12,NULL,NULL,'2025-07-13',5),(17,13,'2025-06-09',NULL,'2025-06-07',5),(18,14,'2025-02-05','2025-03-01','2025-03-01',4),(19,15,'2025-06-11',NULL,'2025-07-11',1);
/*!40000 ALTER TABLE `reservation_book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reservation_book_status`
--

LOCK TABLES `reservation_book_status` WRITE;
/*!40000 ALTER TABLE `reservation_book_status` DISABLE KEYS */;
INSERT INTO `reservation_book_status` VALUES (1,'Отримано','клієнт отримав книгу'),(2,'Прострочено','клієнт не повернув книгу до вказаного строку'),(3,'Очікування','заказ очикує на обробку'),(4,'Повернуто','клієнт повернув книгу до бібліотеки'),(5,'Відхилено','заказ і книги в заказі відхилено');
/*!40000 ALTER TABLE `reservation_book_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `reservation_status`
--

LOCK TABLES `reservation_status` WRITE;
/*!40000 ALTER TABLE `reservation_status` DISABLE KEYS */;
INSERT INTO `reservation_status` VALUES (1,'Підтверджено','заказ прийнято'),(2,'Відхилено','заказ відхилено'),(3,'Очікування','заказ очикує на обробку'),(4,'Прострочено','заказ прострочено'),(5,'Закрито','заказ виповнен і закрит');
/*!40000 ALTER TABLE `reservation_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `shopping_cart`
--

LOCK TABLES `shopping_cart` WRITE;
/*!40000 ALTER TABLE `shopping_cart` DISABLE KEYS */;
INSERT INTO `shopping_cart` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10),(11,14),(12,19);
/*!40000 ALTER TABLE `shopping_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,'client','client of the library'),(2,'admin','admin of the library');
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-18 20:47:23
