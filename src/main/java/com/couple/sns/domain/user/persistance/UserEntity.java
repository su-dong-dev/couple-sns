package com.couple.sns.domain.user.persistance;

import com.couple.sns.domain.user.dto.UserRole;
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
    @Index(columnList = "userName", unique = true),
    @Index(columnList = "registeredAt")
})
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected UserEntity() {
    }

    private UserEntity(String userName, String password, UserRole role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public static UserEntity of(String userName, String password, UserRole role) {
        return new UserEntity(userName, password, role);
    }
}