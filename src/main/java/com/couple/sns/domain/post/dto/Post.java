package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Post {

    private Long id;
    private String title;
    private String body;

    private User user;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Post fromEntity(PostEntity postEntity) {
        return new Post(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getBody(),
            User.fromEntity(postEntity.getUser()),
            postEntity.getRegisteredAt(),
            postEntity.getUpdatedAt(),
            postEntity.getDeletedAt()
        );
    }
}
