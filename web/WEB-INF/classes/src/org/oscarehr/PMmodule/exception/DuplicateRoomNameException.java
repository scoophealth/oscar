package org.oscarehr.PMmodule.exception;

public class DuplicateRoomNameException extends Exception {
	private static final long serialVersionUID = 1L;
	public DuplicateRoomNameException(String message) {
		super(message);
	}
}