package com.couple.sns.domain.post.persistance;

import com.couple.sns.domain.user.persistance.UserEntity;
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
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Setter
    private String title;

    @Setter
    @Column(columnDefinition = "TEXT")
    private String body;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected PostEntity() {
    }

    private PostEntity(UserEntity userEntity, String title, String body) {
        this.user = userEntity;
        this.title = title;
        this.body = body;
    }

    public static PostEntity of(String title, String body, UserEntity userEntity) {
        return new PostEntity(userEntity, title, body);
    }
}