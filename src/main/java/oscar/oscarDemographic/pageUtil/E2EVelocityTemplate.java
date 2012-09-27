/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.oscarDemographic.pageUtil;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.oscarehr.common.model.Patient;
import org.oscarehr.util.MiscUtils;

/**
 * @author Jeremy Ho
 * This class is meant to create a data model of the E2E document in velocity format for E2E Template Exporting
 */

public class E2EVelocityTemplate {
	private static final Logger logger = MiscUtils.getLogger();
	private VelocityContext context;
	
	public E2EVelocityTemplate() {
	}
	
	private String dateString() {
		Date now = new Date();
		SimpleDateFormat dateformatYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
		StringBuilder result = new StringBuilder(dateformatYYYYMMDD.format(now));
		return result.toString();
	}
	
	private void loadPatient(Patient record) {
		String docId = record.getDemographicNo().toString();
		String hin = record.getHin();
		String gender = record.getGender();
		String birthDate = record.getYearOfBirth() + record.getMonthOfBirth() + record.getDateOfBirth();
		String currentDate = dateString();
		
		String genderDesc;
		if(gender.equals("M")) genderDesc = "Male";
		else genderDesc = "Female";
		
		context = new VelocityContext();
		
		context.put("docId", docId);
		context.put("hin", hin);
		context.put("gender", gender);
		context.put("birthDate", birthDate);
		context.put("currentDate", currentDate);
		context.put("genderDesc", genderDesc);
	}
	
	// Assembles the data model & predefined velocity template to yield an E2E document
	public String export(Patient record) throws Exception {
		// Initialize Velocity engine
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
		ve.setProperty("runtime.log.logsystem.log4j.logger", logger.getName());
		
		//ve.setApplicationAttribute("javax.servlet.ServletContext", servletContext);
		/*ve.setProperty("resource.loader", "webapp");
		ve.setProperty("webapp.resource.loader.class","org.apache.velocity.tools.view.servlet.WebappLoader");
		ve.setProperty("webapp.resource.loader.path","/WEB-INF/templates/");*/
		ve.setProperty("resource.loader","file");
		ve.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		ve.setProperty("file.resource.loader.path","/var/lib/tomcat6/webapps/oscar12/WEB-INF/velocitytemplates");
		ve.setProperty("file.resource.loader.cache","true");
		//ve.init();
		
		// Create Data Model
		loadPatient(record);
		
		/*
		BufferedWriter out = new BufferedWriter(new FileWriter("${WORKING_ROOT}/whereisthisfile.txt"));
	    out.write("aString");
	    out.close();
		*/
		
		// Import Template
		Template t = ve.getTemplate("e2etemplate.vm");
		
		// Merge Template & Data Model
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		
		return writer.toString();
	}
}
