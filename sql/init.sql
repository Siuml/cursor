CREATE DATABASE IF NOT EXISTS book_trade DEFAULT CHARSET utf8mb4;
USE book_trade;

DROP TABLE IF EXISTS `notification`;
DROP TABLE IF EXISTS `payment`;
DROP TABLE IF EXISTS `order_log`;
DROP TABLE IF EXISTS `comment`;
DROP TABLE IF EXISTS `trade_order`;
DROP TABLE IF EXISTS `book`;
DROP TABLE IF EXISTS `category`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `username`    VARCHAR(50)  NOT NULL COMMENT 'username',
    `password`    VARCHAR(100) NOT NULL COMMENT 'password(BCrypt)',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT 'nickname',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT 'phone',
    `role`        TINYINT      NOT NULL DEFAULT 0 COMMENT 'role',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT 'logic delete',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='user';

CREATE TABLE IF NOT EXISTS `category` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `name` VARCHAR(50) NOT NULL COMMENT 'category name',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='category';

CREATE TABLE IF NOT EXISTS `book` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `title`       VARCHAR(100)  NOT NULL COMMENT 'title',
    `author`      VARCHAR(50)   DEFAULT NULL COMMENT 'author',
    `isbn`        VARCHAR(20)   DEFAULT NULL COMMENT 'ISBN',
    `price`       DECIMAL(10,2) NOT NULL COMMENT 'price',
    `description` TEXT          DEFAULT NULL COMMENT 'description',
    `cover_image` VARCHAR(255)  DEFAULT NULL COMMENT 'cover image path',
    `category_id` BIGINT        DEFAULT NULL COMMENT 'category id',
    `seller_id`   BIGINT        NOT NULL COMMENT 'seller id',
    `status`      TINYINT       NOT NULL DEFAULT 1 COMMENT '0-off 1-on 2-sold',
    `condition`   TINYINT       NOT NULL DEFAULT 0 COMMENT '0-new 1-good 2-fair',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    `deleted`     TINYINT       NOT NULL DEFAULT 0 COMMENT 'logic delete',
    PRIMARY KEY (`id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='book';

CREATE TABLE IF NOT EXISTS `trade_order` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `order_no`    VARCHAR(32)   NOT NULL COMMENT 'order number',
    `book_id`     BIGINT        NOT NULL COMMENT 'book id',
    `buyer_id`    BIGINT        NOT NULL COMMENT 'buyer id',
    `seller_id`   BIGINT        NOT NULL COMMENT 'seller id',
    `price`       DECIMAL(10,2) NOT NULL COMMENT 'price',
    `status`      TINYINT       NOT NULL DEFAULT 0 COMMENT '0-pending 1-confirmed 2-completed 3-cancelled 4-paid',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='order';

CREATE TABLE IF NOT EXISTS `comment` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `book_id`     BIGINT   NOT NULL COMMENT 'book id',
    `user_id`     BIGINT   NOT NULL COMMENT 'user id',
    `parent_id`   BIGINT   DEFAULT NULL COMMENT 'parent comment id',
    `content`     TEXT     NOT NULL COMMENT 'content',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    PRIMARY KEY (`id`),
    KEY `idx_book_id` (`book_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='comment';

CREATE TABLE IF NOT EXISTS `order_log` (
    `id`            BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `order_id`      BIGINT   NOT NULL COMMENT 'order id',
    `operator_id`   BIGINT   DEFAULT NULL COMMENT 'operator id',
    `operator_name` VARCHAR(50) DEFAULT NULL COMMENT 'operator name',
    `action`        VARCHAR(100) NOT NULL COMMENT 'action',
    `create_time`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='order log';

CREATE TABLE IF NOT EXISTS `payment` (
    `id`        BIGINT        NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `order_id`  BIGINT        NOT NULL COMMENT 'order id',
    `amount`    DECIMAL(10,2) NOT NULL COMMENT 'amount',
    `pay_method` VARCHAR(20)  NOT NULL COMMENT 'payment method',
    `pay_time`  DATETIME      NOT NULL COMMENT 'payment time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='payment';

CREATE TABLE IF NOT EXISTS `notification` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `user_id`     BIGINT       NOT NULL COMMENT 'receiver user id',
    `type`        VARCHAR(20)  NOT NULL COMMENT 'order/comment/system',
    `title`       VARCHAR(200) NOT NULL COMMENT 'title',
    `content`     TEXT         DEFAULT NULL COMMENT 'content',
    `related_id`  BIGINT       DEFAULT NULL COMMENT 'related id',
    `is_read`     TINYINT      NOT NULL DEFAULT 0 COMMENT '0-unread 1-read',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='notification';

INSERT INTO `category` (`name`) VALUES ('Textbook'),('Exam Prep'),('Literature'),('Computer'),('Other');

INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `role`) VALUES
    ('admin', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Admin', '13800000000', 1),
    ('student1', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Zhang San', '13800000001', 0),
    ('student2', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Li Si', '13800000002', 0);

INSERT INTO `book` (`title`, `author`, `isbn`, `price`, `description`, `category_id`, `seller_id`, `status`, `condition`) VALUES
    ('Java Programming', 'Zhang San', '978-7-111-12345-6', 35.00, 'Like new, no notes', 4, 2, 1, 1),
    ('Advanced Mathematics Vol.1', 'Tongji University', '978-7-111-23456-7', 20.00, 'Some notes, readable', 1, 2, 1, 1),
    ('Postgraduate English Vocabulary', 'Zhu Wei', '978-7-111-34567-8', 15.00, 'Almost new', 2, 3, 1, 0);