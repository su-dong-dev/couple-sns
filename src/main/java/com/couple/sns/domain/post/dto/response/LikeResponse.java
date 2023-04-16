package com.couple.sns.domain.post.dto.response;

import static com.couple.sns.domain.post.dto.response.UserLikeResponse.fromUser;

import com.couple.sns.domain.post.dto.Like;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private Long postId;
    private Long count;
    private List<UserLikeResponse> users;


    public static LikeResponse from(Long postId, Page<Like> likes) {
        return new LikeResponse(
            postId,
            likes.getTotalElements(),
            likes.map(like -> fromUser(like.getUserId(), like.getUserName())).stream().toList()
        );
    }
}

