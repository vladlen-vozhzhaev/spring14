package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String comment;
    public Long userId;
    public Long articleId;

    public Comment() {}

    public Comment(String comment, Long userId, Long articleId) {
        this.comment = comment;
        this.userId = userId;
        this.articleId = articleId;
    }
}