-- 创建数据库
CREATE DATABASE IF NOT EXISTS book_trade DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE book_trade;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `role`        TINYINT      NOT NULL DEFAULT 0 COMMENT '角色：0-学生 1-管理员',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍分类表';

-- 书籍表
CREATE TABLE IF NOT EXISTS `book` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`       VARCHAR(100)  NOT NULL COMMENT '书名',
    `author`      VARCHAR(50)   DEFAULT NULL COMMENT '作者',
    `isbn`        VARCHAR(20)   DEFAULT NULL COMMENT 'ISBN',
    `price`       DECIMAL(10,2) NOT NULL COMMENT '售价',
    `description` TEXT          DEFAULT NULL COMMENT '描述',
    `cover_image` VARCHAR(255)  DEFAULT NULL COMMENT '封面图片路径',
    `category_id` BIGINT        DEFAULT NULL COMMENT '分类ID',
    `seller_id`   BIGINT        NOT NULL COMMENT '卖家ID',
    `status`      TINYINT       NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-在售 2-已售',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_seller_id` (`seller_id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

-- 订单表
CREATE TABLE IF NOT EXISTS `trade_order` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `order_no`    VARCHAR(32)   NOT NULL COMMENT '订单编号',
    `book_id`     BIGINT        NOT NULL COMMENT '书籍ID',
    `buyer_id`    BIGINT        NOT NULL COMMENT '买家ID',
    `seller_id`   BIGINT        NOT NULL COMMENT '卖家ID',
    `price`       DECIMAL(10,2) NOT NULL COMMENT '成交价格',
    `status`      TINYINT       NOT NULL DEFAULT 0 COMMENT '状态：0-待确认 1-已确认 2-已完成 3-已取消',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_buyer_id` (`buyer_id`),
    KEY `idx_seller_id` (`seller_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 初始数据
INSERT INTO `category` (`name`) VALUES
    ('教材'),
    ('考研'),
    ('文学'),
    ('计算机'),
    ('其他');

INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `role`) VALUES
    ('admin', '123456', '管理员', '13800000000', 1),
    ('student1', '123456', '张三', '13800000001', 0),
    ('student2', '123456', '李四', '13800000002', 0);

INSERT INTO `book` (`title`, `author`, `isbn`, `price`, `description`, `category_id`, `seller_id`, `status`) VALUES
    ('Java程序设计', '张三', '978-7-111-12345-6', 35.00, '九成新，无笔记', 4, 2, 1),
    ('高等数学（上册）', '同济大学', '978-7-111-23456-7', 20.00, '有少量笔记，不影响阅读', 1, 2, 1),
    ('考研英语词汇', '朱伟', '978-7-111-34567-8', 15.00, '几乎全新', 2, 3, 1);
