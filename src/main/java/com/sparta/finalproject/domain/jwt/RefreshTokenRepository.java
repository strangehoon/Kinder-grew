package com.sparta.finalproject.domain.jwt;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByKakaoId(Long kakaoId);
}