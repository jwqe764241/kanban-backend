package com.standardkim.kanban.domain.refreshtoken.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.standardkim.kanban.domain.model.BaseTimeEntity;
import com.standardkim.kanban.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
@AllArgsConstructor
public class RefreshToken extends BaseTimeEntity {
	@Id
	private Long userId;

	@MapsId
	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(nullable = false)
	private String token;

	public static RefreshToken from(User user, String token) {
		return RefreshToken.builder()
			.user(user)
			.token(token)
			.build();
	}

	public void updateToken(String token) {
		this.token = token;
	}

	public boolean isTokenMatched(String token) {
		return this.token.equals(token);
	}
}
