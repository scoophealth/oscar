/**
 * Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
 * Department of Computer Science
 * LeadLab
 * University of Victoria
 * Victoria, Canada
 */
package org.oscarehr.e2e.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.marc.everest.datatypes.ADXP;
import org.marc.everest.datatypes.AddressPartType;
import org.marc.everest.datatypes.ENXP;
import org.marc.everest.datatypes.EntityNamePartType;
import org.marc.everest.datatypes.EntityNameUse;
import org.marc.everest.datatypes.II;
import org.marc.everest.datatypes.PN;
import org.marc.everest.datatypes.TEL;
import org.marc.everest.datatypes.TS;
import org.marc.everest.datatypes.TelecommunicationsAddressUse;
import org.marc.everest.datatypes.generic.SET;
import org.marc.everest.formatters.interfaces.IFormatterGraphResult;
import org.marc.everest.formatters.xml.datatypes.r1.DatatypeFormatter;
import org.marc.everest.formatters.xml.datatypes.r1.R1FormatterCompatibilityMode;
import org.marc.everest.formatters.xml.its1.XmlIts1Formatter;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.ClinicalDocument;
import org.marc.everest.rmim.uv.cdar2.pocd_mt000040uv.LanguageCommunication;
import org.marc.everest.xml.XMLStateStreamWriter;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.e2e.constant.Constants;
import org.oscarehr.e2e.constant.Constants.IdPrefixes;
import org.oscarehr.e2e.constant.Constants.TelecomType;
import org.oscarehr.e2e.constant.Mappings;
import org.oscarehr.e2e.extension.ObservationWithConfidentialityCode;
import org.oscarehr.util.SpringUtils;

public class EverestUtils {
	private static Logger logger = Logger.getLogger(EverestUtils.class.getName());
	private static final String OSCAR_PREVENTIONITEMS_FILE = "/oscar/oscarPrevention/PreventionItems.xml";
	protected static Map<String, String> preventionTypeCodes = null;

	public static final Map<String, Demographic> demographicCache = new HashMap<String, Demographic>();
	public static final Map<String, ProviderData> providerCache = new HashMap<String, ProviderData>();
	public static final Map<String, String> icd9DescriptionCache = new HashMap<String, String>();
	public static final Map<String, String> measurementTypeCache = new HashMap<String, String>();
	static {
		measurementTypeCache.put("DIAS", "Diastolic Blood Pressure");
		measurementTypeCache.put("SYST", "Systolic Blood Pressure");
	}

	private static String output;
	private static IFormatterGraphResult details;
	
	public static String getOuput() {
		return output;
	}
	
	public static IFormatterGraphResult getDetails() {
		return details;
	}
	
	public EverestUtils() {
		throw new UnsupportedOperationException();
	}

	/**
	 * General Everest Utility Functions
	 */
	// Check String for Null, Empty or Whitespace
	public static Boolean isNullorEmptyorWhitespace(String obj) {
		return obj == null || obj.isEmpty() || obj.trim().isEmpty();
	}

	public static XmlIts1Formatter getFormatter(Boolean validation) {
		
		XmlIts1Formatter fmtr = new XmlIts1Formatter();
		fmtr.setValidateConformance(validation);
		fmtr.getGraphAides().add(new DatatypeFormatter(R1FormatterCompatibilityMode.ClinicalDocumentArchitecture));
		fmtr.addCachedClass(ClinicalDocument.class);
		fmtr.registerXSITypeName("POCD_MT000040UV.Observation", ObservationWithConfidentialityCode.class);
		
		return fmtr;
	}
	
	public static XMLStateStreamWriter getXMLStateStreamWriter(StringWriter sw) throws XMLStreamException
	{
		XMLOutputFactory fact = XMLOutputFactory.newInstance();
		
		XMLStateStreamWriter xssw;

		xssw = new XMLStateStreamWriter(fact.createXMLStreamWriter(sw));
		xssw.writeStartDocument(Constants.XML.ENCODING, Constants.XML.VERSION);
		xssw.writeStartElement("hl7", "ClinicalDocument", "urn:hl7-org:v3");
		xssw.writeNamespace("xs", "http://www.w3.org/2001/XMLSchema");
		xssw.writeNamespace("hl7", "urn:hl7-org:v3");
		xssw.writeNamespace("e2e", "http://standards.pito.bc.ca/E2E-DTC/cda");
		xssw.writeAttribute("xmlns", "xsi", "xsi", "http://www.w3.org/2001/XMLSchema-instance");
		xssw.writeAttribute("xsi", "schemaLocation", "schemaLocation", "urn:hl7-org:v3 Schemas/CDA-PITO-E2E.xsd");
		xssw.writeDefaultNamespace("urn:hl7-org:v3"); // Default hl7 namespace

		return xssw;
	}
	
	public static ProcessingResult setOutputandDetails(ClinicalDocument clinicalDocument, Boolean validation)
	{
		try {
			StringWriter sw = new StringWriter();
			
			XMLStateStreamWriter xssw = getXMLStateStreamWriter(sw);
			
			XmlIts1Formatter fmtr = getFormatter(validation);
			IFormatterGraphResult details = fmtr.graph(xssw, clinicalDocument);
	
			xssw.writeEndElement();
			xssw.writeEndDocument();
			xssw.close();
			
			String output = prettyFormatXML(sw.toString(), Constants.XML.INDENT);
			
			ProcessingResult processingResult = new ProcessingResult();
			processingResult.output = output;
			processingResult.details = details;
			
			return processingResult;
			
		} catch (XMLStreamException e) {
			logger.error(e.toString());
		}
		
		return null;
	}
	
	private static class ProcessingResult {
		public String output;
		public IFormatterGraphResult details;
	}
	
	// Generate Document Function
	public static String generateDocumentToString(ClinicalDocument clinicalDocument, Boolean validation) {
		if(clinicalDocument == null) {
			return null;
		}
			
		ProcessingResult processingResult = setOutputandDetails(clinicalDocument, validation);
		output = processingResult.output;
		details = processingResult.details;
		
		// Temporary Everest Bugfixes
		output = everestBugFixes(output);

		if(validation) {
			E2EEverestValidator.isValidCDA(details);
			E2EXSDValidator.isValidXML(output);
		}

		return output;
	}

	// Temporary Everest Bugfixes
	public static String everestBugFixes(String output) {
		String result = output.replaceAll("xsi:nil=\"true\" ", "");
		result = result.replaceAll("xsi:type=\"_TS\" ", "");
		return result.replaceAll("delimeter", "delimiter");
	}

	// Pretty Print XML
	public static String prettyFormatXML(String input, Integer indent) {
		if(!isNullorEmptyorWhitespace(input)) {
			try {
				Source xmlInput = new StreamSource(new StringReader(input));
				StreamResult xmlOutput = new StreamResult(new StringWriter());

				Transformer tf = TransformerFactory.newInstance().newTransformer();
				tf.setOutputProperty(OutputKeys.ENCODING, Constants.XML.ENCODING);
				tf.setOutputProperty(OutputKeys.INDENT, "yes");
				tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
				tf.transform(xmlInput, xmlOutput);

				return xmlOutput.getWriter().toString().replaceFirst("<Clin", "\n<Clin");
			} catch (TransformerException e) {
				logger.error(e.toString());
			}
		}

		return null;
	}

	/**
	 * Header Utility Functions
	 */
	// Add an address to the addrParts list
	public static void addAddressPart(ArrayList<ADXP> addrParts, String value, AddressPartType addressPartType) {
		if(!isNullorEmptyorWhitespace(value)) {
			ADXP addrPart = new ADXP(value, addressPartType);
			addrParts.add(addrPart);
		}
	}

	// Add a telecom element to the telecoms set
	public static void addTelecomPart(SET<TEL> telecoms, String value, TelecommunicationsAddressUse telecomAddressUse, TelecomType telecomType) {
		if(!isNullorEmptyorWhitespace(value)) {
			if(telecomType == Constants.TelecomType.TELEPHONE) {
				telecoms.add(new TEL(Constants.DocumentHeader.TEL_PREFIX + value.replaceAll("-", ""), telecomAddressUse));
			} else if(telecomType == Constants.TelecomType.EMAIL) {
				telecoms.add(new TEL(Constants.DocumentHeader.EMAIL_PREFIX + value, telecomAddressUse));
			}
		}
	}

	// Add a name to the names set
	public static void addNamePart(SET<PN> names, String firstName, String lastName, EntityNameUse entityNameUse) {
		ArrayList<ENXP> name = new ArrayList<ENXP>();
		if(!isNullorEmptyorWhitespace(firstName)) {
			name.add(new ENXP(firstName, EntityNamePartType.Given));
		}
		if(!isNullorEmptyorWhitespace(lastName)) {
			name.add(new ENXP(lastName, EntityNamePartType.Family));
		}
		if(!name.isEmpty()) {
			names.add(new PN(entityNameUse, name));
		}
	}

	// Add a language to the languages list
	public static void addLanguagePart(ArrayList<LanguageCommunication> languages, String value) {
		if(!isNullorEmptyorWhitespace(value) && Mappings.languageCode.containsKey(value)) {
			LanguageCommunication language = new LanguageCommunication();
			language.setLanguageCode(Mappings.languageCode.get(value));
			languages.add(language);
		}
	}

	/**
	 * Body Utility Functions
	 */
	// Create Prefix-number id object from integer
	public static SET<II> buildUniqueId(IdPrefixes prefix, Integer id) {
		if(id == null) {
			id = 0;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(prefix).append("-").append(id.toString());

		II ii = new II(Constants.EMR.EMR_OID, sb.toString());
		ii.setAssigningAuthorityName(Constants.EMR.EMR_VERSION);
		return new SET<II>(ii);
	}

	// Create Prefix-number id object from long
	public static SET<II> buildUniqueId(IdPrefixes prefix, Long id) {
		if(id == null) {
			id = 0L;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(prefix).append("-").append(id.toString());

		II ii = new II(Constants.EMR.EMR_OID, sb.toString());
		ii.setAssigningAuthorityName(Constants.EMR.EMR_VERSION);
		return new SET<II>(ii);
	}

	// Create a TS object from a Java Date with Day precision
	public static TS buildTSFromDate(Date date) {
		return buildTSFromDate(date, TS.DAY);
	}

	// Create a TS object from a Java Date with specified precision
	public static TS buildTSFromDate(Date date, Integer precision) {
		if(date == null) {
			return null;
		}

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		return new TS(calendar, precision);
	}

	// Create a Date object from dateString
	public static Date stringToDate(String dateString) {
		String[] formatStrings = {"yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd hh:mm", "yyyy-MM-dd"};
		Integer i = 0;
		while(i < formatStrings.length) {
			try {
				return new SimpleDateFormat(formatStrings[i]).parse(dateString);
			} catch (Exception e) {
				i++;
			}
		}

		return null;
	}

	/**
	 * Caching Utility Functions
	 */
	// Find the provider of a given demographicNo
	public static String getDemographicProviderNo(String demographicNo) {
		String providerNo = null;
		try {
			if(!isNullorEmptyorWhitespace(demographicNo) && demographicCache.containsKey(demographicNo)) {
				providerNo =  demographicCache.get(demographicNo).getProviderNo();
			} else {
				DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
				Demographic demographic = demographicDao.getDemographic(demographicNo);
				demographicCache.put(demographicNo, demographic);
				providerNo = demographic.getProviderNo();
			}
		} catch (Exception e) {
			logger.warn("Demographic " + demographicNo + " not found");
		}

		return providerNo;
	}

	// Find the provider from providerNo String
	public static ProviderData getProviderFromString(String providerNo) {
		ProviderData provider = null;
		if(!isNullorEmptyorWhitespace(providerNo) && providerCache.containsKey(providerNo)) {
			provider = providerCache.get(providerNo);
		} else {
			ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);
			try {
				provider = providerDao.findByProviderNo(providerNo);
			} catch (Exception e) {
				logger.warn("Provider " + providerNo + " not found");
			}
			providerCache.put(providerNo, provider);
		}

		return provider;
	}

	// Find the description of icd9 code
	public static String getICD9Description(String code) {
		String description = null;
		if(!isNullorEmptyorWhitespace(code) && icd9DescriptionCache.containsKey(code)) {
			description = icd9DescriptionCache.get(code);
		} else {
			Icd9Dao icd9Dao = SpringUtils.getBean(Icd9Dao.class);
			try {
				description = icd9Dao.findByCode(code).getDescription();
			} catch (Exception e) {
				logger.warn("ICD9 Code '" + code + "' missing description");
			}
			icd9DescriptionCache.put(code, description);
		}

		return description;
	}

	// Find the description of measurement type
	public static String getTypeDescription(String type) {
		String description = type;
		if(!isNullorEmptyorWhitespace(type) && measurementTypeCache.containsKey(type)) {
			description = measurementTypeCache.get(type);
		} else {
			MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);
			try {
				List<MeasurementType> measurementType = measurementTypeDao.findByType(type);
				description = measurementType.get(0).getTypeDescription();
			} catch (Exception e) {
				logger.warn("Measurement type description '" + type + "' not found");
			}
			measurementTypeCache.put(type, description);
		}

		return description;
	}

	// Find ATC code of prevention type
	public static String getPreventionType(String type) {
		String code = null;
		if(preventionTypeCodes == null) {
			preventionTypeCodes = new HashMap<String,String>();
			try {
				InputStream is = EverestUtils.class.getResourceAsStream(OSCAR_PREVENTIONITEMS_FILE);
				Element root = new SAXBuilder().build(is).getRootElement();
				@SuppressWarnings("unchecked")
				List<Element> items = root.getChildren("item");
				for(Element e : items) {
					Attribute name = e.getAttribute("name");
					Attribute atc = e.getAttribute("atc");
					if(atc != null && !isNullorEmptyorWhitespace(atc.getValue())) {
						preventionTypeCodes.put(name.getValue(), atc.getValue());
					}
				}
				is.close();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}

		if(!isNullorEmptyorWhitespace(type) && preventionTypeCodes.containsKey(type)) {
			code = preventionTypeCodes.get(type);
		}

		return code;
	}

	// Find runtime issueID number
	public static Long getIssueID(String issue) {
		Long answer = 0L;
		IssueDAO issueDao = SpringUtils.getBean(IssueDAO.class);
		try {
			answer = issueDao.findIssueByCode(issue).getId();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return answer;
	}
}
