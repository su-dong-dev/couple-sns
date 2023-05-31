package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.CommentLikeEntity;
import com.couple.sns.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeDto {

    private UserDto user;
    private CommentDto comment;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static CommentLikeDto fromEntity(CommentLikeEntity commentLikeEntity) {
        return new CommentLikeDto(
            UserDto.fromEntity(commentLikeEntity.getUser()),
            CommentDto.fromEntity(commentLikeEntity.getComment()),
            commentLikeEntity.getCreatedAt(),
            commentLikeEntity.getUpdatedAt(),
            commentLikeEntity.getDeletedAt()
        );
    }
}
