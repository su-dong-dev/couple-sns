package com.couple.sns.domain.user.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.request.TokenReIssueRequest;
import com.couple.sns.domain.user.dto.request.UserJoinRequest;
import com.couple.sns.domain.user.dto.request.UserLoginRequest;
import com.couple.sns.domain.user.dto.response.UserJoinResponse;
import com.couple.sns.domain.user.dto.response.UserTokenResponse;
import com.couple.sns.domain.user.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUpdateService userUpdateService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto user = userUpdateService.join(
            userJoinRequest.getUserName(),
            userJoinRequest.getPassword(),
            userJoinRequest.getRole());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserTokenResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return Response.success(
            userUpdateService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword()));
    }

    @PostMapping("/reissue")
    public Response<UserTokenResponse> reissue(@RequestBody TokenReIssueRequest tokenReIssueRequest) {
        return Response.success(userUpdateService.reissue(tokenReIssueRequest.getRefreshToken()));
    }
}