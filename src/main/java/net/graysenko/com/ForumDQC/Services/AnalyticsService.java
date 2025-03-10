package net.graysenko.com.ForumDQC.Services;

import net.graysenko.com.ForumDQC.Models.PageViews;
import net.graysenko.com.ForumDQC.Repositories.CommentRepository;
import net.graysenko.com.ForumDQC.Repositories.LikeRepository;
import net.graysenko.com.ForumDQC.Repositories.PageViewRepository;
import net.graysenko.com.ForumDQC.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AnalyticsService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PageViewRepository pageViewRepository;

    @Autowired
    public AnalyticsService(PostRepository postRepository,
                             CommentRepository commentRepository,
                             LikeRepository likeRepository,
                             PageViewRepository pageViewRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.pageViewRepository = pageViewRepository;
    }

    /**
     * Получает количество просмотров страниц для указанной даты
     */
    public int getPageViewsForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);
        return pageViewRepository.countByTimestampBetween(startOfDay, endOfDay);
    }

    /**
     * Получает количество уникальных посетителей для указанной даты
     */
    public int getUniqueVisitorsForDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);
        return pageViewRepository.countDistinctUsersByTimestampBetween(startOfDay, endOfDay);
    }

    /**
     * Получает количество постов за последнюю неделю
     */
    public int getPostCountForLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return postRepository.countByCreationDateAfter(oneWeekAgo);
    }

    public int getPostCountForLastMonth() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusMonths(1);
        return postRepository.countByCreationDateAfter(oneWeekAgo);
    }

    /**
     * Получает количество комментариев за последнюю неделю
     */
    public int getCommentCountForLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return commentRepository.countByCreatedAtAfter(oneWeekAgo);
    }

    public int getCommentCountForLastMonth() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusMonths(1);
        return commentRepository.countByCreatedAtAfter(oneWeekAgo);
    }

    /**
     * Получает количество лайков за последнюю неделю
     */
    public int getLikeCountForLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return likeRepository.countByLikedAtAfter(oneWeekAgo);
    }

    public int getLikeCountForLastMonth() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusMonths(1);
        return likeRepository.countByLikedAtAfter(oneWeekAgo);
    }

    /**
     * Получает количество просмотров за последнюю неделю
     */
    public int getViewCountForLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return pageViewRepository.countByTimestampAfter(oneWeekAgo);
    }

    public int getViewCountForLastMonth() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        return pageViewRepository.countByTimestampAfter(oneMonthAgo);
    }

    /**
     * Записывает просмотр страницы
     */
    public void recordPageView(String path, String userId, String ipAddress) {
        PageViews pageView = new PageViews();
        pageView.setPath(path);
        pageView.setUserId(userId);
        pageView.setIpAddress(ipAddress);
        pageView.setTimestamp(LocalDateTime.now());
        pageViewRepository.save(pageView);
    }


}