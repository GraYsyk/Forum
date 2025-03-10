package net.graysenko.com.ForumDQC.Services;

import net.graysenko.com.ForumDQC.Models.Comments;
import net.graysenko.com.ForumDQC.Repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comments> findAll() {
        return commentRepository.findAll();
    }

    public List<Comments> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comments> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    public void saveComment(Comments comment) {
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
