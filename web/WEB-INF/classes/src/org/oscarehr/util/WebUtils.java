package org.oscarehr.util;

import javax.servlet.http.HttpServletRequest;

public class WebUtils
{
	/**
	 * This method is intended to be used to see if a check box was checked on a form submit
	 */
	public static boolean isChecked(HttpServletRequest request, String parameter)
	{
		String temp=request.getParameter(parameter);
		return(temp!=null && (temp.equalsIgnoreCase("on") || temp.equalsIgnoreCase("true") || temp.equalsIgnoreCase("checked")));
	}

}