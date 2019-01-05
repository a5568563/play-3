package com.play.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.play.entity.Keylist;
import com.play.entity.News;

public interface KeylistRepository extends JpaRepository<Keylist, Integer> {
	Keylist findByNews(News news);
}
