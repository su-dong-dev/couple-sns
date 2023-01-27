package com.couple.sns.domain.user.persistance.repository;

import com.couple.sns.domain.user.persistance.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(String userId);
}
