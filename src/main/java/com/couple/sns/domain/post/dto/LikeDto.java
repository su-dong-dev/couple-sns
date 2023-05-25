package com.couple.sns.domain.post.dto;

import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeDto {

    private Long id;
    private UserDto user;
    private Long typeId;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static LikeDto fromEntity(LikeEntity likeEntity) {
        return new LikeDto(
            likeEntity.getId(),
            UserDto.fromEntity(likeEntity.getUser()),
            likeEntity.getTypeId(),
            likeEntity.getRegisteredAt(),
            likeEntity.getUpdatedAt(),
            likeEntity.getDeletedAt()
        );
    }
}
