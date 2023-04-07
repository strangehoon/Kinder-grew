package com.sparta.finalproject.domain.user.repository;

import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findBykakaoId(Long id);
    List<User> findAllByRole(UserRoleEnum role);
    List<User> findByRoleAndNameContaining(UserRoleEnum userRoleEnum,String name);
}
