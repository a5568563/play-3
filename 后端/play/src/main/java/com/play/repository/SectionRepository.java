package com.play.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.play.entity.Section;


@Repository
public interface SectionRepository extends JpaRepository<Section, Integer>{
	List<Section> findAll();
}
