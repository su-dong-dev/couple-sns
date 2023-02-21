package com.couple.sns.domain.user.persistance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Setter
@Getter
@Entity
@Table(name = "token")
@SQLDelete(sql = "UPDATE token SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String refreshToken;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    private boolean deleted;

    public TokenEntity() {
    }

    public static TokenEntity toEntity(String token, UserEntity userEntity) {
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUser(userEntity);
        tokenEntity.setRefreshToken(token);

        return tokenEntity;
    }
}