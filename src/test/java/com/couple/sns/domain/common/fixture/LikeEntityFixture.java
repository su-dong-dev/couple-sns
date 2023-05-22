package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class LikeEntityFixture {
    public static LikeEntity get(UserEntity user, Long typeId, LikeType type) {
        return LikeEntity.of(user, typeId, type);
    }
}
