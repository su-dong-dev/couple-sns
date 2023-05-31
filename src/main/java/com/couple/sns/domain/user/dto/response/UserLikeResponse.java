package com.couple.sns.domain.user.dto.response;

import com.couple.sns.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLikeResponse {

    private String username;
    private String nickname;
    private String profileImage;

    public static UserLikeResponse from(UserDto user) {
        return new UserLikeResponse(
                user.getUsername(),
                user.getNickname(),
                user.getProfileImage()
        );
    }

}
