package com.couple.sns.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokenResponse {

    private String accessToken;
    private String refreshToken;
}
