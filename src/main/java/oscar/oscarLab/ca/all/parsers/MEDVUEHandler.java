/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarLab.ca.all.parsers;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextMessageInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.segment.OBR;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 * MEDVUE  Radiology report parser 
 * @author divyamantha
 *
 */

public class MEDVUEHandler implements MessageHandler {

	Logger logger = Logger.getLogger(MEDVUEHandler.class);
	
	private ca.uhn.hl7v2.model.v23.message.ORU_R01 msg = null;
	private ArrayList<String> headers = null;
	private OBR obrseg = null;
	private OBX obxseg = null;
	
	
	
	private ca.uhn.hl7v2.model.v23.group.ORU_R01_PATIENT pat_23; //patient object


	public void init(String hl7Body) throws HL7Exception {

		Parser p = new PipeParser();
		
		
		msg = (ca.uhn.hl7v2.model.v23.message.ORU_R01) p.parse(hl7Body.replaceAll("\n", "\r\n")); // for now just fetch the object
		msg.getMSH().getEncodingCharacters().setValue("^~\\&");
		msg.getMSH().getFieldSeparator().setValue("|");

		//this.v2 = ORUR01Manager.getVersion(version);

		p.setValidationContext(new NoValidation());
		ArrayList<String> labs = getMatchingHL7Labs(hl7Body);

		headers = new ArrayList<String>();

		//start iterating the labs, grabbing all data
		for (int i = 0; i < labs.size(); i++) {

			msg = (ca.uhn.hl7v2.model.v23.message.ORU_R01) p.parse(labs.get(i).replaceAll("\n", "\r\n"));
		//msg = (ca.uhn.hl7v2.model.v23.message.ORU_R01) p.parse(((String) hl7Body).replaceAll("\n", "\r\n"));
			ca.uhn.hl7v2.model.v23.group.ORU_R01_RESPONSE pat_res = msg.getRESPONSE();

			ca.uhn.hl7v2.model.v23.group.ORU_R01_ORDER_OBSERVATION obsr = pat_res.getORDER_OBSERVATION();
			
			pat_23 = pat_res.getPATIENT();
			obrseg = obsr.getOBR();
			obxseg = obsr.getOBSERVATION().getOBX();
			
			
			

			/*if (!headers.contains(header)) {
				headers.add(header);
			}*/
				
		} //end lab iteration

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
		return ("MEDVUE");
	}

	public String getMsgDate() {
		return formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue());
		//return (formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
	}

	public String getMsgPriority() {

		return "";
	}

	/**
	 * Methods to get information about the Observation Request
	 */
	public int getOBRCount() {
		return 1;
	}

	public int getOBXCount(int i) {
		
		return 1;
	}

	public String getOBRName(int i) {
		String val = "";
		return (val = obrseg.getUniversalServiceIdentifier().getText().getValue()) == null ? " " : val;
	}

	public String getTimeStamp(int i, int j) {
		try {
			if (isReport(i, j)) {
				return "";
			}
			String ret ;
			ret = obrseg.getObservationDateTime().getTimeOfAnEvent().toString(); //.getTimeOfAnEvent().getValue();
			return (formatDateTime(getString(ret)));
		} catch (Exception e) {
			logger.error("Exception retrieving timestamp", e);
			return ("");
		}
	}

	public boolean isOBXAbnormal(int i, int j) {
		return false;
	}

	public String getOBXAbnormalFlag(int i, int j) {

		return "";
	}

	/**
	 * In order to match ITS Report OBXs with the OBR holding them, and to keep the output style cleaner, for ITS
	 * Reports we need to "trick" the style output.
	 * 
	 * If an OBR contains an OBX segment with ITS or DPT report, then all OBXs for that OBR are considered to be report
	 * types. Because an ITS or DPT is not a test, there is no need to write the test name "ITS/DPT REPORT" when viewing
	 * the results. So we put "ITS/DPT REPORT" near the OBR name to classify this OBR and following OBX's as one full
	 * ITS/DPT report.
	 * 
	 * A refresher of what this method computes:
	 * 
	 * Return the observation header which represents the observation stored in the jth OBX segment of the ith OBR
	 * group. May be stored in either the OBR or OBX segment. It is used to separate the observations into groups. ie/
	 * 'CHEMISTRY' 'HEMATOLOGY' '
	 */
	public String getObservationHeader(int i, int j) {
		try {
			String obrHeader = getString(obrseg.getUniversalServiceIdentifier().getText().getValue());
			String obxHeader = getOBXName(i, j);
			if (obxHeader.indexOf("REPORT") != -1) {
				return obrHeader + " (" + obxHeader + ")";
			}
			return obrHeader;
		} catch (Exception e) {
			logger.error("Exception gettin header", e);
		}

		return "";
	}

	public String getOBXIdentifier(int i, int j) {

		try {

			Terser t = new Terser(msg);
			
			String ident = getString(Terser.get(obxseg, 3, 0, 1, 1));
			String subIdent = Terser.get(obxseg, 3, 0, 1, 2);

			if (subIdent != null)
				ident = ident + "&" + subIdent;

			//logger.info("returning obx identifier: " + ident);
			return (ident);
		} catch (Exception e) {
			logger.error("error returning obx identifier", e);

		}
		return "";
	}

	public String getOBXValueType(int i, int j) {
		String ret = "";
		try { 
			ret = obxseg.getValueType().getValue();
		} catch (Exception e) {
			logger.error("Error returning OBX name", e);
		}

		return ret;
	}

	public String getOBXName(int i, int j) {
		return getString(obxseg.getObservationIdentifier().getText().getValue());
	}


	private boolean isReport(int i, int j) {
		String obxName = getOBXName(i, j);
		return obxName.indexOf("REPORT") != -1;
	}


	
	public String getOBXResult(int i, int j) {

		String result = "";
		try {
			Terser terser = new Terser(msg);
			result = getString(Terser.get(obxseg, 5, 0, 1, 1));
		} catch (Exception e) {
			logger.error("Exception returning result", e);
		}
		
		return result;
	}

	public String getOBXReferenceRange(int i, int j) {
		
		return "";
	}

	public String getOBXUnits(int i, int j) {
		
		return "";
	}

	public String getOBXResultStatus(int i, int j) {
		
		return "MEDVUE";
	}

	public int getOBXFinalResultCount() {
		return 0;
	}

	/**
	 * Retrieve the possible segment headers from the OBX fields
	 */
	public ArrayList<String> getHeaders() {
		String header = getString(obrseg.getUniversalServiceIdentifier().getText().getValue());
		headers.add(header);
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
		try {
			Terser terser = new Terser(msg);
			comment = Terser.get(obxseg, 5, 0, 1, 1);
			
		} catch (Exception e) {
			logger.error("getOBRComment error", e);
			comment = "";
		}
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
		String comment = "",newComment="";
		int len=80; String[] splitComment; String concatComment= "";
		
		try {
			Terser terser = new Terser(msg);
			comment = Terser.get(obxseg, 5, 0, 1, 1);
			comment = comment.replaceAll("<P\\s*/*>", "<br/>");
			splitComment = comment.split("<br/>");
			
			for (int l=0;l<splitComment.length;l++) {
				if (splitComment[l].length()>len) {
					StringBuilder sb = new StringBuilder(splitComment[l]);
					int i1 = 0;
					while ((i1 = sb.indexOf(" ", i1 + len)) != -1) {
					    sb.replace(i1, i1 + 1, "<br/>");
					    
					}
					
					concatComment=sb.toString();
				}
				
				if (!concatComment.equals("")) {
					newComment += "<br/>"+concatComment.toString();
				} else {
					newComment +=  "<br/>"+splitComment[l].toString();
				}
				concatComment = "";
					
			}
			
			logger.info("Modified comment ="+ newComment);
			return newComment;
			
		} catch (Exception e) {
			logger.error("getOBRComment error", e);
			return "";
		}
		//return comment;
	}

	/**
	 * Methods to get information about the patient
	 */
	public String getPatientName() {
		return (getFirstName() + " " + getLastName());
	}

	public String getFirstName() {
		
		try {	
			return (getString(this.pat_23.getPID().getPatientName().getGivenName().getValue()));
		} catch (Exception e) {
			logger.error("Exception getting firstName of the patient" , e);
		}
		
		return "";
	}

	public String getLastName() {
		
		try {	
			return getString(this.pat_23.getPID().getPatientName().getFamilyName().getValue());
		} catch (Exception e) {
			logger.error("Exception getting lastName of the patient" , e);
		}
		
		return "";
			
	}

	public String getDOB() {
		try {
			return (formatDateTime(getString(pat_23.getPID().getDateOfBirth().getTimeOfAnEvent().getValue())).substring(0,
					10));
		} catch (Exception e) {
			logger.error("Exception retrieving DOB", e);
		}
		return "";
	}

	public String getAge() {
		String age = "N/A";
		String dob = getDOB();
		try {
			// Some examples
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = formatter.parse(dob);
			age = UtilDateUtilities.calcAge(date);
		} catch (ParseException e) {
			logger.error("Could not get age", e);

		}
		return age;
	}

	public String getSex() {
		
		try {
			return getString(pat_23.getPID().getSex().getValue());
		} catch (Exception e) {
			logger.error("Exception getting the sex of the patient" , e);
		}
		
		return "";
	}

	public String getHealthNum() {
		//int pat_ids = pat_25.getPID().getPatientIdentifierListReps();
		try {
			ca.uhn.hl7v2.model.v23.datatype.CX patIdList = pat_23.getPID().getPatientIDInternalID(0);
			String hnumber = patIdList.getID().getValue();
			return hnumber;
			
		} catch (HL7Exception e) {
			logger.error("ERROR getting the health number for HL7 lab report patient: " + e.toString());
		}

		return "";
	}

	public String getHomePhone() {
		String phone = "";
		int i = 0;
		try {
			while (!getString(pat_23.getPID().getPhoneNumberHome(i).getAnyText().getValue()).equals("")) {
				if (i == 0) {
					phone = getString(pat_23.getPID().getPhoneNumberHome(i).getAnyText().getValue());
				} else {
					phone = phone + ", " + getString(pat_23.getPID().getPhoneNumberHome(i).getAnyText().getValue());
				}
				i++;
			}
			return (phone);
		} catch (Exception e) {
			logger.error("Could not return phone number", e);
		}
		return "";
	}

	public String getWorkPhone() {
		String phone = "";
		int i = 0;
		try {
			while (!getString(pat_23.getPID().getPhoneNumberBusiness(i).getAnyText().getValue()).equals("")) {
				if (i == 0) {
					phone = getString(pat_23.getPID().getPhoneNumberBusiness(i).getAnyText().getValue());
				} else {
					phone = phone + ", " + getString(pat_23.getPID().getPhoneNumberBusiness(i).getAnyText().getValue());
				}
				i++;
			}
			return (phone);
		} catch (Exception e) {
			logger.error("Could not parse phone number", e);
		}
		return "";
	}

	public String getPatientLocation() {
		//return (getString(msg.getMSH().getSendingFacility().getNamespaceID().getValue()));
		return "MEDVUE";
	}

	public String getServiceDate() {
		try {
			return formatDateTime(getString(obrseg.getObservationDateTime().getTimeOfAnEvent().getValue()));
		} catch (Exception e) {
			logger.error("Exception retrieving service date", e);
		}
		return "";
	}

	public String getOrderStatus() {
		String status = "";
		try {
			status = obxseg.getObservResultStatus().toString();
		} catch (Exception e){
			logger.error("Error getting OBX-11 observation status"+ e);
		}
		return status;
	}

	
	public String getClientRef() {
		String clientref = "";
		try {
			clientref = pat_23.getVISIT().getPV1().getReferringDoctor(0).getIDNumber().getValue();
			return (clientref);
		} catch (Exception e) {
			logger.error("Error returning clieng reference number + ", e);
		}
		
		return "00000000";
	}

	public String getAccessionNum() {
		/*try {
			return msg.getMSH().getMessageControlID().getValue().toString();
		} catch (Exception e) {
			logger.error("Could not return accession num: ", e);
		}*/
		return "";
		//return this.getEncounterId();
	}

	public String getDocName() {
		String docName,docLastName,docFirstName = "";
		try {
			docLastName = pat_23.getVISIT().getPV1().getReferringDoctor(0).getFamilyName().getValue();
			docFirstName = pat_23.getVISIT().getPV1().getReferringDoctor(0).getGivenName().getValue();
			docName = docLastName + "," + docFirstName;
			return (docName);
		} catch (Exception e) {
			logger.error("Could not return doctor names", e);

		}
		return "";	
	}
 
	public String getEncounterId() {
		String encounterId = "";
		try {
			encounterId = pat_23.getVISIT().getPV1().getVisitNumber().getID().getValue();
			return (encounterId);
		} catch (Exception e){
			logger.error("Error getting visit number"+e);
		}
		
		return "";
	}
	public String getCCDocs() {

		String docNames = "";

		try {
			/*Terser terser = new Terser(msg);

			String givenName = terser.get("/.ZDR(0)-4-1");
			String middleName = terser.get("/.ZDR(0)-4-3");
			String familyName = terser.get("/.ZDR(0)-4-2");

			int i = 1;
			while (givenName != null) {

				if (i == 1)
					docNames = givenName;
				else
					docNames = docNames + ", " + givenName;

				if (middleName != null)
					docNames = docNames + " " + middleName;
				if (familyName != null)
					docNames = docNames + " " + familyName;

				givenName = terser.get("/.ZDR(" + i + ")-4-1");
				middleName = terser.get("/.ZDR(" + i + ")-4-3");
				familyName = terser.get("/.ZDR(" + i + ")-4-2");

				i++;
			}*/

			return (docNames);

		} catch (Exception e) {
			// ignore error... it will occur when the zdr segment is not present
			// logger.error("Could not retrieve cc'd docs", e);
		}
		return "";

	}

	public ArrayList<String> getDocNums() {
		String docNum = "";
		ArrayList<String> nums = new ArrayList<String>();
		try {

			
			docNum = pat_23.getVISIT().getPV1().getReferringDoctor(0).getIDNumber().getValue();
			nums.add(docNum);

		} catch (Exception e) {
			 logger.error("Could not return numbers", e);
		}

		return nums;
	}

	public String audit() {
		return "";
	}

	/*private String getFullDocName(ca.uhn.hl7v2.model.v25.datatype.XCN xcn) {
		String docName = "";

		if (xcn.getPrefixEgDR().getValue() != null)
			docName = xcn.getPrefixEgDR().getValue();

		if (xcn.getGivenName().getValue() != null) {
			if (docName.equals(""))
				docName = xcn.getGivenName().getValue();
			else
				docName = docName + " " + xcn.getGivenName().getValue();

		}
		if (xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue() != null) {
			if (docName.equals(""))
				docName = xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue();
			else
				docName = docName + " " + xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().getValue();
		}
		if (xcn.getFamilyName().getSurname().getValue() != null) {
			if (docName.equals(""))
				docName = xcn.getFamilyName().getSurname().getValue();
			else
				docName = docName + " " + xcn.getFamilyName().getSurname().getValue();

		}
		if (xcn.getSuffixEgJRorIII().getValue() != null) {
			if (docName.equals(""))
				docName = xcn.getSuffixEgJRorIII().getValue();
			else
				docName = docName + " " + xcn.getSuffixEgJRorIII().getValue();
		}
		if (xcn.getDegreeEgMD().getValue() != null) {
			if (docName.equals(""))
				docName = xcn.getDegreeEgMD().getValue();
			else
				docName = docName + " " + xcn.getDegreeEgMD().getValue();
		}

		return docName;
	}*/

	private String formatDateTime(String plain) {
		String dateFormat = "yyyyMMddHHmmss";
		dateFormat = dateFormat.substring(0, plain.length());
		String stringFormat = "yyyy-MM-dd HH:mm:ss";
		stringFormat = stringFormat.substring(0,
				stringFormat.lastIndexOf(dateFormat.charAt(dateFormat.length() - 1)) + 1);

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

	public String getFillerOrderNumber(){
		
		return "";
	}
	
	public String getRadiologistInfo(){
		String idNumber, firstName,lastName,info = "";
		idNumber = getString(obxseg.getResponsibleObserver().getIDNumber().getValue());
		firstName = getString(obxseg.getResponsibleObserver().getGivenName().getValue());
		lastName = getString(obxseg.getResponsibleObserver().getFamilyName().getValue());
		
		 info = lastName + "," + firstName + " - " + idNumber;
		 return info;
	}
	public String getNteForOBX(int i, int j){
		return "";
	}

	@Override
    public String getRequestDate(int i) {
	    // TODO Auto-generated method stub
	    return null;
    }
	
	public String getNteForPID() {
    	return "";
    }
	
}
