package com.couple.sns.domain.post.persistance.repository;

import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.post.persistance.LikeEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByTypeAndTypeIdAndUser_Id(LikeType type, Long typeId, Long userId);

    Page<LikeEntity> findAllByTypeAndTypeId(LikeType type, Long typeId, Pageable pageable);

}
