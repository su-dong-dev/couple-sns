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
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW() WHERE post_id = ?")
@Where(clause = "deleted_at is NULL")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Setter
    private String content;

    @Setter
    @Column(length = 50)
    private String location;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected PostEntity() {
    }

    private PostEntity(UserEntity userEntity, String content, String location) {
        this.user = userEntity;
        this.content = content;
        this.location = location;
    }

    public static PostEntity of(UserEntity userEntity, String content, String location) {
        return new PostEntity(userEntity, content, location);
    }
}