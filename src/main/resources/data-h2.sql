INSERT IGNORE INTO `category` (`id`, `name`) VALUES
    (1, 'Textbook'),
    (2, 'Exam Prep'),
    (3, 'Literature'),
    (4, 'Computer'),
    (5, 'Other');

INSERT IGNORE INTO `user` (`id`, `username`, `password`, `nickname`, `phone`, `role`) VALUES
    (1, 'admin', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Admin', '13800000000', 1),
    (2, 'student1', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Zhang San', '13800000001', 0),
    (3, 'student2', '$2a$10$v1Q9lmVxckwBRXyEVGESY.uhA9BohoQM8J7KpBpFsdm/JmMeQF5ke', 'Li Si', '13800000002', 0);

INSERT IGNORE INTO `book` (`id`, `title`, `author`, `isbn`, `price`, `description`, `category_id`, `seller_id`, `status`, `condition`) VALUES
    (1, 'Java Programming', 'Zhang San', '978-7-111-12345-6', 35.00, 'Like new, no notes', 4, 2, 1, 1),
    (2, 'Advanced Mathematics Vol.1', 'Tongji University', '978-7-111-23456-7', 20.00, 'Some notes, readable', 1, 2, 1, 1),
    (3, 'Postgraduate English Vocabulary', 'Zhu Wei', '978-7-111-34567-8', 15.00, 'Almost new', 2, 3, 1, 0),
    (4, 'Computer Networks', 'Xie Xiren', '978-7-115-35153-6', 40.00, 'Brand new, unopened', 4, 3, 1, 0),
    (5, 'One Hundred Years of Solitude', 'Marquez', '978-7-5442-4490-9', 30.00, 'Classic literature', 3, 2, 1, 1);