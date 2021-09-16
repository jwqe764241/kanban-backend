package com.standardkim.kanban.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.standardkim.kanban.dto.MailDto.ProjectInvitationMailInfo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;

	private final Configuration configuration;

	@Value("${config.mail.fromAddress}")
	private String fromAddress;

	@Value("${config.invitation.acceptInvitationUrl}")
	private String acceptInvitationUrl;

	public String getProjectInvitationMessage(ProjectInvitationMailInfo info) throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<>();
		model.put("inviterLogin", info.getInviterLogin());
		model.put("projectName", info.getProjectName());
		model.put("url", String.format(acceptInvitationUrl, info.getProjectId()));

		StringWriter writer = new StringWriter();
		configuration.getTemplate("mail/test.ftlh").process(model, writer);

		return writer.getBuffer().toString();
	}

	public void sendProjectInvitationMessage(ProjectInvitationMailInfo info) {
		String title = String.format("[Kanban] @%s has invited you to join the \"%s\" project", 
			info.getInviterLogin(), info.getProjectName());

		String text;
		try {
			text = getProjectInvitationMessage(info);
		} catch (Exception e) {
			text = "";
		}

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setSubject(title);
				helper.setTo(info.getInviteeMailAddress());
				helper.setText(text, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}
}
