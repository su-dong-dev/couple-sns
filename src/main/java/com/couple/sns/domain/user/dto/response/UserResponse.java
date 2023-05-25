package com.couple.sns.domain.user.dto.response;

import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String userName;
    private UserRole role;

    public static UserResponse from(UserDto user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getUserRole()
        );
    }

}
