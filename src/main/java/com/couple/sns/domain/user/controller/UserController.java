package com.couple.sns.domain.user.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.user.dto.request.TokenReIssueRequest;
import com.couple.sns.domain.user.dto.request.UserJoinRequest;
import com.couple.sns.domain.user.dto.request.UserLoginRequest;
import com.couple.sns.domain.user.dto.response.UserResponse;
import com.couple.sns.domain.user.dto.response.UserTokenResponse;
import com.couple.sns.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        return Response.success(userService.join(userJoinRequest.toDto()));
    }

    @PostMapping("/login")
    public Response<UserTokenResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return Response.success(
            userService.login(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
    }

    @PostMapping("/reissue")
    public Response<UserTokenResponse> reissue(@RequestBody TokenReIssueRequest tokenReIssueRequest) {
        return Response.success(userService.reissue(tokenReIssueRequest.getRefreshToken()));
    }
}