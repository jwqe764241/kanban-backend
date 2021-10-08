package com.standardkim.kanban.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.standardkim.kanban.dto.MailDto.InviteProjectMailParam;

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

	public String getProjectInvitationMessage(InviteProjectMailParam inviteProjectParam) throws IOException, TemplateException {
		Map<String, Object> model = new HashMap<>();
		model.put("inviterLogin", inviteProjectParam.getInviterLogin());
		model.put("projectName", inviteProjectParam.getProjectName());
		model.put("url", String.format(acceptInvitationUrl, inviteProjectParam.getProjectId()));

		StringWriter writer = new StringWriter();
		configuration.getTemplate("mail/test.ftlh").process(model, writer);

		return writer.getBuffer().toString();
	}

	public void sendProjectInvitationMessage(InviteProjectMailParam inviteProjectParam) {
		String title = String.format("[Kanban] @%s has invited you to join the \"%s\" project", 
			inviteProjectParam.getInviterLogin(), inviteProjectParam.getProjectName());

		String text;
		try {
			text = getProjectInvitationMessage(inviteProjectParam);
		} catch (Exception e) {
			text = "";
		}

		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setSubject(title);
				helper.setTo(inviteProjectParam.getInviteeMailAddress());
				helper.setText(text, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}
}
