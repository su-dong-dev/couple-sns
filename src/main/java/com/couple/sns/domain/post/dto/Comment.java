package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.user.dto.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Comment {

    private Long id;

    private User user;
    private Post post;

    private String content;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Comment fromEntity(CommentEntity commentEntity) {
        return new Comment(
            commentEntity.getId(),
            User.fromEntity(commentEntity.getUser()),
            Post.fromEntity(commentEntity.getPost()),
            commentEntity.getContent(),
            commentEntity.getRegisteredAt(),
            commentEntity.getUpdatedAt(),
            commentEntity.getDeletedAt()
        );
    }
}
