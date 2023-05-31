package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.PostDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {

    private String username;
    private String content;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static PostResponse fromPost(PostDto postDto) {
        return new PostResponse(
            postDto.getUser().getUsername(),
            postDto.getContent(),
            postDto.getLocation(),
            postDto.getCreatedAt(),
            postDto.getUpdatedAt()
        );
    }
}

