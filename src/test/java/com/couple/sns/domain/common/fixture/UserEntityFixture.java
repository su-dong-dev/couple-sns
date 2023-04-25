package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.user.persistance.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(Long userId, String userName, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setUserName(userName);
        entity.setPassword(password);
        return entity;
    }
}
