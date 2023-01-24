package com.couple.sns.domain.user.dto;

import com.couple.sns.domain.user.persistance.UserEntity;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class User {

    private Long id;
    private String userId;
    private String password;

    private User(Long id, String userId, String password) {
        this.id = id;
        this.userId = userId;
        this.password = password;
    }

    public static User fromEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUserId(),
                userEntity.getPassword()
        );
    }
}
