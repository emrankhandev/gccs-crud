package com.gccws.config;

import com.gccws.component.RateLimitInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */
@Configuration
@PropertySources({
    @PropertySource("classpath:properties/app.properties"),
    @PropertySource("classpath:properties/swagger.properties")
})
public class AppConfig implements WebMvcConfigurer {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	private RateLimitInterceptor rateLimitInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(rateLimitInterceptor).addPathPatterns("/api/private/**");
	}
}
