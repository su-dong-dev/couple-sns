package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Like {

    private Long id;
    private User user;
    private Post post;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Like fromEntity(LikeEntity likeEntity) {
        return new Like(
            likeEntity.getId(),
            User.fromEntity(likeEntity.getUser()),
            Post.fromEntity(likeEntity.getPost()),
            likeEntity.getRegisteredAt(),
            likeEntity.getUpdatedAt(),
            likeEntity.getDeletedAt()
        );
    }
}
