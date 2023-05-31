package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.CommentLikeDto;
import com.couple.sns.domain.user.dto.response.UserLikeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class CommentLikeResponse {

    private Long commentId;
    private List<UserLikeResponse> users;


    public static CommentLikeResponse from(Page<CommentLikeDto> likes) {
        return new CommentLikeResponse(
            likes.getTotalElements(),
            likes.map(postLikeDto -> UserLikeResponse.from(postLikeDto.getUser())).stream().toList()
        );
    }
}

