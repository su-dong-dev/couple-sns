package com.couple.sns.domain.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.common.fixture.CommentEntityFixture;
import com.couple.sns.domain.common.fixture.LikeEntityFixture;
import com.couple.sns.domain.common.fixture.PostEntityFixture;
import com.couple.sns.domain.common.fixture.UserEntityFixture;
import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.CommentRepository;
import com.couple.sns.domain.post.persistance.repository.LikeRepository;
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
    @MockBean
    private LikeRepository likeRepository;

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
        given(commentRepository.saveAndFlush(any())).willReturn(CommentEntityFixture.get(userName, title, body, content));

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
        CommentEntity comment = getCommentEntity(userName, title, body, content);
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
        CommentEntity comment = getCommentEntity(userName, title, body, content);
        UserEntity user = getUserEntity("userName2", password);

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> commentService.delete(userName, commentId));

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUserName(any());
        then(commentRepository).should().findById(any());
    }

    @Test
    public void 좋아요_버튼이_성공적으로_클릭된경우() {
        CommentEntity commentEntity = getCommentEntity(userName, title, body, content);
        UserEntity userEntity = getUserEntity(userName,password);

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(userEntity));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentEntity));
        given(likeRepository.findByTypeAndTypeIdAndUser_Id(LikeType.COMMENT, commentEntity.getId(), userEntity.getId())).willReturn(Optional.empty());
        given(likeRepository.save(any())).willReturn(any(LikeEntity.class));

        // when
        commentService.like(commentId, userName);

        // then
        then(userRepository).should().findByUserName(any(String.class));
        then(commentRepository).should().findById(any(Long.class));
        then(likeRepository).should().findByTypeAndTypeIdAndUserId(any(), any(), any());
        then(likeRepository).should().save(any(LikeEntity.class));
    }

    @Test
    public void 좋아요_버튼이_이미_클릭되어있는경우() {
        // given
        CommentEntity commentEntity = getCommentEntity(userName, title, body, content);
        UserEntity userEntity = getUserEntity(userName, password);

        LikeEntity likeEntity = getLikeEntity(userEntity, commentEntity.getId(), LikeType.COMMENT);

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(userEntity));
        given(commentRepository.findById(commentId)).willReturn(Optional.of(commentEntity));
        given(likeRepository.findByTypeAndTypeIdAndUser_Id(LikeType.COMMENT, commentEntity.getId(), userEntity.getId())).willReturn(Optional.of(likeEntity));

        // when
        commentService.like(commentId, userName);

        // then
        then(userRepository).should().findByUserName(any(String.class));
        then(commentRepository).should().findById(any(Long.class));
        then(likeRepository).should().delete(any(LikeEntity.class));
    }

    @Test
    public void 댓글에_좋아요수와_좋아요누른_유저_리스트_출력() {
        // given
        Pageable pageable = mock(Pageable.class);
        CommentEntity comment = getCommentEntity(userName, title, body, content);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(likeRepository.findAllByTypeAndTypeId(LikeType.COMMENT, comment.getId(), pageable)).willReturn(Page.empty());

        // when
        commentService.likeList(commentId, pageable);

        // then
        then(commentRepository).should().findById(any(Long.class));
        then(likeRepository).should().findAllByTypeAndTypeId(any(), any(), any());
    }

    private UserEntity getUserEntity(String userName, String password) {
        return UserEntityFixture.get(userName, password);
    }

    private PostEntity getPostEntity(String userName, String title, String body) {
        return PostEntityFixture.get(userName, title, body);
    }

    private CommentEntity getCommentEntity(String userName, String title, String body, String content) {
        return CommentEntityFixture.get(userName, title, body, content);
    }

    private LikeEntity getLikeEntity(UserEntity user, Long typeId, LikeType type) {
        return LikeEntityFixture.get(user, typeId, type);
    }


}
