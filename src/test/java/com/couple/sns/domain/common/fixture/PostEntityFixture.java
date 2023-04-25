package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(String userId, Long postId, Long id, String title, String body) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUserId(userId);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        result.setTitle(title);
        result.setBody(body);

        return result;
    }
}
