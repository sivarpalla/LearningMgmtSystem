package com.mks.lms.model.request;

import com.mks.lms.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignupRequest {

	@NotBlank
	@Size(min = 5, max = 10)
	private  String userName;
	@NotBlank
	@Email
	@Size(max = 50)
	private String email;
	@NotBlank
	@Size(min = 5, max = 10)
	private String password;
	private String department ;
	private String accountType;
	private  String role= UserRole.USER.name();
	private boolean enabled;
	
	

}
