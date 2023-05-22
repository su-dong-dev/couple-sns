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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @Column(length = 500)
    private String content;

    @CreationTimestamp
    private LocalDateTime registeredAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    protected CommentEntity() {
    }

    private CommentEntity(UserEntity user, PostEntity post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }

    public static CommentEntity of(UserEntity user, PostEntity post, String content) {
        return new CommentEntity(user, post, content);
    }
}