package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class LikeEntityFixture {
    public static LikeEntity get(Long likeId, UserEntity user, PostEntity post) {
        LikeEntity like = new LikeEntity();
        like.setId(likeId);
        like.setPost(post);
        like.setUser(user);

        return like;
    }
}
