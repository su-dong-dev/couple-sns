package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.LikeEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByPostIdAndUserId(Long postId, Long userId);

    Page<LikeEntity> findAllByPostId(Long postId, Pageable pageable);
}
