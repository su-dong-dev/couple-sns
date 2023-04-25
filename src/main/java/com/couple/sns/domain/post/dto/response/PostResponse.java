package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private String userName;
    private String title;
    private String body;
    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;


    public static PostResponse fromPost(Post post) {
        return new PostResponse(
            post.getId(),
            post.getUser().getUsername(),
            post.getTitle(),
            post.getBody(),
            post.getRegisteredAt(),
            post.getUpdatedAt(),
            post.getDeletedAt()
        );
    }
}

