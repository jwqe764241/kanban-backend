package com.standardkim.kanban.infra.mail;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.standardkim.kanban.infra.mail.MailDto.InviteProjectMailParam;

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

	private String getInviteProjectMailTitle(InviteProjectMailParam inviteProjectMailParam) {
		String title = String.format("[Kanban] @%s has invited you to join the \"%s\" project", 
			inviteProjectMailParam.getInviterLogin(), inviteProjectMailParam.getProjectName());
		return title;
	}

	private String getInviteProjectMailBody(InviteProjectMailParam inviteProjectMailParam) {
		Map<String, Object> model = new HashMap<>();
		model.put("inviterLogin", inviteProjectMailParam.getInviterLogin());
		model.put("projectName", inviteProjectMailParam.getProjectName());
		model.put("url", String.format(acceptInvitationUrl, inviteProjectMailParam.getProjectId()));

		StringWriter writer = new StringWriter();
		try {
			configuration.getTemplate("mail/test.ftlh").process(model, writer);
		} catch (IOException | TemplateException e) {
			return "";
		}

		return writer.getBuffer().toString();
	}

	public void sendInviteProjectMail(InviteProjectMailParam inviteProjectMailParam) {
		String title = getInviteProjectMailTitle(inviteProjectMailParam);
		String body = getInviteProjectMailBody(inviteProjectMailParam);

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
