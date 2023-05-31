package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.PostLikeDto;
import com.couple.sns.domain.user.dto.response.UserLikeResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class PostLikeResponse {

    private Long postId;
    private List<UserLikeResponse> users;


    public static PostLikeResponse from(Page<PostLikeDto> likes) {
        return new PostLikeResponse(
            likes.getTotalElements(),
            likes.map(postLikeDto -> UserLikeResponse.from(postLikeDto.getUser())).stream().toList()
        );
    }
}

