package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
