package net.graysenko.com.ForumDQC.Services;

import net.graysenko.com.ForumDQC.Models.Likes;
import net.graysenko.com.ForumDQC.Models.Post;
import net.graysenko.com.ForumDQC.Models.UserINF;
import net.graysenko.com.ForumDQC.Repositories.LikeRepository;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikesService {
    private LikeRepository likeRepository;

    public void save(Likes likes) {
        likeRepository.save(likes);
    }

    public List<Likes> findAll() {
        return likeRepository.findAll();
    }

    public Likes findByUserAndPost(UserINF user, Post post) {
        return likeRepository.findByUserAndPost(user, post);
    }
}
