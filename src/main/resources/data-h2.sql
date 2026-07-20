INSERT INTO `category` (`name`) VALUES ('Textbook'),('Exam Prep'),('Literature'),('Computer'),('Other');
INSERT INTO `user` (`username`, `password`, `nickname`, `phone`, `role`) VALUES
    ('admin', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Admin', '13800000000', 1),
    ('student1', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Zhang San', '13800000001', 0),
    ('student2', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Li Si', '13800000002', 0);
INSERT INTO `book` (`title`, `author`, `isbn`, `price`, `description`, `category_id`, `seller_id`, `status`, `condition`) VALUES
    ('Java Programming', 'Zhang San', '978-7-111-12345-6', 35.00, 'Like new, no notes', 4, 2, 1, 1),
    ('Advanced Mathematics Vol.1', 'Tongji University', '978-7-111-23456-7', 20.00, 'Some notes, readable', 1, 2, 1, 1),
    ('Postgraduate English Vocabulary', 'Zhu Wei', '978-7-111-34567-8', 15.00, 'Almost new', 2, 3, 1, 0),
    ('Computer Networks', 'Xie Xiren', '978-7-115-35153-6', 40.00, 'Brand new, unopened', 4, 3, 1, 0),
    ('One Hundred Years of Solitude', 'Marquez', '978-7-5442-4490-9', 30.00, 'Classic literature', 3, 2, 1, 1);