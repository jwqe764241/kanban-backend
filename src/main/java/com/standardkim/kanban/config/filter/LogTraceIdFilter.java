package com.standardkim.kanban.config.filter;

import java.io.IOException;
import java.util.Random;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.standardkim.kanban.util.AlphanumericGenerator;

import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LogTraceIdFilter extends OncePerRequestFilter {
	private AlphanumericGenerator generator = new AlphanumericGenerator(new Random());

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try(CloseableThreadContext.Instance ctc = CloseableThreadContext.put("traceId", generator.generate(10))){
			filterChain.doFilter(request, response);
		} 
	}
}
