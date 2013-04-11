/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */
package oscar.oscarDemographic.pageUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.VelocityUtils;

/**
 * @author Jeremy Ho
 * This class is creates the data models of the E2E document in velocity format for E2E Template Exporting
 */

public class E2EVelocityTemplate {
	private static Logger log = MiscUtils.getLogger();
	private static ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
	private static final String E2E_VELOCITY_TEMPLATE_FILE = "/e2etemplate.vm";
	private static final String E2E_VELOCITY_FORMCODE_FILE = "/e2eformcode.csv";
	private static String template = null;
	protected static Map<String,String> formCodes = null;
	private Clinic clinic = clinicDao.getClinic();
	private VelocityContext context;
	
	public E2EVelocityTemplate() {
		loadTemplate();
		loadFormCode();
	}
	
	// Loads the velocity template
	private void loadTemplate() {
		if(template == null) {
			InputStream is = null;
			try {
				is = E2EVelocityTemplate.class.getResourceAsStream(E2E_VELOCITY_TEMPLATE_FILE);
				template = IOUtils.toString(is);
				log.info("Loaded E2E Velocity Template");
			} catch (Exception e) {
		        log.error(e.getMessage(), e);
		    } finally {
		    	try {
		    		is.close();
		    	} catch (Exception e) {
		    		log.error(e.getMessage(), e);
		    	}
		    }
		}
	}
	
	// Loads the formcode mapping
	private void loadFormCode() {
		if(formCodes == null) {
			InputStream is = null;
			try {
				is = E2EVelocityTemplate.class.getResourceAsStream(E2E_VELOCITY_FORMCODE_FILE);
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
				formCodes = new HashMap<String,String>();
				String line = null;
				String[] content = null;
				while((line = br.readLine()) != null) {
					content = line.split("\\t");
					formCodes.put(content[0],content[1]);
				}
				
				log.info("Loaded E2E Form Code Mapping");
			} catch (Exception e) {
		        log.error(e.getMessage(), e);
		    } finally {
		    	try {
		    		is.close();
		    	} catch (Exception e) {
		    		log.error(e.getMessage(), e);
		    	}
		    }
		}
	}
	
	// Assembles the data model & predefined velocity template to yield an E2E document
	public String export(PatientExport record) {
		E2EResources e2eResources = new E2EResources();
		String eol = System.getProperty("line.separator");
		
		// Create Data Model
		context = VelocityUtils.createVelocityContextWithTools();
		context.put("patient", record);
		context.put("e2e", e2eResources);
		context.put("custodian", clinic);
		context.put("eol", eol);
		
		context.put("authorIdRoot", "DCCD2C68-389B-44C4-AD99-B8FB2DAD1493");
		context.put("custodianIdRoot", "7EEF0BCC-F03E-4742-A736-8BAC57180C5F");
		
		// Merge Template & Data Model
		String result = VelocityUtils.velocityEvaluate(context, template);
		
		// Check for Validity
		if(result.contains("$")) {
			log.error("[Demo: "+record.getDemographicNo()+"] Export contains '$' - may contain errors");
		}
		if(!E2EExportValidator.isValidXML(result)) {
			log.error("[Demo: "+record.getDemographicNo()+"] Export failed E2E XSD validation");
		}
		
		return result;
	}
	
	public class E2EResources {
		public String formCodeMap(String rhs) {
			if(formCodes.containsKey(rhs)) {
				return formCodes.get(rhs);
			}
			
			return null;
		}
	}
}
