package net.graysenko.com.ForumDQC.Controller;


import net.graysenko.com.ForumDQC.Models.Post;
import net.graysenko.com.ForumDQC.Models.UserINF;
import net.graysenko.com.ForumDQC.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPanelController {

    private AnalyticsService analyticsService;
    private PostService postService;
    private UserService userService;
    private SystemService systemMonitorService;
    private CommentService commentService;

    @Autowired
    public AdminPanelController(AnalyticsService analyticsService, PostService postService, UserService userService, CommentService commentService, SystemService systemMonitorService) {
        this.analyticsService = analyticsService;
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.systemMonitorService = systemMonitorService;
    }

    @GetMapping("")
    public String admin(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        UserINF user = userService.getUserByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User was not Found"));

        int userCount = userService.getAllUsers().size();
        int postCount = postService.findAll().size();
        int commentCount = commentService.findAll().size();

        long pageViews = analyticsService.getViewCountForLastMonth();

        List<Post> recentPosts = postService.findRecentPosts();
        List<UserINF> newUsers = userService.getNewUsers();

        List<String> trafficDates = new ArrayList<>();
        List<Integer> pageViewsData = new ArrayList<>();
        List<Integer> uniqueVisitorsData = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM");

        for (int i = 6; i>0; i--) {
            LocalDate date = LocalDate.now();
            trafficDates.add(date.format(dateFormatter));

            int pageViewsForDate = analyticsService.getPageViewsForDate(date);
            pageViewsData.add(pageViewsForDate);

            int uniqueVisitors = analyticsService.getUniqueVisitorsForDate(date);
            uniqueVisitorsData.add(uniqueVisitors);
        }


        List<String> activityLabels = Arrays.asList("Posts", "Comments", "Likes", "Views");
        List<Integer> activityData = Arrays.asList(
                analyticsService.getPostCountForLastWeek(),
                analyticsService.getCommentCountForLastWeek(),
                analyticsService.getLikeCountForLastWeek(),
                analyticsService.getViewCountForLastWeek()
        );

        List<String> activityColors = Arrays.asList(
                "rgba(46, 204, 113, 0.7)",
                "rgba(52, 152, 219, 0.7)",
                "rgba(155, 89, 182, 0.7)",
                "rgba(243, 156, 18, 0.7)"
        );

        model.addAttribute("user", user);

        model.addAttribute("userCount", userCount);
        model.addAttribute("postCount", postCount);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("recentPosts", recentPosts);
        model.addAttribute("newUsers", newUsers);
        model.addAttribute("pageViews", pageViews);

        model.addAttribute("trafficDates", trafficDates);
        model.addAttribute("pageViewsData", pageViewsData);
        model.addAttribute("uniqueVisitorsData", uniqueVisitorsData);
        model.addAttribute("activityLabels", activityLabels);
        model.addAttribute("activityData", activityData);
        model.addAttribute("activityColors", activityColors);

        model.addAttribute("cpuUsage", systemMonitorService.getCPUusage());
        model.addAttribute("memoryUsage", Math.round(systemMonitorService.getMemoryUsage() * 10) / 10.0);
        model.addAttribute("diskUsage", systemMonitorService.getDiskUsage());
        model.addAttribute("networkSpeed", systemMonitorService.getNetworkSpeed());
        model.addAttribute("serverTemp", systemMonitorService.getCpuTemperature());
        model.addAttribute("uptime", systemMonitorService.getUpTime());

        return "admin/dashboard";
    }
}
