package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private String username;
    private String content;

    public static CommentResponse fromComment(CommentDto commentDto) {
        return new CommentResponse(
            commentDto.getUser().getUsername(),
            commentDto.getContent()
        );
    }
}

