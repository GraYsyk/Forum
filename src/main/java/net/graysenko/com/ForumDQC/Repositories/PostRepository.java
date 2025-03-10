package net.graysenko.com.ForumDQC.Repositories;

import net.graysenko.com.ForumDQC.Models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAll(Pageable pageable);

    Page<Post> findByTitleContaining(String Titel, Pageable pageable);

    Page<Post> findByDescrContaining(String Description, Pageable pageable);

    Optional<Post> findById(Long id);

    int countByCreationDateAfter(LocalDateTime creationDateAfter);
}
