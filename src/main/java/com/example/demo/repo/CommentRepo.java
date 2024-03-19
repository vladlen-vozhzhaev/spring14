package com.example.demo.repo;

import com.example.demo.model.Comment;
import com.example.demo.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends CrudRepository<Comment, Long> {
    List<Comment> findByArticleId(Long id);
}
