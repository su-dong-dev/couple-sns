package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.persistance.UserEntity;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostDto {

    private Long postId;

    private String content;
    private String location;

    private UserDto user;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostDto of(Long postId, String content, String location, UserDto user,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new PostDto(postId, content, location, user, createdAt, updatedAt, deletedAt);
    }

    public static PostDto of(String content, String location) {
        return new PostDto(null, content, location, null, null, null, null);
    }

    public static PostDto fromEntity(PostEntity postEntity) {
        return new PostDto(
            postEntity.getId(),
            postEntity.getContent(),
            postEntity.getLocation(),
            UserDto.fromEntity(postEntity.getUser()),
            postEntity.getCreatedAt(),
            postEntity.getUpdatedAt(),
            postEntity.getDeletedAt()
        );
    }

    public PostEntity toEntity(UserEntity user) {
        return PostEntity.of(
            user,
            content,
            location
        );
    }

}
