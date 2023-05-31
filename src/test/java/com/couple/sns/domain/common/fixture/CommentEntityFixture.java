package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.UserEntity;

public class CommentEntityFixture {
    public static CommentEntity get(String username, String password, String postContent, String location, String commentContent) {
        UserEntity user =  UserEntity.of(username, password, UserRole.USER, "nickname", "phone", "profileImage");
        PostEntity post = PostEntity.of(user, postContent, location);

        return CommentEntity.of(user, post, commentContent);
    }
}
