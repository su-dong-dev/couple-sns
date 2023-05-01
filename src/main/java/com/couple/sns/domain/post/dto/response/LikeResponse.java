package com.couple.sns.domain.post.dto.response;

import static com.couple.sns.domain.post.dto.response.UserLikeResponse.fromUser;

import com.couple.sns.domain.post.dto.Like;
import com.couple.sns.domain.post.dto.LikeType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private LikeType type;
    private Long typeId;
    private Long count;
    private List<UserLikeResponse> users;


    public static LikeResponse from(LikeType type, Long typeId, Page<Like> likes) {
        return new LikeResponse(
            type,
            typeId,
            likes.getTotalElements(),
            likes.map(like -> fromUser(like.getUserId(), like.getUserName())).stream().toList()
        );
    }
}

