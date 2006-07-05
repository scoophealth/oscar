package org.caisi.service;


import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ManagerFactory {
	
	public static ApplicationContext getAppContext(ServletContext sc) {
			return  WebApplicationContextUtils.getWebApplicationContext(sc);
		
	}

	public static InfirmBedProgramManager getBedProgramManager(ServletContext sc) {
		InfirmBedProgramManager bpm = (InfirmBedProgramManager) getAppContext(sc)
				.getBean("bedProgramManager");
		return bpm;
	}
}

