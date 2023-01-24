package com.couple.sns.domain.user.persistance;

import com.couple.sns.domain.user.dto.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;
    private String password;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserEntity(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    protected UserEntity() {

    }

    // 팩토릭 메서드, new 키워드 사용 X , 도메인 Article 생성시 필요한 값 전달
    public static UserEntity of(String userId, String password) {
        return new UserEntity(userId, password);
    }

    public static UserEntity toEntity(String userId, String password) {
        UserEntity entity = new UserEntity();
        entity.setUserId(userId);
        entity.setPassword(password);

        return entity;
    }
}
