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


package oscar.oscarLab.ca.all.parsers.OscarToOscarHl7V2;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.oscarehr.common.Gender;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.util.DateUtils;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v26.datatype.DTM;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.PID;

/**
 * This class is loosely based on the MessageHandler interface. The purpose of this class is to provide an adapter for that interface with some minor changes like a default parsing and an init method that's a message instead of a string. The general
 * expectation is that all HL7 messages will include an MSH and a PID, if your message does not, then this class will be of little use to you.
 */
public abstract class ChainnedMessageAdapter<T extends AbstractMessage> implements MessageHandler {
	private static final Logger logger = MiscUtils.getLogger();

	protected T hl7Message;

	/**
	 * This method is unused in this class, use init(Message) instead.
	 */
	public void init(String hl7Body) throws HL7Exception {
		throw (new UnsupportedOperationException("Don't use this method, use init(Message) instead."));
	}

	public ChainnedMessageAdapter(T hl7Message) {
		this.hl7Message = hl7Message;
	}

	public abstract MSH getMsh();

	public abstract PID getPid();

	/**
	 * The message category is just a random string representing the type of hl7 message.
	 */
	public String getMessageStructureType()
	{
		return(getMsh().getMessageType().getMessageStructure().getValue());
	}

	public String getMsgType() {
		return (OscarToOscarUtils.UPLOAD_MESSAGE_TYPE);
	}

	public String getMsgDate() {
		try {
			DTM dtm = getMsh().getDateTimeOfMessage();
			GregorianCalendar cal = DataTypeUtils.getCalendarFromDTM(dtm);
			return (DateUtils.getISODateTimeFormatNoT(cal));
		} catch (DataTypeException e) {
			logger.error("Unexpected error.", e);
			return (null);
		}
	}

	public String getMsgPriority() {
		return ("");
	}

	public int getOBRCount() {
		return (0);
	}

	public int getOBXCount(int i) {
		return (0);
	}

	public String getOBRName(int i) {
		return (null);
	}

	public String getTimeStamp(int i, int j) {
		return (null);
	}

	public boolean isOBXAbnormal(int i, int j) {
		return (false);
	}

	public String getOBXAbnormalFlag(int i, int j) {
		return (null);
	}

	public String getObservationHeader(int i, int j) {
		return (getMessageStructureType());
	}

	public String getOBXIdentifier(int i, int j) {
		return (null);
	}

	public String getOBXValueType(int i, int j) {
		return (null);
	}

	public String getOBXName(int i, int j) {
		return (null);
	}

	public String getOBXResult(int i, int j) {
		return (null);
	}

	public String getOBXReferenceRange(int i, int j) {
		return (null);
	}

	public String getOBXUnits(int i, int j) {
		return (null);
	}

	public String getOBXResultStatus(int i, int j) {
		return (null);
	}

	public ArrayList<String> getHeaders() {
		ArrayList<String> headers=new ArrayList<String>();
		headers.add(getMessageStructureType());
		return (headers);
	}

	public int getOBRCommentCount(int i) {
		return (0);
	}

	public String getOBRComment(int i, int j) {
		return (null);
	}

	public int getOBXCommentCount(int i, int j) {
		return (0);
	}

	public String getOBXComment(int i, int j, int k) {
		return (null);
	}

	public String getPatientName() {
		return (getFirstName() + ' ' + getLastName());
	}

	public String getFirstName() {
		return (getPid().getPatientName()[0].getGivenName().getValue());
	}

	public String getLastName() {
		return (getPid().getPatientName()[0].getFamilyName().getSurname().getValue());
	}

	public String getDOB() {
		try {
			DTM dtm = getPid().getDateTimeOfBirth();
			GregorianCalendar cal = DataTypeUtils.getCalendarFromDTM(dtm);
			
			if (cal==null) return(null);
			
			return (DateUtils.getISODateTimeFormatNoT(cal));
		} catch (DataTypeException e) {
			logger.error("Unexpected Error.", e);
			return (null);
		}
	}

	public String getAge() {
		try {
			DTM dtm = getPid().getDateTimeOfBirth();
			GregorianCalendar cal = DataTypeUtils.getCalendarFromDTM(dtm);
			
			if (cal==null) return(null);
			
			int age = DateUtils.getAge(cal, new GregorianCalendar());
			return (String.valueOf(age));
		} catch (DataTypeException e) {
			logger.error("Unexpected Error.", e);
			return (null);
		}
	}

	public String getSex() {
		String hl7Gender = getPid().getAdministrativeSex().getValue();
		Gender oscarGender = DataTypeUtils.getOscarGenderFromHl7Gender(hl7Gender);
		if (oscarGender==null) return(null);
		return (oscarGender.name());
	}

	public String getHealthNum() {
		try {
			return (getPid().getPatientIdentifierList(0).getIDNumber().getValue());
		} catch (HL7Exception e) {
			logger.error("Unexpected Error.", e);
			return (null);
		}
	}

	public String getHomePhone() {
		try {
			return (getPid().getPhoneNumberHome(0).getUnformattedTelephoneNumber().getValue());
		} catch (HL7Exception e) {
			logger.error("Unexpected Error.", e);
			return (null);
		}
	}

	public String getWorkPhone() {
		return (null);
	}

	public String getPatientLocation() {
		return (getMsh().getSendingFacility().getNamespaceID().getValue());
	}

	public String getServiceDate() {
		return (null);
	}

	public String getRequestDate(int i) {
		return (null);
	}

	public String getOrderStatus() {
		return ("F");
	}

	public int getOBXFinalResultCount() {
		return (0);
	}

	public String getClientRef() {
		return (null);
	}

	public String getAccessionNum()
	{
		return(null);
	}

	public abstract String getDocName();
	
	public String getCCDocs()
	{
		return(null);
	}

	public ArrayList<String> getDocNums()
	{
		return(null);
	}

	public String audit()
	{
		return("");
	}
}
