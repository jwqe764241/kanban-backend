package com.standardkim.kanban.infra.mail.application;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.standardkim.kanban.infra.mail.dto.InviteProjectMailParam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InviteProjectMailSendService {
	private final JavaMailSender mailSender;

	private final Configuration configuration;

	@Value("${config.mail.fromAddress}")
	private String fromAddress;

	@Value("${config.invitation.acceptInvitationUrl}")
	private String acceptInvitationUrl;


	
	private String getTitle(InviteProjectMailParam inviteProjectMailParam) {
		String title = String.format("[Kanban] @%s has invited you to join the \"%s\" project", 
			inviteProjectMailParam.getInviterUsername(), inviteProjectMailParam.getProjectName());
		return title;
	}

	private String getBody(InviteProjectMailParam inviteProjectMailParam) {
		Map<String, Object> model = new HashMap<>();
		model.put("inviterUsername", inviteProjectMailParam.getInviterUsername());
		model.put("projectName", inviteProjectMailParam.getProjectName());
		model.put("url", String.format(acceptInvitationUrl, inviteProjectMailParam.getProjectId()));

		StringWriter writer = new StringWriter();
		try {
			configuration.getTemplate("mail/InviteProject.ftlh").process(model, writer);
		} catch (IOException | TemplateException e) {
			return "";
		}

		return writer.getBuffer().toString();
	}

	public void send(InviteProjectMailParam inviteProjectMailParam) {
		String title = getTitle(inviteProjectMailParam);
		String body = getBody(inviteProjectMailParam);

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setSubject(title);
				helper.setTo(inviteProjectMailParam.getInviteeMailAddress());
				helper.setText(body, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}
}
