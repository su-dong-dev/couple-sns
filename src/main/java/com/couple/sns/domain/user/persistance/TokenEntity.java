package com.couple.sns.domain.user.persistance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "token")
@SQLDelete(sql = "UPDATE token SET deleted = true WHERE token_id = ?")
@Where(clause = "deleted = false")
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String refreshToken;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private boolean deleted;

    protected TokenEntity() {
    }

    private TokenEntity(String token, UserEntity userEntity) {
        this.refreshToken = token;
        this.user = userEntity;
    }

    public static TokenEntity of(String token, UserEntity userEntity) {
        return new TokenEntity(token, userEntity);
    }
}