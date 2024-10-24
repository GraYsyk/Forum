package net.graysenko.com.ForumDQC.Models;

import jakarta.persistence.*;
import net.graysenko.com.ForumDQC.Enums.UserStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Entity
@Table(name = "UserINF")
public class UserINF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar")
    private byte[] avatar;

    @Column(name = "createdat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "owner")
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comments> comments;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public UserINF() {
    }

    public UserINF(String username, String password, byte[] avatar, Date createdAt, String role, List<Post> posts) {
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.role = role;
        this.posts = posts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "User id: " + id +
                " username: " + username +
                " avatar: " + Arrays.toString(avatar);
    }
}
