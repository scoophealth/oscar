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
package org.oscarehr.exports.e2e;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.exports.PatientExport;
import org.oscarehr.exports.VelocityTemplate;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.VelocityUtils;

/**
 * Creates the data models of the E2E document in velocity format for E2E Template Exporting
 * 
 * @author Jeremy Ho
 */
public class E2EVelocityTemplate extends VelocityTemplate {
	private static final String E2E_VELOCITY_TEMPLATE_FILE = "/e2e/e2etemplate.vm";
	private static String template = null;
	private static E2EResources e2eResources = null;
	private static ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);
	private Clinic clinic = clinicDao.getClinic();

	public E2EVelocityTemplate() {
		loadTemplate();
		if(e2eResources == null) {
			e2eResources = new E2EResources();
		}
	}

	/**
	 * Loads the velocity template
	 */
	protected void loadTemplate() {
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

	/**
	 * Assembles the data model & predefined velocity template to yield an E2E document
	 * 
	 * @return String representing E2E export from template
	 */
	public String export(PatientExport p) {
		E2EPatientExport record = (E2EPatientExport) p;
		if(record.isLoaded() == false) {
			log.error("PatientExport object is not loaded with a patient");
			return "";
		}
		else if(template == null) {
			log.error("E2EVelocityTemplate is not loaded with a template");
			return "";
		}

		// Create Data Model
		context.put("patient", record);
		context.put("e2e", e2eResources);
		context.put("custodian", clinic);

		context.put("authorIdRoot", "DCCD2C68-389B-44C4-AD99-B8FB2DAD1493");
		context.put("custodianIdRoot", "7EEF0BCC-F03E-4742-A736-8BAC57180C5F");

		// Merge Template & Data Model
		String result = VelocityUtils.velocityEvaluate(context, template);

		// Check for Validity
		String demoNo = record.getDemographic().getDemographicNo().toString();
		if(result.contains("$")) {
			String msg = "[Demo: ".concat(demoNo).concat("] Export contains '$' - may contain errors");
			log.warn(msg);
			addExportLogEntry(msg);
		}
		E2EExportValidator v = new E2EExportValidator();
		if(!v.isValidXML(result)) {
			String msg = "[Demo: ".concat(demoNo).concat("] Export failed E2E XSD validation");
			log.error(msg);
			addExportLogEntry(msg);
			addExportLogEntry(v.getExportLog());
		}

		return result;
	}

	/**
	 * Contains E2E specific resources necessary for export generation
	 * 
	 * @author Jeremy Ho
	 */
	public static class E2EResources {
		private static final String E2E_VELOCITY_FORMCODE_FILE = "/e2e/e2eformcode.csv";
		private static Map<String,String> formCodes = null;

		public E2EResources() {
			loadFormCode();
		}

		/**
		 * Loads the formcode mapping
		 */
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

		/**
		 * Takes in a formcode string and returns the E2E Form Code result if available
		 * 
		 * @param rhs
		 * @return String if applicable, else null
		 */
		public String formCodeMap(String rhs) {
			if(formCodes.containsKey(rhs)) {
				return formCodes.get(rhs);
			}

			return null;
		}
	}
}
