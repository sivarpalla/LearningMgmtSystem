package com.mks.lms.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FileRequestDetails {

	
	private String fileId;
	private String fileName;
	private String dept;
	private String account;
	private String category;
}
