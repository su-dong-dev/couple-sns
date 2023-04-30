package com.couple.sns.domain.post.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.dto.Comment;
import com.couple.sns.domain.post.dto.response.CommentResponse;
import com.couple.sns.domain.post.persistance.CommentEntity;
import com.couple.sns.domain.post.persistance.PostEntity;
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

    public Page<Comment> list(Long postId, Pageable pageable) {
        return commentRepository.findByPostId(postId, pageable).map(Comment::fromEntity);
    }

    @Transactional
    public CommentResponse create(String userName, Long postId, String content) {
        PostEntity post = getPostOrElseThrow(postId);
        UserEntity user = getUserOrElseThrow(userName);

        CommentEntity commentEntity = CommentEntity.toEntity(user, post, content);

        return CommentResponse.fromComment(Comment.fromEntity(commentRepository.saveAndFlush(commentEntity)));
    }

    @Transactional
    public void delete(String userName, Long commentId) {
        UserEntity user = getUserOrElseThrow(userName);
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
            () -> new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND,
                "comment not founded"));

        if (comment.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, commentId));
        }

        commentRepository.delete(comment);
    }

    private UserEntity getUserOrElseThrow(String userName) {
        return userRepository.findByUserName(userName)
            .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                String.format("%s not founded", userName)));
    }

    private PostEntity getPostOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(
            () -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
                String.format("%s not founded", postId)));
    }
}
