package com.couple.sns.domain.user.dto.response;

import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private String username;
    private UserRole role;
    private String nickname;
    private String phone;
    private String profileImage;

    public static UserResponse from(UserDto user) {
        return new UserResponse(
                user.getUsername(),
                user.getRole(),
                user.getNickname(),
                user.getPhone(),
                user.getProfileImage()
        );
    }

}
