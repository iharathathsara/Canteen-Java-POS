-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.31 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for canteen_db
CREATE DATABASE IF NOT EXISTS `canteen_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `canteen_db`;

-- Dumping structure for table canteen_db.brand
CREATE TABLE IF NOT EXISTS `brand` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.brand: ~2 rows (approximately)
/*!40000 ALTER TABLE `brand` DISABLE KEYS */;
/*!40000 ALTER TABLE `brand` ENABLE KEYS */;

-- Dumping structure for table canteen_db.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.category: ~11 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Dumping structure for table canteen_db.company
CREATE TABLE IF NOT EXISTS `company` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.company: ~2 rows (approximately)
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` (`id`, `name`, `mobile`) VALUES
	(1, 'none', '0700000000');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;

-- Dumping structure for table canteen_db.company_branch
CREATE TABLE IF NOT EXISTS `company_branch` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `company_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_company_branch_company` (`company_id`),
  CONSTRAINT `FK_company_branch_company` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.company_branch: ~2 rows (approximately)
/*!40000 ALTER TABLE `company_branch` DISABLE KEYS */;
/*!40000 ALTER TABLE `company_branch` ENABLE KEYS */;

-- Dumping structure for table canteen_db.grn
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.grn: ~21 rows (approximately)
/*!40000 ALTER TABLE `grn` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn` ENABLE KEYS */;

-- Dumping structure for table canteen_db.grn_item
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.grn_item: ~34 rows (approximately)
/*!40000 ALTER TABLE `grn_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn_item` ENABLE KEYS */;

-- Dumping structure for table canteen_db.grn_payment
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.grn_payment: ~21 rows (approximately)
/*!40000 ALTER TABLE `grn_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `grn_payment` ENABLE KEYS */;

-- Dumping structure for table canteen_db.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date_time` datetime DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `unique_id` text,
  PRIMARY KEY (`id`),
  KEY `FK_invoice_user` (`user_id`),
  CONSTRAINT `FK_invoice_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.invoice: ~12 rows (approximately)
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;

-- Dumping structure for table canteen_db.invoice_item
CREATE TABLE IF NOT EXISTS `invoice_item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `qty` int DEFAULT NULL,
  `invoice_id` int DEFAULT NULL,
  `stock_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_invoice_item_invoice` (`invoice_id`),
  KEY `FK_invoice_item_stock` (`stock_id`),
  CONSTRAINT `FK_invoice_item_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `FK_invoice_item_stock` FOREIGN KEY (`stock_id`) REFERENCES `stock` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.invoice_item: ~12 rows (approximately)
/*!40000 ALTER TABLE `invoice_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_item` ENABLE KEYS */;

-- Dumping structure for table canteen_db.invoice_payment
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.invoice_payment: ~12 rows (approximately)
/*!40000 ALTER TABLE `invoice_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_payment` ENABLE KEYS */;

-- Dumping structure for table canteen_db.payment_type
CREATE TABLE IF NOT EXISTS `payment_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.payment_type: ~2 rows (approximately)
/*!40000 ALTER TABLE `payment_type` DISABLE KEYS */;
INSERT INTO `payment_type` (`id`, `name`) VALUES
	(1, 'Cash');
/*!40000 ALTER TABLE `payment_type` ENABLE KEYS */;

-- Dumping structure for table canteen_db.product
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.product: ~3 rows (approximately)
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;

-- Dumping structure for table canteen_db.status
CREATE TABLE IF NOT EXISTS `status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.status: ~2 rows (approximately)
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`id`, `name`) VALUES
	(1, 'Active'),
	(2, 'Inactive');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;

-- Dumping structure for table canteen_db.stock
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.stock: ~21 rows (approximately)
/*!40000 ALTER TABLE `stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock` ENABLE KEYS */;

-- Dumping structure for table canteen_db.supplier
CREATE TABLE IF NOT EXISTS `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `mobile` varchar(50) DEFAULT NULL,
  `company_branch_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_supplier_company_branch` (`company_branch_id`),
  CONSTRAINT `FK_supplier_company_branch` FOREIGN KEY (`company_branch_id`) REFERENCES `company_branch` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.supplier: ~2 rows (approximately)
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;

-- Dumping structure for table canteen_db.user
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

-- Dumping data for table canteen_db.user: ~2 rows (approximately)
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `fname`, `lname`, `uniqid`, `mobile`, `email`, `password`, `line1`, `line2`, `city`, `user_type_id`, `status_id`, `registered_date`, `updated_date`) VALUES
	(1, 'ihara', 'thathsara', 'U_1719907188715580', '0763947527', 'ihara@gmail.com', '56913fee33399ef2870405ae32de375d946b0545a879957e74279978ce2cac94', 'kaduruduwa', 'wanchawala', 'galle', 1, 1, '2024-07-02 13:29:48', '2024-12-26 18:29:10');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;

-- Dumping structure for table canteen_db.user_type
CREATE TABLE IF NOT EXISTS `user_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table canteen_db.user_type: ~4 rows (approximately)
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
