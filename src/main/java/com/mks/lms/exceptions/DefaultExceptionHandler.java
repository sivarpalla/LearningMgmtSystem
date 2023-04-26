package com.mks.lms.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.mks.lms.constants.LmsConstants;
import com.mks.lms.model.DefaultResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<DefaultResponse> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
		log.error("Invalid request parameters");
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    DefaultResponse defaultResponse = DefaultResponse.builder().message(errors+"").status(LmsConstants.ERROR).build();
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(defaultResponse);
	}
	
	@ExceptionHandler({ServiceException.class})
	public ResponseEntity<DefaultResponse> handleServiceException(ServiceException ex) {
		DefaultResponse defaultResponse = DefaultResponse.builder().message(ex.getMessage()).status(LmsConstants.ERROR).build();
		log.error("handleServiceException message: {} and stack {}",ex.getMessage(),ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(defaultResponse);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<DefaultResponse> handleException(Exception ex) {
		DefaultResponse defaultResponse = DefaultResponse.builder().message(ex.getMessage()).status(LmsConstants.ERROR).build();
		log.error("handleException message: {} and stack {}",ex.getMessage(),ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(defaultResponse);
	}
}
