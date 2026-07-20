-- Railway MySQL schema (idempotent - safe for spring.sql.init)
-- All ALTER TABLE statements check if column exists first

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(100) NOT NULL, `nickname` VARCHAR(50) DEFAULT NULL,
    `phone` VARCHAR(20) DEFAULT NULL, `role` TINYINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `book` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `title` VARCHAR(100) NOT NULL,
    `author` VARCHAR(50) DEFAULT NULL, `isbn` VARCHAR(20) DEFAULT NULL,
    `price` DECIMAL(10,2) NOT NULL, `description` TEXT DEFAULT NULL,
    `cover_image` VARCHAR(255) DEFAULT NULL, `category_id` BIGINT DEFAULT NULL,
    `seller_id` BIGINT NOT NULL, `status` TINYINT NOT NULL DEFAULT 1,
    `condition` TINYINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`), KEY `idx_seller_id` (`seller_id`), KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `trade_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `order_no` VARCHAR(32) NOT NULL,
    `book_id` BIGINT NOT NULL, `buyer_id` BIGINT NOT NULL, `seller_id` BIGINT NOT NULL,
    `price` DECIMAL(10,2) NOT NULL, `status` TINYINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_buyer_id` (`buyer_id`), KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `book_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL, `parent_id` BIGINT DEFAULT NULL,
    `content` TEXT NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), KEY `idx_book_id` (`book_id`),
    KEY `idx_user_id` (`user_id`), KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `order_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `order_id` BIGINT NOT NULL,
    `operator_id` BIGINT DEFAULT NULL, `operator_name` VARCHAR(50) DEFAULT NULL,
    `action` VARCHAR(100) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`), KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `payment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `order_id` BIGINT NOT NULL,
    `amount` DECIMAL(10,2) NOT NULL, `pay_method` VARCHAR(20) NOT NULL,
    `pay_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `notification` (
    `id` BIGINT NOT NULL AUTO_INCREMENT, `user_id` BIGINT NOT NULL,
    `type` VARCHAR(20) NOT NULL, `title` VARCHAR(200) NOT NULL,
    `content` TEXT DEFAULT NULL, `related_id` BIGINT DEFAULT NULL,
    `is_read` TINYINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`), KEY `idx_user_id` (`user_id`), KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Conditionally add missing `deleted` columns (idempotent - never fails)
SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='user' AND COLUMN_NAME='deleted')=0, 'ALTER TABLE `user` ADD COLUMN `deleted` TINYINT NOT NULL DEFAULT 0', 'SELECT 1'); PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
SET @sql = IF((SELECT COUNT(*) FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='book' AND COLUMN_NAME='deleted')=0, 'ALTER TABLE `book` ADD COLUMN `deleted` TINYINT NOT NULL DEFAULT 0', 'SELECT 1'); PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
