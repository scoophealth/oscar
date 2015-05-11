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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.HL7HandlerMSHMappingDao;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.HL7HandlerMSHMapping;
import org.oscarehr.common.model.Hl7TextMessageInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.pageUtil.ORUR01Manager;
import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.datatype.ID;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.OBX;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 * TDIS HL7 Report Parser/Handler.
 * 
 * Handles incoming HL7 files containing ITS or DEPARTMENTAL reports as part of the OBR/OBX segments.
 * 
 * 
 * @author dritan
 */
public class TDISHandler implements MessageHandler {

	Logger logger = Logger.getLogger(TDISHandler.class);

	private ca.uhn.hl7v2.model.v25.message.ORU_R01 msg = null;
	private ArrayList<String> headers = null;
	private HashMap<OBR, ArrayList<OBX>> obrSegMap = null;
	private ArrayList<ca.uhn.hl7v2.model.v25.segment.OBR> obrSegKeySet = null;

	//lab versions
	private String version = "";
	private int v2 = 0; //an integer representation of the version number without the decimal

	private HashMap<Integer, Integer> obxCount = null;
	private HashMap<Integer, Object> orderStatus = null;
	
	private ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT pat_25; //patient object

	public void init(String hl7Body) throws HL7Exception {

		Parser p = new PipeParser();
		this.version = p.getVersion(hl7Body);
		logger.info("A NEW HL7HANDLER PARSER OBJECT INSTANTIATED TO PARSE HL7 FILE " + version);

		// so far so good, but p.parse() returns an object corresponding to the lab version uploaded
		// thus, attempts to casting the crafted object into a different version causes errors.
		msg = (ca.uhn.hl7v2.model.v25.message.ORU_R01) p.parse(hl7Body.replaceAll("\n", "\r\n")); // for now just fetch the object
		msg.getMSH().getEncodingCharacters().setValue("^~\\&");
		msg.getMSH().getFieldSeparator().setValue("|");

		this.v2 = ORUR01Manager.getVersion(version);
		

		p.setValidationContext(new NoValidation());
		ArrayList<String> labs = getMatchingHL7Labs(hl7Body);

		/*//for debug purposes, show the parsed labs
		logger.info("TOTAL LABS: " + labs.size());
		for (int k = 0; k < labs.size(); k++) {
			logger.info("\n\n===========LAB[" + k + "]: \n\n" + labs.get(k) + "================END LAB\n\n");
		}
		*/
		headers = new ArrayList<String>();
		obrSegMap = new LinkedHashMap<OBR, ArrayList<OBX>>(); //a hashmap with obr object corresponding to obx objects in an arraylist
		obrSegKeySet = new ArrayList<OBR>(); //keep track of obr objects here
		obxCount = new HashMap<Integer, Integer>();
		orderStatus = new HashMap<Integer, Object>();

		boolean isITS = false;
		String obxName = "";
		//start iterating the labs, grabbing all data
		for (int i = 0; i < labs.size(); i++) {

			msg = (ca.uhn.hl7v2.model.v25.message.ORU_R01) p.parse(labs.get(i).replaceAll("\n", "\r\n"));

			int pat_reps = msg.getPATIENT_RESULTReps();
			for (int k = 0; k < pat_reps; k++) {
				ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT pat_res = msg.getPATIENT_RESULT(k);

				this.pat_25 = pat_res.getPATIENT();
				int obrCount = pat_res.getORDER_OBSERVATIONReps();

				//for each observed report (ie: obr & obx, obx, obx.... pairs)
				for (int h = 0; h < obrCount; h++) {
					isITS = false;

					ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION obsr = pat_res.getORDER_OBSERVATION(h);
					orderStatus.put(h, obsr.getORC().getOrderStatus().getValue());
					int observation_reps = obsr.getOBSERVATIONReps(); //fetch the number of OBX lines, aka: observation line (repetition)
					obxCount.put(h, observation_reps); //keep track of how many OBX lines there are for each OBR at index h

					ca.uhn.hl7v2.model.v25.segment.OBR obrSeg = obsr.getOBR();
					ArrayList<OBX> obxSegs = new ArrayList<OBX>();//(ArrayList) obrSegMap.get(obrSeg);

					for (int l = 0; l < observation_reps; l++) {
						OBX obx = obsr.getOBSERVATION(l).getOBX();
						if (obx.getObservationIdentifier().getText().getValue() != null) {
							if ((obxName = obx.getObservationIdentifier().getText().getValue()).indexOf("ITS") != -1
									|| obxName.indexOf("DEPARTMENTAL") != -1) {
								isITS = true;
							}
						} else {
							obx.getObservationIdentifier().getText().setValue("");
							obx.getValueType().setValue("");
						}
						obxSegs.add(obx);
					}

					//create the association directly with the OBR segment object and corresponding OBX's array list
					obrSegMap.put(obrSeg, obxSegs);
					//keep track of each obr segment
					obrSegKeySet.add(obrSeg);

					// ADD THE HEADER TO THE HEADERS ARRAYLIST
					String header = getString(obrSeg.getUniversalServiceIdentifier().getText().getValue());
					if (isITS) {
						header += " (" + obxName + ")";
					}

					if (!headers.contains(header)) {
						headers.add(header);
					}
				}
			}
		} //end lab iteration

	}

	private ArrayList<String> getMatchingHL7Labs(String hl7Body) {
		Base64 base64 = new Base64(0);
		ArrayList<String> ret = new ArrayList<String>();
		int monthsBetween = 0;
		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
		

		try {
			List<Hl7TextMessageInfo> matchingLabs = hl7TextInfoDao.getMatchingLabs(hl7Body);
			for (Hl7TextMessageInfo l: matchingLabs ) {
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
		return ("TDIS");
	}

	public String getMsgDate() {
		return formatDateTime(msg.getMSH().getDateTimeOfMessage().getTime().getValue());
		//return (formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
	}

	public String getMsgPriority() {

		for (int i = 0; i < obrSegKeySet.size(); i++) {
			try {

				if (getString(obrSegKeySet.get(i).getPriorityOBR().getValue()).equals("S")) {
					return "S";
				}
			} catch (Exception e) {
				logger.error("Error finding priority", e);
			}
		}

		return "R";
	}

	/**
	 * Methods to get information about the Observation Request
	 */
	public int getOBRCount() {
		return (obrSegMap.size());
	}

	public int getOBXCount(int i) {
		Integer res = this.obxCount.get(i);
		if (res == null)
			return 0;

		//return (((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).size());
		return this.obxCount.get(i);
	}

	public String getOBRName(int i) {
		String val = "";
		return (val = obrSegKeySet.get(i).getUniversalServiceIdentifier().getText().getValue()) == null ? " " : val;
	}

	public String getTimeStamp(int i, int j) {
		try {
			if (isReport(i, j)) {
				return "";
			}
			String ret = obrSegKeySet.get(i).getResultsRptStatusChngDateTime().getTime().toString(); //.getTimeOfAnEvent().getValue();
			if (ret == null)
				ret = obrSegKeySet.get(i).getObservationDateTime().getTime().toString(); //.getTimeOfAnEvent().getValue();
			return (formatDateTime(getString(ret)));
		} catch (Exception e) {
			logger.error("Exception retrieving timestamp", e);
			return ("");
		}
	}

	public boolean isOBXAbnormal(int i, int j) {
		String abnormalFlag = getOBXAbnormalFlag(i, j);
		if (abnormalFlag.equals("") || abnormalFlag.equals("N"))
			return (false);
		else
			return (true);
	}

	public String getOBXAbnormalFlag(int i, int j) {

		try {

			return (getString((obrSegMap.get(obrSegKeySet.get(i))
					.get(j)).getAbnormalFlags(0).getValue()));
		} catch (Exception e) {
			logger.error("Exception retrieving abnormal flag", e);

		}
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
			String obrHeader = getString(obrSegKeySet.get(i).getUniversalServiceIdentifier().getText().getValue());
			String obxHeader = getOBXName(i, j, true);
			if (obxHeader.indexOf("ITS") != -1 || obxHeader.indexOf("DEPARTMENTAL") != -1) {
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
			Segment obxSeg = ((ca.uhn.hl7v2.model.v25.segment.OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i)))
					.get(j));
			String ident = getString(Terser.get(obxSeg, 3, 0, 1, 1));
			String subIdent = Terser.get(obxSeg, 3, 0, 1, 2);

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
			OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
			ret = obxSeg.getValueType().getValue();
		} catch (Exception e) {
			logger.error("Error returning OBX name", e);
		}

		return ret;
	}

	public String getOBXName(int i, int j) {
		return getOBXName(i, j, false);
	}

	/**
	 * return the OBX name for the specified OBR index 'i' and OBX index 'j'
	 * 
	 * If the OBX is an ITS or DPT then return a single space to trick the full report style. See above for default call
	 * to getOBXName()
	 */
	public String getOBXName(int i, int j, boolean its) {
		String ret = "";
		try {

			OBX obx = ( obrSegMap.get(obrSegKeySet.get(i))).get(j);

			if (!obx.getValueType().getValue().equals("FT"))
				ret = getString(obx.getObservationIdentifier().getText().getValue());

			//override to actually return the name of this OBX regardless of whether it's ITS^REPORT
			if (its)
				return ret;

			//if it's an ITS^REPORT then show nothing, the report will be displayed as a comment
			if (ret.indexOf("ITS") != -1 || ret.indexOf("DEPARTMENTAL") != -1) {
				return " ";
			}

		} catch (Exception e) {
			logger.error("Error returning OBX name", e);
		}

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
	 * Return the result of the jth OBX at ith OBR.
	 * 
	 * If it's an ITS^REPORT, the show nothing as this result will be fetched in the previous column.
	 * 
	 * @param i
	 * @param j
	 * @param its
	 * @return String obx result
	 */
	@SuppressWarnings("unchecked")
	public String getOBXResult(int i, int j, boolean its) {

		String result = "", newComment = "";
		int len = 80; 
		
		try {
			OBX obx = null;
			Terser terser = new Terser(msg);
			result = getString(Terser.get((obx = (OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)), 5, 0,
					1, 1));

			// format the result
			if (result.endsWith("."))
				result = result.substring(0, result.length() - 1); 
			
				if (result.length()>len) {
					StringBuilder sb = new StringBuilder(result);
		
					int i1 = 0;
					while ((i1 = sb.indexOf(" ", i1 + len)) != -1) {
					    sb.replace(i1, i1 + 1, "<br/>");
					}
					newComment = sb.toString();
					logger.info("Modified comment ="+ newComment);
				} else {
					newComment = result;
					logger.info("Original comment ="+ newComment);
				}

				//if true, then we're giving the report values to the function showing the "test name" per OBX
				if (its)
					return newComment; //return result;
				String name = "";
				//default behavior is for ITS^REPORT is to show now result as this will be in the "test name" field.
				if ((name = obx.getObservationIdentifier().getText().getValue()).indexOf("ITS") != -1
						|| name.indexOf("DEPARTMENTAL") != -1)
					return "";
		} catch (Exception e) {
			logger.error("Exception returning result", e);
		}
		return result;
	}

	public String getOBXReferenceRange(int i, int j) {
		String ret = "";
		try {
			Terser terser = new Terser(msg);

			ca.uhn.hl7v2.model.v25.segment.OBX obxSeg = (ca.uhn.hl7v2.model.v25.segment.OBX) ((ArrayList) obrSegMap
					.get(obrSegKeySet.get(i))).get(j);

			// If the units are not specified use the formatted reference range
			// which will usually contain the units as well

			if (getOBXUnits(i, j).equals(""))
				ret = getString(Terser.get(obxSeg, 7, 0, 2, 1));

			// may have to fall back to original reference range if the second
			// component is empty
			if (ret.equals("")) {
				ret = getString(obxSeg.getReferencesRange().getValue());
				if (!ret.equals("")) {
					// format the reference range if using the unformatted one
					String[] ranges = ret.split("-");
					for (int k = 0; k < ranges.length; k++) {
						if (ranges[k].endsWith("."))
							ranges[k] = ranges[k].substring(0, ranges[k].length() - 1);
					}

					if (ranges.length > 1) {
						if (ranges[0].contains(">") || ranges[0].contains("<"))
							ret = ranges[0] + "= " + ranges[1];
						else
							ret = ranges[0] + " - " + ranges[1];
					} else if (ranges.length == 1) {
						ret = ranges[0] + " -";
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception retrieving reception range", e);
		}
		return ret.replaceAll("\\\\\\.br\\\\", "");
	}

	public String getOBXUnits(int i, int j) {
		String ret = "";
		try {
			ca.uhn.hl7v2.model.v25.segment.OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
			ret = getString(obxSeg.getUnits().getIdentifier().getValue());

			// if there are no units specified check the formatted reference
			// range for the units
			if (ret.equals("")) {
				Terser terser = new Terser(msg);
				ret = getString(Terser.get(obxSeg, 7, 0, 2, 1));

				// only display units from the formatted reference range if they
				// have not already been displayed as the reference range
				if (ret.contains("-") || ret.contains("<") || ret.contains(">") || ret.contains("NEGATIVE"))
					ret = "";
			}
		} catch (Exception e) {
			logger.error("Exception retrieving units", e);
		}
		return ret.replaceAll("\\\\\\.br\\\\", "");
	}

	public String getOBXResultStatus(int i, int j) {
		try {

			// result status is stored in the wrong field.... i think
			//return (getString(((ca.uhn.hl7v2.model.v25.segment.OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)).getNatureOfAbnormalTest()
			//	.getValue()));

			ArrayList<ca.uhn.hl7v2.model.v25.segment.OBX> obr_res = obrSegMap
					.get(obrSegKeySet.get(i));
			ca.uhn.hl7v2.model.v25.segment.OBX obx_res = obr_res.get(j);

			//int obx_res_reps = obx_res.getNatureOfAbnormalTestReps();
			//TODO: fix so that it can handle multiple nature results
			/*
			for (int k=0; k<obx_res_reps; k++){
				ca.uhn.hl7v2.model.v25.datatype.ID nature = res.getNatureOfAbnormalTest(k);
				
			}
			*/
			ID nat = obx_res.getNatureOfAbnormalTest(0);
			if (nat.getValue() != null)
				return nat.getValue();

		} catch (Exception e) {
			logger.error("Exception retrieving results status", e);

		}
		return "TDIS";
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
		int obxCount = getOBXCount(i);
		for (int j = 0; j < obxCount; j++) {
			if (getString(
					((obrSegMap.get(obrSegKeySet.get(i))).get(j))
							.getValueType().getValue()).equals("FT"))
				count++;
		}

		return count;

	}

	public String getOBRComment(int i, int j) {
		String comment = "";

		// update j to the number of the comment not the index of a comment
		// array
		j++;
		try {
			Terser terser = new Terser(msg);

			int obxCount = getOBXCount(i);
			int count = 0;
			int l = 0;
			ca.uhn.hl7v2.model.v25.segment.OBX obxSeg = null;

			while (l < obxCount && count < j) {

				obxSeg =  (obrSegMap.get(obrSegKeySet.get(i))).get(l);
				if (getString(obxSeg.getValueType().getValue()).equals("FT")) {
					count++;
				}
				l++;

			}
			l--;

			int k = 0;
			String nextComment = Terser.get(obxSeg, 5, k, 1, 1);
			while (nextComment != null) {
				comment = comment + nextComment.replaceAll("\\\\\\.br\\\\", "<br />");
				k++;
				nextComment = Terser.get(obxSeg, 5, k, 1, 1);
			}

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
		try {
			//for ITS^REPORT return the fields of the ITS REPORT as comments
			if (isReport(i, j))
				return 1; //1 line of report per OBX line as comment

			Terser terser = new Terser(msg);
			String comment = "";
			OBX obxSeg = getOBX(i, j);
			while (comment != null) {
				count++;
				comment = Terser.get(obxSeg, 7, count, 1, 1);
				if (comment == null)
					comment = Terser.get(obxSeg, 7, count, 2, 1);
			}
			count--;

		} catch (Exception e) {
			logger.error("Exception retrieving obx comment count", e);
			count = 0;
		}
		return count;
	}

	public String getOBXComment(int i, int j, int k) {
		String comment = "";
		
		try {
			if (isReport(i, j)) {
				return getOBXResult(i, j, true);
			}
			k++;

			Terser terser = new Terser(msg);
			ca.uhn.hl7v2.model.v25.segment.OBX obxSeg = (ca.uhn.hl7v2.model.v25.segment.OBX) ((ArrayList) obrSegMap
					.get(obrSegKeySet.get(i))).get(j);
			comment = Terser.get(obxSeg, 7, k, 1, 1);
			if (comment == null)
				comment = Terser.get(obxSeg, 7, k, 2, 1);
			
			

		} catch (Exception e) {
			logger.error("Cannot return comment", e);
		}
		
		return comment.replaceAll("\\\\\\.br\\\\", "<br />");
	}

	private OBX getOBX(int i, int j) {
		return (obrSegMap.get(obrSegKeySet.get(i))).get(j);
	}

	/**
	 * Methods to get information about the patient
	 */
	public String getPatientName() {
		return (getFirstName() + " " + getLastName());
	}

	public String getFirstName() {

		try {
			
			return getString(this.pat_25.getPID().getPatientName(0).getGivenName().getValue());

		} catch (HL7Exception e) {
			logger.error("Exception while parsing first name of patient: " + e);
		}
		return null;
		//return (getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getGivenName().getValue()));
	}

	public String getLastName() {
		try {
			return getString(this.pat_25.getPID().getPatientName(0).getFamilyName().getSurname().getValue());
		} catch (HL7Exception e) {
			logger.error("Exception while parsing last name of patient: " + e);
		}
		return "";
	}

	public String getDOB() {
		try {
			return (formatDateTime(getString(pat_25.getPID().getDateTimeOfBirth().getTime().getValue())).substring(0,
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
		return getString(pat_25.getPID().getAdministrativeSex().getValue());
	}

	public String getHealthNum() {
		//int pat_ids = pat_25.getPID().getPatientIdentifierListReps();
		 String hin;
		try {
			String[] hinArray = null ;
			ca.uhn.hl7v2.model.v25.datatype.CX patIdList = pat_25.getPID().getPatientIdentifierList(0);
			String hnumber = patIdList.getIDNumber().getValue();
			if (hnumber != null)
				hinArray = hnumber.split("-");
				if ((hinArray!=null)&& hinArray.length>0) {
				hin = hinArray[0];
				return hin; } //return first instance that is not null
		} catch (HL7Exception e) {
			logger.error("ERROR getting the health number for HL7 lab report patient: " + e.toString());
		}

		return "";
	}

	public String getHomePhone() {
		String phone = "";
		int i = 0;
		try {
			while (!getString(pat_25.getPID().getPhoneNumberHome(i).getAnyText().getValue()).equals("")) {
				if (i == 0) {
					phone = getString(pat_25.getPID().getPhoneNumberHome(i).getAnyText().getValue());
				} else {
					phone = phone + ", " + getString(pat_25.getPID().getPhoneNumberHome(i).getAnyText().getValue());
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
			while (!getString(pat_25.getPID().getPhoneNumberBusiness(i).getAnyText().getValue()).equals("")) {
				if (i == 0) {
					phone = getString(pat_25.getPID().getPhoneNumberBusiness(i).getAnyText().getValue());
				} else {
					phone = phone + ", " + getString(pat_25.getPID().getPhoneNumberBusiness(i).getAnyText().getValue());
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
		String locacronym = getString(msg.getMSH().getSendingFacility().getNamespaceID().getValue());
		String location = null;
		HL7HandlerMSHMappingDao hL7HandlerMSHMappingDao = (HL7HandlerMSHMappingDao) SpringUtils.getBean("hl7HandlerMSHMappingDao");
		
		try{
			HL7HandlerMSHMapping mapping = hL7HandlerMSHMappingDao.findByFacility(locacronym);
			location = mapping.getFacilityName();
         } catch(Exception e){
             logger.error("Exception getting HL7 patientLocation", e);
         }
         if (location==null) return locacronym; else return location;
		
	}

	public String getServiceDate() {
		try {
			return formatDateTime(getString(obrSegKeySet.get(0).getObservationDateTime().getTime().getValue()));
		} catch (Exception e) {
			logger.error("Exception retrieving service date", e);
		}
		return "";
	}

	public String getOrderStatus() {
		/*String res = (String) orderStatus.get(0);
		if (res != null && res.equals("0"))
			return "P";
		return "F";*/
		String status  = getString(obrSegKeySet.get(0).getResultStatus().getValue()) ;
		return status;
	}

	/*public String getReportStatus() {
		String status  = getString(obrSegKeySet.get(0).getObr25_ResultStatus().getValue()) ;
		return status;
	}*/
	
	public String getClientRef() {
		try {

			//getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDInternalID(0).getAssigningAuthority().getNamespaceID().getValue()));
			String fillerOrder = msg.getPATIENT_RESULT(0).getORDER_OBSERVATION(0).getORC().getOrderingProvider(0)
					.getAssigningAuthority().getNamespaceID().getValue();

			PID pid = pat_25.getPID();
			CX lol = pid.getPatientID();
			
			//		.getNamespaceID().getValue();
			String one = lol.getAssigningAuthority().getNamespaceID().getValue();
			String two = lol.getAssigningFacility().getNamespaceID().getValue();
			if (one != null)
				return one;
			else if (two != null)
				return two;
			else if (fillerOrder != null)
				return fillerOrder;

			//return (getString(pat_25.getPID().getPatientID().getAssigningAuthority().getNamespaceID().getValue()));
		} catch (Exception e) {
			logger.error("ERROR Could not return accession num, getClientRef(): ", e);
		}
		return "00000000";
	}

	public String getAccessionNum() {
		try {
			return msg.getMSH().getMessageControlID().getValue().toString();
		} catch (Exception e) {
			logger.error("Could not return accession num: ", e);
		}
		return "00000";
	}

	public String getDocName() {
		String docName = "";
		int i = 0;
		try {
			while (!getFullDocName(obrSegKeySet.get(0).getOrderingProvider(i)).equals("")) {
				if (i == 0) {
					docName = getFullDocName(obrSegKeySet.get(0).getOrderingProvider(i));
				} else {
					docName = docName + ", " + getFullDocName(obrSegKeySet.get(0).getOrderingProvider(i));
				}
				i++;
			}
			return (docName);
		} catch (Exception e) {
			logger.error("Could not return doctor names", e);

		}
		return "";	
	}
 
	public String getCCDocs() {

		String docNames = "";

		try {
			int ccs = obrSegKeySet.get(0).getResultCopiesToReps();
			for (int j=0; j<ccs;j++){
				String name = getFullDocName(obrSegKeySet.get(0).getResultCopiesTo(j));
				if (name != null && !name.equals("")) {
					if (j>0) docNames +=", "+name;
					else docNames +=name;
				}
			}

			return (docNames);

		} catch (Exception e) {
			// ignore error... it will occur when the zdr segment is not present
			// logger.error("Could not retrieve cc'd docs", e);
		}
		return "";

	}

	public ArrayList<String> getDocNums() {
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		String docNum = "";
		ArrayList<String> nums = new ArrayList<String>();
		int i = 0;
		try {

			// requesting client number
			docNum = obrSegKeySet.get(0).getOrderingProvider(i).getIDNumber().getValue();
			String billingNum = providerDao.getProviderByPractitionerNo(docNum).getOhipNo();
			nums.add(billingNum == null?docNum:billingNum);
			// cc'd docs numbers
			int ccs = obrSegKeySet.get(0).getResultCopiesToReps();
			for (int j=0; j<ccs;j++){
				String num = obrSegKeySet.get(0).getResultCopiesTo(j).getIDNumber().getValue();
				if (num != null && !num.equals("") && !num.equals(docNum)) {
					billingNum = providerDao.getProviderByPractitionerNo(num).getOhipNo();
					nums.add(billingNum == null?num:billingNum);
				}
			}

		} catch (Exception e) {
			// ignore error... it will occur when the zdr segment is not present
			// logger.error("Could not return numbers", e);
		}

		return nums;
	}

	public String audit() {
		return "";
	}

	private String getFullDocName(ca.uhn.hl7v2.model.v25.datatype.XCN xcn) {
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
	}

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
		String fonum = "";
		String entityId = getString(obrSegKeySet.get(0).getFillerOrderNumber().getEntityIdentifier().getValue());
		String namseSpaceId = getString(obrSegKeySet.get(0).getFillerOrderNumber().getNamespaceID().getValue());
		String universalId = getString(obrSegKeySet.get(0).getFillerOrderNumber().getUniversalID().getValue());
		String universalIdType = getString(obrSegKeySet.get(0).getFillerOrderNumber().getUniversalIDType().getValue()); 
		
		fonum = entityId+namseSpaceId+universalId+universalIdType;
		
		return fonum.toString();
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

	@Override
    public String getRequestDate(int i) {
	    // TODO Auto-generated method stub
	    return null;
    }
    public String getNteForPID(){
    	
    	return "";
    }
}
