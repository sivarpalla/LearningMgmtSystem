package com.mks.lms.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "mks_lms_content", schema = "lms")
public class LmsContentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="fileId")
	private Integer fileId;
	
	@Column(name="fileName")
	private String fileName;
	
	@Column(name="title")
	private String title;
	
	@Column(name="fileDescription")
	private String fileDesc;
	
	@Column(name="fileUrl")
	private String filePath;
	
	@Column(name="department")
	private String deptId;
	
	@Column(name="account")
	private String accountId;
	
	@Column(name="category")
	private String category;
	
	@Column(name="createdDateTime")
	private Timestamp dateTime;
	
}
