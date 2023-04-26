package com.mks.lms.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mks.lms.entity.LmsContentEntity;
import com.mks.lms.model.FileRequestDetails;

public interface FileUpload {

	void uploadFile(MultipartFile file, String dept, String account,String desc,String title,String category) throws IOException;
	String removeFile(FileRequestDetails fileDetails);
	List<LmsContentEntity> findByFileName(String name);


}
