package com.couple.sns.domain.user.dto.response;

import com.couple.sns.domain.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String userId;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUserId()
        );
    }

}
