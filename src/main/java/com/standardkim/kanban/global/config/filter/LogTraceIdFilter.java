package com.standardkim.kanban.global.config.filter;

import java.io.IOException;
import java.util.Random;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.global.util.AlphanumericGenerator;

import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class LogTraceIdFilter extends OncePerRequestFilter {
	private AlphanumericGenerator generator = new AlphanumericGenerator(new Random());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

		try(CloseableThreadContext.Instance ctc = CloseableThreadContext.put("traceId", generator.generate(10))){
			filterChain.doFilter(wrappedRequest, wrappedResponse);
		}

		//Must to call this!
		wrappedResponse.copyBodyToResponse();
	}
}
