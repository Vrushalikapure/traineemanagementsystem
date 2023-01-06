package com.lexisnexis.tms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.GetMapping;

import com.lexisnexis.tms.service.EmailSenderMail;

import jakarta.mail.MessagingException;

@SpringBootApplication
public class TmsApplication {	
	
	@Autowired
	private EmailSenderMail emailsendermail;
	
	
	public static void main(String[] args) {
		SpringApplication.run(TmsApplication.class, args);
	}
	
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//	 return new BCryptPasswordEncoder();
//	}	
	
//	@EventListener(ApplicationReadyEvent.class)
//	public void triggerEmail() throws MessagingException {	
//		emailsendermail.sendEmail("shubhammishra11102000@gmail.com", "This is Body", "change addhar card address", "C:\\log4j\\counter-strike-global-offensive-cs-go-wallpaper-preview.jpg");
//	}
}
