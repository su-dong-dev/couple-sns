package com.couple.sns.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostIdResponse {

    private Long postId;
    private String userId;

}

