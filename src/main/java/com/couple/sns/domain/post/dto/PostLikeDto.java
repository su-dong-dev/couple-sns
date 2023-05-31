package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.PostLikeEntity;
import com.couple.sns.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeDto {

    private UserDto user;
    private PostDto post;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostLikeDto fromEntity(PostLikeEntity postLikeEntity) {
        return new PostLikeDto(
            UserDto.fromEntity(postLikeEntity.getUser()),
            PostDto.fromEntity(postLikeEntity.getPost()),
            postLikeEntity.getCreatedAt(),
            postLikeEntity.getUpdatedAt(),
            postLikeEntity.getDeletedAt()
        );
    }
}
