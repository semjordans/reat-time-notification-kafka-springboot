package com.kafka.realtime.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
	
@Service
public class KafkaPublisher {
	
	 @Autowired
	    private KafkaTemplate<String,Object> template;

	 public void sendMessageToTopic(String topic,String message) {
		 
//		 System.out.println("##########"+message);
		 
			ListenableFuture<SendResult<String, Object>> future = template.send(topic, message);
			
		    // Add a callback to handle the result of the send operation
	        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
	            @Override
	            public void onSuccess(SendResult<String, Object> result) {
	                // Message sent successfully
//	                System.out.println("Message sent successfully: " + result.getRecordMetadata());
	                System.out.println("Producer Record <> "+result.getProducerRecord());
	            }

	            @Override
	            public void onFailure(Throwable ex) {
	                // Message sending failed
	                System.err.println("Error sending message: " + ex.getMessage());
	            }
	        });
		}

	  

}
