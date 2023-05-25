package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private UserDto user;
    private PostDto post;

    private String content;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static CommentDto fromEntity(CommentEntity commentEntity) {
        return new CommentDto(
            commentEntity.getId(),
            UserDto.fromEntity(commentEntity.getUser()),
            PostDto.fromEntity(commentEntity.getPost()),
            commentEntity.getContent(),
            commentEntity.getRegisteredAt(),
            commentEntity.getUpdatedAt(),
            commentEntity.getDeletedAt()
        );
    }
}
