package com.couple.sns.domain.user.service;

import com.couple.sns.common.configuration.util.JwtTokenUtils;
import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.common.property.JwtProperties;
import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.response.UserResponse;
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
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final BCryptPasswordEncoder encoder;
    private final JwtProperties jwtProperties;

    @Transactional
    public UserResponse join(UserDto dto) {
        userRepository.findByUsername(dto.getUsername()).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, "중복된 아이디가 존재합니다.");
        });

        userRepository.save(dto.toEntity(encoder.encode(dto.getPassword())));

        return UserResponse.from(dto);
    }

    public UserTokenResponse login(String username, String password) {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND, "가입된 아이디를 찾을 수 없습니다.");
            });

        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");
        }

        String token = JwtTokenUtils.createToken(username, user.getRole(),
            jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());
        String refreshToken = JwtTokenUtils.createRefreshToken(username, jwtProperties.getSecretKey(),
            jwtProperties.getExpiredTimeMs());

        tokenRepository.save(TokenEntity.of(refreshToken, user));

        return UserTokenResponse.of(token, refreshToken);
    }

    @Transactional
    public UserTokenResponse reissue(String token) {
        String username = JwtTokenUtils.getUsername(token, jwtProperties.getSecretKey());

        UserEntity userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.USER_NOT_FOUND, "가입된 아이디를 찾을 수 없습니다.");
            });

        TokenEntity tokenEntity = tokenRepository.findByRefreshToken(token)
            .orElseThrow(() -> {
                throw new SnsApplicationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND, "토근을 찾을 수 없습니다.");
        });

        String accessToken = JwtTokenUtils.createToken(username, userEntity.getRole(),
            jwtProperties.getSecretKey(), jwtProperties.getExpiredTimeMs());
        String refreshToken = JwtTokenUtils.createRefreshToken(username, jwtProperties.getSecretKey(),
            jwtProperties.getExpiredTimeMs());

        tokenRepository.delete(tokenEntity);
        tokenRepository.save(TokenEntity.of(refreshToken, userEntity));

        return UserTokenResponse.of(accessToken, refreshToken);
    }
}
