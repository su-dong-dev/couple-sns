package com.couple.sns.domain.user.persistance.repository;

import com.couple.sns.domain.user.persistance.TokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByRefreshToken(String token);

}
