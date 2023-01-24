package com.couple.sns.domain.user.service;

import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserUpdateServiceTest {

    @Autowired
    private UserUpdateService userUpdateService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void 회원가입_정상동작 (){
        String userId = "userId";
        String password = "password";

        given(userRepository.save(any())).willReturn(createArticle());

        Assertions.assertDoesNotThrow(() -> userUpdateService.join(userId,password));
    }

    private UserEntity createArticle() {
        UserEntity user = UserEntity.of(
                "userId",
                "password"
        );
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }
}