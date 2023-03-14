package com.couple.sns.domain.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.PostRepository;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PostServiceTest {

    @Autowired private PostService postService;
    @MockBean private PostRepository postRepository;
    @MockBean private UserRepository userRepository;

    @Test
    public void 포스트_작성이_성공한_경우() {
        String userId = "userId";
        String title = "title";
        String body = "body";

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(mock(UserEntity.class)));
        given(postRepository.save(any())).willReturn(mock(PostEntity.class));

        // when
        postService.create(title, body, userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().save(any(PostEntity.class));
    }

    @Test
    public void 포스트_작성시_요청한_userId가_존재하지않는경우() {
        String userId = "userId";
        String title = "title";
        String body = "body";

        given(userRepository.findByUserId(userId)).willReturn(Optional.empty());
        given(postRepository.save(any())).willReturn(mock(PostEntity.class));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(title,body,userId));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
    }
}
