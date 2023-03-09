package com.couple.sns.domain.user.controller;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.dto.request.TokenReIssueRequest;
import com.couple.sns.domain.user.dto.request.UserJoinRequest;
import com.couple.sns.domain.user.dto.response.UserTokenResponse;
import com.couple.sns.domain.user.service.UserUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private UserUpdateService userUpdateService;

    @Test
    public void 회원가입() throws Exception {
        String userId = "userId";
        String password = "password";

        given(userUpdateService.join(userId,password)).willReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, password)))
                ).andDo(print())
                .andExpect(status().isOk());
        then(userUpdateService).should().join(any(String.class),any(String.class));
    }

    @Test
    public void 회원가입시_이미_회원가입된_userId로_회원가입을_하는경우() throws Exception {
        String userId = "userId";
        String password = "password";

        when(userUpdateService.join(userId, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_ID));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        String userId ="userId";
        String password = "password";

        given(userUpdateService.login(userId, password))
                .willReturn(new UserTokenResponse("access", "refresh"));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 로그인시_회원가입이_안된_userId일_경우() throws Exception {
        String userId ="userId";
        String password = "password";

        given(userUpdateService.login(userId, password)).willThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void 로그인시_password가_틀릴_경우() throws Exception {
        String userId ="userId";
        String password = "password";

        given(userUpdateService.login(userId, password)).willThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(userId, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void 토큰만료시_refreshToken으로_새로운_Token_발급() throws Exception {
        given(userUpdateService.reissue("refreshToken")).willReturn(
            new UserTokenResponse("accessToken", "refreshToken"));

        mockMvc.perform(post("/api/v1/users/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new TokenReIssueRequest("refreshToken")))
            ).andDo(print())
            .andExpect(status().isOk());
    }

}