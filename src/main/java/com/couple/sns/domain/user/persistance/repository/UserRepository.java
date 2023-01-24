package com.couple.sns.domain.user.persistance.repository;

import com.couple.sns.domain.user.persistance.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity, Long> {

}
