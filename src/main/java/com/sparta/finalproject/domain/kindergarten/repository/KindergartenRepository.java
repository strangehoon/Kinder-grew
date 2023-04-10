package com.sparta.finalproject.domain.kindergarten.repository;

import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KindergartenRepository extends JpaRepository<Kindergarten, Long> {
    List<Kindergarten> findAllByKindergartenNameContaining(String keyword);
}
