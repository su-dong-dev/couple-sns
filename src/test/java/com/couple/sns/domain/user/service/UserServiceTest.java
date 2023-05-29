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

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입_정상동작 (){
        // given
        String userName = "userName";
        String password = "password";

        given(userRepository.save(any())).willReturn(UserEntityFixture.get(userName,password));
        given(encoder.encode(password)).willReturn("encrypt_passowrd");

        // when
        userService.join(UserDto.of(userName, password, UserRole.USER));

        // then
        then(userRepository).should().save(any(UserEntity.class));
    }

    @Test
    void 회원가입시_이미_회원가입된_userName로_회원가입_하면_에러를_내뱉는다(){
        // given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(fixture));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.join(UserDto.of(userName, password, UserRole.USER)));

        // then
        assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
        then(userRepository).should().findByUserName(any(String.class));
    }

    @Test
    void 로그인_정상동작 (){
        // given
        String userName = "userName";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        given(userRepository.findByUserName(any())).willReturn(Optional.of(fixture));
        given(encoder.matches(password, fixture.getPassword())).willReturn(true);
        given(tokenRepository.save(any())).willReturn(any(TokenEntity.class));

        // when
        userService.login(userName, password);

        // then
        then(userRepository).should().findByUserName(any());
    }

    @Test
    void 로그인시_userName로_회원가입한_유저가_없는_경우 (){
        // given
        String userName = "userName";
        String password = "password";

        given(userRepository.findByUserName(any())).willReturn(Optional.empty());

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));

        // then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
        then(userRepository).should().findByUserName(any());
    }

    @Test
    void 로그인시_잘못된_password를_입력했을_경우() {
        // given
        String userName = "userName";
        String password = "password";
        String wrongPassword = "wrongPassword";

        UserEntity fixture = UserEntityFixture.get(userName, password);

        given(userRepository.findByUserName(any())).willReturn(Optional.of(fixture));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword));

        // then
        assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());

        then(userRepository).should().findByUserName(any());
    }

}