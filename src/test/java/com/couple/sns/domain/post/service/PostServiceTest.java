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
import com.couple.sns.domain.common.fixture.LikeEntityFixture;
import com.couple.sns.domain.common.fixture.PostEntityFixture;
import com.couple.sns.domain.common.fixture.UserEntityFixture;
import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
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
public class PostServiceTest {

    @Autowired private PostService postService;
    @MockBean private PostRepository postRepository;
    @MockBean private UserRepository userRepository;
    @MockBean private LikeRepository likeRepository;


    static String userId = "userId";
    static String password = "password";
    static Long postId = 1L;
    static String title = "title";
    static String body = "body";
    static Long likeId = 1L;

    @Test
    public void 전체_포스트_목록_요청이_성공한_경우() {
        Pageable pageable = mock(Pageable.class);

        given(postRepository.findAll(pageable)).willReturn(Page.empty());

        postService.list(pageable);

        then(postRepository).should().findAll(any(Pageable.class));
    }

    @Test
    public void 내_포스트_목록_요청이_성공한_경우() {
        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity = getUserEntity(userId, password);

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findAllByUser(userEntity, pageable)).willReturn(Page.empty());

        postService.my(userId, pageable);

        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findAllByUser(any(UserEntity.class), any(Pageable.class));
    }

    @Test
    public void 포스트_작성이_성공한_경우() {
        given(userRepository.findByUserId(userId)).willReturn(Optional.of(
            getUserEntity(userId, password)));
        given(postRepository.save(any())).willReturn(getPostEntity(userId, title, body));
        given(postRepository.saveAndFlush(any())).willReturn(getPostEntity(userId, title, body));

        // when
        postService.create(title, body, userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().save(any(PostEntity.class));
        then(postRepository).should().saveAndFlush(any(PostEntity.class));
    }

    @Test
    public void 포스트_작성시_요청한_userId가_존재하지않는경우() {
        given(userRepository.findByUserId(userId)).willReturn(Optional.empty());

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userId));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
    }

    @Test
    public void 포스트_수정이_성공한_경우() {
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
        given(postRepository.saveAndFlush(any())).willReturn(postEntity);

        // when
        postService.modify(postId, title, body, userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(postRepository).should().saveAndFlush(any(PostEntity.class));
    }

    @Test
    public void 포스트_수정시_포스트가_존재하지않은_경우() {
        // given
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.modify(postId, title, body, userId));

        // then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_수정시_권한이_없는경우() {
        // given
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = getUserEntity("userId2", password);

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.modify(postId, title, body, userId));

        // then
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_삭제가_성공한_경우() {
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        // when
        postService.delete(postId, userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_삭제시_포스트가_존재하지않은_경우() {
        // given
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.delete(postId, userId));

        // then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_삭제시_권한이_없는경우() {
        // given
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = getUserEntity("userId2", password);

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.delete(postId, userId));

        // then
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 좋아요_버튼이_성공적으로_클릭된경우() {
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = getUserEntity(userId,password);

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
        given(likeRepository.findByPostAndUser(postEntity, userEntity)).willReturn(Optional.empty());
        given(likeRepository.save(any())).willReturn(any(LikeEntity.class));

        // when
        postService.like(postId, userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(likeRepository).should().findByPostAndUser(any(PostEntity.class), any(UserEntity.class));
        then(likeRepository).should().save(any(LikeEntity.class));
    }

    @Test
    public void 좋아요_버튼이_이미_클릭되어있는경우() {
        // given
        PostEntity postEntity = getPostEntity(userId, title, body);
        UserEntity userEntity = getUserEntity(userId, password);

        LikeEntity likeEntity = getLikeEntity(likeId, userEntity, postEntity);

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
        given(likeRepository.findByPostAndUser(postEntity, userEntity)).willReturn(Optional.of(likeEntity));

        // when
        postService.like(postId, userId);

        // then
        then(userRepository).should().findByUserId(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(likeRepository).should().findByPostAndUser(any(PostEntity.class), any(UserEntity.class));
        then(likeRepository).should().delete(any(LikeEntity.class));
    }

    @Test
    public void 포스트에_좋아요수와_좋아요누른_유저_리스트_출력() {
        // given
        Pageable pageable = mock(Pageable.class);
        PostEntity postEntity = getPostEntity(userId, title, body);

        given(postRepository.findById(postEntity.getId())).willReturn(Optional.of(postEntity));
        given(likeRepository.findAllByPost(eq(postEntity), any(Pageable.class))).willReturn(Page.empty());

        // when
        postService.likeList(postId, pageable);

        // then
        then(postRepository).should().findById(any(Long.class));
        then(likeRepository).should().findAllByPost(any(PostEntity.class), any(Pageable.class));
    }


    private UserEntity getUserEntity(String userId, String password) {
        return UserEntityFixture.get(1L, userId, password);
    }

    private PostEntity getPostEntity(String userId, String title, String body) {
        return PostEntityFixture.get(userId, postId, 1L, title, body);
    }

    private LikeEntity getLikeEntity(Long likeId, UserEntity user, PostEntity post) {
        return LikeEntityFixture.get(likeId, user, post);
    }

}
