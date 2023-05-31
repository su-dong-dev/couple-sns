package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.PostLikeEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class PostLikeEntityFixture {
    public static PostLikeEntity get(UserEntity user, PostEntity post) {
        return PostLikeEntity.of(user, post);
    }
}
