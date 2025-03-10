package net.graysenko.com.ForumDQC.Repositories;

import net.graysenko.com.ForumDQC.Models.Likes;
import net.graysenko.com.ForumDQC.Models.Post;
import net.graysenko.com.ForumDQC.Models.UserINF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {

    List<Likes> findByPost(Post post);
    List<Likes> findByUser(UserINF user);
    Likes findByUserAndPost(UserINF user, Post post);
    int countByLikedAtAfter(LocalDateTime localDateTime);
}
