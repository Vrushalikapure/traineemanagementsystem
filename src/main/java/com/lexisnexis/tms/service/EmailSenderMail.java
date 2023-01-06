package com.lexisnexis.tms.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSenderMail {

//	@Autowired
//	private JavaMailSender javasender;
//	public void sendEmail(String toEmail,String subject,String body,String attchment) throws MessagingException {
//
//		MimeMessage mimemessage=javasender.createMimeMessage();
//		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimemessage,true);
//
//		mimeMessageHelper.setFrom("Shubham.Mishra@lexisnexisrisk.com");
//		mimeMessageHelper.setTo(toEmail);
//		mimeMessageHelper.setText(body);
//		mimeMessageHelper.setSubject(subject);
//
//		FileSystemResource fileSystem=new FileSystemResource(new File(attchment));
//
//		mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);
//
//		javasender.send(mimemessage);
//
//		System.out.println("Mail with attchment sent successfully");
//	}
}
