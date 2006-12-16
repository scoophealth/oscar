package org.caisi.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import oscar.OscarProperties;

public class IsPropertiesOn{
	
	public static boolean propertiesOn(String proName){
		
		OscarProperties proper = OscarProperties.getInstance();

		if (proper.getProperty(proName, "").equalsIgnoreCase("yes")
			|| proper.getProperty(proName, "").equalsIgnoreCase(
					"true")
			|| proper.getProperty(proName, "").equalsIgnoreCase("on"))
			return true;
		else return false;
		
	}
	
	public static boolean isCaisiEnable(){
		return propertiesOn("caisi");
	}
	
	public static boolean isProgramEnable(){
		return propertiesOn("caisi");
	}
	
	public static boolean isTicklerPlusEnable(){
		return propertiesOn("ticklerplus");
	}
}
