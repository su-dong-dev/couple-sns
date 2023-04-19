package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.LikeEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Like {

    private Long id;
    private Long userId;
    private String userName;
    private Long postId;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Like fromEntity(LikeEntity likeEntity) {
        return new Like(
            likeEntity.getId(),
            likeEntity.getUserId(),
            likeEntity.getUserName(),
            likeEntity.getPostId(),
            likeEntity.getRegisteredAt(),
            likeEntity.getUpdatedAt(),
            likeEntity.getDeletedAt()
        );
    }
}
