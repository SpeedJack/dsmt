Drop SCHEMA IF EXISTS `auctions`;
CREATE SCHEMA `auctions` ;
USE `auctions`;


CREATE TABLE `auctions`.`user` (
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`username`));
  
  INSERT INTO user VALUES ('user', 'password');

SELECT * FROM user WHERE username = 'user' and password = 'password'