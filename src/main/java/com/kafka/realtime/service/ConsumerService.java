package com.kafka.realtime.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
	
	 @Autowired
	    private MailService mailService;


	@KafkaListener(topics = "user-login-successfully", groupId = "logs-01")
	public void consume(String message) {
		
		System.out.println("My Message <##> "+message);
		
		try {
			
			String body ="<p>We&aposre delighted to welcome you back to RealTime Notifications! You&apos;ve successfully logged into our system.</p>\r\n"
					+ "<p>Stay connected and receive instant updates on important events.</p>\r\n"
					+ "<p>Thank you for choosing <strong>RealTime Notifications.</strong></p>\r\n"
					+ "<p>Best regards,</p>\r\n"
					+ "<p><br></p>";
			
			mailService.sendEmail(message, "Welcome Back to RealTime Notifications!", body);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@KafkaListener(topics = "account-creation", groupId = "logs-01")
	public void consumeAccountCreation(String message) {
		
		System.out.println("My Message <##> "+message);
		
		try {
			
			String body = "<p><strong>Congratulations </strong>on successfully registering with RealTime Notifications! "
					+ "Your username and password are as follows:</p>\r\n"
					+ "<p><strong>Username</strong>: Username</p>\r\n"
					+ "<p><strong>Password</strong>: Password</p>\r\n"
					+ "<p>With RealTime Notifications, you&apos;ll receive instant updates on important events. "
					+ "Log in now at http://localhost:30000 to get started.</p>\r\n"
					+ "<p>Welcome aboard!</p>\r\n"
					+ "<p>Best regards,</p>\r\n"
					+ "<p><br></p>\r\n"
					+ "<p>[Your Name]</p>\r\n"
					+ "<p>[Your Title]</p>\r\n"
					+ "<p>RealTime Notifications Team</p>";
			
			mailService.sendEmail(message, "Your RealTime Notifications Registration Details", body);
				
//				System.out.println("Email Sent Successfully");
//			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	
}
