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
/*
 * SpireHandler.java
 *
 * Created on April 19, 2012, 1:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.parsers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import oscar.oscarLab.ca.all.spireHapiExt.v23.message.ORU_R01;
import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v23.datatype.CN;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.parser.CustomModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;
//import ca.uhn.hl7v2.model.v23.message.ORU_R01;
//import ca.uhn.hl7v2.parser.Parser;
//import oscar.oscarLab.ca.all.spireHapiExt.v23.segment.ZDS;

class Lines {
	private String message = "";
	private String delimiter = "|";
	private int numLines = 0;
	private boolean update = false;
	
	public Lines(String message) {
		this.setMessage(message);
	}
	
	public Lines(String message, String delimiter) {
		this.setMessage(message);
		this.setDelimiter(delimiter);
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
		this.update = true;
		if (this.delimiter == null)
			this.delimiter = "";
	}
	
	public void setMessage(String message) {
		this.message = message;
		this.update = true;
		if (this.message == null)
			this.message = "";
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public String getDelimiter() {
		return this.delimiter;
	}
	
	private boolean invalidLine(int i) {
		return (i < 0 || i > numLines());
	}
	
	public boolean remove(int i) {
		if (invalidLine(i))
			return false;
			
		this.update = true;
			
		int[] indices = findIndices(i);
		
		if (indices[1] == this.message.length())
			indices[1] = indices[1]-1;
		
		String removePattern = Pattern.quote( message.substring(indices[0], indices[1]+1) );
		
		String left = message.substring(0, indices[0]);
		String right = message.substring(indices[0]);
		right = right.replaceFirst(removePattern, "");
		
		this.setMessage(left + right);
		
		return true;
	}
	
	public boolean insert(String text, int i) {
		if (invalidLine(i) || text == null)
			return false;
			
		this.update = true;
			
		int[] indices = findIndices(i);
		
		String left = message.substring(0, indices[0]);
		String right = message.substring(indices[0]);
		
		this.setMessage(left + text + this.delimiter + right);
		
		return true;
		
	}
	
	public String cut(int i) {
		if (invalidLine(i))
			return null;
		
		String value = copy(i);
		remove(i);
		return value;
	}
	
	public String copy(int i) {
		if (invalidLine(i))
			return null;
			
		int[] indices = findIndices(i);
		
		return message.substring(indices[0], indices[1]);
	}
	
	private int[] findIndices(int i) {
		if (invalidLine(i))
			return null;
		
		int num = 0;
		int indexStart = 0;
		int indexEnd = message.indexOf(this.delimiter);
		if (indexEnd == -1)
			indexEnd = this.message.length();
		
		while (num != i) {
			num++;
			indexStart = indexEnd + this.delimiter.length();
			indexEnd = message.indexOf(this.delimiter, indexStart);
		}
		
		if (indexEnd == -1)
			indexEnd = this.message.length();
		
		return new int[] {indexStart, indexEnd};
	}
	
	public boolean contains(String text, int i) {
		if (text == null || invalidLine(i))
			return false;
			
		return copy(i).toLowerCase().contains(text.toLowerCase());
	}
	
	public int numOccurances(String text) {
		if (text == null)
			return 0;
		
		int num = 0;
		
		for (int i=0; i < numLines(); i++)
			if (contains(text, i))
				num++;
				
		return num;
	}
	
	public int findFirstOccurance(String text) {
		if (text == null)
			return -1;
			
		for (int i=0; i < numLines(); i++)
			if (contains(text, i))
				return i;
				
		return -1;
	}
	
	public int findFirstOccurance(String text, int start) {
		if (text == null || invalidLine(start))
			return -1;
			
		for (int i=start; i < numLines(); i++)
			if (contains(text, i))
				return i;
				
		return -1;
	}
	
	public int findLastOccurance(String text, int end) {
		if (text == null || invalidLine(end))
			return -1;
			
		for (int i=end; i >= 0; i--)
			if (contains(text, i))
				return i;
				
		return -1;
	}
	
	public int numLines() {
		if (!this.update)
			return this.numLines;
		
		if (this.message.equals(""))
			return 0;
		
		this.numLines = 1;
		int index = message.indexOf(this.delimiter);
		boolean found = index != -1;
		
		while (found) {
			this.numLines++;
			index = message.indexOf(this.delimiter, index+this.delimiter.length());
			found = index != -1;
		}
		
		this.update = false;
		
		return this.numLines;		
	}
}

/**
 *
 * @author j.chisholm
 */
public class SpireHandler implements MessageHandler {
    
    ORU_R01 msg = null;
    Logger logger = Logger.getLogger(SpireHandler.class);
    
    /** Creates a new instance of SpireHandler */
    public SpireHandler(){
    }
    
    public void init(String hl7Body) throws HL7Exception {
        hl7Body = fixMessage(hl7Body);
        
        ModelClassFactory cmf = new CustomModelClassFactory("oscar.oscarLab.ca.all.spireHapiExt");
		PipeParser p = new PipeParser(cmf);
        p.setValidationContext(new NoValidation());
        
        msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
    }
    
    /**
     * Method fixMessage
     * Will make corrections to Spire HL7 labs (change event types with the appropriate HL7 Type, etc)
     * 
     * @param message The HL7 message
     * 
     * @return The message with the Spire HL7 lab data corrected
     */ 
    private String fixMessage(String message) {
		/*
		 * May need to replace the following as well:
		 * DATE 			DT
		 * MDOC_RAD 		FT
		 * AP 				FT
		 * Variable			CE
		 */ 
		message = message.replaceAll("\\|NUM\\|","|NM|");
		message = message.replaceAll("\\|TXT\\|","|TX|");
		message = message.replaceAll("\\|DOC\\|","|FT|");
		
		// fix message type
		message = message.replaceAll("\\|MDM\\^R01\\|","|ORU^R01|");
		//message = message.replaceAll("\\|MDM^R01\\|","|ORU^R01|");
		
		message = message.replaceAll("\\\\.br\\\\", "<br>");
		
		return reorderSegments(message);
	}
	
	private String reorderSegments(String message) {
		if (!message.contains("ZDS|"))
			return message;	
		
		// put ZDS segments at end of OBR 'group'
		Lines lines = new Lines(message, "\r");
		int obrIndex = lines.findFirstOccurance("OBR|");
		int obrNextIndex = lines.findFirstOccurance("OBR|", obrIndex+1);
		int zdsIndex = 0;
		boolean finished = false;
		
		while (!finished) {
			
			if (obrNextIndex == -1) {
				obrNextIndex = lines.numLines()-1;
				finished = true; // does one more iteration of inner while loop
			}
			
			zdsIndex = lines.findLastOccurance("ZDS|", obrNextIndex);
			
			String[] zdsText = new String[ lines.numOccurances("ZDS|") ];
			int i = 0;
			while (zdsIndex != -1 && zdsIndex > obrIndex) {
				zdsText[i] = lines.cut(zdsIndex);
				i++;
				
				obrNextIndex = lines.findFirstOccurance("OBR|", obrIndex+1);
				if (obrNextIndex == -1)
					obrNextIndex = lines.numLines()-1;
				zdsIndex = lines.findLastOccurance("ZDS|", obrNextIndex);
			}

			for (int j=0; j < zdsText.length; j++) {
				if (zdsText[j] != null)
					lines.insert(zdsText[j], obrNextIndex);
			}
			
			obrIndex = lines.findFirstOccurance("OBR|", obrIndex+1);
			if (obrIndex == -1)
				break;
			obrNextIndex = lines.findFirstOccurance("OBR|", obrIndex+1);
		}
		
		return lines.getMessage();
	}
    
    public String getMsgType(){
        return("Spire");
    }
    
    public String getMsgDate(){
        try {
            //return(formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
            return(formatDateTime(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue()));
        } catch (Exception e) {
            logger.error("Could not retrieve message date", e);
            return("");
        }
    }
    
    public String getMsgPriority(){
        return("");
    }
    
    /**
     *  Methods to get information about the Observation Request
     */
    public int getOBRCount(){
        return(msg.getRESPONSE().getORDER_OBSERVATIONReps());
    }
    
    public int getOBXCount(int i){
        try{
            return(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps());
        }catch(Exception e){
            return(0);
        }
    }
    
    public String getOBRName(int i){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getText().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getTimeStamp(int i, int j){
        try{
            return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue())));
        }catch(Exception e){
            return("");
        }
    }
    
    public boolean isOBXAbnormal(int i, int j){
        try{
			String abnormalFlag = getOBXAbnormalFlag(i, j);
			
            if(abnormalFlag.equals("ABN") || abnormalFlag.equals("HI") || abnormalFlag.equals("LOW") || abnormalFlag.equals("CRIT") || abnormalFlag.equals("NA") || abnormalFlag.equals("Unknown")){
                return(true);
            }else{
                return(false);
            }
            
        }catch(Exception e){
            return(false);
        }
    }
    
    public String getOBXAbnormalFlag(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getAbnormalFlags(0).getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getObservationHeader(int i, int j){
		try {
			return (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getCe1_Identifier().getValue()));
		} catch(Exception e){
            return("");
        }
        /*
        try{
            Terser terser = new Terser(msg);
            return (getString(terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),4,0,1,1))+" "+
                    getString(terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),4,0,2,1))+" "+
                    getString(terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),4,0,3,1))).trim();
        }catch(Exception e){
            return("");
        }
        */
    }
    
    public String getOBXIdentifier(int i, int j){
        try{
    		Segment obxSeg = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();	
    		String subIdent = Terser.get(obxSeg, 3, 0, 1, 2) ;
    		if(subIdent != null){ //HACK: for gdml labs generated with SubmitLabByFormAction
    			return getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue())+"&"+subIdent;
    		}
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXValueType(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXName(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getText().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXResult(int i, int j){
        try{
            Terser terser = new Terser(msg);
            return(getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),5,0,1,1)));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXReferenceRange(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getReferencesRange().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXUnits(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getUnits().getIdentifier().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXResultStatus(int i, int j){
        String status = "";
        try{
            status = getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservResultStatus().getValue());
            if (status.equalsIgnoreCase("I"))
                status = "Pending";
            else if (status.equalsIgnoreCase("F"))
                status = "Final";
        }catch(Exception e){
            logger.error("Error retrieving obx result status", e);
            return status;
        }
        return status;
    }
    
    public int getOBXFinalResultCount(){
        int obrCount = getOBRCount();
        int obxCount;
        int count = 0;
        for (int i=0; i < obrCount; i++){
            obxCount = getOBXCount(i);
            for (int j=0; j < obxCount; j++){
                if (getOBXResultStatus(i, j).equals("Final"))
                    count++;
            }
        }
        return count;
    }
    
    public int getNumZDSSegments() {
		try {
			return msg.getNumZDSSegments();
		} catch (HL7Exception e) {
			logger.error("Could not get number of ZDS Segments", e);
		}
		
		return 0;
	}
    
    public String getZDSName(int i) {
		try {
			return msg.getZDS(i).getActionCode().encode();
		} catch (HL7Exception e) {
			logger.error("Could not get ZDS name", e);
		}
		
		return "";
	}
	
	public String getZDSResult(int i) {
		return "";
	}
	
	public String getZDSProvider(int i) {
		try {
			CN info = msg.getZDS(i).getProvider();
			String retVal = "";
			if (info.getGivenName().getValue() != null)
				retVal += info.getGivenName().getValue() + " ";
			if (info.getMiddleInitialOrName().getValue() != null)
				retVal += info.getMiddleInitialOrName().getValue() + " ";
			if (info.getFamilyName().getValue() != null)
				retVal += info.getFamilyName().getValue() + " ";
			
			if (info.getAssigningAuthority().getUniversalIDType().getValue() != null)
				retVal += info.getAssigningAuthority().getUniversalIDType().getValue() + " ";
			if (info.getSourceTable().getValue() != null)
				retVal += info.getSourceTable().getValue() + " ";
				
			if (info.getDegreeEgMD().getValue() != null)
				retVal += info.getDegreeEgMD().getValue() + " ";
			if (info.getPrefixEgDR().getValue() != null)
				retVal += info.getPrefixEgDR().getValue() + " ";
			if (info.getSuffixEgJRorIII().getValue() != null)
				retVal += info.getSuffixEgJRorIII().getValue() + " ";
					
			//retVal += info.getGivenName().getValue() + " " + info.getMiddleInitialOrName().getValue() + " " + info.getFamilyName().getValue();
			//retVal += " " + info.getAssigningAuthority().getUniversalIDType().getValue();
			//retVal += " " + info.getSourceTable().getValue();
			return retVal;
		} catch (HL7Exception e) {
			logger.error("Could not get ZDS name", e);
		}
		
		return "";
	}
	
	public String getZDSResultStatus(int i) {
		try {
			return msg.getZDS(i).getActionStatus().encode();
		} catch (HL7Exception e) {
			logger.error("Could not get ZDS Result Status", e);
		}
		
		return "";
	}
	
	public String getZDSTimeStamp(int i) {
		try {
			return formatDateTime(msg.getZDS(i).getDateAndTime().encode());
		} catch (HL7Exception e) {
			logger.error("Could not get ZDS Timestamp", e);
		}
		
		return "";
	}
    
    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList getHeaders(){
        int i;
        int j;
        int k = 0;
        ArrayList<String> headers = new ArrayList<String>();
        String currentHeader;
        try{
            for (i=0; i < msg.getRESPONSE().getORDER_OBSERVATIONReps(); i++){
                
                for (j=0; j < msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps(); j++){
                    // only check the obx segment for a header if it is one that will be displayed
                    if (!getOBXName(i, j).equals("")){
                        currentHeader = getObservationHeader(i, j);
                        
                        if (!headers.contains(currentHeader)){
                            logger.debug("Adding header: '"+currentHeader+"' to list");
                            headers.add(currentHeader);
                        }
                    }
                    
                }
                
            }
            
            return(headers);
        }catch(Exception e){
            logger.error("Could not create header list", e);
            
            return(null);
        }
    }
    
    /**
     *  Methods to get information from observation notes
     */
    public int getOBRCommentCount(int i){
        /*try {
            int lastOBX = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps() - 1;
            return(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(lastOBX).getNTEReps());
        } catch (Exception e) {*/
        return(0);
        // }
    }
    
    public String getOBRComment(int i, int j){
       /* try {
            int lastOBX = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps() - 1;
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(lastOBX).getNTE(j).getComment(0).getValue()));
        } catch (Exception e) {*/
        return("");
        //}
    }
    
    /**
     *  Methods to get information from observation notes
     */
    public int getOBXCommentCount(int i, int j){
        int count = 0;
        try {
            count = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTEReps();
            
            // a bug in getNTEReps() causes it to return 1 instead of 0 so we check to make
            // sure there actually is a comment there
            if (count == 1){
                String comment = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE().getComment(0).getValue();
                if (comment == null)
                    count = 0;
            }
            
        } catch (Exception e) {
            logger.error("Error retrieving obx comment count", e);
        }
        return count;
    }
    
    public String getOBXComment(int i, int j, int k){
        try {
            //int lastOBX = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps() - 1;
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k).getComment(0).getValue()));
        } catch (Exception e) {
            return("");
        }
    }
    
    
    /**
     *  Methods to get information about the patient
     */
    public String getPatientName(){
        return(getFirstName()+" "+getLastName());
    }
    
    public String getFirstName(){
        return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getGivenName().getValue()));
    }
    
    public String getLastName(){
        return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getFamilyName().getValue()));
    }
    
    public String getDOB(){
        try{
            return(formatDateTime(getString(msg.getRESPONSE().getPATIENT().getPID().getDateOfBirth().getTimeOfAnEvent().getValue())));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getAge(){
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
    
    public String getSex(){
		String sex = getString(msg.getRESPONSE().getPATIENT().getPID().getSex().getValue());
		
		if (sex.length() > 0)
			sex = sex.substring(0, 1);
		
        return sex;
    }
    
    public String getHealthNum(){
        String hin = (getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDExternalID().getID().getValue()));
        
        if (hin != null && hin.equals(""))
			hin = null;
        
        return hin;
    }
    
    public String getHomePhone(){
        String phone = "";
        int i=0;
        try{
            while(!getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberHome(i).get9999999X99999CAnyText().getValue()).equals("")){
                if (i==0){
                    phone = getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberHome(i).get9999999X99999CAnyText().getValue());
                }else{
                    phone = phone + ", " + getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberHome(i).get9999999X99999CAnyText().getValue());
                }
                i++;
            }
            return(phone);
        }catch(Exception e){
            logger.error("Could not return phone number", e);
            return("");
        }
    }
    
    public String getWorkPhone(){
        String phone = "";
        int i=0;
        try{
            while(!getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberBusiness(i).get9999999X99999CAnyText().getValue()).equals("")){
                if (i==0){
                    phone = getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberBusiness(i).get9999999X99999CAnyText().getValue());
                }else{
                    phone = phone + ", " + getString(msg.getRESPONSE().getPATIENT().getPID().getPhoneNumberBusiness(i).get9999999X99999CAnyText().getValue());
                }
                i++;
            }
            return(phone);
        }catch(Exception e){
            logger.error("Could not return phone number", e);
            return("");
        }
    }
    
    public String getPatientLocation(){
        return(getString(msg.getMSH().getSendingFacility().getNamespaceID().getValue()));
    }
    
    public String getServiceDate(){
        try{
            return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getOrderEffectiveDateTime().getTimeOfAnEvent().getValue())));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getRequestDate(int i){
        try{
            return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getRequestedDateTime().getTimeOfAnEvent().getValue())));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOrderStatus(){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getOrderStatus().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getClientRef(){
        String docNum = "";
        int i=0;
        try{
            while(!getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue()).equals("")){
                if (i==0){
                    docNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue());
                }else{
                    docNum = docNum + ", " + getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue());
                }
                i++;
            }
            return(docNum);
        }catch(Exception e){
            logger.error("Could not return doctor id numbers", e);
            return("");
        }
    }
    
    public String getAccessionNum(){
        String accessionNum = "";
        try{
			accessionNum = msg.getMSH().getMessageControlID().getValue();
			
			/*
            accessionNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getPlacerOrderNumber(0).getEntityIdentifier().getValue());
            if(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getFillerOrderNumber().getEntityIdentifier().getValue() != null){
                accessionNum = accessionNum+", "+getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getORC().getFillerOrderNumber().getEntityIdentifier().getValue());
            }
            */
            return(accessionNum);
        }catch(Exception e){
            logger.error("Could not return accession number", e);
            return("");
        }
    }
    
    public List<String> getDocNames() {
		List<String> docNames = new ArrayList<String>();
		
		docNames.add( getDocName() );
		
        try{
            XCN[] attendingDocs = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAttendingDoctor();
			XCN[] consultingDocs = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getConsultingDoctor();
			XCN[] admittingDocs = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getAdmittingDoctor();
			
			for (int i=0; i < attendingDocs.length; i++) {
				docNames.add( attendingDocs[i].getGivenName() + " " + attendingDocs[i].getFamilyName() );				
			}
			
			for (int i=0; i < consultingDocs.length; i++) {
				docNames.add( consultingDocs[i].getGivenName() + " " + consultingDocs[i].getFamilyName() );
			}
			
			for (int i=0; i < admittingDocs.length; i++) {
				docNames.add( admittingDocs[i].getGivenName() + " " + admittingDocs[i].getFamilyName() );
			}
        }catch(Exception e){
            logger.error("Could not return doctor names", e);
            return(null);
        }
        
        return docNames;
	}
    
    public String getDocName(){
        String docName = "";
        int i=0;
        try{
            while(!getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i)).equals("")){
                if (i==0){
                    docName = getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i));
                }else{
                    docName = docName + ", " + getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i));
                }
                i++;
            }
            return(docName);
        }catch(Exception e){
            logger.error("Could not return doctor names", e);
            return("");
        }
    }
    
    public String getCCDocs(){
        String docName = "";
        int i=0;
        try{
            while(!getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i)).equals("")){
                if (i==0){
                    docName = getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
                }else{
                    docName = docName + ", " + getFullDocName(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i));
                }
                i++;
            }
            return(docName);
        }catch(Exception e){
            logger.error("Could not return cc'ed doctors", e);
            return("");
        }
    }
    
    public ArrayList getDocNums(){
        ArrayList<String> docNums = new ArrayList<String>();
        String id;
        int i;
        
        try{
            String providerId = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(0).getIDNumber().getValue();
            docNums.add(providerId);
            
            i=0;
            while((id = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getResultCopiesTo(i).getIDNumber().getValue()) != null){
                if (!id.equals(providerId))
                    docNums.add(id);
                i++;
            }
        }catch(Exception e){
            logger.error("Could not return doctor nums", e);
            
        }
        
        return(docNums);
    }
    
    public String audit(){
        return "";
    }
    
    
    private String getFullDocName(XCN docSeg){
        String docName = "";
        
        if(docSeg.getPrefixEgDR().getValue() != null)
            docName = docSeg.getPrefixEgDR().getValue();
        
        if(docSeg.getGivenName().getValue() != null){
            if (docName.equals(""))
                docName = docSeg.getGivenName().getValue();
            else
                docName = docName +" "+ docSeg.getGivenName().getValue();
        }
        if(docSeg.getMiddleInitialOrName().getValue() != null){
            if (docName.equals(""))
                docName = docSeg.getMiddleInitialOrName().getValue();
            else
                docName = docName +" "+ docSeg.getMiddleInitialOrName().getValue();
        }
        if(docSeg.getFamilyName().getValue() != null){
            if (docName.equals(""))
                docName = docSeg.getFamilyName().getValue();
            else
                docName = docName +" "+ docSeg.getFamilyName().getValue();
        }
        if(docSeg.getSuffixEgJRorIII().getValue() != null){
            if (docName.equals(""))
                docName = docSeg.getSuffixEgJRorIII().getValue();
            else
                docName = docName +" "+ docSeg.getSuffixEgJRorIII().getValue();
        }
        if(docSeg.getDegreeEgMD().getValue() != null){
            if (docName.equals(""))
                docName = docSeg.getDegreeEgMD().getValue();
            else
                docName = docName +" "+ docSeg.getDegreeEgMD().getValue();
        }
        
        return (docName);
    }
    
    
    protected String formatDateTime(String plain){
    	if (plain==null || plain.trim().equals("")) return "";
    	
        String dateFormat = "yyyyMMddHHmmss";
        dateFormat = dateFormat.substring(0, plain.length());
        String stringFormat = "yyyy-MM-dd HH:mm:ss";
        stringFormat = stringFormat.substring(0, stringFormat.lastIndexOf(dateFormat.charAt(dateFormat.length()-1))+1);
        
        Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
        return UtilDateUtilities.DateToString(date, stringFormat);
    }
    
    protected String getString(String retrieve){
        if (retrieve != null){
            retrieve.replaceAll("^", " ");
            return(retrieve.trim());
        }else{
            return("");
        }
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
