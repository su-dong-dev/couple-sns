package com.couple.sns.domain.user.service;

import com.couple.sns.common.configuration.util.JwtTokenUtils;
import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.common.property.JwtProperties;
import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.dto.response.UserTokenResponse;
import com.couple.sns.domain.user.persistance.TokenEntity;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.TokenRepository;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final BCryptPasswordEncoder encoder;
    private final JwtProperties jwtProperties;

    @Transactional
    public User join(String userId, String password) {
        userRepository.findByUserId(userId).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_ID,
                String.format("%s is duplicated", userId));
        });

        UserEntity userEntity = userRepository.save(
            UserEntity.toEntity(userId, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public UserTokenResponse login(String userId, String password) {
        UserEntity user = userRepository.findByUserId(userId)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                    String.format("%s not founded", userId));
            });

        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.createToken(userId, user.getRole(),
            jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());
        String refreshToken = JwtTokenUtils.createRefreshToken(userId, jwtProperties.getSecretKey(),
            jwtProperties.getExpiredTimeMs());

        tokenRepository.save(TokenEntity.toEntity(refreshToken, user));

        return new UserTokenResponse(token, refreshToken);
    }

    @Transactional
    public UserTokenResponse reissue(String token) {
        String userId = JwtTokenUtils.getUserId(token, jwtProperties.getSecretKey());

        UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                    String.format("%s not founded", userId));
            });

        TokenEntity tokenEntity = tokenRepository.findByRefreshToken(token).orElseThrow(() -> {
            throw new SnsApplicationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                String.format("%s not founded ", token));
        });

        String accessToken = JwtTokenUtils.createToken(userId, userEntity.getRole(),
            jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());
        String refreshToken = JwtTokenUtils.createRefreshToken(userId, jwtProperties.getSecretKey(),
            jwtProperties.getExpiredTimeMs());

        tokenRepository.delete(tokenEntity);
        tokenRepository.save(TokenEntity.toEntity(refreshToken, userEntity));

        return new UserTokenResponse(accessToken, refreshToken);
    }
}
