package com.mks.lms.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Getter
@Setter
public class JwtResponse {
	
	private String token;
	  private String type = "Bearer";
	  private Long id;
	  private String username;
	  private String email;
	  private List<String> roles;

}
