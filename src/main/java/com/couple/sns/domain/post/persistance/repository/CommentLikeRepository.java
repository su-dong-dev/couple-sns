package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.CommentLikeEntity;
import com.couple.sns.domain.post.persistance.PostLikeEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity, Long> {

    Optional<CommentLikeEntity> findByCommentIdAndUserId(Long commentId, Long userId);

    Page<CommentLikeEntity> findByCommentId(Long commentId, Pageable pageable);

}
