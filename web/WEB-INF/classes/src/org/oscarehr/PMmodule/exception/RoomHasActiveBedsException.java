package org.oscarehr.PMmodule.exception;

public class RoomHasActiveBedsException extends Exception {

    private static final long serialVersionUID = 1L;

	public RoomHasActiveBedsException(String message) {
	    super(message);
    }

}
