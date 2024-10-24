package net.graysenko.com.ForumDQC.Repositories;

import net.graysenko.com.ForumDQC.Models.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostId(Long postId);
    List<Comments> findByUserId(Long userId);
}
