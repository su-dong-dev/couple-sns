package com.couple.sns.domain.user.service;

import com.couple.sns.domain.user.dto.User;
import com.couple.sns.domain.user.persistance.UserEntity;
import com.couple.sns.domain.user.persistance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserRepository userRepository;

    // TODO : password encoding
    public User join(String userId, String password) {
        UserEntity userEntity = userRepository.save(UserEntity.toEntity(userId,password));

        return User.fromEntity(userEntity);
    }

}
