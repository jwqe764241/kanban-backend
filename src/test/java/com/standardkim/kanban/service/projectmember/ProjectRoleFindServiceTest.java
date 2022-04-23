package com.standardkim.kanban.service.projectmember;

import com.standardkim.kanban.domain.projectmember.application.ProjectRoleFindService;
import com.standardkim.kanban.domain.projectmember.dao.ProjectRoleRepository;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;
import com.standardkim.kanban.domain.projectmember.exception.ProjectRoleNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class ProjectRoleFindServiceTest {
	@Mock
	ProjectRoleRepository projectRoleRepository;

	@InjectMocks
	ProjectRoleFindService projectRoleFindService;
	
	@Test
	void findByName_ProjectRoleIsNotExist_ThrowProjectRoleNotFoundException() {
		given(projectRoleRepository.findByName(any(ProjectRoleName.class))).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectRoleFindService.findByName(ProjectRoleName.ADMIN);
		}).isInstanceOf(ProjectRoleNotFoundException.class);
	}
}
