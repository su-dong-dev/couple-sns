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

    private Long id;
    private String title;
    private String body;

    private UserDto user;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static PostDto of(Long id, String title, String body, UserDto user,
        LocalDateTime registeredAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new PostDto(id, title, body, user, registeredAt, updatedAt, deletedAt);
    }

    public static PostDto of(String title, String body) {
        return new PostDto(null, title, body, null, null, null, null);
    }

    public static PostDto fromEntity(PostEntity postEntity) {
        return new PostDto(
            postEntity.getId(),
            postEntity.getTitle(),
            postEntity.getBody(),
            UserDto.fromEntity(postEntity.getUser()),
            postEntity.getRegisteredAt(),
            postEntity.getUpdatedAt(),
            postEntity.getDeletedAt()
        );
    }

    public PostEntity toEntity(UserEntity user) {
        return PostEntity.of(
            title,
            body,
            user
        );
    }

}
