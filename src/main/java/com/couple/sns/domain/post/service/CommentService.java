package com.couple.sns.domain.post.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.dto.CommentDto;
import com.couple.sns.domain.post.dto.CommentLikeDto;
import com.couple.sns.domain.post.dto.response.CommentLikeResponse;
import com.couple.sns.domain.post.dto.response.CommentResponse;
import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.CommentLikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.CommentLikeRepository;
import com.couple.sns.domain.post.persistance.repository.CommentRepository;
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
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public Page<CommentDto> list(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable).map(CommentDto::fromEntity);
    }

    @Transactional
    public CommentResponse create(String userName, Long postId, String content) {
        PostEntity post = getPostOrElseThrow(postId);
        UserEntity user = getUserOrElseThrow(userName);

        CommentEntity commentEntity = CommentEntity.of(user, post, content);

        return CommentResponse.fromComment(CommentDto.fromEntity(commentRepository.saveAndFlush(commentEntity)));
    }

    @Transactional
    public void delete(String userName, Long commentId) {
        UserEntity user = getUserOrElseThrow(userName);
        CommentEntity comment = getCommentOrElseThrow(commentId);

        if (comment.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, commentId));
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public Boolean like(Long commentId, String userName) {

        CommentEntity comment = getCommentOrElseThrow(commentId);
        UserEntity user = getUserOrElseThrow(userName);

        if (commentLikeRepository.findByCommentIdAndUserId(comment.getId(), user.getId()).isEmpty()) {
            commentLikeRepository.save(CommentLikeEntity.of(user, comment));
            return true;
        } else {
            commentLikeRepository.delete(
                commentLikeRepository.findByCommentIdAndUserId(comment.getId(), user.getId()).get());
            return false;
        }
    }

    public CommentLikeResponse likeList(Long commentId, Pageable pageable) {
        CommentEntity comment = getCommentOrElseThrow(commentId);

        return CommentLikeResponse.from(
            commentLikeRepository.findByCommentId(comment.getId(), pageable).map(
            CommentLikeDto::fromEntity));

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

    private CommentEntity getCommentOrElseThrow(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND,
                "comment not founded"));
    }
}
