package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.PostLikeEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    Optional<PostLikeEntity> findByPostIdAndUserId(Long postId, Long userId);

    Page<PostLikeEntity> findByPostId(Long postId, Pageable pageable);

}
