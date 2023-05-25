package com.couple.sns.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long userId;
    private String userName;

    public static UserResponse fromUser(Long userId, String userName) {
        return new UserResponse(
            userId,
            userName
        );
    }
}

