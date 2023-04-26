package com.mks.lms.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mks.lms.entity.LmsContentEntity;
import com.mks.lms.model.FileRequestDetails;
import com.mks.lms.model.response.ContentDetails;
import com.mks.lms.repository.LmsContentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileUploadImpl implements FileUpload {

	@Autowired
	LmsContentRepository repo;

	@Value("${com.mks.lms.filepath}")
	private String urlPath;

	@Override
	public void uploadFile(MultipartFile file, String dept, String account, String desc, String title, String category)
			throws IOException {
		String fileName = file.getOriginalFilename();
		System.out.println(fileName);
		String filePath = urlPath + dept + "\\" + account + "\\" + category;
		File f = new File(filePath);
		boolean status = false;

		if (!f.exists()) {
			status = f.mkdirs();
		}
		if (status) {
			log.info("folder created {}", filePath);
		} else {
			System.out.println("folders already been created...");
		}
		byte[] bytes = file.getBytes();
		Path path = Paths.get(filePath + "\\" + fileName);
		Files.write(path, bytes);

		List<LmsContentEntity> getFilePath = repo.findByDeptIdAndAccountIdAndCategoryAndFileName(dept, account,
				category, fileName);
		LmsContentEntity entity = LmsContentEntity.builder().build();
		if (!getFilePath.isEmpty()) {
			int id = getFilePath.get(0).getFileId();
			log.info("File Already Existed with id {}", id);
			entity = LmsContentEntity.builder().accountId(account).deptId(dept).category(category).fileId(id)
					.title(title).fileDesc(desc).fileName(file.getOriginalFilename())
					.filePath(filePath + "\\" + fileName).dateTime(Timestamp.from(Instant.now())).build();
		} else {
			entity = LmsContentEntity.builder().accountId(account).deptId(dept).category(category).title(title)
					.fileDesc(desc).fileName(file.getOriginalFilename()).filePath(filePath + "\\" + fileName)
					.dateTime(Timestamp.from(Instant.now())).build();
		}
		repo.save(entity);
	}

	@Override
	public String removeFile(FileRequestDetails fileDetails) {
		File file = new File(urlPath + fileDetails.getDept() + "\\" + fileDetails.getAccount() + "\\"
				+ fileDetails.getCategory() + "\\" + fileDetails.getFileName());
		if (file.delete()) {
			repo.deleteById(Integer.valueOf(fileDetails.getFileId()));
			log.info("File Deleted id {}", fileDetails.getFileId());
			return "File deleted successfully";
		}

		return "Failed";

	}
	
	public List<ContentDetails> getFileContentDetails(int pageNumber, int pageSize) {
		ContentDetails dtls = ContentDetails.builder().build();
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<LmsContentEntity> content = repo.findAll(pageable);
		Map<String, Map<String, Map<String, List<LmsContentEntity>>>> result = content.stream().collect(
				Collectors.groupingBy(LmsContentEntity::getAccountId, Collectors.groupingBy(LmsContentEntity::getDeptId,
						Collectors.groupingBy(LmsContentEntity::getCategory, Collectors.toList()))));

		List<ContentDetails> conntentDetails = result.entrySet().stream()
				.flatMap(e -> parseMapping(e.getKey(), e.getValue()).stream()).collect(Collectors.toList());
		return conntentDetails;
	}
	
	private List<ContentDetails> parseMapping(String dept, Map<String, Map<String, List<LmsContentEntity>>> content) {
		ContentDetails details = null;
		List<ContentDetails> contentList = new ArrayList<>();
		for (Entry<String, Map<String, List<LmsContentEntity>>> mappers : content.entrySet()) {
			Map<String, List<LmsContentEntity>> categorykeyValue = mappers.getValue();
			for (Entry<String, List<LmsContentEntity>> tempMap : categorykeyValue.entrySet()) {
				details = ContentDetails.builder().build();
				details.setDepartment(dept);
				details.setAccount(mappers.getKey());
				details.setCategory(tempMap.getKey());
				details.setLmscontent(tempMap.getValue());
				contentList.add(details);
			}
		}

		return contentList;
	}

	@Override
	public List<LmsContentEntity> findByFileName(String name) {
		return repo.findByFileName(name);
	}

}