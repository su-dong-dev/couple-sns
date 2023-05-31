package com.couple.sns.domain.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.couple.sns.common.configuration.util.JwtTokenUtils;
import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.common.fixture.UserEntityFixture;
import com.couple.sns.domain.user.dto.UserDto;
import com.couple.sns.domain.user.dto.UserRole;
import com.couple.sns.domain.user.persistance.TokenEntity;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.TokenRepository;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
@Import(JwtTokenUtils.class)
class UserServiceTest {

    @Autowired private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private TokenRepository tokenRepository;
    @MockBean private BCryptPasswordEncoder encoder;

    static String username = "username";
    static String password = "password";
    @Test
    void 회원가입_정상동작 (){
        given(userRepository.save(any())).willReturn(UserEntityFixture.get(username,password));
        given(encoder.encode(password)).willReturn("encrypt_password");

        userService.join(UserDto.of(username, password, UserRole.USER));

        then(userRepository).should().save(any(UserEntity.class));
    }

    @Test
    void 회원가입시_이미_회원가입된_username로_회원가입_하면_에러를_내뱉는다(){
        UserEntity fixture = UserEntityFixture.get(username, password);

        given(userRepository.findByUsername(username)).willReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.join(UserDto.of(username, password, UserRole.USER)));

        assertEquals(ErrorCode.DUPLICATED_USERNAME, e.getErrorCode());
        then(userRepository).should().findByUsername(any(String.class));
    }

    @Test
    void 로그인_정상동작 (){
        UserEntity fixture = UserEntityFixture.get(username, password);

        given(userRepository.findByUsername(any())).willReturn(Optional.of(fixture));
        given(encoder.matches(password, fixture.getPassword())).willReturn(true);
        given(tokenRepository.save(any())).willReturn(any(TokenEntity.class));

        userService.login(username, password);

        // then
        then(userRepository).should().findByUsername(any());
    }

    @Test
    void 로그인시_username로_회원가입한_유저가_없는_경우 (){
        String username = "username";
        String password = "password";

        given(userRepository.findByUsername(any())).willReturn(Optional.empty());

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(username, password));

        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUsername(any());
    }

    @Test
    void 로그인시_잘못된_password를_입력했을_경우() {
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(username, password);
        given(userRepository.findByUsername(any())).willReturn(Optional.of(fixture));

        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(username, wrongPassword));

        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
        then(userRepository).should().findByUsername(any());
    }

}