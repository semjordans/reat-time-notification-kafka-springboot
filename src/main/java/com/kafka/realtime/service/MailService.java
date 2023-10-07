package com.kafka.realtime.service;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;

@Service
public class MailService {	

//	    public Boolean sendEmail(String toEmail, String subject, String messageText) {
//	    	
//	    	System.out.println("toEmail <#####> "+toEmail);
//	    	
//	        Email email = new SimpleEmail();
//	        email.setHostName("smtp.gmail.com"); // SMTP server for ucu.ac.ug
//	        email.setSmtpPort(587); // Port for SMTP (587 is common for TLS)
//	        email.setAuthenticator(new DefaultAuthenticator("senyondojordan@gmail.com", "Michealjj#96"));
//
//	        try {
//	            email.setFrom("jsenyondo@ucu.ac.ug");
//	            email.setSubject(subject);
//	            email.setMsg(messageText);
//	            email.addTo("senyondojordan@gmail.com");
//	            email.send();
//
//	            System.out.println("Email sent successfully to " + toEmail);
//	            
//	            return true;
//	        } catch (EmailException e) {
//	            e.printStackTrace();
//	            System.err.println("Error sending email: " + e.getMessage());
//	            return false;
//	        }
//	    }
	    
	    public static void sendEmail(String toEmail, String subject, String message) {
	    	
	    	 System.out.println("Test send " + toEmail);
	        try {
	            Email email = new HtmlEmail();
	            email.setHostName("smtp.gmail.com");
	            email.setSmtpPort(587);
	            email.setAuthenticator(new DefaultAuthenticator("senyondojordan@gmail.com", ""));
	            email.setStartTLSRequired(true);
	            email.setFrom("senyondojordan@gmail.com");
	            email.setSubject(subject);
	            email.setMsg(message);
	            email.addTo(toEmail);
	            
	            email.send();

	            System.out.println("Email sent successfully to " + toEmail);
	        } catch (EmailException e) {
	            throw new RuntimeException(e);
	            
//	            System.out.println("Nothing");
	        }
	    }

}
