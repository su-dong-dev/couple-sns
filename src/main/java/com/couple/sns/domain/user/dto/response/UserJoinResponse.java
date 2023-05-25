package com.couple.sns.domain.user.dto.response;

import com.couple.sns.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String userName;

    public static UserJoinResponse fromUser(UserDto user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
