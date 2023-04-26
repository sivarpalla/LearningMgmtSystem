package com.mks.lms.controller;


import com.mks.lms.entity.UserEntity;
import com.mks.lms.jwt.JwtUtils;
import com.mks.lms.model.request.LoginRequest;
import com.mks.lms.model.request.SignupRequest;
import com.mks.lms.model.response.JwtResponse;
import com.mks.lms.model.response.MessageResponse;
import com.mks.lms.service.UserDetailsImpl;
import com.mks.lms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
		  AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;


	  @Autowired
	  JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	    String jwt = jwtUtils.generateJwtToken(authentication);

	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    List<String> roles = userDetails.getAuthorities().stream()
	        .map(item -> item.getAuthority())
	        .collect(Collectors.toList());

	    JwtResponse jwtResponse = JwtResponse.builder().token(jwt).type("Bearer").username(userDetails.getUsername()).email(userDetails.getUsername()).roles(roles).build();
	    return ResponseEntity.ok(jwtResponse);

	}

	@PostMapping("/signup")
	  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {


	    if (userService.existsByEmail(signUpRequest.getEmail())) {
	      return ResponseEntity
	          .badRequest()
	          .body(new MessageResponse("Error: Email is already in use!"));
	    }

		UserEntity userEntity= userService.createUser(signUpRequest);
	    return ResponseEntity.ok(new MessageResponse("User registered successfully!"+userEntity.getUserName()));
	  }

}
