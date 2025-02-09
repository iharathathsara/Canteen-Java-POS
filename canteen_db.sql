/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `canteen_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `canteen_db`;

CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
INSERT INTO `brand` (`id`, `name`) VALUES
	(1, 'ABC');
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`id`, `name`) VALUES
	(1, 'none');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` (`id`, `name`, `mobile`) VALUES
	(1, 'none', '0700000000');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `company_branch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `company_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_company_branch_company` (`company_id`),
  CONSTRAINT `FK_company_branch_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `company_branch` DISABLE KEYS */;
INSERT INTO `company_branch` (`id`, `name`, `mobile`, `address`, `company_id`) VALUES
	(1, 'none', '0700000000', 'none', 1);
/*!40000 ALTER TABLE `company_branch` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `grn` (
  `id` int NOT NULL AUTO_INCREMENT,
  `supplier_id` int DEFAULT NULL,
  `date_time` datetime DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `unique_id` text,
  PRIMARY KEY (`id`),
  KEY `FK_grn_supplier` (`supplier_id`),
  KEY `FK_grn_user` (`user_id`),
  CONSTRAINT `FK_grn_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
  CONSTRAINT `FK_grn_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
INSERT INTO `grn` (`id`, `supplier_id`, `date_time`, `user_id`, `unique_id`) VALUES
	(1, 1, '2025-01-18 18:28:05', 1, '1737205085620-1');
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `grn_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `quantity` int DEFAULT NULL,
  `buying_price` double DEFAULT NULL,
  `grn_id` int DEFAULT NULL,
  `stock_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__grn` (`grn_id`),
  KEY `FK__stock` (`stock_id`),
  CONSTRAINT `FK__grn` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
  CONSTRAINT `FK__stock` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `grn_item` DISABLE KEYS */;
INSERT INTO `grn_item` (`id`, `quantity`, `buying_price`, `grn_id`, `stock_id`) VALUES
	(1, 5, 100, 1, 1);
/*!40000 ALTER TABLE `grn_item` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `grn_payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `grn_id` int DEFAULT NULL,
  `payment_type_id` int DEFAULT NULL,
  `payment` double DEFAULT NULL,
  `balance` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_grn_payment_grn` (`grn_id`),
  KEY `FK_grn_payment_payment_type` (`payment_type_id`),
  CONSTRAINT `FK_grn_payment_grn` FOREIGN KEY (`grn_id`) REFERENCES `grn` (`id`),
  CONSTRAINT `FK_grn_payment_payment_type` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `grn_payment` DISABLE KEYS */;
INSERT INTO `grn_payment` (`id`, `grn_id`, `payment_type_id`, `payment`, `balance`) VALUES
	(1, 1, 1, 500, 0);
/*!40000 ALTER TABLE `grn_payment` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `unique_id` text,
  PRIMARY KEY (`id`),
  KEY `FK_invoice_user` (`user_id`),
  CONSTRAINT `FK_invoice_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` (`id`, `date_time`, `user_id`, `unique_id`) VALUES
	(1, '2025-01-18 18:28:57', 1, '1737205137011-1'),
	(2, '2025-01-18 23:35:13', 1, '1737223513236-1'),
	(3, '2025-01-20 12:57:26', 1, '1737358046517-1');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` int DEFAULT NULL,
  `invoice_id` int DEFAULT NULL,
  `stock_id` int DEFAULT NULL,
  `selling_price` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_invoice_item_invoice` (`invoice_id`),
  KEY `FK_invoice_item_stock` (`stock_id`),
  CONSTRAINT `FK_invoice_item_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `FK_invoice_item_stock` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
INSERT INTO `invoice_item` (`id`, `qty`, `invoice_id`, `stock_id`, `selling_price`) VALUES
	(1, 2, 1, 1, 200),
	(2, 4, 2, 1, 200),
	(3, 1, 3, 1, 200);
/*!40000 ALTER TABLE `invoice_item` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `invoice_payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_id` int DEFAULT NULL,
  `payment_type_id` int DEFAULT NULL,
  `payment` double DEFAULT NULL,
  `balance` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_invoice_payment_invoice` (`invoice_id`),
  KEY `FK_invoice_payment_payment_type` (`payment_type_id`),
  CONSTRAINT `FK_invoice_payment_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `FK_invoice_payment_payment_type` FOREIGN KEY (`payment_type_id`) REFERENCES `payment_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `invoice_payment` DISABLE KEYS */;
INSERT INTO `invoice_payment` (`id`, `invoice_id`, `payment_type_id`, `payment`, `balance`) VALUES
	(1, 1, 1, 400, 0),
	(2, 2, 1, 1000, 200),
	(3, 3, 1, 200, 0);
/*!40000 ALTER TABLE `invoice_payment` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `payment_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `payment_type` DISABLE KEYS */;
INSERT INTO `payment_type` (`id`, `name`) VALUES
	(1, 'Cash');
/*!40000 ALTER TABLE `payment_type` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(50) DEFAULT NULL,
  `brand_id` int DEFAULT NULL,
  `category_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_product_brand` (`brand_id`),
  KEY `FK_product_category` (`category_id`),
  CONSTRAINT `FK_product_brand` FOREIGN KEY (`brand_id`) REFERENCES `brand` (`id`),
  CONSTRAINT `FK_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` (`id`, `title`, `brand_id`, `category_id`) VALUES
	(1, 'test1', 1, 1);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `selling_price` double DEFAULT NULL,
  `mfd` date DEFAULT NULL,
  `exd` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK__product` (`product_id`),
  CONSTRAINT `FK__product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
INSERT INTO `stock` (`id`, `product_id`, `quantity`, `selling_price`, `mfd`, `exd`) VALUES
	(1, 1, -2, 200, '2025-01-01', '2025-01-31');
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `company_branch_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_supplier_company_branch` (`company_branch_id`),
  CONSTRAINT `FK_supplier_company_branch` FOREIGN KEY (`company_branch_id`) REFERENCES `company_branch` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` (`id`, `name`, `mobile`, `company_branch_id`) VALUES
	(1, 'none', '0700000000', 1);
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lname` varchar(50) NOT NULL,
  `uniqid` varchar(50) NOT NULL,
  `mobile` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `line1` varchar(100) NOT NULL,
  `line2` varchar(100) NOT NULL,
  `city` varchar(50) NOT NULL,
  `user_type_id` int DEFAULT '0',
  `status_id` int NOT NULL DEFAULT '1',
  `registered_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqid` (`uniqid`),
  KEY `FK_user_user_type` (`user_type_id`),
  KEY `FK_user_status` (`status_id`),
  CONSTRAINT `FK_user_status` FOREIGN KEY (`status_id`) REFERENCES `status` (`id`),
  CONSTRAINT `FK_user_user_type` FOREIGN KEY (`user_type_id`) REFERENCES `user_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `fname`, `lname`, `uniqid`, `mobile`, `email`, `password`, `line1`, `line2`, `city`, `user_type_id`, `status_id`, `registered_date`, `updated_date`) VALUES
	(1, 'ihara', 'thathsara', 'U_1719907188715580', '0763947527', 'ihara@gmail.com', '56913fee33399ef2870405ae32de375d946b0545a879957e74279978ce2cac94', 'kaduruduwa', 'wanchawala', 'galle', 1, 1, '2024-07-02 13:29:48', '2024-12-26 18:29:10');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

CREATE TABLE IF NOT EXISTS `user_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40000 ALTER TABLE `user_type` DISABLE KEYS */;
INSERT INTO `user_type` (`id`, `name`) VALUES
	(1, 'Super Admin'),
	(2, 'Admin'),
	(3, 'Manager'),
	(4, 'Cashier'),
	(5, 'Employee');
/*!40000 ALTER TABLE `user_type` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
