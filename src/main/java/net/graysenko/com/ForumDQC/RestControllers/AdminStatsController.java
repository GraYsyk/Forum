package net.graysenko.com.ForumDQC.RestControllers;

import net.graysenko.com.ForumDQC.Services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/info")
public class AdminStatsController {

    private final AnalyticsService analyticsService;

    @Autowired
    public AdminStatsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/stats")
    @ResponseBody
    public Map<String, Object> getStatsByPeriod(@RequestParam String period) {
        Map<String, Object> result = new HashMap<>();

        // Определяем начальную дату в зависимости от периода
        LocalDate startDate;
        LocalDate endDate = LocalDate.now();

        switch (period) {
            case "week":
                startDate = endDate.minusWeeks(1);
                break;
            case "month":
                startDate = endDate.minusMonths(1);
                break;
            case "quarter":
                startDate = endDate.minusMonths(3);
                break;
            case "year":
                startDate = endDate.minusYears(1);
                break;
            default:
                startDate = endDate.minusWeeks(1);
        }

        // Подготавливаем данные для графика трафика
        List<String> trafficDates = new ArrayList<>();
        List<Integer> pageViewsData = new ArrayList<>();
        List<Integer> uniqueVisitorsData = new ArrayList<>();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM");

        // Определяем интервал для датирования точек на графике
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        int interval = 1; // По умолчанию показываем каждый день

        if (daysBetween > 30 && daysBetween <= 90) {
            interval = 7; // Еженедельно для квартала
        } else if (daysBetween > 90) {
            interval = 30; // Ежемесячно для года
        }

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(interval)) {
            trafficDates.add(date.format(dateFormatter));

            int pageViewsForDate = analyticsService.getPageViewsForDate(date);
            pageViewsData.add(pageViewsForDate);

            int uniqueVisitors = analyticsService.getUniqueVisitorsForDate(date);
            uniqueVisitorsData.add(uniqueVisitors);
        }

        // Подготавливаем данные активности в зависимости от периода
        int postCount, commentCount, likeCount, viewCount;

        // Для разных периодов могут потребоваться разные методы получения данных
        // В этом примере мы будем использовать имеющиеся методы для недели и месяца
        if (period.equals("week")) {
            postCount = analyticsService.getPostCountForLastWeek();
            commentCount = analyticsService.getCommentCountForLastWeek();
            likeCount = analyticsService.getLikeCountForLastWeek();
            viewCount = analyticsService.getViewCountForLastWeek();
        } else if (period.equals("month")) {
            postCount = analyticsService.getPostCountForLastMonth();
            commentCount = analyticsService.getCommentCountForLastMonth();
            likeCount = analyticsService.getLikeCountForLastMonth();
            viewCount = analyticsService.getViewCountForLastMonth();
        } else {
            // Для других периодов используем примерные оценки
            // В идеале здесь должны быть соответствующие методы в сервисе
            postCount = analyticsService.getPostCountForLastWeek() * (period.equals("quarter") ? 12 : 52);
            commentCount = analyticsService.getCommentCountForLastWeek() * (period.equals("quarter") ? 12 : 52);
            likeCount = analyticsService.getLikeCountForLastWeek() * (period.equals("quarter") ? 12 : 52);
            viewCount = analyticsService.getViewCountForLastMonth() * (period.equals("quarter") ? 3 : 12);
        }

        List<String> activityLabels = Arrays.asList("Posts", "Comments", "Likes", "Views");
        List<Integer> activityData = Arrays.asList(postCount, commentCount, likeCount, viewCount);

        List<String> activityColors = Arrays.asList(
                "rgba(46, 204, 113, 0.7)",
                "rgba(52, 152, 219, 0.7)",
                "rgba(155, 89, 182, 0.7)",
                "rgba(243, 156, 18, 0.7)"
        );

        // Заполняем результирующий Map
        result.put("trafficDates", trafficDates);
        result.put("pageViewsData", pageViewsData);
        result.put("uniqueVisitorsData", uniqueVisitorsData);
        result.put("activityLabels", activityLabels);
        result.put("activityData", activityData);
        result.put("activityColors", activityColors);

        return result;
    }

    @GetMapping("/activity-details")
    public Map<String, Object> getActivityDetails(@RequestParam String category) {
        Map<String, Object> result = new HashMap<>();

        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        List<String> colors = new ArrayList<>();

        // В зависимости от выбранной категории, возвращаем детализированные данные
        switch (category) {
            case "Posts":
                // Можно получить распределение постов по категориям
                labels = Arrays.asList("Announcements", "Tutorials", "Questions", "Discussions", "Other");
                values = Arrays.asList(15, 25, 35, 20, 5); // Пример данных
                colors = Arrays.asList(
                        "rgba(46, 204, 113, 0.8)",
                        "rgba(46, 204, 113, 0.7)",
                        "rgba(46, 204, 113, 0.6)",
                        "rgba(46, 204, 113, 0.5)",
                        "rgba(46, 204, 113, 0.4)"
                );
                break;

            case "Comments":
                // Можно получить распределение комментариев по дням недели
                labels = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
                values = Arrays.asList(65, 80, 95, 115, 75, 55, 45); // Пример данных
                colors = Arrays.asList(
                        "rgba(52, 152, 219, 0.9)",
                        "rgba(52, 152, 219, 0.8)",
                        "rgba(52, 152, 219, 0.7)",
                        "rgba(52, 152, 219, 0.6)",
                        "rgba(52, 152, 219, 0.5)",
                        "rgba(52, 152, 219, 0.4)",
                        "rgba(52, 152, 219, 0.3)"
                );
                break;

            case "Likes":
                // Распределение лайков по типам контента
                labels = Arrays.asList("Posts", "Comments", "Profiles", "Other");
                values = Arrays.asList(500, 320, 30, 0); // Пример данных
                colors = Arrays.asList(
                        "rgba(155, 89, 182, 0.8)",
                        "rgba(155, 89, 182, 0.6)",
                        "rgba(155, 89, 182, 0.4)",
                        "rgba(155, 89, 182, 0.2)"
                );
                break;

            case "Views":
                // Распределение просмотров по страницам
                labels = Arrays.asList("Home", "Forum", "Tutorials", "Users", "Other");
                values = Arrays.asList(1500, 1200, 950, 750, 800); // Пример данных
                colors = Arrays.asList(
                        "rgba(243, 156, 18, 0.9)",
                        "rgba(243, 156, 18, 0.8)",
                        "rgba(243, 156, 18, 0.7)",
                        "rgba(243, 156, 18, 0.6)",
                        "rgba(243, 156, 18, 0.5)"
                );
                break;

            default:
                labels = Arrays.asList("No data available");
                values = Arrays.asList(0);
                colors = Arrays.asList("rgba(189, 195, 199, 0.5)");
        }

        result.put("labels", labels);
        result.put("values", values);
        result.put("colors", colors);

        return result;
    }

    // Можно добавить метод для получения детальной информации о системных ресурсах
    @GetMapping("/system-status")
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> result = new HashMap<>();

        // Здесь можно добавить вызов SystemService для получения текущих значений
        // системных ресурсов и мониторинга

        return result;
    }
}