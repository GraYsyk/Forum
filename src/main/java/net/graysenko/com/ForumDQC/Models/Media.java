package net.graysenko.com.ForumDQC.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "media_url")
    private byte[] mediaURL;

    @Column(name = "type")
    private String type;

    @Column(name = "filename")
    private String filename;

    public Media() {
    }

    public Media(Post post, byte[] mediaURL, String type, String filename) {
        this.post = post;
        this.mediaURL = mediaURL;
        this.type = type;
        this.filename = filename;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public byte[] getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(byte[] mediaURL) {
        this.mediaURL = mediaURL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
