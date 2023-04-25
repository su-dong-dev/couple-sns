package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.user.persistance.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(Long id, String userId, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setUserId(userId);
        entity.setPassword(password);
        return entity;
    }
}
