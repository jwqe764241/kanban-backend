package com.standardkim.kanban.global.config;

import com.standardkim.kanban.global.config.filter.JwtAuthenticationFilter;
import com.standardkim.kanban.global.config.handler.AuthenticationFailedHandler;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	private final AuthenticationFailedHandler authenticationFailedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/users", "/auth/login", "/auth/logout").permitAll()
				.antMatchers(HttpMethod.GET, "/auth/access-token", "/auth/ws-token", "/kanban-event/**").permitAll()
				.anyRequest().authenticated()
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.exceptionHandling()
					.authenticationEntryPoint(authenticationFailedHandler);

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
