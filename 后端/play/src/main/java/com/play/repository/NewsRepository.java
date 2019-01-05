package com.play.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.play.entity.News;
import com.play.entity.Section;

public interface NewsRepository extends JpaRepository<News, Integer> {
	List<News> findBySection(Section section);
}
