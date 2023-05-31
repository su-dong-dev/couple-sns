package com.couple.sns.domain.post.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.dto.CommentDto;
import com.couple.sns.domain.post.dto.LikeDto;
import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.post.dto.response.CommentResponse;
import com.couple.sns.domain.post.dto.response.LikeResponse;
import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.LikeEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.post.persistance.repository.CommentRepository;
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
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

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

        if (likeRepository.findByTypeAndTypeIdAndUser_Id(LikeType.COMMENT, comment.getId(), user.getId()).isEmpty()) {
            likeRepository.save(LikeEntity.of(user, comment.getId(), LikeType.COMMENT));
            return true;
        } else {
            likeRepository.delete(likeRepository.findByTypeAndTypeIdAndUser_Id(LikeType.COMMENT, comment.getId(), user.getId()).get());
            return false;
        }
    }

    public LikeResponse likeList(Long commentId, Pageable pageable) {
        CommentEntity comment = getCommentOrElseThrow(commentId);

        return LikeResponse.from(
            comment.getId(), likeRepository.findAllByTypeAndTypeId(LikeType.COMMENT, comment.getId(), pageable).map(
            LikeDto::fromEntity));

    }

    private UserEntity getUserOrElseThrow(String userName) {
        return userRepository.findByUsername(userName)
            .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("%s not founded", userName)));
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
