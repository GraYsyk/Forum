package net.graysenko.com.ForumDQC.Repositories;

import net.graysenko.com.ForumDQC.Models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByPost_Id(Long id);
    Media findByFilename(String Filename);
}
