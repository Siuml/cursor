package com.booktrade.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestEntity {

    public static void main(String[] args) {
        System.out.println("===== Testing User Class =====");
        User user = new User();
        user.setId(1L);
        user.setUsername("zhangsan");
        user.setPassword("123456");
        user.setNickname("张三");
        user.setPhone("13800138000");
        user.setRole(0);
        user.setCreateTime(LocalDateTime.now());
        user.setDeleted(0);

        System.out.println("User ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Password: " + user.getPassword());
        System.out.println("Nickname: " + user.getNickname());
        System.out.println("Phone: " + user.getPhone());
        System.out.println("Role: " + user.getRole());
        System.out.println("CreateTime: " + user.getCreateTime());
        System.out.println("Deleted: " + user.getDeleted());

        System.out.println("\n===== Testing Book Class =====");
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Java Programming");
        book.setAuthor("John Doe");
        book.setIsbn("978-1234567890");
        book.setPrice(new BigDecimal("59.99"));
        book.setDescription("A comprehensive guide to Java");
        book.setCoverImage("cover.jpg");
        book.setCategoryId(1L);
        book.setSellerId(1L);
        book.setStatus(1);
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());
        book.setDeleted(0);

        System.out.println("Book ID: " + book.getId());
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Price: " + book.getPrice());
        System.out.println("Description: " + book.getDescription());
        System.out.println("CoverImage: " + book.getCoverImage());
        System.out.println("CategoryId: " + book.getCategoryId());
        System.out.println("SellerId: " + book.getSellerId());
        System.out.println("Status: " + book.getStatus());
        System.out.println("CreateTime: " + book.getCreateTime());
        System.out.println("UpdateTime: " + book.getUpdateTime());
        System.out.println("Deleted: " + book.getDeleted());

        System.out.println("\n===== All tests completed successfully! =====");
    }
}