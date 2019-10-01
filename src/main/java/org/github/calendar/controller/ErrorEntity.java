package org.github.calendar.controller;

import java.util.Date;

/**
 * DTO Which is sent when some error happens
 */
public class ErrorEntity {

	private Date timestamp;
	private int status;
	private String error;
	private String message;
	
	public ErrorEntity(int status, String error, String message) {
		timestamp = new Date();
		this.status = status;
		this.error = error;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
