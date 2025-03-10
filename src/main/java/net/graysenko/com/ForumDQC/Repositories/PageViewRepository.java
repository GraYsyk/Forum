package net.graysenko.com.ForumDQC.Repositories;

import net.graysenko.com.ForumDQC.Models.PageViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;


@Repository
public interface PageViewRepository extends JpaRepository<PageViews, Long> {

    int countByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(DISTINCT p.userId) FROM PageViews p WHERE p.userId IS NOT NULL AND p.timestamp BETWEEN ?1 AND ?2")
    int countDistinctUsersByTimestampBetween(LocalDateTime start, LocalDateTime end);

    int countByTimestampAfter(LocalDateTime timestamp);
}