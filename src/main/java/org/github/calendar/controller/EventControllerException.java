package org.github.calendar.controller;

/**
 * Custom Exception which is thrown during error checks in EventController
 */
public class EventControllerException extends Exception {

	private static final long serialVersionUID = -1570137805495853261L;

	public EventControllerException(String message) {
		super(message);
	}

}
