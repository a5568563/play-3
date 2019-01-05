package com.play.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.play.entity.Comment;
import com.play.entity.News;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	List<Comment> findByNews(News news);
}
