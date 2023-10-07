package com.kafka.realtime.controller;

import org.apache.kafka.common.quota.ClientQuotaAlteration.Op;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kafka.realtime.exception.AppException;
import com.kafka.realtime.model.Role;
import com.kafka.realtime.model.RoleName;
import com.kafka.realtime.model.User;
import com.kafka.realtime.payload.ApiResponse;
import com.kafka.realtime.payload.JwtAuthenticationResponse;
import com.kafka.realtime.payload.LoginRequest;
import com.kafka.realtime.payload.SignUpRequest;
import com.kafka.realtime.repository.RoleRepository;
import com.kafka.realtime.repository.UserRepository;
import com.kafka.realtime.security.JwtTokenProvider;
import com.kafka.realtime.service.KafkaPublisher;
import com.kafka.realtime.service.MailService;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;
    
    @Autowired
    private KafkaPublisher publisher;
    
    @Autowired
    private MailService mailService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    	
    	Boolean findByEmail = userRepository.existsByEmail(loginRequest.getUsernameOrEmail());
    	Boolean findByUsername = userRepository.existsByUsername(loginRequest.getUsernameOrEmail());
    	
    	if(findByEmail || findByUsername) {
    		
    		Optional<User> user = userRepository.findByEmail(loginRequest.getUsernameOrEmail());
    		
    		 Authentication authentication = authenticationManager.authenticate(
    	                new UsernamePasswordAuthenticationToken(
    	                        loginRequest.getUsernameOrEmail(),
    	                        loginRequest.getPassword()
    	                )
    	        );

    	        SecurityContextHolder.getContext().setAuthentication(authentication);
    	        String jwt = tokenProvider.generateToken(authentication);
    	        
    	        System.out.println("Token <>"+jwt);
    	        
    	        if(user.isPresent()) {
    	        	
    	        	try {
                		publisher.sendMessageToTopic("user-login-successfully",user.get().getEmail());
                		
        			} catch (Exception e) {
        				// TODO: handle exception
        			}
    	        }
    	        
    	    	
            	
    	        
    	        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    		
    	}
    	 
    	return new ResponseEntity(new ApiResponse(false, "Not in the system"),
                HttpStatus.BAD_REQUEST);
       
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);
        
        if(result != null) {
        	
        	try {
        		publisher.sendMessageToTopic("account-creation",signUpRequest.getEmail());
        		
			} catch (Exception e) {
				// TODO: handle exception
			}
        	
        	
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
