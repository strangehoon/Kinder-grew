package com.sparta.finalproject.domain.user.repository;

import com.sparta.finalproject.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findBykakaoId(Long id);
}
