package net.graysenko.com.ForumDQC.Services;

import net.graysenko.com.ForumDQC.Models.Post;
import net.graysenko.com.ForumDQC.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Page<Post> findByTitel(String titel, Pageable pageable) {
        return postRepository.findByTitleContaining(titel, pageable);
    }

    public Page<Post> findByDescription(String desc,Pageable pageable){
        return postRepository.findByDescrContaining(desc,pageable);
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }


    public List<Post> findRecentPosts() {
        List<Post> allPosts = postRepository.findAll();

        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3); // Текущая дата минус 3 дня

        return allPosts.stream()
                .filter(post -> post != null && post.getCreationDate().isAfter(threeDaysAgo)) // Фильтрация постов
                .sorted(Comparator.comparing(Post::getCreationDate).reversed()) // Сортировка по дате (новые сверху)
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}
