package com.kafka.realtime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kafka.realtime.service.KafkaPublisher;

@Controller
@RequestMapping("/kafka-api")
public class EventController {
	
	  @Autowired
	    private KafkaPublisher publisher;

	    @GetMapping("/publish/{message}")
	    public ResponseEntity<?> publishMessage(@PathVariable String message) {
	    	
	        try {
	                publisher.sendMessageToTopic("user-login-successfully",message);
	            
	            return ResponseEntity.ok("message published successfully ..");
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .build();
	        }
	    }
	    
	    
}
