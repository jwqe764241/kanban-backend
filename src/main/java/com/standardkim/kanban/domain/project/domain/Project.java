package com.standardkim.kanban.domain.project.domain;

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

import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.model.BaseTimeEntity;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectRole;
import com.standardkim.kanban.domain.user.domain.User;

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

	public static Project of(String name, String description, User registerUser) {
		return Project.builder()
			.name(name)
			.description(description)
			.registerUser(registerUser)
			.build();
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void updateDescription(String description) {
		this.description = description;
	}

	public ProjectMember addMember(User user, ProjectRole projectRole) {
		ProjectMember member = ProjectMember.of(this, user, projectRole);
		members.add(member);
		return member;
	}
}
