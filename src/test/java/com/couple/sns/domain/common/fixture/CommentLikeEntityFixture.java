package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.CommentLikeEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class CommentLikeEntityFixture {
    public static CommentLikeEntity get(UserEntity user, CommentEntity comment) {
        return CommentLikeEntity.of(user, comment);
    }
}
