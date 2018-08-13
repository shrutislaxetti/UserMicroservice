package com.bridgelabz.usermicroservice.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.bridgelabz.usermicroservice.model.Mail;

@Component
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendMail(Mail mail) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setSubject(mail.getSubject());
		helper.setTo(mail.getTo());
		helper.setText(mail.getBody());

		javaMailSender.send(message);

	}
}