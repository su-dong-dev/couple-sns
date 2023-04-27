package com.couple.sns.domain.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.common.fixture.CommentEntityFixture;
import com.couple.sns.domain.common.fixture.PostEntityFixture;
import com.couple.sns.domain.common.fixture.UserEntityFixture;
import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.CommentRepository;
import com.couple.sns.domain.post.persistance.repository.PostRepository;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockBean
    private PostRepository postRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private CommentRepository commentRepository;

    static Long userId = 1L;
    static String userName = "userName";
    static String password = "password";
    static Long postId = 1L;
    static String title = "title";
    static String body = "body";
    static Long commentId = 1L;
    static String content = "content";
    Pageable pageable = mock(Pageable.class);

    @Test
    public void 포스트_댓글목록_요청이_성공한경우() {
        given(commentRepository.findByPostId(postId, pageable)).willReturn(Page.empty());

        commentService.list(postId, pageable);

        then(commentRepository).should().findByPostId(any(), any());
    }

    @Test
    public void 댓글작성_성공() {
        given(userRepository.findByUserName(userName)).willReturn(Optional.of(getUserEntity(userName, password)));
        given(postRepository.findById(postId)).willReturn(Optional.of(getPostEntity(userName, title, body)));
        given(commentRepository.saveAndFlush(any())).willReturn(CommentEntityFixture.get(userName, userId, postId, commentId, content));

        commentService.create(userName, postId, content);

        then(userRepository).should().findByUserName(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(commentRepository).should().saveAndFlush(any(CommentEntity.class));
    }

    @Test
    public void 댓글작성시_유저가_존재하지않는경우() {
        given(postRepository.findById(postId)).willReturn(Optional.of(getPostEntity(userName, title, body)));
        given(userRepository.findByUserName(userName)).willReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.create(userName, postId, content));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(postRepository).should().findById(any(Long.class));
        then(userRepository).should().findByUserName(any(String.class));
    }

    @Test
    public void 댓글작성시_포스트가_존재하지않는경우() {
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.create(userName, postId, content));

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        then(postRepository).should().findById(any(Long.class));
    }


    @Test
    public void 댓글삭제_성공() {
        CommentEntity comment = getCommentEntity(userName, userId, postId,commentId, content);
        UserEntity user = comment.getUser();

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        commentService.delete(userName, commentId);

        then(userRepository).should().findByUserName(any());
        then(commentRepository).should().findById(any());
        then(commentRepository).should().delete(any());
    }

    @Test
    public void 댓글삭제시_댓글을_작성한_유저가아닌경우() {
        CommentEntity comment = getCommentEntity(userName, userId, postId,commentId, content);
        UserEntity user = getUserEntity("userName2", password);

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.delete(userName, commentId));

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUserName(any());
        then(commentRepository).should().findById(any());
    }

    private UserEntity getUserEntity(String userName, String password) {
        return UserEntityFixture.get(1L, userName, password);
    }

    private PostEntity getPostEntity(String userName, String title, String body) {
        return PostEntityFixture.get(userName, postId, 1L, title, body);
    }

    private CommentEntity getCommentEntity(String userName, Long userId, Long postId, Long commentId, String content) {
        return CommentEntityFixture.get(userName, userId, postId,commentId, content);
    }


}