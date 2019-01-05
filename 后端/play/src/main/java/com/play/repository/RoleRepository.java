package com.play.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.play.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	Role findByName(String name);
}
