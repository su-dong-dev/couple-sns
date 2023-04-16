package com.couple.sns.domain.post.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.dto.Like;
import com.couple.sns.domain.post.dto.Post;
import com.couple.sns.domain.post.dto.response.LikeResponse;
import com.couple.sns.domain.post.dto.response.PostIdResponse;
import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.LikeRepository;
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
    private final LikeRepository likeRepository;

    public Page<Post> list(Pageable pageable) {
       return postRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userId, Pageable pageable) {
        UserEntity user = getUserOrElseThrow(userId);
        return postRepository.findAllByUser(user, pageable).map(Post::fromEntity);
    }

    @Transactional
    public Post create(String title, String body, String userId) {
        UserEntity user = getUserOrElseThrow(userId);
        PostEntity post = postRepository.save(PostEntity.toEntity(title, body, user));

        return Post.fromEntity(postRepository.saveAndFlush(post));
    }

    @Transactional
    public Post modify(Long postId, String title, String body, String userId) {
        UserEntity user = getUserOrElseThrow(userId);
        PostEntity post = getPostOrElseThrow(postId);

        if (post.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userId, postId));
        }

        post.setTitle(title);
        post.setBody(body);

        return Post.fromEntity(postRepository.saveAndFlush(post));
    }

    @Transactional
    public PostIdResponse delete(Long postId, String userId) {
        UserEntity user = getUserOrElseThrow(userId);
        PostEntity post = getPostOrElseThrow(postId);

        if (post.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userId, postId));
        }

        postRepository.delete(post);

        return new PostIdResponse(postId, userId);
    }

    @Transactional
    public void like(Long postId, String userId) {
        PostEntity post = getPostOrElseThrow(postId);
        UserEntity user = getUserOrElseThrow(userId);

        if(likeRepository.findByPostAndUser(post,user).isPresent()){
            likeRepository.delete(likeRepository.findByPostAndUser(post, user).get());
        }else {
            likeRepository.save(LikeEntity.toEntity(user, post));
        }
    }

    public LikeResponse likeList(Long postId, Pageable pageable) {
        PostEntity post = getPostOrElseThrow(postId);
        return LikeResponse.from(post.getId(), likeRepository.findAllByPost(post, pageable).map(Like::fromEntity));
    }

    private UserEntity getUserOrElseThrow(String userId) {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("%s not founded", userId)));
    }

    private PostEntity getPostOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("%s not founded", postId)));
    }
}
