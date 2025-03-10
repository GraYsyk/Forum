package net.graysenko.com.ForumDQC.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "user_id"}))
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserINF user;

    @Column(name = "liked_at")
    private LocalDateTime likedAt;

    public Likes() {
    }

    public Likes(Post post, UserINF user) {
        this.post = post;
        this.user = user;
        this.likedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.likedAt = LocalDateTime.now();
    }

    public UserINF getUser() {
        return user;
    }

    public void setUser(UserINF user) {
        this.user = user;
    }

    public LocalDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(LocalDateTime likedAt) {
        this.likedAt = likedAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
