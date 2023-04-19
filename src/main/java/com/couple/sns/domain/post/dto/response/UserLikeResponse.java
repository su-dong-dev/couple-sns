package com.couple.sns.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLikeResponse {

    private Long userId;
    private String userName;

    public static UserLikeResponse fromUser(Long userId, String userName) {
        return new UserLikeResponse(
            userId,
            userName
        );
    }
}

