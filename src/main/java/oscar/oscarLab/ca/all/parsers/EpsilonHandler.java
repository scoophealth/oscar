/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.oscarLab.ca.all.parsers;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class EpsilonHandler  extends CMLHandler implements MessageHandler {
	private static Logger logger = MiscUtils.getLogger();
	
	@Override
	public String getMsgType() {
		return "EPSILON";
	}

	@Override
	public void init(String hl7Body) throws HL7Exception {
		if (hl7Body.startsWith("MSH")) {
			int index = 0;
			for (int i = 0; i < 11; i++)
				index = hl7Body.indexOf('|', index + 1);
			int tmp = index + 1;
			String tmph = hl7Body.substring(0, tmp);
			index = hl7Body.indexOf('|', index + 1);
			if (hl7Body.substring(tmp, index).trim().length() == 0)
				hl7Body = tmph + "2.3" + hl7Body.substring(index);
		}
		Parser p = new PipeParser();
		p.setValidationContext(new NoValidation());
		msg = (ORU_R01) p.parse(hl7Body.replaceAll("\n", "\r\n"));
	}

	@Override
	public String getOBXResultStatus(int i, int j) {
		String status = "";
		try {
			status = getString(msg.getRESPONSE().getORDER_OBSERVATION(i)
					.getOBSERVATION(j).getOBX().getObservResultStatus()
					.getValue());
			if (status.equalsIgnoreCase("I"))
				status = "Pending";
			else if (status.equalsIgnoreCase("F"))
				status = "Final";
		} catch (Exception e) {
			logger.error("Error retrieving obx result status", e);
			return status;
		}
		return status;
	}

	@Override
	public String getOrderStatus() {
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(0)
					.getOBR().getResultStatus().getValue()));
		} catch (Exception e) {
			return ("");
		}
	}

	@Override
	public String getMsgPriority() {
		try {
			return msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR()
					.getPriority().getValue();
		} catch (HL7Exception e) {
			return ("");
		}
	}

	@Override
	public String getServiceDate() {
		try {
			return (formatDateTime(getString(Terser.get(msg.getRESPONSE()
					.getORDER_OBSERVATION(0).getOBR(), 6, 0, 1, 1))));
		} catch (Exception e) {
			return ("");
		}
	}

	@Override
	public boolean isOBXAbnormal(int i, int j) {
		if (("").equals(getOBXAbnormalFlag(i, j).trim())) {
			return (false);
		} else {
			return (true);
		}

	}

	@Override
	public String getAccessionNum() {
		String accessionNum = "";
		try {
			accessionNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0)
					.getOBR().getPlacerOrderNumber(0).getEntityIdentifier()
					.getValue());
			if (msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR()
					.getFillerOrderNumber().getEntityIdentifier().getValue() != null) {
				accessionNum = accessionNum
						+ (accessionNum != null && accessionNum.trim().length() > 0 ? ", " : "")
						+ getString(msg.getRESPONSE().getORDER_OBSERVATION(0)
								.getOBR().getFillerOrderNumber()
								.getEntityIdentifier().getValue());
			}
			return (accessionNum);
		} catch (Exception e) {
			logger.error("Could not return accession number", e);
			return ("");
		}
	}

	@Override
	public String getObservationHeader(int i, int j) {
		try {
			
			return	getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getText().getValue()).trim();
			
		} catch (Exception e) {
			return ("");
		}
	}

	@Override
	public String getTimeStamp(int i, int j) {
		try {
			return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().
					getSpecimenReceivedDateTime().getTimeOfAnEvent().getValue())));
		} catch (Exception e) {
			return ("");
		}
	}

	String delimiter = "  ";
	char bl = ' ';

	public String getAuditLine(String procDate, String procTime, String logId,
			String formStatus, String formType, String accession, String hcNum,
			String hcVerCode, String patientName, String orderingClient,
			String messageDate, String messageTime) {
		logger.info("Getting Audit Line");

		return getPaddedString(procDate, 11, bl) + delimiter
				+ getPaddedString(procTime, 8, bl) + delimiter
				+ getPaddedString(logId, 7, bl) + delimiter
				+ getPaddedString(formStatus, 1, bl) + delimiter
				+ getPaddedString(formType, 1, bl) + delimiter
				+ getPaddedString(accession, 9, bl) + delimiter
				+ getPaddedString(hcNum, 10, bl) + delimiter
				+ getPaddedString(hcVerCode, 2, bl) + delimiter
				+ getPaddedString(patientName, 61, bl) + delimiter
				+ getPaddedString(orderingClient, 8, bl) + delimiter
				+ getPaddedString(messageDate, 11, bl) + delimiter
				+ getPaddedString(messageTime, 8, bl) + "\n\r";

	}

	String getPaddedString(String originalString, int length, char paddingChar) {
		StringBuilder str = new StringBuilder(length);
		str.append(originalString);

		for (int i = str.length(); i < length; i++) {
			str.append(paddingChar);
		}

		return str.substring(0, length);
	}

	public String getHealthNumVersion() {
		try {
			return (getString(Terser.get(msg.getRESPONSE().getPATIENT()
					.getPID(), 4, 0, 2, 1)));
		} catch (HL7Exception e) {
			return "";
		}
	}

	@Override
	public String getMsgDate() {
		try {
			return formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue());
		} catch (Exception e) {
			logger.error("Error of parsing message date :", e);
		}		
		return "";
	}
	
	public Date getMsgDateAsDate() {
		Date date = null;
		try {
			date = getDateTime(getMsgDate());
		} catch (Exception e) {
			logger.error("Error of parsing message date :", e);
		}
		return date;
	}

	private Date getDateTime(String plain) {
		String dateFormat = "yyyyMMddHHmmss";
		dateFormat = dateFormat.substring(0, plain.length());
		Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
		return date;
	}

	public String getUnescapedName() {
		return getLastName() + "^" + getFirstName() + "^" + getMiddleName();
	}

	private String getMiddleName() {
		return (getString(msg.getRESPONSE().getPATIENT().getPID()
				.getMotherSMaidenName().getMiddleInitialOrName().getValue()));
	}
	
    @Override
	public String getPatientLocation(){
        return(getString(msg.getMSH().getSendingApplication().getNamespaceID().getValue()));
    }


	@Override
	public String audit() {
		return "";		
	}
	
	 public String getOBXName(int i, int j){
	        String ret = "";
	        try{
	            // leave the name blank if the value type is 'FT' this is because it
	            // is a comment, if the name is blank the obx segment will not be displayed
	            OBX obxSeg = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
	            if ((obxSeg.getValueType().getValue()!=null) && (!obxSeg.getValueType().getValue().equals("FT")))
	                ret = getString(obxSeg.getObservationIdentifier().getText().getValue());
	        }catch(Exception e){
	            logger.error("Error returning OBX name", e);
	        }
	        
	        return ret;
	    }
	  
	 public String getOBXResult(int i, int j){
	        
	        String result = "";
	        try{
	            
	            Terser terser = new Terser(msg);
	            result = getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),5,0,1,1));
	            
	            // format the result
	            if (result.endsWith("."))
	                result = result.substring(0, result.length()-1);
	            
	        }catch(Exception e){
	            logger.error("Exception returning result", e);
	        }
	        return result;
	    }
	 
	 
	 
	 public String getOBXComment(int i, int j, int k){
	        String comment = "";
	        try{
	            k++;
	            
	            Terser terser = new Terser(msg);
	            OBX obxSeg = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
	            comment = Terser.get(obxSeg,7,k,1,1);
	            if (comment == null)
	                comment = Terser.get(obxSeg,7,k,2,1);
	            
	        }catch(Exception e){
	            logger.error("Cannot return comment", e);
	        }
	        return comment.replaceAll("\\\\\\.br\\\\", "<br />");
	    }
	  
	 public String getOBRComment(int i, int j){
	        String comment = "";
	        
	        // update j to the number of the comment not the index of a comment array
	        j++;
	        try {
	            Terser terser = new Terser(msg);
	            
	            int obxCount = getOBXCount(i);
	            int count = 0;
	            int l = 0;
	            OBX obxSeg = null;
	            
	            while ( l < obxCount && count < j){
	                
	                obxSeg = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
	                if (getString(obxSeg.getValueType().getValue()).equals("FT")){
	                    count++;
	                }
	                l++;
	                
	            }
	            l--;
	            
	            int k = 0;
	            String nextComment = Terser.get(obxSeg,5,k,1,1);
	            while(nextComment != null){
	                comment = comment + nextComment.replaceAll("\\\\\\.br\\\\", "<br />");
	                k++;
	                nextComment = Terser.get(obxSeg,5,k,1,1);
	            }
	            
	        } catch (Exception e) {
	            logger.error("getOBRComment error", e);
	            comment = "";
	        }
	        if (comment.equals(getOBXResult(i,j))) comment = "";
	        return comment;
	    }
	 
	 public int getOBRCommentCount(int i){
	        int count = 0;
	        try {
	        	for (int j=0; j < getOBXCount(i); j++){
		            if (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue()).equals("FT"))
		                count++;
		        }
	        } catch (Exception e){
	        	logger.error("Error getting OBRCommentCount");
	        }
	        
	        
	        return count;
	        
	    }
	 
	 /**
	     *  Retrieve the possible segment headers from the OBX fields
	     */
	    public ArrayList<String> getHeaders(){
	        int i, j;
	        
	        ArrayList<String> headers = new ArrayList<String>();
	        String currentHeader;
	        try{
	            for (i=0; i < msg.getRESPONSE().getORDER_OBSERVATIONReps(); i++){
	                
	                for (j=0; j < msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps(); j++){
	                    // only check the obx segment for a header if it is one that will be displayed
	                	 currentHeader = super.getOBXIdentifier(i, j);//obxSeg.getObservationIdentifier().getIdentifier().toString();
	                        
	                        if (!headers.contains(currentHeader)){
	                            logger.info("Adding header: '"+currentHeader+"' to list");
	                            headers.add(currentHeader);
	                        }
	                  //  }
	                    
	                }
	                
	            }
	            return(headers);
	        }catch(Exception e){
	            logger.error("Could not create header list", e);
	            
	            return(null);
	        }
	    }
	    
	  
}
