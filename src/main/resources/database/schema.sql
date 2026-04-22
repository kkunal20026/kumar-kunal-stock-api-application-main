SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `stocks` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `stocks`;

CREATE TABLE `stock`(
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `current_price` decimal(19,2) DEFAULT NULL,
  `last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `stocks`.`stock`(`id`,`name`,`current_price`,`last_update`) VALUES 
(1, 'Kumar', 12.0, CURRENT_TIMESTAMP),
(2, 'Kunal', 13.0, CURRENT_TIMESTAMP), 
(3, 'Jerome',14.0 , CURRENT_TIMESTAMP),
(4, 'Oskam', 15.0, CURRENT_TIMESTAMP);

COMMIT;