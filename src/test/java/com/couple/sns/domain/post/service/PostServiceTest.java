package com.couple.sns.domain.post.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.common.fixture.PostEntityFixture;
import com.couple.sns.domain.common.fixture.PostLikeEntityFixture;
import com.couple.sns.domain.common.fixture.UserEntityFixture;
import com.couple.sns.domain.post.dto.PostDto;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.PostLikeEntity;
import com.couple.sns.domain.post.persistance.repository.PostLikeRepository;
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
    @MockBean private PostLikeRepository postLikeRepository;


    static String username = "username";
    static String password = "password";
    static Long postId = 1L;
    static String title = "title";
    static String body = "body";

    @Test
    public void 전체_포스트_목록_요청이_성공한_경우() {
        Pageable pageable = mock(Pageable.class);

        given(postRepository.findAllJoinFetch(pageable)).willReturn(Page.empty());

        postService.getPosts(pageable);

        then(postRepository).should().findAllJoinFetch(any(Pageable.class));
    }

    @Test
    public void 내_포스트_목록_요청이_성공한_경우() {
        Pageable pageable = mock(Pageable.class);
        UserEntity userEntity = getUserEntity(username, password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findAllByUser(userEntity, pageable)).willReturn(Page.empty());

        postService.getMyPosts(username, pageable);

        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findAllByUser(any(UserEntity.class), any(Pageable.class));
    }

    @Test
    public void 포스트_작성이_성공한_경우() {
        given(userRepository.findByUsername(username)).willReturn(Optional.of(getUserEntity(
            username, password)));
        given(postRepository.save(any())).willReturn(getPostEntity(username, password, title, body));

        // when
        postService.create(username, PostDto.of(title, body));

        // then
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().save(any());
    }

    @Test
    public void 포스트_작성시_요청한_userName가_존재하지않는경우() {
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> postService.create(
            username, PostDto.of(title, body)));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUsername(any(String.class));
    }

    @Test
    public void 포스트_수정시_포스트가_존재하지않은_경우() {
        // given
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.modify(postId, username, any()));

        // then
        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_수정이_성공한_경우() {
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        // when
        postService.modify(postId, username, PostDto.of(title, body));

        // then
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_수정시_권한이_없는경우() {
        // given
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = getUserEntity("userName2", password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.modify(postId, username, PostDto.of(title, body)));

        // then
        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_삭제가_성공한_경우() {
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        // when
        postService.delete(postId, username);

        // then
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_삭제시_포스트가_존재하지않은_경우() {
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = postEntity.getUser();

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.delete(postId, username));

        assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 포스트_삭제시_권한이_없는경우() {
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = getUserEntity("userName2", password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));

        SnsApplicationException e = assertThrows(SnsApplicationException.class,
            () -> postService.delete(postId, username));

        assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
    }

    @Test
    public void 좋아요_버튼이_성공적으로_클릭된경우() {
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = getUserEntity(username,password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
        given(postLikeRepository.findByPostIdAndUserId(postEntity.getId(), userEntity.getId())).willReturn(Optional.empty());
        given(postLikeRepository.save(any())).willReturn(any(PostLikeEntity.class));

        postService.like(postId, username);

        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(postLikeRepository).should().findByPostIdAndUserId(any(), any());
        then(postLikeRepository).should().save(any(PostLikeEntity.class));
    }

    @Test
    public void 좋아요_버튼이_이미_클릭되어있는경우() {
        PostEntity postEntity = getPostEntity(username, password, title, body);
        UserEntity userEntity = getUserEntity(username, password);

        PostLikeEntity postLikeEntity = getLikeEntity(userEntity, postEntity);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(userEntity));
        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
        given(postLikeRepository.findByPostIdAndUserId(postEntity.getId(), userEntity.getId())).willReturn(Optional.of(
            postLikeEntity));

        postService.like(postId, username);

        then(userRepository).should().findByUsername(any(String.class));
        then(postRepository).should().findById(any(Long.class));
        then(postLikeRepository).should().delete(any(PostLikeEntity.class));
    }

    @Test
    public void 포스트에_좋아요수와_좋아요누른_유저_리스트_출력() {
        Pageable pageable = mock(Pageable.class);
        PostEntity postEntity = getPostEntity(username, password, title, body);

        given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
        given(postLikeRepository.findByPostId(postEntity.getId(), pageable)).willReturn(Page.empty());

        postService.likeList(postId, pageable);

        then(postRepository).should().findById(any(Long.class));
        then(postLikeRepository).should().findByPostId(any(), any());
    }


    private UserEntity getUserEntity(String username, String password) {
        return UserEntityFixture.get(username, password);
    }

    private PostEntity getPostEntity(String username, String password, String title, String body) {
        return PostEntityFixture.get(username, password, title, body);
    }

    private PostLikeEntity getLikeEntity(UserEntity user, PostEntity post) {
        return PostLikeEntityFixture.get(user, post);
    }

}
