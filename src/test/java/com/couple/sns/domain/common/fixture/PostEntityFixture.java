package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String userName, String password, String title, String body) {
        UserEntity user = UserEntity.of(userName, password, UserRole.USER, "nickname", "phone", "profileImage");

        return PostEntity.of(title, body, user);
    }
}
