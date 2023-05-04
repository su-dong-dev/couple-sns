package com.couple.sns.domain.post.persistance;

import com.couple.sns.domain.post.dto.LikeType;
import com.couple.sns.domain.user.persistance.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Setter
@Getter
@Entity
@Table(name = "likes")
@SQLDelete(sql = "UPDATE likes SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Long typeId;

    @Enumerated(EnumType.STRING)
    private LikeType type;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public LikeEntity() {
    }

    public static LikeEntity toEntity(UserEntity userEntity, Long typeId, LikeType type) {
        LikeEntity likeEntity = new LikeEntity();

        likeEntity.setUser(userEntity);
        likeEntity.setTypeId(typeId);
        likeEntity.setType(type);

        return likeEntity;
    }
}