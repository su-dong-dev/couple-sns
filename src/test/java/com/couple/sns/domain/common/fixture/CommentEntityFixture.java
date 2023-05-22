package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.UserEntity;

public class CommentEntityFixture {
    public static CommentEntity get(String userName, String title, String body, String content) {
        UserEntity user =  UserEntity.of(userName, "password", UserRole.USER);
        PostEntity post = PostEntity.of(title, body, user);

        return CommentEntity.of(user, post, content);
    }
}
