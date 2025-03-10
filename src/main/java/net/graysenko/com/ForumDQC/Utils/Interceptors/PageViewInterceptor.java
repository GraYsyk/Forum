package net.graysenko.com.ForumDQC.Utils.Interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.graysenko.com.ForumDQC.Services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PageViewInterceptor implements HandlerInterceptor {

    private final AnalyticsService analyticsService;

    @Autowired
    public PageViewInterceptor(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        if (request.getMethod().equals("GET") && response.getStatus() >= 200 && response.getStatus() < 300) {
            String path = request.getRequestURI();

            // Получаем ID пользователя, если он аутентифицирован
            String userId = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
                userId = auth.getName();
            }

            // Получаем IP адрес
            String ipAddress = request.getRemoteAddr();

            // Записываем просмотр
            analyticsService.recordPageView(path, userId, ipAddress);
        }
    }
}

