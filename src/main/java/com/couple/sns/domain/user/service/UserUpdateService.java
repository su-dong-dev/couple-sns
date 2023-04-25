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
    public User join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
                String.format("%s is duplicated", userName));
        });

        UserEntity userEntity = userRepository.save(
            UserEntity.toEntity(userName, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public UserTokenResponse login(String userName, String password) {
        UserEntity user = userRepository.findByUserName(userName)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                    String.format("%s not founded", userName));
            });

        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.createToken(userName, user.getRole(),
            jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());
        String refreshToken = JwtTokenUtils.createRefreshToken(userName, jwtProperties.getSecretKey(),
            jwtProperties.getExpiredTimeMs());

        tokenRepository.save(TokenEntity.toEntity(refreshToken, user));

        return new UserTokenResponse(token, refreshToken);
    }

    @Transactional
    public UserTokenResponse reissue(String token) {
        String userName = JwtTokenUtils.getUserName(token, jwtProperties.getSecretKey());

        UserEntity userEntity = userRepository.findByUserName(userName)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND,
                    String.format("%s not founded", userName));
            });

        TokenEntity tokenEntity = tokenRepository.findByRefreshToken(token).orElseThrow(() -> {
            throw new SnsApplicationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND,
                String.format("%s not founded ", token));
        });

        String accessToken = JwtTokenUtils.createToken(userName, userEntity.getRole(),
            jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());
        String refreshToken = JwtTokenUtils.createRefreshToken(userName, jwtProperties.getSecretKey(),
            jwtProperties.getExpiredTimeMs());

        tokenRepository.delete(tokenEntity);
        tokenRepository.save(TokenEntity.toEntity(refreshToken, userEntity));

        return new UserTokenResponse(accessToken, refreshToken);
    }
}
