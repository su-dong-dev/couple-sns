package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String username, String password) {
        return UserEntity.of(username, password, UserRole.USER, "nickname", "phone", "profileImage");
    }
}
