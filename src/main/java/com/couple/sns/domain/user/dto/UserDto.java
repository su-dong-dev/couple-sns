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

    private Long id;
    private String userName;
    private String password;
    private UserRole userRole;

    private LocalDateTime registeredAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static UserDto of(Long id, String userName, String password, UserRole userRole, LocalDateTime registeredAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new UserDto(id, userName, password, userRole, registeredAt, updatedAt, deletedAt);
    }

    public static UserDto of(String userName, String password, UserRole userRole) {
        return new UserDto(null, userName, password, userRole, null, null, null);
    }

    public static UserDto fromEntity(UserEntity userEntity) {
        return new UserDto(
                userEntity.getId(),
                userEntity.getUserName(),
                userEntity.getPassword(),
                userEntity.getRole(),
                userEntity.getRegisteredAt(),
                userEntity.getUpdatedAt(),
                userEntity.getDeletedAt()
        );
    }

    public UserEntity toEntity(String encodePassword) {
        return UserEntity.of(userName, encodePassword, userRole);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
    }

    @Override
    public String getUsername() {
        return this.userName;
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
