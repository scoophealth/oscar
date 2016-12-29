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

import org.apache.log4j.Logger;

import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v23.datatype.FT;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.NTE;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.model.v23.segment.ORC;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 * Dual message handler for both the manual and automated lab uploads in the Calgary Lab Service HL7 format.
 *
 */
public class CLSHandler implements MessageHandler {

	private enum NameType {
		FIRST, MIDDLE, LAST;
	}

	private static Logger logger = Logger.getLogger(CLSHandler.class);

	private ORU_R01 msg;

	private Terser terser;


	public void init(String hl7Body) throws HL7Exception {
		Parser p = new PipeParser();
		p.setValidationContext(new NoValidation());
		msg = (ORU_R01) p.parse(hl7Body);
		terser = new Terser(msg);
	}

	public String getMsgType() {
		return "CLS";
	}

	/**
	 * This is the OBR date. The MessageHandler architecture uses this to store in hl7TextInfo.obr_date
	 */
	public String getMsgDate() {
		try {
			return (formatDateTime(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue()));
		} catch (Exception e) {
			logger.error("Could not retrieve message date", e);
			return ("");
		}
	}

	public Date getMsgDateTime() {
		try {
			String dateFormat = "yyyyMMddHHmmss";
			Date dateTime = UtilDateUtilities.StringToDate(get("/.MSH-7"), dateFormat);
			return dateTime;
		} catch (Exception e) {
			logger.error("Could not retrieve message date", e);
			return null;
		}
	}

	public String getMsgPriority() {
		return ("");
	}

	public int getOBRCount() {
		return (msg.getRESPONSE().getORDER_OBSERVATIONReps());
	}

	public ORU_R01_ORDER_OBSERVATION getOBR(int i) throws HL7Exception {
		return msg.getRESPONSE().getORDER_OBSERVATION(i);
	}

	public ORC getORC(int i) throws HL7Exception {
        return msg.getRESPONSE().getORDER_OBSERVATION(i).getORC();
	}

	public OBX getOBX(int i, int ii) throws HL7Exception {
		return msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(ii).getOBX();
	}

	public NTE getNTE(int i, int j, int k) throws HL7Exception {
        return msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k);
	}

	public NTE getOBRNTE(int i, int j) throws HL7Exception {
        return msg.getRESPONSE().getORDER_OBSERVATION(i).getNTE(j);
	}

	public void insertOBR(ORU_R01_ORDER_OBSERVATION newOBR) {
		try {
        	msg.getRESPONSE().insertORDER_OBSERVATION(newOBR, getOBRCount());
        } catch (HL7Exception e) {
			logger.error("Error Adding OBR segment.", e);
        }
	}

	public int getOBXCount(int i) {
		try {
			return (msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps());
		} catch (Exception e) {
			return (0);
		}
	}

	public String getOBRName(int i) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getText().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getTimeStamp(int i, int j) {
		try {
			return (formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue())));
		} catch (Exception e) {
			return ("");
		}
	}

	public boolean isOBXAbnormal(int i, int j) {
		try {
			return getOBXAbnormalFlag(i, j).equals("C") || getOBXAbnormalFlag(i, j).equals("H") || getOBXAbnormalFlag(i, j).equals("L") || getOBXAbnormalFlag(i, j).equals("A");
		} catch (Exception e) {
			return false;
		}
	}

	public String getOBXAbnormalFlag(int i, int j) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getAbnormalFlags(0).getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getObservationHeader(int i, int j) {
		return getOBRName(i);
	}

	public String getOBXIdentifier(int i, int j) {
		try {
			Segment obxSeg = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
			String subIdent = Terser.get(obxSeg, 3, 0, 1, 2);
			if (subIdent != null) { //HACK: for gdml labs generated with SubmitLabByFormAction
				return getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()) + "&" + subIdent;
			}
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getOBXValueType(int i, int j) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getOBXName(int i, int j) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getText().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getOBXResult(int i, int j) {
		try {
			return (getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(), 5, 0, 1, 1)));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getOBXReferenceRange(int i, int j) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getReferencesRange().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getOBXUnits(int i, int j) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getUnits().getIdentifier().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getOBXResultStatus(int i, int j) {
		String status = "";
		try {
			status = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservResultStatus().getValue());
			if (status.equalsIgnoreCase("C")) {
				status = "Corrected";
			} else if (status.equalsIgnoreCase("F")) {
				status = "Final";
			} else if (status.equalsIgnoreCase("P")) {
				status = "Preliminary";
			}
			// TODO find out about "Cancelled" status
		} catch (Exception e) {
			logger.error("Error retrieving obx result status", e);
			return status;
		}
		return status;
	}

	public int getOBXFinalResultCount() {
		int obrCount = getOBRCount();
		int obxCount;
		int count = 0;
		for (int i = 0; i < obrCount; i++) {
			obxCount = getOBXCount(i);
			for (int j = 0; j < obxCount; j++) {
				if (getOBXResultStatus(i, j).equals("Final") ||
						getOBXResultStatus(i, j).equals("Corrected"))
				{
					count++;
				}
			}
		}
		return count;
	}

	/**
	 *  Retrieve the possible segment headers from the OBX fields
	 */
	public ArrayList<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		for (int i = 0; i < getOBRCount(); i++) {
			String obrName = getOBRName(i);
			if (!headers.contains(obrName)) {
				headers.add(obrName);
			}
		}
		return headers;
	}

	/**
	 *  Methods to get information from observation notes
	 */
	public int getOBRCommentCount(int i) {
		// OBR comments are not provided - comments are provided in NTE segment following OBX segment
		int count = 0;
		try {
			count = msg.getRESPONSE().getORDER_OBSERVATION(i).getNTEReps();
		} catch(Exception e) {
			return 0;
		}

		return count;
	}

	public String getOBRComment(int i, int j) {
		try {
			FT[] tmp = msg.getRESPONSE().getORDER_OBSERVATION(i).getNTE(j).getComment();
			StringBuilder comment = new StringBuilder();
			for(FT t:tmp) {
				comment.append(t.getValue());
			}
			return comment.toString();
		} catch (Exception e) {
			return ("");
		}
	}

	/**
	 *  Methods to get information from observation notes
	 */
	public int getOBXCommentCount(int i, int j) {
		int count = 0;
		try {
			count = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTEReps();

			// a bug in getNTEReps() causes it to return 1 instead of 0 so we check to make
			// sure there actually is a comment there
			if (count == 1) {
				String comment = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE().getComment(0).getValue();
				if (comment == null) count = 0;
			}

		} catch (Exception e) {
			logger.error("Error retrieving obx comment count", e);
		}
		return count;
	}

	public String getOBXComment(int i, int j, int k) {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k).getComment(0).getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getPatientName() {
		return (getFirstName() + " " + getLastName());
	}

	public String getFirstName() {
		return getName(NameType.FIRST);
	}

	private String getName(NameType type) {
		// PID contains
		// Last, First Middle
		String name = get("/.PID-5-1");
		if (name == null) {
			return "";
		}

		int firstLastDelimiterIndex = name.indexOf(',');
		if (firstLastDelimiterIndex == -1) {
			if (type == NameType.LAST) {
				return name.trim(); // assume it's the last name
			} else {
				return "";
			}
		}

		String last = name.substring(0, firstLastDelimiterIndex).trim();
		if (type == NameType.LAST) {
			return last; // assume it's the last name
		}

		String firstMiddle = name.substring(Math.min(name.length(), firstLastDelimiterIndex + 1));
		if (firstMiddle != null) {
			firstMiddle = firstMiddle.trim();
		}

		int firstMiddleDelimiterIndex = firstMiddle.lastIndexOf(' ');
		if (firstMiddleDelimiterIndex == -1) {
			if (type == NameType.FIRST) {
				return firstMiddle; // assume it's the first name
			} else {
				return "";
			}
		}

		String result;
		if (type == NameType.FIRST) {
			result = firstMiddle.substring(0, firstMiddleDelimiterIndex);
			return result.trim();
		} else if (type == NameType.MIDDLE) {
			result = firstMiddle.substring(firstMiddleDelimiterIndex);
			return result.trim();
		}

		throw new IllegalArgumentException("Invalid name type " + type);
	}

	public String getLastName() {
		return getName(NameType.LAST);
	}

	public String getDOB() {
		try {
			return (formatDateTime(getString(msg.getRESPONSE().getPATIENT().getPID().getDateOfBirth().getTimeOfAnEvent().getValue())));
		} catch (Exception e) {
			return ("");
		}
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
		return (getString(msg.getRESPONSE().getPATIENT().getPID().getSex().getValue()));
	}

	public String getHealthNum() {
		return get("/.PID-2-1");
	}

	public String getHomePhone() {
		return "";
	}

	public String getWorkPhone() {
		return "";
	}

	public String getPatientLocation() {
		return (getString(msg.getMSH().getSendingFacility().getNamespaceID().getValue()));
	}

	public String getServiceDate() {
		try {
			String serviceDate = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getOrderEffectiveDateTime().getTimeOfAnEvent().getValue());
			if (serviceDate == null || serviceDate.isEmpty()) {
				serviceDate = get("/.OBR-14");
			}
			return (formatDateTime(serviceDate));
		} catch (Exception e) {
			return ("");
		}
	}

	public String getRequestDate(int i) {
		try {
			return (formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getRequestedDateTime().getTimeOfAnEvent().getValue())));
		} catch (Exception e) {
			return ("");
		}
	}

	   public String getOrderStatus(){
	        try{
	        	// of ORC is present - return it
	        	String orderStatus = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getOrderStatus().getValue());
	        	if (orderStatus != null && !orderStatus.isEmpty()) {
	        		return orderStatus;
	        	}
	        	// otherwise get first OBR status
	            return get("/.OBR-25-1");
	        }catch(Exception e){
	            return("");
	        }
	    }

	public String getClientRef() {
		String docNum = "";
		int i = 0;
		try {
			while (!getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue()).equals("")) {
				if (i == 0) {
					docNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue());
				} else {
					docNum = docNum + ", " + getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue());
				}
				i++;
			}
			return (docNum);
		} catch (Exception e) {
			logger.error("Could not return doctor id numbers", e);
			return ("");
		}
	}

	public String getDocName() {
		String docName = "";
		int i = 0;
		try {
			while (!getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i)).equals("")) {
				if (i == 0) {
					docName = getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i));
				} else {
					docName = docName + ", " + getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i));
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
		int i = 0;
		try {
			while (!getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i)).equals("")) {
				if (i == 0) {
					docName = getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
				} else {
					docName = docName + ", " + getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
				}
				i++;
			}
			return (docName);
		} catch (Exception e) {
			logger.error("Could not return cc'ed doctors", e);
			return ("");
		}
	}

	public ArrayList<String> getDocNums() {
		ArrayList<String> docNums = new ArrayList<String>();
		String id;
		int i;

		try {
			String providerId = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(0).getIDNumber().getValue();
			docNums.add(providerId);

			i = 0;
			while ((id = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i).getIDNumber().getValue()) != null) {
				if (!id.equals(providerId)) docNums.add(id);
				i++;
			}
		} catch (Exception e) {
			logger.error("Could not return doctor nums", e);

		}

		return (docNums);
	}

	public String audit() {
		return "";
	}

	private String getFullDocName(XCN docSeg) {
		String docName = "";

		if (docSeg.getPrefixEgDR().getValue() != null) docName = docSeg.getPrefixEgDR().getValue();

		if (docSeg.getGivenName().getValue() != null) {
			if (docName.equals("")) docName = docSeg.getGivenName().getValue();
			else docName = docName + " " + docSeg.getGivenName().getValue();
		}
		if (docSeg.getMiddleInitialOrName().getValue() != null) {
			if (docName.equals("")) docName = docSeg.getMiddleInitialOrName().getValue();
			else docName = docName + " " + docSeg.getMiddleInitialOrName().getValue();
		}
		if (docSeg.getFamilyName().getValue() != null) {
			if (docName.equals("")) docName = docSeg.getFamilyName().getValue();
			else docName = docName + " " + docSeg.getFamilyName().getValue();
		}
		if (docSeg.getSuffixEgJRorIII().getValue() != null) {
			if (docName.equals("")) docName = docSeg.getSuffixEgJRorIII().getValue();
			else docName = docName + " " + docSeg.getSuffixEgJRorIII().getValue();
		}
		if (docSeg.getDegreeEgMD().getValue() != null) {
			if (docName.equals("")) docName = docSeg.getDegreeEgMD().getValue();
			else docName = docName + " " + docSeg.getDegreeEgMD().getValue();
		}

		return (docName);
	}

	protected String formatDateTime(String plain) {
		if (plain == null || plain.trim().equals("")) return "";

		String dateFormat = "yyyyMMddHHmmss";
		dateFormat = dateFormat.substring(0, plain.length());
		String stringFormat = "yyyy-MM-dd HH:mm:ss";
		stringFormat = stringFormat.substring(0, stringFormat.lastIndexOf(dateFormat.charAt(dateFormat.length() - 1)) + 1);

		Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
		return UtilDateUtilities.DateToString(date, stringFormat);
	}

	protected String getString(String retrieve) {
		if (retrieve != null) {
			retrieve.replaceAll("^", " ");
			return (retrieve.trim());
		} else {
			return ("");
		}
	}

	public String getFillerOrderNumber() {
		// this is different from the filler order number in ORC
		return get("/.OBR-3-1");
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

	public String getAssigningAuthority() {
		return get("/.PID-2-4");
	}

	private String get(String path) {
		try {
			return terser.get(path);
		} catch (HL7Exception e) {
			logger.warn("Unable to get field at " + path, e);
			return null;
		}
	}

	public String getMiddleName() {
		return getName(NameType.MIDDLE);
	}

	/**
	 * Gets the ordering provider name.
	 *
	 * @return
	 * 		Returns the provider name or an empty string if it's not specified
	 */
	public String getOrderingProvider() {
		return get("/.OBR-16-2");
	}

	/**
	 * Gets the ordering provider ID for matching provider with the correct inbox routing.
	 *
	 * @return
	 * 		Returns the provider id or an empty string if it's not specified
	 */
	public String getOrderingProviderId() {
		return get("/.OBR-16-1");
	}

	/**
	 * Gets the date and time the specimen was collected
	 *
	 * @param i
	 * 		Segment count
	 * @return
	 * 		Returns the date / time of the specimen collection or null if it's not available.
	 */
	public String getOBRDateTime(int i) {
		return get("/.OBR-7-1");
	}

	/**
	 * Gets the date and time the specimen was collected
	 * 
	 * @param i
	 * 		Segment count
	 * @return
	 * 		Returns the date / time of the specimen collection or null if it's not available.
	 */
	public Date getOBRDateTimeAsDate(int i) {
		// 20101203122200
		String date = getOBRDateTime(i);
		if (date == null || date.equals("")) {
			return null;
		}
		return ConversionUtils.fromDateString(date, "yyyyMMddHHmmss");
	}

	public String getAccessionNum() {
		return get("/.OBR-20");
	}

	////this.isUnstructuredDoc = "TX".equals(handler.getOBXValueType(0,0));
	public boolean isUnstructured() {
		boolean result=true;
		for(int j = 0; j<this.getOBRCount();j++) {
			for(int k=0;k<this.getOBXCount(j);k++) {
				if(!"TX".equals(getOBXValueType(j, k))) {
					result=false;
				}
			}
		}
		return result;
	}

    public String getNteForPID() {
	    return "";
    }
}
