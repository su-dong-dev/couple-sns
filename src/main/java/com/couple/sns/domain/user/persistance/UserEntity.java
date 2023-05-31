package com.couple.sns.domain.user.persistance;

import com.couple.sns.domain.user.dto.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "user", indexes = {
    @Index(columnList = "username", unique = true)
})
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 200, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private UserRole role;

    @Column(length = 50)
    private String nickname;

    @Column(length = 11)
    private String phone;
    private String profileImage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected UserEntity() {
    }

    private UserEntity(String username, String password, UserRole role, String nickname, String phone, String profileImage) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImage = profileImage;
    }

    public static UserEntity of(String username, String password, UserRole role, String nickname, String phone, String profileImage) {
        return new UserEntity(username, password, role, nickname, phone, profileImage);
    }

}