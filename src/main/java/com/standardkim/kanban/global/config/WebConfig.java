package com.standardkim.kanban.global.config;

import com.standardkim.kanban.global.config.interceptor.HttpLoggingInterceptor;
import com.standardkim.kanban.global.config.property.ConfigProperties;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer{
	private final HttpLoggingInterceptor httpLoggingInterceptor;

	private final ConfigProperties configProperties;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins(configProperties.getAllowedOrigins())
			.allowedMethods(HttpMethod.GET.name(),
				HttpMethod.POST.name(),
				HttpMethod.PUT.name(),
				HttpMethod.PATCH.name(),
				HttpMethod.DELETE.name(),
				HttpMethod.OPTIONS.name())
			.maxAge(3000)
			.allowCredentials(true);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(httpLoggingInterceptor).addPathPatterns("/**");
	}
}
