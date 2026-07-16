INSERT INTO `category` (`name`) VALUES
    ('教材'),
    ('考研'),
    ('文学'),
    ('计算机'),
    ('其他');

INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `role`) VALUES
    ('admin', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', '管理员', '13800000000', 1),
    ('student1', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', '张三', '13800000001', 0),
    ('student2', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', '李四', '13800000002', 0);

INSERT INTO `book` (`title`, `author`, `isbn`, `price`, `description`, `category_id`, `seller_id`, `status`, `condition`) VALUES
    ('Java程序设计', '张三', '978-7-111-12345-6', 35.00, '九成新，无笔记', 4, 2, 1, 1),
    ('高等数学（上册）', '同济大学', '978-7-111-23456-7', 20.00, '有少量笔记，不影响阅读', 1, 2, 1, 1),
    ('考研英语词汇', '朱伟', '978-7-111-34567-8', 15.00, '几乎全新', 2, 3, 1, 0),
    ('计算机网络', '谢希仁', '978-7-115-35153-6', 40.00, '全新未拆封', 4, 3, 1, 0),
    ('百年孤独', '马尔克斯', '978-7-5442-4490-9', 30.00, '经典文学作品', 3, 2, 1, 1);