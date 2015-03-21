/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
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
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextMessageInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v22.datatype.CN;
import ca.uhn.hl7v2.model.v22.message.ORU_R01;
import ca.uhn.hl7v2.model.v22.segment.OBR;
import ca.uhn.hl7v2.model.v22.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 * 
 * @author lingmin.zhou  jackeyoption@gmail.com
 */
public class AlphaHL7Handler implements MessageHandler {

	Logger logger = Logger.getLogger(AlphaHL7Handler.class);
	ORU_R01 msg = null;
	ArrayList<String> headers = null;
	HashMap<OBR, ArrayList<OBX>> obrSegMap = null;
	ArrayList<OBR> obrSegKeySet = null;

	/** Creates a new instance of CMLHandler */
	public AlphaHL7Handler() {
	}

	public void init(String hl7Body) throws HL7Exception {

		Parser p = new PipeParser();
		p.setValidationContext(new NoValidation());
		msg = (ORU_R01) p.parse(hl7Body.replaceAll("\n", "\r\n"));

		ArrayList<String> labs = getMatchingAlphaLabs(hl7Body);
		headers = new ArrayList<String>();
		obrSegMap = new LinkedHashMap<OBR, ArrayList<OBX>>();
		obrSegKeySet = new ArrayList<OBR>();

		for (int i = 0; i < labs.size(); i++) {
			msg = (ORU_R01) p.parse((labs.get(i)).replaceAll("\n", "\r\n"));
			int obrCount = msg.getPATIENT_RESULT().getORDER_OBSERVATIONReps();

			for (int j = 0; j < obrCount; j++) {

				// ADD OBR SEGMENTS AND THEIR OBX SEGMENTS TO THE OBRSEGMAP
				OBR obrSeg = msg.getPATIENT_RESULT().getORDER_OBSERVATION(j)
						.getOBR();
				ArrayList<OBX> obxSegs = obrSegMap.get(obrSeg);

				// if the obrsegment has not yet been created it will be now
				if (obxSegs == null)
					obxSegs = new ArrayList<OBX>();

				int obxCount = msg.getPATIENT_RESULT().getORDER_OBSERVATION(j)
						.getOBSERVATIONReps();
				for (int k = 0; k < obxCount; k++) {
					obxSegs.add(msg.getPATIENT_RESULT().getORDER_OBSERVATION(j)
							.getOBSERVATION(k).getOBX());
				}

				obrSegMap.put(obrSeg, obxSegs);
				obrSegKeySet.add(obrSeg);

				// ADD THE HEADER TO THE HEADERS ARRAYLIST
				String header = getString(Terser.get(obrSeg, 4, 0, 2, 1));
				if (!headers.contains(header)) {
					headers.add(header);
				}
			}
		}
	}

	private ArrayList<String> getMatchingAlphaLabs(String hl7Body) {
		Base64 base64 = new Base64(0);
		ArrayList<String> ret = new ArrayList<String>();
		int monthsBetween = 0;
		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils
				.getBean("hl7TextInfoDao");

		try {
			List<Hl7TextMessageInfo> matchingLabs = hl7TextInfoDao
					.getMatchingLabs(hl7Body);
			for (Hl7TextMessageInfo l : matchingLabs) {
				Date dateA = UtilDateUtilities.StringToDate(l.labDate_A,
						"yyyy-MM-dd hh:mm:ss");
				Date dateB = UtilDateUtilities.StringToDate(l.labDate_B,
						"yyyy-MM-dd hh:mm:ss");
				if (dateA.before(dateB)) {
					monthsBetween = UtilDateUtilities
							.getNumMonths(dateA, dateB);
				} else {
					monthsBetween = UtilDateUtilities
							.getNumMonths(dateB, dateA);
				}
				if (monthsBetween < 4) {
					ret.add(new String(base64.decode(l.message
							.getBytes("ASCII")), "ASCII"));
				}
				if (l.lab_no_A == l.lab_no_B)
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
		return ("AlphaHL7");
	}

	public String getMsgDate() {
		return (formatDateTime(msg.getMSH().getDateTimeOfMessage()
				.getTimeOfAnEvent().getValue()));
	}

	public String getMsgPriority() {
		String priority = "R";
		for (int i = 0; i < getOBRCount(); i++) {
			try {
				if (getString(
						msg.getPATIENT_RESULT().getORDER_OBSERVATION(i)
								.getOBR().getPriorityNotused().getValue())
						.equals("S")) {
					priority = "S";
					break;
				}
			} catch (Exception e) {
				logger.error("Error finding priority", e);
			}
		}

		return priority;
	}

	/**
	 * Methods to get information about the Observation Request
	 */
	public int getOBRCount() {
		return (obrSegMap.size());
	}

	public int getOBXCount(int i) {
		return ((obrSegMap.get(obrSegKeySet.get(i))).size());
	}

	public String getOBRName(int i) {
		try {
			return (getString(Terser.get(obrSegKeySet.get(i), 4, 0, 2, 1)));
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	public String getTimeStamp(int i, int j) {
		try {
			String ret = (obrSegKeySet.get(i))
					.getResultsReportStatusChangeDateTime().getTimeOfAnEvent()
					.getValue();
			if (ret == null)
				ret = (obrSegKeySet.get(i)).getObservationDateTime()
						.getTimeOfAnEvent().getValue();
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
			return (getString(((obrSegMap.get(obrSegKeySet.get(i))).get(j))
					.getAbnormalFlags(0).getValue()));
		} catch (Exception e) {
			logger.error("Exception retrieving abnormal flag", e);
			return ("");
		}
	}

	public String getObservationHeader(int i, int j) {
		try {
			return getString(Terser.get(obrSegKeySet.get(i), 4, 0, 2, 1));
		} catch (Exception e) {
			logger.error("Exception gettin header", e);
			return ("");
		}
	}

	public String getOBXIdentifier(int i, int j) {
		try {
//			Segment obxSeg = ().get(j);
//			String ident = getString(Terser.get(obxSeg, 3, 0, 1, 1));
//			String subIdent = Terser.get(obxSeg, 3, 0, 1, 2);
			String ident = getString(obrSegMap.get(obrSegKeySet.get(i)).get(j).getObservationIdentifier().getIdentifier().getValue());
			String subIdent = getString(obrSegMap.get(obrSegKeySet.get(i)).get(j).getObservationSubID().getValue());

			if (subIdent != null)
				ident = ident + "&" + subIdent;

			logger.info("returning obx identifier: " + ident);
			return (ident);
		} catch (Exception e) {
			logger.error("error returning obx identifier", e);
			return ("");
		}
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
		String ret = "";
		try {
			// leave the name blank if the value type is 'FT' this is because it
			// is a comment, if the name is blank the obx segment will not be
			// displayed
			OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
			if (!obxSeg.getValueType().getValue().equals("FT"))
				ret = getString(Terser.get(obxSeg, 3, 0, 2, 1));
		} catch (Exception e) {
			logger.error("Error returning OBX name", e);
		}

		return ret;
	}

	public String getOBXResult(int i, int j) {

		String result = "";
		try {
			result = getString(Terser.get(
					(obrSegMap.get(obrSegKeySet.get(i))).get(j), 5, 0, 1, 1));

			// format the result
			if (result.endsWith("."))
				result = result.substring(0, result.length() - 1);

		} catch (Exception e) {
			logger.error("Exception returning result", e);
		}
		return result;
	}

	public String getOBXReferenceRange(int i, int j) {
		String ret = "";
		try {
			OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);

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
							ranges[k] = ranges[k].substring(0,
									ranges[k].length() - 1);
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
		return ret;
	}

	public String getOBXUnits(int i, int j) {
		String ret = "";
		try {
			OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
			ret = getString(obxSeg.getUnits().getIdentifier().getValue());

			// if there are no units specified check the formatted reference
			// range for the units
			if (ret.equals("")) {
				ret = getString(Terser.get(obxSeg, 7, 0, 2, 1));

				// only display units from the formatted reference range if they
				// have not already been displayed as the reference range
				if (ret.contains("-") || ret.contains("<") || ret.contains(">")
						|| ret.contains("NEGATIVE"))
					ret = "";
			}
		} catch (Exception e) {
			logger.error("Exception retrieving units", e);
		}
		return ret;
	}

	public String getOBXResultStatus(int i, int j) {
		try {
			// result status is stored in the wrong field.... i think
			return (getString(obrSegMap.get(obrSegKeySet.get(i)).get(j).getObservationResultStatus().getValue()));
		} catch (Exception e) {
			logger.error("Exception retrieving results status", e);
			return ("");
		}
	}
	
	public int getOBXFinalResultCount() {
		// not applicable to alpha labs
		int count = 0;
		for (int i = 0; i < obrSegKeySet.size(); i++) {
			OBR obr = obrSegKeySet.get(i);
			for (int j = 0; j < obrSegMap.get(obr).size(); j++){
				if ("F".equalsIgnoreCase(obrSegMap.get(obr).get(j).getObservationResultStatus().getValue())) {
					count++;
				}
			}
		}
		return count;
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

		for (int j = 0; j < getOBXCount(i); j++) {
			if (getString(
					((obrSegMap.get(obrSegKeySet.get(i))).get(j))
							.getValueType().getValue()).equals("FT"))
				count++;
		}

		return count;

	}

	public String getOBRComment(int i, int j) {
		String comment = "";
		// update j to the number of the comment not the index of a comment array
        j++;
		try {
            int count = 0;
            int l = 0;
            OBX obxSeg = null;
            ArrayList<OBX> arrObx = obrSegMap.get(obrSegKeySet.get(i));
            int obxCount = arrObx.size();

            while (l < obxCount && count < j) {
                if (getString(arrObx.get(l).getValueType().getValue()).equals("FT")) {
                    count++;
                }
                l++;
            }
            obxSeg = arrObx.get(l - 1);

			int k = 0;
			String nextComment = Terser.get(obxSeg, 5, k, 1, 1);
			while (nextComment != null) {
				comment = comment + nextComment;
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
			String comment = "";
			OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
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
			k++;
			OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
			comment = Terser.get(obxSeg, 7, k, 1, 1);
			if (comment == null)
				comment = Terser.get(obxSeg, 7, k, 2, 1);

		} catch (Exception e) {
			logger.error("Cannot return comment", e);
		}
		return comment;
	}

	/**
	 * Methods to get information about the patient
	 */
	public String getPatientName() {
		return (getFirstName() + " " + getLastName());
	}

	public String getFirstName() {
		return (getString(msg.getPATIENT_RESULT().getPATIENT().getPID()
				.getPatientName().getGivenName().getValue()));
	}

	public String getLastName() {
		return (getString(msg.getPATIENT_RESULT().getPATIENT().getPID()
				.getPatientName().getFamilyName().getValue()));
	}

	public String getDOB() {
		try {
			return (formatDateTime(getString(msg.getPATIENT_RESULT()
					.getPATIENT().getPID().getDateOfBirth().getTimeOfAnEvent()
					.getValue())).substring(0, 10));
		} catch (Exception e) {
			logger.error("Exception retrieving DOB", e);
			return ("");
		}
	}

	public String getAge() {
		String age = "N/A";
		String dob = getDOB();
		try {
			// Some examples
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date dobDate = formatter.parse(dob);
			java.util.Date serviceDate = formatter.parse(getServiceDate());
			age = UtilDateUtilities.calcAgeAtDate(dobDate, serviceDate);
		} catch (ParseException e) {
			logger.error("Could not get age", e);

		}
		return age;
	}

	public String getSex() {
		return (getString(msg.getPATIENT_RESULT().getPATIENT().getPID()
				.getSex().getValue()));
	}

	public String getHealthNum() {
		String hin = "";
		try {
			hin = getString(Terser.get(msg.getPATIENT_RESULT().getPATIENT().getPID(), 4, 0, 1, 1));
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			logger.debug("Failed to get health number:" + e.toString());
		}
		return hin;
	}

	public String getHomePhone() {
		String phone = "";
		int i = 0;
		try {
			while (!getString(
					msg.getPATIENT_RESULT().getPATIENT().getPID()
							.getPhoneNumberHome(i).getValue()).equals("")) {
				if (i == 0) {
					phone = getString(msg.getPATIENT_RESULT().getPATIENT()
							.getPID().getPhoneNumberHome(i).getValue());
				} else {
					phone = phone
							+ ", "
							+ getString(msg.getPATIENT_RESULT().getPATIENT()
									.getPID().getPhoneNumberHome(i).getValue());
				}
				i++;
			}
			return (phone);
		} catch (Exception e) {
			logger.error("Could not return phone number", e);

			return ("");
		}
	}

	public String getWorkPhone() {
		String phone = "";
		int i = 0;
		try {
			while (!getString(
					msg.getPATIENT_RESULT().getPATIENT().getPID()
							.getPhoneNumberBusiness(i).getValue()).equals("")) {
				if (i == 0) {
					phone = getString(msg.getPATIENT_RESULT().getPATIENT()
							.getPID().getPhoneNumberBusiness(i).getValue());
				} else {
					phone = phone
							+ ", "
							+ getString(msg.getPATIENT_RESULT().getPATIENT()
									.getPID().getPhoneNumberBusiness(i)
									.getValue());
				}
				i++;
			}
			return (phone);
		} catch (Exception e) {
			logger.error("Could not return phone number", e);

			return ("");
		}
	}

	public String getPatientLocation() {
		return (getString(msg.getMSH().getSendingFacility().getValue()));
	}

	public String getServiceDate() {
		try {
			return (formatDateTime(getString(msg.getPATIENT_RESULT()
					.getORDER_OBSERVATION(0).getOBR().getObservationDateTime()
					.getTimeOfAnEvent().getValue())));
		} catch (Exception e) {
			logger.error("Exception retrieving service date", e);
			return ("");
		}
	}

	public String getRequestDate(int i) {
		try {
			String ret = (obrSegKeySet.get(i)).getRequestedDateTimeNotused()
					.getTimeOfAnEvent().getValue();
			return (formatDateTime(getString(ret)));
		} catch (Exception e) {
			logger.error("Exception retrieving request date", e);
			return ("");
		}
	}

	public String getOrderStatus() {
		try {
			return (getString(msg.getPATIENT_RESULT().getORDER_OBSERVATION(0)
					.getOBR().getResultStatus().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getClientRef() {
		/*
		 * String docNum = ""; int i=0; try{
		 * while(!getString(msg.getRESPONSE().getORDER_OBSERVATION
		 * (0).getOBR().getOrderingProvider
		 * (i).getIDNumber().getValue()).equals("")){ if (i==0){ docNum =
		 * getString
		 * (msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider
		 * (i).getIDNumber().getValue()); }else{ docNum = docNum + ", " +
		 * getString
		 * (msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider
		 * (i).getIDNumber().getValue()); } i++; } return(docNum);
		 * }catch(Exception e){
		 * logger.error("Could not return doctor id numbers", e);
		 * 
		 * return(""); }
		 */
		try {
			return (getString(msg.getPATIENT_RESULT().getORDER_OBSERVATION(0)
					.getOBR().getPlacerOrderNumber().getUniquePlacerId()
					.getValue()));
		} catch (Exception e) {
			logger.error("Could not return accession num: ", e);
			return ("");
		}
	}

	public String getAccessionNum() {
		try {
			return (getString(msg.getPATIENT_RESULT().getPATIENT().getPID()
					.getPatientIDInternalID(0).getIDNumber().getValue()));
		} catch (Exception e) {
			logger.error("Could not return accession num: ", e);
			return ("");
		}
	}

	public String getDocName() {
		String docName = "";
		int i = 0;
		try {
			while (!getFullDocName(
					msg.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR()
							.getOrderingProvider()).equals("")) {
				if (i == 0) {
					docName = getFullDocName(msg.getPATIENT_RESULT()
							.getORDER_OBSERVATION(0).getOBR()
							.getOrderingProvider());
				} else {
					docName = docName
							+ ", "
							+ getFullDocName(msg.getPATIENT_RESULT()
									.getORDER_OBSERVATION(i).getOBR()
									.getOrderingProvider());
				}
				i++;
			}
			return (docName);
		} catch (Exception e) {
			logger.error("Could not return doctor names", e);

			return ("");
		}
	}

	public String getCCDocs() {
	      String docName = "";
	        int i=0;
	        try{
	            while(!getFullDocName(msg.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i)).equals("")){
	                if (i==0){
	                    docName = getFullDocName(msg.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
	                }else{
	                    docName = docName + ", " + getFullDocName(msg.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
	                }
	                i++;
	            }
	            return(docName);
	        }catch(Exception e){
	            logger.error("Could not return cc'ed doctors", e);
	            return("");
	        }
	}

	public ArrayList<String> getDocNums() {
		ArrayList<String> docNums = new ArrayList<String>();
        String id;
        int i;
        try{
            String providerId = msg.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getOrderingProvider().getIDNumber().getValue();
            docNums.add(providerId);

            i=0;
            while((id = msg.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i).getIDNumber().getValue()) != null){
                if (!id.equals(providerId))
                    docNums.add(id);
                i++;
            }
        }catch(Exception e){
            logger.error("Could not return doctor nums", e);

        }

        return(docNums);
	}

	public String audit() {
		return "";
	}

	private String getFullDocName(CN docSeg) {
		String docName = "";

		if (docSeg.getPrefixEgDR().getValue() != null)
			docName = docSeg.getPrefixEgDR().getValue();

		if (docSeg.getGivenName().getValue() != null) {
			if (docName.equals(""))
				docName = docSeg.getGivenName().getValue();
			else
				docName = docName + " " + docSeg.getGivenName().getValue();

		}
		if (docSeg.getMiddleInitialOrName().getValue() != null) {
			if (docName.equals(""))
				docName = docSeg.getMiddleInitialOrName().getValue();
			else
				docName = docName + " "
						+ docSeg.getMiddleInitialOrName().getValue();

		}
		if (docSeg.getFamilyName().getValue() != null) {
			if (docName.equals(""))
				docName = docSeg.getFamilyName().getValue();
			else
				docName = docName + " " + docSeg.getFamilyName().getValue();

		}
		if (docSeg.getSuffixEgJRorIII().getValue() != null) {
			if (docName.equals(""))
				docName = docSeg.getSuffixEgJRorIII().getValue();
			else
				docName = docName + " "
						+ docSeg.getSuffixEgJRorIII().getValue();
		}
		if (docSeg.getDegreeEgMD().getValue() != null) {
			if (docName.equals(""))
				docName = docSeg.getDegreeEgMD().getValue();
			else
				docName = docName + " " + docSeg.getDegreeEgMD().getValue();
		}

		return (docName);
	}

	private String formatDateTime(String plain) {
		if (plain == null || plain.trim().equals(""))
			return "";

		String dateFormat = "yyyyMMddHHmmss";
		dateFormat = dateFormat.substring(0, plain.length());
		String stringFormat = "yyyy-MM-dd HH:mm:ss";
		stringFormat = stringFormat.substring(0, stringFormat
				.lastIndexOf(dateFormat.charAt(dateFormat.length() - 1)) + 1);

		Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
		return UtilDateUtilities.DateToString(date, stringFormat);
	}

	private String getString(String retrieve) {
		if (retrieve != null) {
			return (retrieve.trim());
		} else {
			return ("");
		}
	}

	public String getFillerOrderNumber() {
		try {
			return getString(msg.getPATIENT_RESULT().getORDER_OBSERVATION(0)
					.getOBR().getFillerOrderNumber().getUniqueFillerId()
					.getValue());
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	public String getEncounterId() {
		return "";
	}

	public String getRadiologistInfo() {
		return "";
	}

	public String getNteForOBX(int i, int j) {

		return "";
	}

	public String getNteForPID() {
		return "";
	}

}
