package com.couple.sns.domain.post.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.post.persistance.PostEntity;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.post.persistance.repository.PostRepository;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public void create(String title, String body, String userId) {
        UserEntity user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userId)));

        postRepository.save(PostEntity.toEntity(title, body, user));
    }
}
