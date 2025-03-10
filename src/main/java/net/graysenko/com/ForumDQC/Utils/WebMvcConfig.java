package net.graysenko.com.ForumDQC.Utils;

import net.graysenko.com.ForumDQC.Utils.Interceptors.PageViewInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Регистрация перехватчика
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final PageViewInterceptor pageViewInterceptor;

    @Autowired
    public WebMvcConfig(PageViewInterceptor pageViewInterceptor) {
        this.pageViewInterceptor = pageViewInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(pageViewInterceptor);
    }
}
