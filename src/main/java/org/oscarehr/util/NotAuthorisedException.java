package org.oscarehr.util;

/**
 * This is meant to be a generic exception that's thrown when ever some one
 * requests something they are not authorised to do.
 */
public class NotAuthorisedException extends Exception
{
	public NotAuthorisedException(String s)
	{
		super(s);
	}
}