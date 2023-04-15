package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.persistance.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserAndPost(PostEntity postEntity, UserEntity userEntity);

}
