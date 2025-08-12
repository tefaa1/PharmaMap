package com.spring.authservice.repository;

import com.spring.authservice.model.TokensBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenBlacklistRepository extends JpaRepository<TokensBlacklist,Long> {

    Boolean existsByToken(String token);
}
