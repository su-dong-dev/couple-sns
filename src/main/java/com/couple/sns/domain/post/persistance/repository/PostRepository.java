package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.persistance.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(value = "select p from PostEntity p join fetch p.user",
        countQuery = "SELECT COUNT(DISTINCT p) FROM PostEntity p INNER JOIN p.user")
    Page<PostEntity> findAllJoinFetch(Pageable pageable);

    Page<PostEntity> findAllByUser(UserEntity userEntity, Pageable pageable);

}
