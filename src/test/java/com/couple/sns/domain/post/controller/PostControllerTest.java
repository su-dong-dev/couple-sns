package com.couple.sns.domain.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.common.fixture.PostEntityFixture;
import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.post.dto.Post;
import com.couple.sns.domain.post.dto.request.PostCreateRequest;
import com.couple.sns.domain.post.dto.request.PostModifyRequest;
import com.couple.sns.domain.post.dto.response.LikeResponse;
import com.couple.sns.domain.post.dto.response.PostIdResponse;
import com.couple.sns.domain.post.dto.response.PostResponse;
import com.couple.sns.domain.post.dto.response.UserLikeResponse;
import com.couple.sns.domain.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
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
public class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @WithMockUser(authorities = "USER")
    public void 전체_피드목록() throws Exception {

        given(postService.list(any(Pageable.class))).willReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    @WithAnonymousUser
    public void 전체_피드목록_요청시_로그인하지않은경우() throws Exception {

        given(postService.list(any())).willThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(get("/api/vi/posts")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 내_피드목록() throws Exception {

        given(postService.my(any(), any(Pageable.class))).willReturn(Page.empty());

        mockMvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 내_피드목록_요청시_로그인하지않은경우() throws Exception {

        given(postService.my(any(), any(Pageable.class))).willThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(get("/api/v1/posts/my")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 포스트_작성_성공() throws Exception {
        String title = "title";
        String body = "body";

        given(postService.create(eq(title), eq(body), any()))
            .willReturn(Post.fromEntity(PostEntityFixture.get("userName", title, body)));

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 가입한적없는_유저가_포스트_작성할_경우() throws Exception {
        String title = "title";
        String body = "body";

        given(postService.create(eq(title), eq(body), any())).willThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 포스트_수정_성공() throws Exception {
        String title = "modify_title";
        String body = "modify_body";

        given(postService.modify(any(),any(), any(), any()))
            .willReturn(PostResponse.fromPost(Post.fromEntity(PostEntityFixture.get("userName", title, body))));

        mockMvc.perform(put("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body)))
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 포스트_삭제_성공() throws Exception {

        given(postService.delete(any(),any()))
            .willReturn(new PostIdResponse(any(), any()));

        mockMvc.perform(delete("/api/v1/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostIdResponse(1L, "userName")))
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 좋아요_기능() throws Exception {

        mockMvc.perform(post("/api/v1/posts/1/likes")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void 좋아요_기능_로그인하지_않은_경우() throws Exception {

        mockMvc.perform(post("/api/v1/posts/1/likes")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 좋아요버튼_클릭시_포스트가_없는_경우() throws Exception {

        willThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).given(postService).like(any(), any());

        mockMvc.perform(post("/api/v1/posts/1/likes")
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void 좋아요누른_유저_목록() throws Exception {

        Long postId = 1L;
        Long pageSize = 1L;
        List<UserLikeResponse> users = new ArrayList<>();

        given(postService.likeList(eq(postId),any(Pageable.class))).willReturn(new LikeResponse(
            LikeType.POST, postId, pageSize, users));

        mockMvc.perform(get("/api/v1/posts/1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new LikeResponse(LikeType.POST, 1L, 1L, users)))
            ).andDo(print())
            .andExpect(status().isOk());
    }

}
