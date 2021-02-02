CREATE DATABASE IF NOT EXISTS `clojure`;

USE `clojure`;

DROP TABLE IF EXISTS `images`;

CREATE TABLE `images` (
  `id` int NOT NULL AUTO_INCREMENT,
  `src` varchar(50) NOT NULL,
  `alt` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `producttypes`;

CREATE TABLE `producttypes` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `description` varchar(1000) NOT NULL,
    `imgid` int(11) DEFAULT 1,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_IMAGES_PRODUCTTYPES` FOREIGN KEY (`imgid`) REFERENCES `images` (`id`) ON DELETE restrict ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `products`;

CREATE TABLE `products` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(50) NOT NULL,
    `imageid` int(11),
    `description` varchar(1000) NOT NULL,
    `producttypeid` int(11) NOT NULL,
    `price` int(11) DEFAULT 0 NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_IMAGES_PRODUCTS` FOREIGN KEY (`imageid`) REFERENCES `images` (`id`) ON DELETE restrict ON UPDATE CASCADE,
    CONSTRAINT `FK_PRODUCTTYPES_PRODUCTS` FOREIGN KEY (`producttypeid`) REFERENCES `producttypes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `mail` varchar(100) NOT NULL,
  `isadmin` bit(1) NOT NULL,
  `address` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ordertime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `userid` int NOT NULL,
  `isfinished` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_USERS_ORDERS` (`userid`),
  CONSTRAINT `FK_USERS_ORDERS` FOREIGN KEY (`userid`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `orders_product`;

CREATE TABLE `orders_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `orderid` int NOT NULL,
  `productid` int NOT NULL,
  `qty` int NOT NULL,
  `productprice` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ORDERS_ORDERS_PRODUCT` (`orderid`),
  KEY `FK_PRODUCTS_ORDERS_PRODUCT` (`productid`),
  CONSTRAINT `FK_ORDERS_ORDERS_PRODUCT` FOREIGN KEY (`orderid`) REFERENCES `orders` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_PRODUCTS_ORDERS_PRODUCT` FOREIGN KEY (`productid`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO USERS(username,password,firstname,lastname,mail,isadmin,address) VALUES ('administrator12','bcrypt+sha512$f7de1e2aba57f3685e1f68d4988d4bd4$12$d4185c1fb77e9149383b82bcac0baf51439ceed3c7e8c636','Admin','Admin','randomemail123@gmail.com',1,'Adresa 123')


