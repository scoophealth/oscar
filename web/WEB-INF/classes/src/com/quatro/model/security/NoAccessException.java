package com.quatro.model.security;

public class NoAccessException extends Exception{
	private static final long serialVersionUID = -4444364883235024698L;

	public NoAccessException(String s){
		super(s);
	}
	public NoAccessException()
	{
		super("Access to the requested page has been denied due to insufficient privileges.");
	}
}
