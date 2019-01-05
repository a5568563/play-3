package com.play.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.play.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
}
