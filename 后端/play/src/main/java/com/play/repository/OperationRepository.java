package com.play.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.play.entity.Operation;
import com.play.entity.User;


public interface OperationRepository extends JpaRepository<Operation, Integer>{
	Page<Operation> findByUser(User user, Pageable pageable);
}
