package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.PostDto;
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


    public static PostResponse fromPost(PostDto postDto) {
        return new PostResponse(
            postDto.getId(),
            postDto.getUser().getUsername(),
            postDto.getTitle(),
            postDto.getBody(),
            postDto.getRegisteredAt(),
            postDto.getUpdatedAt()
        );
    }
}

