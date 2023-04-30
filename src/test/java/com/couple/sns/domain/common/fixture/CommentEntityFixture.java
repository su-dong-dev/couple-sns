package com.couple.sns.domain.common.fixture;

import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.persistance.UserEntity;

public class CommentEntityFixture {
    public static CommentEntity get(String userName, Long userId, Long postId, Long commentId, String content) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity post = new PostEntity();
        post.setUser(user);
        post.setId(postId);
        post.setTitle("title");
        post.setBody("body");

        CommentEntity result = new CommentEntity();
        result.setId(commentId);
        result.setPost(post);
        result.setUser(user);
        result.setContent(content);

        return result;
    }
}
