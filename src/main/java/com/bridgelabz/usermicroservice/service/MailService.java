package com.bridgelabz.usermicroservice.service;

import javax.mail.MessagingException;

import com.bridgelabz.usermicroservice.model.Mail;

public interface MailService {
	
	 void sendMail(Mail mail) throws MessagingException;
}