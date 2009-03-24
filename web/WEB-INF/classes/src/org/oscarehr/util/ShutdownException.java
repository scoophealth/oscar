package org.oscarehr.util;

public class ShutdownException extends Exception {
	public ShutdownException()
	{
		super("Shutdown received.");
	}
}
