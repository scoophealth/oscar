/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 */
package oscar.oscarLab.ca.all.parsers;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import ca.uhn.hl7v2.HL7Exception;

public class OscarToOscarHl7V2Handler implements MessageHandler {
	private Logger logger = MiscUtils.getLogger();

	public void init(String hl7Body) throws HL7Exception {
		logger.error("not finished : " + hl7Body);
	}

	public String audit() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAccessionNum() {
		// TODO Auto-generated method stub
		logger.error("stubbed test data");
		String result = "1";
		return (result);
	}

	public String getAge() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCCDocs() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientRef() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDOB() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDocName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList getDocNums() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> getHeaders() {
		// TODO Auto-generated method stub
		logger.error("stubbed test data");
		ArrayList<String> results=new ArrayList<String>();
		results.add("referral test");
		return(results);
	}

	public String getHealthNum() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHomePhone() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMsgDate() {
		// TODO Auto-generated method stub
		logger.error("stubbed test data");
		String result = DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date()).replace('T', ' ');
		return (result);
	}

	public String getMsgPriority() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMsgType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBRComment(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOBRCommentCount(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getOBRCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getOBRName(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXAbnormalFlag(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXComment(int i, int j, int k) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOBXCommentCount(int i, int j) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getOBXCount(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getOBXFinalResultCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getOBXIdentifier(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXName(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXReferenceRange(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXResult(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXResultStatus(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXUnits(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOBXValueType(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getObservationHeader(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOrderStatus() {
		// TODO Auto-generated method stub
		logger.error("stubbed test data");
		String result = "1";
		return (result);
	}

	public String getPatientLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPatientName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServiceDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSex() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTimeStamp(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWorkPhone() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOBXAbnormal(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}

}