package com.couple.sns.domain.user.controller;

import com.couple.sns.common.responce.Response;
import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.dto.request.UserJoinRequest;
import com.couple.sns.domain.user.dto.request.UserLoginRequest;
import com.couple.sns.domain.user.dto.response.UserJoinResponse;
import com.couple.sns.domain.user.dto.response.UserLoginResponse;
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
        User user = userUpdateService.join(userJoinRequest.getUserId(), userJoinRequest.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userUpdateService.login(userLoginRequest.getUserId(), userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}