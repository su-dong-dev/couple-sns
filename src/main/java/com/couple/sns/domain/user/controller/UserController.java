package com.couple.sns.domain.user.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.user.dto.User;
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
        User user = userUpdateService.join(
            userJoinRequest.getUserId(),
            userJoinRequest.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserTokenResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return Response.success(
            userUpdateService.login(userLoginRequest.getUserId(), userLoginRequest.getPassword()));
    }

    @PostMapping("/reissue")
    public Response<UserTokenResponse> reissue(@RequestBody TokenReIssueRequest tokenReIssueRequest) {
        return Response.success(userUpdateService.reissue(tokenReIssueRequest.getRefreshToken()));
    }
}