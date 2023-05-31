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
import com.couple.sns.domain.common.fixture.CommentLikeEntityFixture;
import com.couple.sns.domain.common.fixture.PostEntityFixture;
import com.couple.sns.domain.common.fixture.UserEntityFixture;
import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.CommentLikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.PostLikeEntity;
import com.couple.sns.domain.post.persistance.repository.CommentLikeRepository;
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

    @Autowired private CommentService commentService;
    @MockBean private PostRepository postRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private CommentRepository commentRepository;
    @MockBean private CommentLikeRepository commentLikeRepository;

    static String username = "username";
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
        given(userRepository.findByUsername(username)).willReturn(Optional.of(getUserEntity(
            username, password)));
        given(postRepository.findById(postId)).willReturn(Optional.of(getPostEntity(username, password, title, body)));
        given(commentRepository.saveAndFlush(any())).willReturn(CommentEntityFixture.get(username, password, title, body, content));

        commentService.create(username, postId, content);

        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(commentRepository).should().saveAndFlush(any(CommentEntity.class));
    }

    @Test
    public void 댓글작성시_유저가_존재하지않는경우() {
        given(postRepository.findById(postId)).willReturn(Optional.of(getPostEntity(username, password, title, body)));
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.create(username, postId, content));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(postRepository).should().findById(any(Long.class));
        then(userRepository).should().findByUsername(any(String.class));
    }

    @Test
    public void 댓글작성시_포스트가_존재하지않는경우() {
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.create(username, postId, content));

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        then(postRepository).should().findById(any(Long.class));
    }


    @Test
    public void 댓글삭제_성공() {
        CommentEntity comment = getCommentEntity(username, password, title, body, content);
        UserEntity user = comment.getUser();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        commentService.delete(username, commentId);

        then(userRepository).should().findByUsername(any());
        then(commentRepository).should().findById(any());
        then(commentRepository).should().delete(any());
    }

    @Test
    public void 댓글삭제시_댓글을_작성한_유저가아닌경우() {
        CommentEntity comment = getCommentEntity(username, password, title, body, content);
        UserEntity user = getUserEntity("userName2", password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.delete(username, commentId));

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUsername(any());
        then(commentRepository).should().findById(any());
    }

    @Test
    public void 좋아요_버튼이_성공적으로_클릭된경우() {
        CommentEntity commentEntity = getCommentEntity(username, password, title, body, content);
        UserEntity userEntity = getUserEntity(username,password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentEntity));
        given(commentLikeRepository.findByCommentIdAndUserId(commentEntity.getId(), userEntity.getId())).willReturn(Optional.empty());
        given(commentLikeRepository.save(any())).willReturn(any(PostLikeEntity.class));

        commentService.like(commentId, username);

        then(userRepository).should().findByUsername(any(String.class));
        then(commentRepository).should().findById(any(Long.class));
        then(commentLikeRepository).should().findByCommentIdAndUserId(any(), any());
        then(commentLikeRepository).should().save(any(CommentLikeEntity.class));
    }

    @Test
    public void 좋아요_버튼이_이미_클릭되어있는경우() {
        CommentEntity commentEntity = getCommentEntity(username, password, title, body, content);
        UserEntity userEntity = getUserEntity(username, password);

        CommentLikeEntity commentLikeEntity = getLikeEntity(userEntity, commentEntity);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentEntity));
        given(commentLikeRepository.findByCommentIdAndUserId(commentEntity.getId(), userEntity.getId())).willReturn(Optional.of(
            commentLikeEntity));

        commentService.like(commentId, username);

        then(userRepository).should().findByUsername(any(String.class));
        then(commentRepository).should().findById(any(Long.class));
        then(commentLikeRepository).should().delete(any(CommentLikeEntity.class));
    }

    @Test
    public void 댓글에_좋아요수와_좋아요누른_유저_리스트_출력() {
        Pageable pageable = mock(Pageable.class);
        CommentEntity comment = getCommentEntity(username, password, title, body, content);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentLikeRepository.findByCommentId(comment.getId(), pageable)).willReturn(Page.empty());

        commentService.likeList(commentId, pageable);

        then(commentRepository).should().findById(any(Long.class));
        then(commentLikeRepository).should().findByCommentId(any(), any());
    }

    private UserEntity getUserEntity(String username, String password) {
        return UserEntityFixture.get(username, password);
    }

    private PostEntity getPostEntity(String username, String password, String title, String body) {
        return PostEntityFixture.get(username, password, title, body);
    }

    private CommentEntity getCommentEntity(String username, String password, String title, String body, String content) {
        return CommentEntityFixture.get(username, password, title, body, content);
    }

    private CommentLikeEntity getLikeEntity(UserEntity user, CommentEntity comment) {
        return CommentLikeEntityFixture.get(user, comment);
    }


}
