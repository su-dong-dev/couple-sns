package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private String userId;
    private String title;
    private String body;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
            post.getId(),
            post.getUser().getUserId(),
            post.getTitle(),
            post.getBody()
        );
    }
}

