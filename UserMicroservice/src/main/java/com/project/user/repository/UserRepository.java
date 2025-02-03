package com.project.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	  User findByUserName(String userName);
	  User findByEmail(String email);
}

