package com.standardkim.kanban.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.standardkim.kanban.dto.UserDto.CreateUserParam;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "login" }) })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(length = 50, nullable = false)
	private String login;

	@Column(length = 200, nullable = false)
	private String password;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 320, nullable = false)
	private String email;

	@CreationTimestamp
	@Column(name = "register_date", nullable = false)
	private LocalDateTime registerDate;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@OrderBy("register_date desc")
	private Set<ProjectMember> projects;

	public static User from(CreateUserParam param, PasswordEncoder passwordEncoder) {
		return User.builder()
			.login(param.getLogin())
			.password(passwordEncoder.encode(param.getPassword()))
			.name(param.getName())
			.email(param.getEmail())
			.build();
	}
}
