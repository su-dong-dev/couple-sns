package com.couple.sns.domain.user.dto.request;

import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequest {

    private String username;
    private String password;
    private UserRole role;

    private String nickname;
    private String phone;
    private String profileImage;

    public UserDto toDto() {
        return UserDto.of(
            username,
            password,
            role,
            nickname,
            phone,
            profileImage
        );
    }

}
