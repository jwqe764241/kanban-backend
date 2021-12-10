package com.standardkim.kanban.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.entity.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Project extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@Column(length = 200)
	private String description;

	@ManyToOne(cascade = CascadeType.DETACH, optional = false)
	@JoinColumn(name = "register_user_id", referencedColumnName = "id")
	private User registerUser;

	@Builder.Default
	@OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<ProjectMember> members = new HashSet<>();

	@Builder.Default
	@OneToMany(mappedBy = "project")
	private Set<Kanban> kanbans = new HashSet<>();

	public static Project from(CreateProjectParam param, User registerUser) {
		return Project.builder()
				.name(param.getName())
				.description(param.getDescription())
				.registerUser(registerUser)
				.build();
	}

	public void addMember(User user, boolean isRegister) {
		ProjectMemberKey memberId = ProjectMemberKey.from(id, user.getId());
		ProjectMember member = ProjectMember.builder()
			.id(memberId)
			.project(this)
			.user(user)
			.isRegister(isRegister)
			.build();
		members.add(member);
	}
}
