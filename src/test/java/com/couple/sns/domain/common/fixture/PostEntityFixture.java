package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String username, String password, String content, String location) {
        UserEntity user = UserEntity.of(username, password, UserRole.USER, "nickname", "phone", "profileImage");

        return PostEntity.of(user, content, location);
    }
}
