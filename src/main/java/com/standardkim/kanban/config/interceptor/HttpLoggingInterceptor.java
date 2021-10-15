package com.standardkim.kanban.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpLoggingInterceptor implements AsyncHandlerInterceptor {
	private final Logger logger = LoggerFactory.getLogger(HttpLoggingInterceptor.class);
	private final ObjectMapper objectMapper;

	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(request.getRequestURI().equals("/auth/refresh-access-token")) {
			return;
		}
		
		if(!request.getMethod().equals("GET")) {
			final ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
			final ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) response;
			
			logger.info("[{} {}] RequestBody {} / ResponseBody {} {}",
				wrappedRequest.getMethod(),
				wrappedRequest.getRequestURI(), 
				objectMapper.readTree(wrappedRequest.getContentAsByteArray()),
				wrappedResponse.getStatus(),
				objectMapper.readTree(wrappedResponse.getContentAsByteArray()));
		}
    }
}
