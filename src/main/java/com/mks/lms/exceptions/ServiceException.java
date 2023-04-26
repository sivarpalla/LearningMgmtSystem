package com.mks.lms.exceptions;

public class ServiceException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(Exception e) {
		super(e);
	}
	public ServiceException(String  msg) {
		super(msg);
	}
}
