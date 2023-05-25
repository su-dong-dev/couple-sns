package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long postId;
    private Long commentId;
    private String userName;
    private String content;

    public static CommentResponse fromComment(CommentDto commentDto) {
        return new CommentResponse(
            commentDto.getPost().getId(),
            commentDto.getId(),
            commentDto.getUser().getUsername(),
            commentDto.getContent()
        );
    }
}

