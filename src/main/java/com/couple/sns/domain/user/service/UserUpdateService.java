package com.couple.sns.domain.user.service;

import com.couple.sns.common.configuration.util.JwtTokenUtils;
import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.common.property.JwtProperties;
import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtProperties jwtProperties;

    @Transactional
    public User join(String userId, String password) {
        userRepository.findByUserId(userId).ifPresent( it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_ID, String.format("%s is duplicated", userId));
        });

        UserEntity userEntity = userRepository.save(UserEntity.toEntity(userId, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String userId, String password) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow( () -> {throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userId));});

        if(!encoder.matches(password, user.getPassword())){
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // token 생성
        String token = JwtTokenUtils.createToken(userId, jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());

        return token;
    }
}
