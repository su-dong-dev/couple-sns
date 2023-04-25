package com.couple.sns.domain.user.persistance;

import com.couple.sns.domain.user.dto.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserEntity() {
    }

    public static UserEntity toEntity(String userId, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setRole(UserRole.USER);
        userEntity.setPassword(password);

        return userEntity;
    }
}