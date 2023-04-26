package com.mks.lms.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mks.lms.entity.LmsContentEntity;
import com.mks.lms.exceptions.ServiceException;
import com.mks.lms.model.FileRequestDetails;
import com.mks.lms.model.response.ContentDetails;
import com.mks.lms.repository.LmsContentRepository;
import com.mks.lms.service.FileUploadImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/lms")
@Slf4j
public class FileUploadController {

	@Autowired
	LmsContentRepository repo;

	@Autowired
	FileUploadImpl upload;

	@PostMapping("fileUpload")
	public ResponseEntity<String> fileUpload(@RequestParam("doc") MultipartFile file, @RequestParam("dept") String dept,
			@RequestParam("account") String account, @RequestParam("category") String category,
			@RequestParam("filedesc") String desc, @RequestParam("title") String title)
			throws IllegalStateException, IOException {
		upload.uploadFile(file, dept, account, desc, title, category);
		return ResponseEntity.ok().body("File upload Successfully");
	}

	@PostMapping("removeFile")
	public String removeFile(@RequestBody FileRequestDetails details) throws IOException {
		return upload.removeFile(details);
	}

	@GetMapping("getContentDetails")
	public ResponseEntity<List<ContentDetails>> getContentDetails(@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize) {
		List<ContentDetails> conntentDetails = upload.getFileContentDetails(pageNumber, pageSize);
		return ResponseEntity.ok().body(conntentDetails);

	}

	@GetMapping("searchfile/{fileName}")
	public ResponseEntity<List<LmsContentEntity>> findByFileName(@PathVariable("fileName") String name) {
		if (StringUtils.isBlank(name)) {
			log.error("LmsContentEntity get file name request, No content found");
			return new ResponseEntity("FILE NOT FOUND", HttpStatus.NO_CONTENT);
		}
		try {
			return Optional.ofNullable(upload.findByFileName(name)).map(accounts -> ResponseEntity.ok().body(accounts))
					.orElseGet(() -> ResponseEntity.notFound().build());
		} catch (ServiceException e) {
			log.error("Exception while getting Account name  {} message {}", name, e.getMessage());
			return new ResponseEntity("FILE NOT FOUND", HttpStatus.NO_CONTENT);

		}
	}

}
