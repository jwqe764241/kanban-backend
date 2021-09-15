package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.MailDto.ProjectInvitationMailInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;

	@Value("${config.mail.fromAddress}")
	private String fromAddress;

	@Value("${config.invitation.acceptInvitationUrl}")
	private String acceptInvitationUrl;

	public void send(SimpleMailMessage message) {
        message.setFrom(fromAddress);

		mailSender.send(message);
	}

	public void sendProjectInvitationMessage(ProjectInvitationMailInfo info) {
		String title = String.format("[Kanban] @%s has invited you to join the \"%s\" project", 
			info.getInviterLogin(), info.getProjectName());

		StringBuilder builder = new StringBuilder();
			builder.append(String.format("@%s has invited you to join the \"%s\" project\n", 
				info.getInviterLogin(), info.getProjectName()));
			builder.append(String.format(acceptInvitationUrl, info.getProjectId()));

		SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(info.getInviteeMailAddress());
			message.setSubject(title);
			message.setText(builder.toString());
		send(message);
	}
}
