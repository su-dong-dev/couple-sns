package com.couple.sns.domain.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.dto.response.CommentResponse;
import com.couple.sns.domain.post.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    Long postId = 1L;
    Long commentId = 1L;
    String userName = "userName";
    String content = "댓글";

    @Test
    @WithMockUser(authorities = "USER")
    public void 댓글목록() throws Exception {

        given(commentService.list(eq(postId), any(Pageable.class))).willReturn(Page.empty());

        mockMvc.perform(get("/api/v1/" + postId + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 댓글작성_성공() throws Exception {

        given(commentService.create(eq(userName), eq(postId), eq(content))).willReturn(
            new CommentResponse(postId, commentId, userName, content));

        mockMvc.perform(post("/api/v1/" + postId + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(content))
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 가입한적없는_유저가_댓글을_작성할_경우() throws Exception {

        given(commentService.create(eq(userName), eq(postId), eq(content))).willThrow(
            new SnsApplicationException(
                ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/" + postId + "/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(content))
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 댓글삭제_성공() throws Exception {

        mockMvc.perform(delete("/api/v1/comment/" + commentId)
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

}
