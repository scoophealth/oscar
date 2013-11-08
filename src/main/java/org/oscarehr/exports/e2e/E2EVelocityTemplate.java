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
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
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
	private static String clinicUUID = null;
	private boolean validate = true;

	public E2EVelocityTemplate() {
		super();

		// Load E2E Resources
		if(e2eResources == null) {
			e2eResources = new E2EResources();
		}

		// Generate Clinic UUID
		if(clinicUUID == null) {
			try {
				String clinicData = clinic.getId().toString().concat(clinic.getClinicName()).concat(clinic.getClinicAddress());
				clinicUUID = UUID.nameUUIDFromBytes(clinicData.getBytes()).toString().toUpperCase();
			} catch (Exception e) {
				log.info("Failed to generate UUID from clinic data - using randomized UUID");
				clinicUUID = UUID.randomUUID().toString().toUpperCase();
			}
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

		// Generate GUIDs
		String randDocGUID = UUID.randomUUID().toString().toUpperCase();
		String authorGUID = null;
		try {
			String authorData = record.getAuthorId().concat(record.getProviderFirstName(record.getAuthorId())).concat(record.getProviderLastName(record.getAuthorId()));
			authorGUID = UUID.nameUUIDFromBytes(authorData.getBytes()).toString().toUpperCase();
			//authorGUID = UUIDConverter.getUUID(authorData).toString().toUpperCase();
		} catch (Exception e) {
			log.info("Failed to generate GUID from author name - using randomized GUID");
			authorGUID = UUID.randomUUID().toString().toUpperCase();
		}

		// Create Data Model
		context.put("patient", record);
		context.put("e2e", e2eResources);
		context.put("custodian", clinic);
		context.put("randDocGUID", randDocGUID);
		context.put("authorIdRoot", authorGUID);
		context.put("custodianIdRoot", clinicUUID);

		// Merge Template & Data Model
		String result = VelocityUtils.velocityEvaluate(context, template);

		// Check for Validity
		if(validate) {
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
		}

		return result;
	}

	/**
	 * Disables validation during export process
	 */
	public void disableValidation() {
		this.validate = false;
	}

	/**
	 * Contains E2E specific resources necessary for export generation
	 * 
	 * @author Jeremy Ho
	 */
	public static class E2EResources {
		private static final String E2E_VELOCITY_FORMCODE_FILE = "/e2e/e2eformcode.csv";
		private static final String E2E_VELOCITY_MEASUREMENTCODE_FILE = "/e2e/e2emeasurementcode.csv";
		private static final String E2E_VELOCITY_PRRCODE_FILE = "/e2e/e2eprrcode.csv";
		private static final String OSCAR_PREVENTIONITEMS_FILE = "/oscar/oscarPrevention/PreventionItems.xml";
		private static Map<String,String> formCodes = null;
		private static Map<String,String> measurementCodes = null;
		private static Map<String,String> measurementUnits = null;
		private static Map<String,String> prrCodes = null;
		private static Map<String,String> preventionTypeCodes = null;

		public E2EResources() {
			loadFormCode();
			loadMeasurementCode();
			loadPRRCode();
			loadPreventionItems();
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
		 * Loads the measurementcode and unit mapping
		 */
		private void loadMeasurementCode() {
			if(measurementCodes == null || measurementUnits == null) {
				InputStream is = null;
				try {
					is = E2EVelocityTemplate.class.getResourceAsStream(E2E_VELOCITY_MEASUREMENTCODE_FILE);
					BufferedReader br = new BufferedReader(new InputStreamReader(is));

					measurementCodes = new HashMap<String,String>();
					measurementUnits = new HashMap<String,String>();
					String line = null;
					String[] content = null;
					while((line = br.readLine()) != null) {
						content = line.split("\\t");
						measurementCodes.put(content[0],content[1]);
						measurementUnits.put(content[0],content[2]);
					}

					log.info("Loaded E2E Measurement Code/Unit Mapping");
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
		 * Loads the personal relationship role code mapping
		 */
		private void loadPRRCode() {
			if(prrCodes == null) {
				InputStream is = null;
				try {
					is = E2EVelocityTemplate.class.getResourceAsStream(E2E_VELOCITY_PRRCODE_FILE);
					BufferedReader br = new BufferedReader(new InputStreamReader(is));

					prrCodes = new HashMap<String,String>();
					String line = null;
					String[] content = null;
					while((line = br.readLine()) != null) {
						content = line.split("\\t");
						prrCodes.put(content[1].toLowerCase(),content[0]);
					}

					log.info("Loaded E2E Personal Relationship Role Code Mapping");
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
		 * Loads the preventionitems name to atc mapping
		 */
		private void loadPreventionItems() {
			if(preventionTypeCodes == null) {
				InputStream is = null;
				try {
					is = E2EVelocityTemplate.class.getResourceAsStream(OSCAR_PREVENTIONITEMS_FILE);

					preventionTypeCodes = new HashMap<String,String>();
					SAXBuilder parser = new SAXBuilder();
					Document doc = parser.build(is);
					Element root = doc.getRootElement();
					@SuppressWarnings("unchecked")
					List<Element> items = root.getChildren("item");
					for(Element e : items) {
						Attribute name = e.getAttribute("name");
						Attribute atc = e.getAttribute("atc");
						if(atc != null && atc.getValue().length() > 0) {
							preventionTypeCodes.put(name.getValue(), atc.getValue());
						}
					}

					log.info("Loaded OSCAR Prevention Items Code Mapping");
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

		/**
		 * Takes in a measurementcode string and returns the E2E Measurement Unit result if available
		 * 
		 * @param rhs
		 * @return String if applicable, else null
		 */
		public String measurementUnitMap(String rhs) {
			if(measurementUnits.containsKey(rhs) && !measurementUnits.get(rhs).equals("UNK")) {
				return measurementUnits.get(rhs);
			}

			// Blood Pressure split case
			if(rhs.equals("SYST") || rhs.equals("DIAS")) return "mm[Hg]";

			return null;
		}

		/**
		 * Takes in a measurementcode string and returns the E2E Measurement Code result if available
		 * 
		 * @param rhs
		 * @return String if applicable, else null
		 */
		public String measurementCodeMap(String rhs) {
			if(measurementCodes.containsKey(rhs)) {
				return measurementCodes.get(rhs);
			}

			// Blood Pressure split case
			if(rhs.equals("SYST")) return "8480-6";
			if(rhs.equals("DIAS")) return "8462-4";

			return null;
		}

		/**
		 * Takes in a personal relationship role string and returns the E2E PRR Code result if available
		 * 
		 * @param rhs
		 * @return String if applicable, else null
		 */
		public String prrCodeMap(String rhs) {
			if(prrCodes.containsKey(rhs.toLowerCase())) {
				return prrCodes.get(rhs.toLowerCase());
			}

			return null;
		}

		/**
		 * Takes in a prevention type string and returns the ATC result if available
		 * 
		 * @param rhs
		 * @return String if applicable, else null
		 */
		public String preventionTypeMap(String rhs) {
			if(preventionTypeCodes.containsKey(rhs)) {
				return preventionTypeCodes.get(rhs);
			}

			return null;
		}
	}
}
