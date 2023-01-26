package com.couple.sns.domain.user.fixture;

import com.couple.sns.domain.user.persistance.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(String userId, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUserId(userId);
        entity.setPassword(password);
        return entity;
    }
}
