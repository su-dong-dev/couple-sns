package com.couple.sns.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenResponse {

    private String accessToken;
    private String refreshToken;

    public static UserTokenResponse of(String accessToken, String refreshToken) {
        return new UserTokenResponse(
            accessToken,
            refreshToken
        );
    }
}
