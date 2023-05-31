package com.couple.sns.domain.post.dto.request;

import com.couple.sns.domain.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequest {

    private String content;
    private String location;

    public PostDto toDto() {
        return PostDto.of(
            content,
            location
        );
    }

}
