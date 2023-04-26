package com.mks.lms.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateUser implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private  long userId;
	private  String userName;
    private String password;
    private String email;


}
