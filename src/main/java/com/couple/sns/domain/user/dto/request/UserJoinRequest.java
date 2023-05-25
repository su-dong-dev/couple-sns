package com.couple.sns.domain.user.dto.request;

import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinRequest {

    private String userName;
    private String password;
    private UserRole role;

    public UserDto toDto() {
        return UserDto.of(
            userName,
            password,
            role
        );
    }

}
