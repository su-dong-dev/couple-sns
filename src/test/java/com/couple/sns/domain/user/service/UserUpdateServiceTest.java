package com.couple.sns.domain.user.service;

import com.couple.sns.common.exception.ErrorCode;
import com.couple.sns.common.exception.SnsApplicationException;
import com.couple.sns.domain.user.fixture.UserEntityFixture;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserUpdateServiceTest {

    @InjectMocks
    private UserUpdateService userUpdateService;

    @Mock
    private UserRepository userRepository;

    @Test
    void 회원가입_정상동작 (){
        // given
        String userId = "userId";
        String password = "password";

        given(userRepository.save(any())).willReturn(UserEntityFixture.get(userId,password));

        // when
        userUpdateService.join(userId, password);

        // then
        then(userRepository).should().save(any(UserEntity.class));
    }

    @Test
    void 회원가입시_이미_회원가입된_userId로_회원가입_하면_에러를_내뱉는다(){
        // given
        String userId = "userId";
        String password = "password";

        UserEntity fixture = UserEntityFixture.get(userId, password);

        given(userRepository.findByUserId(userId)).willReturn(Optional.of(fixture));

        // when
        SnsApplicationException e = assertThrows(SnsApplicationException.class, () -> userUpdateService.join(userId, password));

        // then
        assertEquals(ErrorCode.DUPLICATED_USER_ID, e.getErrorCode());
        then(userRepository).should().findByUserId(any(String.class));
    }

}