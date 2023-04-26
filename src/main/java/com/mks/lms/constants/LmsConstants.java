package com.mks.lms.constants;

import org.springframework.web.multipart.MultipartFile;

public class LmsConstants {

	public static final String FILE_PATH="com.mks.lms.filepath";
	public static final String ERROR="ERROR";
	public static final String SUCCESS="SUCCESS";
	public static final String FAILED="FAILED";
	public static final String DEFAULT_PASSWORD = "password";
	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String INVALID = "Invalid Account or Department";
	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}
}
