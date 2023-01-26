package com.couple.sns.domain.user.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserRepository userRepository;

    // TODO : password encoding
    @Transactional
    public User join(String userId, String password) {
        userRepository.findByUserId(userId).ifPresent( it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("%s is duplicated", userId));
        });

        UserEntity userEntity = userRepository.save(UserEntity.toEntity(userId,password));

        return User.fromEntity(userEntity);
    }
}
