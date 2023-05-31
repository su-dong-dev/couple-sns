package com.couple.sns.domain.user.dto;

import com.couple.sns.domain.user.persistance.UserEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserDto implements UserDetails {

    private String username;
    private String password;
    private UserRole role;

    private String nickname;
    private String phone;
    private String profileImage;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserDto of(String username, String password, UserRole role) {
        return new UserDto(username, password, role, null, null, null, null, null, null);
    }

    public static UserDto of(String username, String password, UserRole role, String nickname, String phone, String profileImage) {
        return new UserDto(username, password, role, nickname, phone, profileImage, null, null, null);
    }

    public static UserDto of(String username, String password, UserRole role, String nickname, String phone, String profileImage, LocalDateTime registeredAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new UserDto(username, password, role, nickname, phone, profileImage, registeredAt, updatedAt, deletedAt);
    }

    public static UserDto fromEntity(UserEntity userEntity) {
        return new UserDto(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getNickname(),
                userEntity.getPhone(),
                userEntity.getProfileImage(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt(),
                userEntity.getDeletedAt()
        );
    }

    public UserEntity toEntity(String encodePassword) {
        return UserEntity.of(username, encodePassword, role, nickname, phone, profileImage);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return this.deletedAt == null;
    }
}
