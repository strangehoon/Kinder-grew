package com.sparta.finalproject.domain.parent.repository;

import com.sparta.finalproject.domain.parent.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long>{

}
