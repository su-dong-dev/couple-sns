package com.couple.sns.domain.post.dto.response;

import com.couple.sns.domain.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLikeResponse {

    private Long id;
    private String userId;

    public static UserLikeResponse fromUser(User user) {
        return new UserLikeResponse(
            user.getId(),
            user.getUserId()
        );
    }
}

