package com.kafka.realtime.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {
	
	public NewTopic createTopic() {
		return new NewTopic("user-login-successfully", 5 , (short) 1);
	}
	
	public NewTopic createExceptionTopic() {
		return new NewTopic("user-failed-login", 5 , (short) 1);
	}
	
	public NewTopic createAccountTopic() {
		return new NewTopic("account-creation", 5 , (short) 1);
	}
	
	
	public NewTopic createDefaultExceptionTopic() {
		return new NewTopic("message-sent", 5 , (short) 1);
	}
	


}
