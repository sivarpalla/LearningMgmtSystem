package com.mks.lms.controller;

import java.util.List;

import com.mks.lms.constants.LmsConstants;
import com.mks.lms.entity.UserEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.DefaultResponse;
import com.mks.lms.model.User;
import com.mks.lms.model.ValidateUser;
import com.mks.lms.model.request.ChangePasswordRequest;
import com.mks.lms.model.response.ChangePasswordResponse;
import com.mks.lms.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
	
	     @Autowired
	    private UserService userService;

	    @GetMapping("")
	    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required=false, defaultValue = "0") int page, @RequestParam(required=false, defaultValue = "10") int size) {
	    	List<User> users= userService.getAllUsers(page,size);
	         return ResponseEntity.ok().body(users);
	    }

	    @GetMapping("/{userId}")
	    public ResponseEntity<User> getUserById(@PathVariable int userId) {
	    	User user =  userService.getUserById(userId);
	    	return ResponseEntity.ok().body(user);
	    }
	    
	    @GetMapping("/search")
	    public ResponseEntity<List<User>> getUserByName(@RequestParam(value = "keyword") String keyword) {
	    	 List<User> users = userService.searchByKeyword(keyword);
	         
	         if (users.isEmpty()) {
	             return ResponseEntity.noContent().build();
	         } else {
	             return ResponseEntity.ok(users);
	         }
	    }

	    @PostMapping("/addUser")
	    public ResponseEntity<DefaultResponse> createUser(@RequestBody User user) {
	    	UserEntity userEntity= userService.createUser(user);
	    	DefaultResponse defaultResponse = DefaultResponse.builder().message("CREATED ACCOUNT:"+userEntity.getUserId()).status(LmsConstants.SUCCESS).build();
	        return ResponseEntity.status(HttpStatus.CREATED).body(defaultResponse);
	    }

	    @PutMapping("/{userId}")
	    public ResponseEntity<DefaultResponse> updateUser(@PathVariable Integer userId, @RequestBody User userDetails) {
	    	UserEntity userEntity= userService.updateUser(userId, userDetails);
	    	DefaultResponse defaultResponse = DefaultResponse.builder().message("UPDATE ACCOUNT:"+userEntity.getUserId()).status(LmsConstants.SUCCESS).build();
	        return ResponseEntity.status(HttpStatus.ACCEPTED).body(defaultResponse);
	    	
	    }

	    @DeleteMapping("/{userId}")
	    public ResponseEntity<DefaultResponse> deleteUser(@PathVariable Integer userId) {
	    	userService.deleteUser(userId);
	    	DefaultResponse defaultResponse = DefaultResponse.builder().message("DELETED ACCOUNT:"+userId).status(LmsConstants.SUCCESS).build();
	        return ResponseEntity.ok().body(defaultResponse);
	    }
	    
	    @PostMapping("/validate")
	    public ResponseEntity<DefaultResponse> validateUser(@RequestBody ValidateUser user) {
	        boolean status = userService.validateUser(user);
	        DefaultResponse defaultResponse ;
	        if(status)
	        	defaultResponse = DefaultResponse.builder().message("VALID").status(LmsConstants.SUCCESS).build();
	        else
	        	defaultResponse = DefaultResponse.builder().message("INVALID").status(LmsConstants.FAILED).build();
	        return ResponseEntity.status(HttpStatus.OK).body(defaultResponse);
	    }
	    
	    @PostMapping(value = "/import",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	    public ResponseEntity<DefaultResponse> importUsers(@RequestParam("file") MultipartFile excel) {
	    	 try{
	             String message="";
	             
	             if(excel == null || !LmsConstants.hasExcelFormat(excel)){
	                 log.error("Reading excel user data request, No content found");
	                 DefaultResponse defaultResponse = DefaultResponse.builder().message("File Not found !").status(LmsConstants.FAILED).build();
	                 return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(defaultResponse);
	             }
	             log.info("importUsers excel{} :",excel.getName());
	             userService.importUser(excel);
	             message = "Uploaded the file successfully:- " + excel.getOriginalFilename();
	             DefaultResponse defaultResponse = DefaultResponse.builder().message(message).status(LmsConstants.SUCCESS).build();
	             return ResponseEntity.status(HttpStatus.CREATED).body(defaultResponse);
	         } catch (ServiceException e) {
	             log.error("Exception while reading excel user data, message {}", e.getMessage());
	             throw e;
	         }
	    }
	@PatchMapping(value = "/changePassword")
		public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
			return  userService.changePassword(changePasswordRequest);
		}

	/*public void forgetPasswprd(){}*/

}
