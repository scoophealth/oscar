package org.oscarehr.PMmodule.exception;

public class DuplicateBedNameException extends Exception {
	private static final long serialVersionUID = 1L;
	public DuplicateBedNameException(String message) {
		super(message);
	}
}