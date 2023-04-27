package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private Long postId;
    private Long commentId;
    private String userName;
    private String content;

    public static CommentResponse fromComment(Comment comment) {
        return new CommentResponse(
            comment.getPost().getId(),
            comment.getId(),
            comment.getUser().getUsername(),
            comment.getContent()
        );
    }
}

