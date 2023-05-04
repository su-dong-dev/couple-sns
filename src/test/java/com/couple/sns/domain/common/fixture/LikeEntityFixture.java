package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class LikeEntityFixture {
    public static LikeEntity get(Long likeId, UserEntity user, Long typeId, LikeType type) {
        LikeEntity like = new LikeEntity();
        like.setId(likeId);
        like.setUserId(user.getId());
        like.setUserName(user.getUserName());
        like.setTypeId(typeId);
        like.setType(type);

        return like;
    }
}
