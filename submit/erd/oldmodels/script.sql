-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';


-- -----------------------------------------------------
-- Schema nhn_academy_29
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema nhn_academy_29
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `nhn_academy_29` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;

-- -----------------------------------------------------
-- Table `nhn_academy_29`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`users` (
                                                        `user_id` VARCHAR(50) NOT NULL COMMENT '아이디',
    `user_name` VARCHAR(50) NOT NULL COMMENT '이름',
    `user_password` VARCHAR(200) NOT NULL COMMENT 'mysql password 사용',
    `user_birth` VARCHAR(8) NOT NULL COMMENT '생년월일 : 19840503',
    `user_auth` VARCHAR(10) NOT NULL COMMENT '권한: ROLE_ADMIN,ROLE_USER',
    `user_point` INT NOT NULL COMMENT 'default : 1000000',
    `created_at` DATETIME NOT NULL COMMENT '가입일자',
    `latest_login_at` DATETIME NULL DEFAULT NULL COMMENT '마지막 로그인 일자',
    PRIMARY KEY (`user_id`),
    UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '회원';


-- -----------------------------------------------------
-- Table `nhn_academy_29`.`useraddress`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`useraddress` (
                                                              `useraddrID` INT NOT NULL AUTO_INCREMENT,
                                                              `useraddress` VARCHAR(150) NOT NULL,
    `userID` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`useraddrID`),
    UNIQUE INDEX `useraddrID_UNIQUE` (`useraddrID` ASC) VISIBLE,
    INDEX `userID_idx` (`userID` ASC) VISIBLE,
    UNIQUE INDEX `userID_UNIQUE` (`userID` ASC) VISIBLE,
    CONSTRAINT `fk_useraddress_userid`
    FOREIGN KEY (`userID`)
    REFERENCES `nhn_academy_29`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `nhn_academy_29`.`userpointDetail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`userpointDetail` (
                                                                  `userpoinDetailsID` INT NOT NULL DEFAULT 0,
                                                                  `userID` VARCHAR(50) NULL,
    `userpointchange` VARCHAR(100) NULL,
    `userpointchangedate` DATETIME NULL,
    PRIMARY KEY (`userpoinDetailsID`),
    UNIQUE INDEX `userpointID_UNIQUE` (`userpoinDetailsID` ASC) VISIBLE,
    INDEX `fk_userpointDetail_userID_idx` (`userID` ASC) VISIBLE,
    CONSTRAINT `fk_userpointDetail_userID`
    FOREIGN KEY (`userID`)
    REFERENCES `nhn_academy_29`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

USE `nhn_academy_29` ;

-- -----------------------------------------------------
-- Table `nhn_academy_29`.`Products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`Products` (
                                                           `ProductID` INT NOT NULL AUTO_INCREMENT,
                                                           `ModelNumber` VARCHAR(10) CHARACTER SET 'utf8mb3' NULL DEFAULT NULL,
    `ModelName` VARCHAR(120) CHARACTER SET 'utf8mb3' NULL DEFAULT NULL,
    `ProductThumbnail` VARCHAR(45) NULL,
    `ProductImage` VARCHAR(45) NULL DEFAULT NULL,
    `UnitCost` DECIMAL(15,0) NULL DEFAULT NULL,
    `Description` TEXT NULL DEFAULT NULL,
    PRIMARY KEY (`ProductID`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nhn_academy_29`.`Categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`Categories` (
                                                             `CategoryID` INT NOT NULL AUTO_INCREMENT,
                                                             `CategoryName` VARCHAR(50) NULL DEFAULT NULL,
    `ProductID` INT NULL,
    PRIMARY KEY (`CategoryID`),
    INDEX `fk_Categories_Products_idx` (`ProductID` ASC) VISIBLE,
    CONSTRAINT `fk_Categories_Products`
    FOREIGN KEY (`ProductID`)
    REFERENCES `nhn_academy_29`.`Products` (`ProductID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nhn_academy_29`.`Orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`Orders` (
                                                         `OrderID` INT NOT NULL DEFAULT 0,
                                                         `userID` VARCHAR(50) NOT NULL,
    `OrderDate` DATETIME NULL DEFAULT NULL,
    `ShipDate` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`OrderID`),
    INDEX `fk_Order_user_idx` (`userID` ASC) VISIBLE,
    CONSTRAINT `fk_Order_user`
    FOREIGN KEY (`userID`)
    REFERENCES `nhn_academy_29`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `nhn_academy_29`.`OrderDetails`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`OrderDetails` (
                                                               `OrderID` INT NOT NULL,
                                                               `ProductID` INT NOT NULL,
                                                               `Quantity` INT NULL DEFAULT NULL,
                                                               `UnitCost` DECIMAL(15,0) NULL DEFAULT NULL,
    PRIMARY KEY (`OrderID`, `ProductID`),
    INDEX `fk_OrderDetails_Products` (`ProductID` ASC) VISIBLE,
    CONSTRAINT `fk_OrderDetails_Orders`
    FOREIGN KEY (`OrderID`)
    REFERENCES `nhn_academy_29`.`Orders` (`OrderID`),
    CONSTRAINT `fk_OrderDetails_Products`
    FOREIGN KEY (`ProductID`)
    REFERENCES `nhn_academy_29`.`Products` (`ProductID`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;



-- -----------------------------------------------------
-- Table `nhn_academy_29`.`ShoppingCart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `nhn_academy_29`.`ShoppingCart` (
                                                               `RecordID` INT NOT NULL AUTO_INCREMENT,
                                                               `CartID` VARCHAR(150) CHARACTER SET 'utf8mb3' NULL DEFAULT NULL,
    `Quantity` INT NULL DEFAULT NULL,
    `ProductID` INT NULL DEFAULT NULL,
    `DateCreateed` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`RecordID`),
    INDEX `fk_cart_ProductID` (`ProductID` ASC) VISIBLE,
    UNIQUE INDEX `ProductID_UNIQUE` (`ProductID` ASC) VISIBLE,
    CONSTRAINT `fk_cart_ProductID`
    FOREIGN KEY (`ProductID`)
    REFERENCES `nhn_academy_29`.`Products` (`ProductID`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
