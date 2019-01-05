package com.play.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.play.entity.User;
import com.play.entity.User_info;

public interface UserInfoRepository extends JpaRepository<User_info, Integer> {
	User_info findByNickname(String nickname);
}
