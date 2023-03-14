package com.couple.sns.domain.post.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.couple.sns.domain.post.dto.request.PostCreateRequest;
import com.couple.sns.domain.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
    @WithMockUser()
    public void 포스트_작성_성공() throws Exception {
        String title = "title";
        String body = "body";

        willDoNothing().given(postService).create(title,body,"userId");

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body)))
            ).andDo(print())
            .andExpect(status().isOk());
    }
}
