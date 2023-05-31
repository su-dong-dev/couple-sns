package com.couple.sns.domain.post.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.dto.PostLikeDto;
import com.couple.sns.domain.post.dto.PostDto;
import com.couple.sns.domain.post.dto.response.PostLikeResponse;
import com.couple.sns.domain.post.dto.response.PostResponse;
import com.couple.sns.domain.post.persistance.PostLikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.PostLikeRepository;
import com.couple.sns.domain.post.persistance.repository.PostRepository;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    public Page<PostDto> getPosts(Pageable pageable) {
       return postRepository.findAllJoinFetch(pageable).map(PostDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> getMyPosts(String username, Pageable pageable) {
        UserEntity user = getUserOrElseThrow(username);
        return postRepository.findAllByUser(user, pageable).map(PostDto::fromEntity);
    }

    @Transactional
    public PostResponse create(String username, PostDto dto) {
        UserEntity user = getUserOrElseThrow(username);
        PostEntity post = postRepository.save(dto.toEntity(user));

        return PostResponse.fromPost(PostDto.fromEntity(post));
    }

    @Transactional
    public PostResponse modify(Long postId, String username, PostDto dto) {
        UserEntity user = getUserOrElseThrow(username);
        PostEntity post = getPostOrElseThrow(postId);

        if (post.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }

        post.setContent(dto.getContent());
        post.setLocation(dto.getLocation());

        return PostResponse.fromPost(PostDto.fromEntity(post));
    }

    @Transactional
    public void delete(Long postId, String username) {
        UserEntity user = getUserOrElseThrow(username);
        PostEntity post = getPostOrElseThrow(postId);

        if (post.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, postId));
        }

        postRepository.delete(post);
    }

    @Transactional
    public Boolean like(Long postId, String username) {

        PostEntity post = getPostOrElseThrow(postId);
        UserEntity user = getUserOrElseThrow(username);

        if (postLikeRepository.findByPostIdAndUserId(post.getId(), user.getId()).isEmpty()) {
            postLikeRepository.save(PostLikeEntity.of(user, post));
            return true;
        } else {
            postLikeRepository.delete(
                postLikeRepository.findByPostIdAndUserId(post.getId(), user.getId()).get());
            return false;
        }
    }

    public PostLikeResponse likeList(Long postId, Pageable pageable) {
        PostEntity post = getPostOrElseThrow(postId);

        return PostLikeResponse.from(postLikeRepository.findByPostId(post.getId(), pageable).map(
            PostLikeDto::fromEntity));
    }

    private UserEntity getUserOrElseThrow(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("%s not founded", username)));
    }

    private PostEntity getPostOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("%s not founded", postId)));
    }
}
