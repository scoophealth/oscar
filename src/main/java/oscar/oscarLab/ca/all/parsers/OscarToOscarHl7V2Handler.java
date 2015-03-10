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

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OscarToOscarUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.parsers.OscarToOscarHl7V2.ChainnedMessageAdapter;
import oscar.oscarLab.ca.all.parsers.OscarToOscarHl7V2.OruR01Handler;
import oscar.oscarLab.ca.all.parsers.OscarToOscarHl7V2.RefI12Handler;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.message.REF_I12;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.PID;

public final class OscarToOscarHl7V2Handler implements MessageHandler {
	private static Logger logger = MiscUtils.getLogger();
	
	private ChainnedMessageAdapter<? extends AbstractMessage> chainnedMessageAdapter;
	
	public void init(String hl7Body) throws HL7Exception {
		AbstractMessage message=OscarToOscarUtils.pipeParserParse(hl7Body);

		logger.debug("Received hl7 message type : "+message.getClass().getName());
		
		if (message instanceof REF_I12) chainnedMessageAdapter=new RefI12Handler((REF_I12) message);
		else if (message instanceof ORU_R01) chainnedMessageAdapter=new OruR01Handler((ORU_R01) message);
		else logger.error("Recevied unsupported message type : "+message.getClass().getSimpleName());
	}

	public String audit() {
	    return chainnedMessageAdapter.audit();
    }

	public String getAccessionNum() {
	    return chainnedMessageAdapter.getAccessionNum();
    }

	public String getAge() {
	    return chainnedMessageAdapter.getAge();
    }

	public String getCCDocs() {
	    return chainnedMessageAdapter.getCCDocs();
    }

	public String getClientRef() {
	    return chainnedMessageAdapter.getClientRef();
    }

	public String getDOB() {
	    return chainnedMessageAdapter.getDOB();
    }

	public String getDocName() {
	    return chainnedMessageAdapter.getDocName();
    }

	public ArrayList<String> getDocNums() {
	    return chainnedMessageAdapter.getDocNums();
    }

	public String getFirstName() {
	    return chainnedMessageAdapter.getFirstName();
    }

	public ArrayList<String> getHeaders() {
	    return chainnedMessageAdapter.getHeaders();
    }

	public String getHealthNum() {
	    return chainnedMessageAdapter.getHealthNum();
    }

	public String getHomePhone() {
	    return chainnedMessageAdapter.getHomePhone();
    }

	public String getLastName() {
	    return chainnedMessageAdapter.getLastName();
    }

	public String getMsgDate() {
	    return chainnedMessageAdapter.getMsgDate();
    }

	public String getMsgPriority() {
	    return chainnedMessageAdapter.getMsgPriority();
    }

	public String getMsgType() {
	    return chainnedMessageAdapter.getMsgType();
    }

	public MSH getMsh() {
	    return chainnedMessageAdapter.getMsh();
    }

	public String getOBRComment(int i, int j) {
	    return chainnedMessageAdapter.getOBRComment(i, j);
    }

	public int getOBRCommentCount(int i) {
	    return chainnedMessageAdapter.getOBRCommentCount(i);
    }

	public int getOBRCount() {
	    return chainnedMessageAdapter.getOBRCount();
    }

	public String getOBRName(int i) {
	    return chainnedMessageAdapter.getOBRName(i);
    }

	public String getObservationHeader(int i, int j) {
	    return chainnedMessageAdapter.getObservationHeader(i, j);
    }

	public String getOBXAbnormalFlag(int i, int j) {
	    return chainnedMessageAdapter.getOBXAbnormalFlag(i, j);
    }

	public String getOBXComment(int i, int j, int k) {
	    return chainnedMessageAdapter.getOBXComment(i, j, k);
    }

	public int getOBXCommentCount(int i, int j) {
	    return chainnedMessageAdapter.getOBXCommentCount(i, j);
    }

	public int getOBXCount(int i) {
	    return chainnedMessageAdapter.getOBXCount(i);
    }

	public int getOBXFinalResultCount() {
	    return chainnedMessageAdapter.getOBXFinalResultCount();
    }

	public String getOBXIdentifier(int i, int j) {
	    return chainnedMessageAdapter.getOBXIdentifier(i, j);
    }

	public String getOBXName(int i, int j) {
	    return chainnedMessageAdapter.getOBXName(i, j);
    }

	public String getOBXReferenceRange(int i, int j) {
	    return chainnedMessageAdapter.getOBXReferenceRange(i, j);
    }

	public String getOBXResult(int i, int j) {
	    return chainnedMessageAdapter.getOBXResult(i, j);
    }

	public String getOBXResultStatus(int i, int j) {
	    return chainnedMessageAdapter.getOBXResultStatus(i, j);
    }

	public String getOBXUnits(int i, int j) {
	    return chainnedMessageAdapter.getOBXUnits(i, j);
    }

	public String getOBXValueType(int i, int j) {
	    return chainnedMessageAdapter.getOBXValueType(i, j);
    }

	public String getOrderStatus() {
	    return chainnedMessageAdapter.getOrderStatus();
    }

	public String getPatientLocation() {
	    return chainnedMessageAdapter.getPatientLocation();
    }

	public String getPatientName() {
	    return chainnedMessageAdapter.getPatientName();
    }

	public PID getPid() {
	    return chainnedMessageAdapter.getPid();
    }

	public String getServiceDate() {
	    return chainnedMessageAdapter.getServiceDate();
    }

	public String getRequestDate(int i) {
	    return chainnedMessageAdapter.getRequestDate(i);
    }

	public String getSex() {
	    return chainnedMessageAdapter.getSex();
    }

	public String getTimeStamp(int i, int j) {
	    return chainnedMessageAdapter.getTimeStamp(i, j);
    }

	public String getWorkPhone() {
	    return chainnedMessageAdapter.getWorkPhone();
    }

	public boolean isOBXAbnormal(int i, int j) {
	    return chainnedMessageAdapter.isOBXAbnormal(i, j);
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
    
    public String getNteForPID(){
    	
    	return "";
    }

}
