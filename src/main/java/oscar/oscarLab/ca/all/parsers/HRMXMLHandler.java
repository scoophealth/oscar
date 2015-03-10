/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

/*
 * HL7Handler.java
 * An HL7 lab parser, structure was borrowed from GDML template. Fixed to handle TDIS reports.
 *
 */

package oscar.oscarLab.ca.all.parsers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextMessageInfo;
import org.oscarehr.hospitalReportManager.SFTPConnector;
import org.oscarehr.hospitalReportManager.xsd.DateFullOrPartial;
import org.oscarehr.hospitalReportManager.xsd.HealthCard;
import org.oscarehr.hospitalReportManager.xsd.OmdCds;
import org.oscarehr.hospitalReportManager.xsd.PatientRecord;
import org.oscarehr.hospitalReportManager.xsd.PhoneNumber;
import org.oscarehr.hospitalReportManager.xsd.ReportsReceived;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;

/**
 * TDIS HL7 Report Parser/Handler. Handles incoming HL7 files containing ITS or DEPARTMENTAL reports as part of the OBR/OBX segments.
 *
 * @author dritan
 */
public class HRMXMLHandler implements MessageHandler {

	private static Logger logger = MiscUtils.getLogger();

	private ArrayList<String> headers = null;

	private OmdCds root = null;
	private PatientRecord pr = null;

	public void init(String hl7Body) throws HL7Exception {

		// this.version = p.getVersion(hl7Body);
		logger.info("A NEW HRM XML PARSER OBJECT INSTANTIATED TO PARSE HL7 FILE " + hl7Body);

		try {
			ByteArrayInputStream byeArrayInputStream = new ByteArrayInputStream(hl7Body.getBytes());

			// Create a SchemaFactory capable of understanding WXS schemas.
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// Load a WXS schema, represented by a Schema instance.
			Source schemaFile = new StreamSource(new File(SFTPConnector.OMD_directory + "report_manager_cds.xsd"));
			Schema schema = factory.newSchema(schemaFile); //new File(SFTPConnector.OMD_directory + "report_manager_cds.xsd"));

			JAXBContext jc = JAXBContext.newInstance("org.oscarehr.hospitalReportManager.xsd");
			Unmarshaller u = jc.createUnmarshaller();
			root = (OmdCds) u.unmarshal(byeArrayInputStream);
			pr = root.getPatientRecord();

		} catch (Exception e) {
			logger.error("error", e);
		}

		headers = new ArrayList<String>();

	}

	private ArrayList<String> getMatchingHL7Labs(String hl7Body) {
		Base64 base64 = new Base64(0);
		ArrayList<String> ret = new ArrayList<String>();
		int monthsBetween = 0;
		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");

		try {
			List<Hl7TextMessageInfo> matchingLabs = hl7TextInfoDao.getMatchingLabs(hl7Body);
			for ( Hl7TextMessageInfo l: matchingLabs ) {
				Date dateA = UtilDateUtilities.StringToDate(l.labDate_A,"yyyy-MM-dd hh:mm:ss");
				Date dateB = UtilDateUtilities.StringToDate(l.labDate_B,"yyyy-MM-dd hh:mm:ss");
				if (dateA.before(dateB)) {
					monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
				} else {
					monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
				}
				if (monthsBetween < 4) {
					ret.add(new String(base64.decode(l.message.getBytes("ASCII")), "ASCII"));
				}
				if (l.lab_no_A==l.lab_no_B)
					break;
			}


		} catch (Exception e) {
			logger.error("Exception in HL7 getMatchingLabs: ", e);
		}

		// if there have been no labs added to the database yet just return this
		// lab
		if (ret.size() == 0)
			ret.add(hl7Body);
		return ret;
	}

	public String getMsgType() {
		return ("HRMXML");
	}

	public String getMsgDate() {
		Iterator<ReportsReceived> rr = pr.getReportsReceived().iterator();
		if (rr.hasNext()) {
			ReportsReceived r = rr.next();
			DateFullOrPartial datetime = r.getEventDateTime();
			if (datetime == null) {
				return "";
			}

			return getFormattedDate(datetime.getFullDate(), false);

		}
		return "";
	}

	public String getMsgPriority() {

		// if (getString(obrSegKeySet.get(i).getPriorityOBR().getValue()).equals("S"))
		// return "S";

		return "R";
	}

	/**
	 * Methods to get information about the Observation Request
	 */
	public int getOBRCount() {
		return 0;
	}

	public int getOBXCount(int i) {
		return 0;
	}

	public String getOBRName(int i) {
		String val = "";
		return val;
		// return (val = obrSegKeySet.get(i).getUniversalServiceIdentifier().getText().getValue()) == null ? " " : val;
	}

	public String getTimeStamp(int i, int j) {
		return "";
	}

	public boolean isOBXAbnormal(int i, int j) {
		String abnormalFlag = getOBXAbnormalFlag(i, j);
		if (abnormalFlag.equals("") || abnormalFlag.equals("N")) return (false);
		else return (true);
	}

	public String getOBXAbnormalFlag(int i, int j) {

		return "";
	}

	/**
	 * In order to match ITS Report OBXs with the OBR holding them, and to keep the output style cleaner, for ITS Reports we need to "trick" the style output. If an OBR contains an OBX segment with ITS or DPT report, then all OBXs for that OBR are
	 * considered to be report types. Because an ITS or DPT is not a test, there is no need to write the test name "ITS/DPT REPORT" when viewing the results. So we put "ITS/DPT REPORT" near the OBR name to classify this OBR and following OBX's as one full
	 * ITS/DPT report. A refresher of what this method computes: Return the observation header which represents the observation stored in the jth OBX segment of the ith OBR group. May be stored in either the OBR or OBX segment. It is used to separate the
	 * observations into groups. ie/ 'CHEMISTRY' 'HEMATOLOGY' '
	 */
	public String getObservationHeader(int i, int j) {

		return "";
	}

	public String getOBXIdentifier(int i, int j) {

		return "";
	}

	public String getOBXValueType(int i, int j) {
		String ret = "";

		return ret;
	}

	public String getOBXName(int i, int j) {
		return getOBXName(i, j, false);
	}

	/**
	 * return the OBX name for the specified OBR index 'i' and OBX index 'j' If the OBX is an ITS or DPT then return a single space to trick the full report style. See above for default call to getOBXName()
	 */
	public String getOBXName(int i, int j, boolean its) {
		String ret = "";

		return ret;
	}

	private boolean isReport(int i, int j) {
		String obxName = getOBXName(i, j, true);
		return obxName.indexOf("REPORT") != -1;
	}

	public String getOBXResult(int i, int j) {

		return getOBXResult(i, j, false);
	}

	/**
	 * Return the result of the jth OBX at ith OBR. If it's an ITS^REPORT, the show nothing as this result will be fetched in the previous column.
	 *
	 * @param i
	 * @param j
	 * @param its
	 * @return String
	 */
	public String getOBXResult(int i, int j, boolean its) {

		String result = "";

		return result;
	}

	public String getOBXReferenceRange(int i, int j) {
		String ret = "";

		return ret;
	}

	public String getOBXUnits(int i, int j) {
		String ret = "";

		return ret;
	}

	public String getOBXResultStatus(int i, int j) {

		return "";
	}

	public int getOBXFinalResultCount() {
		return 0;
	}

	/**
	 * Retrieve the possible segment headers from the OBX fields
	 */
	public ArrayList<String> getHeaders() {
		return headers;
	}

	/**
	 * Methods to get information from observation notes
	 */
	public int getOBRCommentCount(int i) {
		int count = 0;

		return count;

	}

	public String getOBRComment(int i, int j) {
		String comment = "";

		return comment;
	}

	/**
	 * Methods to get information from observation notes
	 */
	public int getOBXCommentCount(int i, int j) {
		int count = 0;

		return count;
	}

	public String getOBXComment(int i, int j, int k) {
		String comment = "";

		return comment;
	}

	/**
	 * Methods to get information about the patient
	 */
	public String getPatientName() {
		return (getFirstName() + " " + getLastName());
	}

	public String getFirstName() {
		return pr.getDemographics().getNames().getLegalName().getFirstName().getPart();

	}

	public String getLastName() {
		return pr.getDemographics().getNames().getLegalName().getLastName().getPart();
	}

	public String getDOB() {
		return getFormattedDate(pr.getDemographics().getDateOfBirth().getFullDate(), true);

	}

	public String getAge() {
		String age = "N/A";
		try {
			// Some examples
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = formatter.parse(getDOB());
			age = UtilDateUtilities.calcAge(date);
		} catch (ParseException e) {
			logger.error("Could not get age", e);

		}
		return age;
	}

	public String getSex() {
		return pr.getDemographics().getGender().value();

	}

	public String getHealthNum() {
		HealthCard hc;
		return (hc = pr.getDemographics().getHealthCard()).getNumber() + hc.getVersion() + " " + hc.getProvinceCode();
	}

	public String getHomePhone() {
		List<PhoneNumber> pnums = pr.getDemographics().getPhoneNumber();
		for (PhoneNumber pnum : pnums) {
			if (pnum.getPhoneNumberType().equals("R")) { // R for residential
				return pnum.getContent().get(0).toString(); // return first instance of number
			}
		}
		return "";
	}

	public String getWorkPhone() {

		return "";
	}

	public String getPatientLocation() {
		return "";
	}

	public String getServiceDate() {
		return "";
	}

	public String getRequestDate(int i) {
		return "";
	}

	public String getOrderStatus() {

		// if (res != null && res.equals("0"))
		// return "P";
		return "F";
	}

	public String getClientRef() {

		return "00000000";
	}

	public String getAccessionNum() {

		return new Random().nextInt() + "";
	}

	public String getDocName() {
		return "";
	}

	public String getCCDocs() {
		return "";
	}

	public ArrayList<String> getDocNums() {
		return null;
	}

	public String audit() {
		return "";
	}

	private String getFullDocName(ca.uhn.hl7v2.model.v25.datatype.XCN xcn) {
		String docName = "";

		if (xcn.getPrefixEgDR().getValue() != null) docName = xcn.getPrefixEgDR().getValue();

		if (xcn.getGivenName().getValue() != null) {
			if (docName.equals("")) docName = xcn.getGivenName().getValue();
			else docName = docName + " " + xcn.getGivenName().getValue();

		}
		if (xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue() != null) {
			if (docName.equals("")) docName = xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue();
			else docName = docName + " " + xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue();
		}
		if (xcn.getFamilyName().getSurname().getValue() != null) {
			if (docName.equals("")) docName = xcn.getFamilyName().getSurname().getValue();
			else docName = docName + " " + xcn.getFamilyName().getSurname().getValue();

		}
		if (xcn.getSuffixEgJRorIII().getValue() != null) {
			if (docName.equals("")) docName = xcn.getSuffixEgJRorIII().getValue();
			else docName = docName + " " + xcn.getSuffixEgJRorIII().getValue();
		}
		if (xcn.getDegreeEgMD().getValue() != null) {
			if (docName.equals("")) docName = xcn.getDegreeEgMD().getValue();
			else docName = docName + " " + xcn.getDegreeEgMD().getValue();
		}

		return docName;
	}

	private String formatDateTime(String plain) {
		if (plain==null || plain.trim().equals("")) return "";

		String dateFormat = "yyyyMMddHHmmss";
		dateFormat = dateFormat.substring(0, plain.length());
		String stringFormat = "yyyy-MM-dd HH:mm:ss";
		stringFormat = stringFormat.substring(0, stringFormat.lastIndexOf(dateFormat.charAt(dateFormat.length() - 1)) + 1);

		Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
		return UtilDateUtilities.DateToString(date, stringFormat);
	}

	private String getString(String retrieve) {
		if (retrieve != null) {
			return (retrieve.trim());
		} else {
			return "";
		}
	}

	private String getFormattedDate(XMLGregorianCalendar date, boolean omitTime) {
		if (date == null) {
			return "";
		}
		return date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + (!omitTime ? date.getHour() + ":" + date.getMinute() : "");
	}

	 public String getFillerOrderNumber(){


			return "";
		}

	    public String getEncounterId(){
	    	return "";
	    }
	    public String getRadiologistInfo(){
			return "";
		}

	    public String getNteForOBX(int i, int j){

	    	return "";
	    }
	    public String getNteForPID() {
	    	return "";
	    }
}
