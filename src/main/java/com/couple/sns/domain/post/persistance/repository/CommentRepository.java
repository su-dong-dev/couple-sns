package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @EntityGraph(attributePaths = {"user"})
    Page<CommentEntity> findByPostId(Long postId, Pageable pageable);

}
