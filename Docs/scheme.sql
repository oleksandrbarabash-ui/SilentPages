-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `library` ;

-- -----------------------------------------------------
-- Table `library`.`user_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`user_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `description` TEXT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`app_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`app_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `firstname` VARCHAR(40) NOT NULL,
  `lastname` VARCHAR(40) NULL DEFAULT NULL,
  `email` VARCHAR(40) NOT NULL,
  `tel` VARCHAR(20) NOT NULL,
  `user_password` VARCHAR(255) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE,
  UNIQUE INDEX `tel` (`tel` ASC) VISIBLE,
  INDEX `idx_lastname` (`lastname` ASC) VISIBLE,
  INDEX `role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `app_user_ibfk_1`
    FOREIGN KEY (`role_id`)
    REFERENCES `library`.`user_role` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 478
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`book_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`book_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`genre` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`book` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `author` VARCHAR(100) NOT NULL,
  `language` VARCHAR(100) NOT NULL,
  `pages` INT NOT NULL,
  `book_status_id` INT NOT NULL,
  `genre_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `idx_name` (`name` ASC) VISIBLE,
  INDEX `book_status_id` (`book_status_id` ASC) VISIBLE,
  INDEX `genre_id` (`genre_id` ASC) VISIBLE,
  CONSTRAINT `book_ibfk_1`
    FOREIGN KEY (`book_status_id`)
    REFERENCES `library`.`book_status` (`id`),
  CONSTRAINT `book_ibfk_2`
    FOREIGN KEY (`genre_id`)
    REFERENCES `library`.`genre` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`shopping_cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`shopping_cart` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `shopping_cart_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `library`.`app_user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`cart_book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`cart_book` (
  `cart_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`cart_id`, `book_id`),
  INDEX `book_id` (`book_id` ASC) VISIBLE,
  CONSTRAINT `cart_book_ibfk_1`
    FOREIGN KEY (`cart_id`)
    REFERENCES `library`.`shopping_cart` (`id`),
  CONSTRAINT `cart_book_ibfk_2`
    FOREIGN KEY (`book_id`)
    REFERENCES `library`.`book` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`reservation_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`reservation_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`reservation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `owner_id` INT NOT NULL,
  `reservation_status_id` INT NOT NULL,
  `employee_id` INT NULL DEFAULT NULL,
  `create_time` DATE NOT NULL,
  `start_date` DATE NOT NULL,
  `update_time` DATE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `owner_id` (`owner_id` ASC) VISIBLE,
  INDEX `reservation_status_id` (`reservation_status_id` ASC) VISIBLE,
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `reservation_ibfk_1`
    FOREIGN KEY (`owner_id`)
    REFERENCES `library`.`app_user` (`id`),
  CONSTRAINT `reservation_ibfk_2`
    FOREIGN KEY (`reservation_status_id`)
    REFERENCES `library`.`reservation_status` (`id`),
  CONSTRAINT `reservation_ibfk_3`
    FOREIGN KEY (`employee_id`)
    REFERENCES `library`.`app_user` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`reservation_book_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`reservation_book_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(40) NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `library`.`reservation_book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`reservation_book` (
  `book_id` INT NOT NULL,
  `reservation_id` INT NOT NULL,
  `date_of_issue` DATE NOT NULL,
  `return_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `reservation_book_status_id` INT NOT NULL,
  PRIMARY KEY (`book_id`, `reservation_id`),
  INDEX `reservation_id` (`reservation_id` ASC) VISIBLE,
  INDEX `reservation_book_status_id` (`reservation_book_status_id` ASC) VISIBLE,
  CONSTRAINT `reservation_book_ibfk_1`
    FOREIGN KEY (`book_id`)
    REFERENCES `library`.`book` (`id`),
  CONSTRAINT `reservation_book_ibfk_2`
    FOREIGN KEY (`reservation_id`)
    REFERENCES `library`.`reservation` (`id`),
  CONSTRAINT `reservation_book_ibfk_3`
    FOREIGN KEY (`reservation_book_status_id`)
    REFERENCES `library`.`reservation_book_status` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
