package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.post.dto.Like;
import java.util.List;
import java.util.stream.Collectors;
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
            likes.get().map(Like::getUser).map(UserLikeResponse::fromUser).collect(Collectors.toList())
        );
    }
}

