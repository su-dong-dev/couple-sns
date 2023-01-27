package com.couple.sns.domain.user.dto;

import com.couple.sns.domain.user.persistance.UserEntity;
import lombok.*;

@Getter
@AllArgsConstructor
public class User {

    private Long id;
    private String userId;
    private String password;

    public static User fromEntity(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUserId(),
                userEntity.getPassword()
        );
    }
}
