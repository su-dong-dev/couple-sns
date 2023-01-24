package com.couple.sns.domain.user.controller;

import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.dto.request.UserJoinRequest;
import com.couple.sns.domain.user.dto.response.UserJoinResponse;
import com.couple.sns.domain.user.service.UserUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserController {

    private final UserUpdateService userUpdateService;

    // TODO : create response class
    @PostMapping("/join")
    public UserJoinResponse join(@RequestBody UserJoinRequest request) {
        User user = userUpdateService.join(request.getUserId(), request.getPassword());
        return UserJoinResponse.fromUser(user);
    }
}


