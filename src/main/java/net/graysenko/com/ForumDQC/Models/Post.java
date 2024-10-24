package net.graysenko.com.ForumDQC.Models;

import jakarta.persistence.*;
import net.graysenko.com.ForumDQC.Enums.PostStatus;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserINF owner;

    @Column(name = "title")
    private String title;

    @Column(name = "descr")
    private String descr;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Media> media;

    @OneToMany(mappedBy = "post")
    private List<Comments> comment;

    public Post() {
    }

    public Post(UserINF ownerId,String title, String descr, String content, Date creationDate, Date updateDate, PostStatus status) {
        this.owner = ownerId;
        this.title = title;
        this.descr = descr;
        this.content = content;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        this.creationDate = new Date();
        this.updateDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = new Date();
    }

    public List<Comments> getComment() {
        return comment;
    }

    public void setComment(List<Comments> comment) {
        this.comment = comment;
    }

    public UserINF getOwner() {
        return owner;
    }

    public void setOwner(UserINF owner) {
        this.owner = owner;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public PostStatus getStatus() {
        return status;
    }

    public void setStatus(PostStatus status) {
        this.status = status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Post with id: " + id
                + ", title: " + title
                + ", content: " + content
                + ", creationDate: " + creationDate;
    }
}
