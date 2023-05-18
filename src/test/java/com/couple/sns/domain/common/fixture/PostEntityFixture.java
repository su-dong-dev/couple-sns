package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String userName, Long postId, Long userId, String title, String body) {
        UserEntity user = UserEntity.of(userName, "password", UserRole.USER);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        result.setTitle(title);
        result.setBody(body);

        return result;
    }
}
