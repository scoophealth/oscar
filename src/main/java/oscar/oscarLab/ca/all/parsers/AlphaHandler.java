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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v22.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class AlphaHandler extends DefaultGenericHandler implements MessageHandler {

    Logger logger = Logger.getLogger(AlphaHandler.class);
    ca.uhn.hl7v2.model.v22.message.ORU_R01 msg22 = null;
    ca.uhn.hl7v2.model.v23.message.ORU_R01 msg23 = null;
    String version=null;

    
    

    public String getVersion() {
    	return version;
    }


	/** Creates a new instance of ICLHandler */
    public AlphaHandler(){
    }

    
    public void init(String hl7Body) throws HL7Exception {
    	//super.init(hl7Body);
    	Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());

        // force parsing as a generic message by changing the message structure
       // hl7Body = hl7Body.replaceAll("R01", "");
        version = p.parse(hl7Body.replaceAll( "\n", "\r\n")).getVersion();
        if (version.equals("2.2")) {
        	msg22 = (ca.uhn.hl7v2.model.v22.message.ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));	
        } else {
        	msg23 = (ca.uhn.hl7v2.model.v23.message.ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
        }
        
        if (version.equals("2.2")) {
        	terser = new Terser(msg22);
        } else{
        	terser = new Terser(msg23);
        }
        
        
        int obrCount = getOBRCount();

        int obrNum;
        boolean obrFlag;
        String segmentName;
        String[] segments = terser.getFinder().getRoot().getNames();
        obrGroups = new ArrayList<ArrayList<Segment>>();

        /*
         *  Fill the OBX array list for use by future methods
         */
        for (int i=0; i < obrCount; i++){
            ArrayList<Segment> obxSegs = new ArrayList<Segment>();

            obrNum = i+1;
            obrFlag = false;

            for (int k=0; k < segments.length; k++){

                segmentName = segments[k].substring(0, 3);

                if (obrFlag && segmentName.equals("OBX")){

                    // make sure to count all of the obx segments in the group
                    Structure[] segs = terser.getFinder().getRoot().getAll(segments[k]);
                    for (int l=0; l < segs.length; l++){
                        Segment obxSeg = (Segment) segs[l];
                        obxSegs.add(obxSeg);
                    }

                }else if (obrFlag && segmentName.equals("OBR")){
                    break;
                }else if ( segments[k].equals("OBR"+obrNum) || ( obrNum==1 && segments[k].equals("OBR"))){
                    obrFlag = true;
                }

            }
            obrGroups.add(obxSegs);
        }
        
        debugMSHAndPid();
    }
    
    public String getObxSetId(int i, int j) {
    	try{
        	if (version.equals("2.2")) {
                return(getString(Terser.get(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),1,0,1,1)));

        	} else {
                return(getString(Terser.get(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),1,0,1,1)));

        	}
        }catch(Exception e){
            logger.error("Error retrieving obx set id", e);
            return("");
        }
    }
    
    public String getAlphaReqNum(){
        try{
            return(getString(terser.get("/.PID-2")));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getClientRef(){
        try{
            return(getString(terser.get("/.PID-3")));
        }catch(Exception e){
            return("");
        }
    }
    
    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList<String> getHeaders(){
        int i;
        int arraySize;

        ArrayList<String> headers = new ArrayList<String>();
        String currentHeader;

        try{
        	if (version.equals("2.2")) {
            for (i=0; i < msg22.getPATIENT_RESULT().getORDER_OBSERVATIONReps(); i++){
            	if (Terser.get(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR(),25,0,1,2)==null ||!Terser.get(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR(),25,0,1,2).equals("D")) {
            		currentHeader = getObservationHeader(i, 0);
                    arraySize = headers.size();
                    if (arraySize == 0 || !currentHeader.equals(headers.get(arraySize-1))){
                        headers.add(currentHeader);
                    }
            	}
            } 
        	}else {
        		for (i=0; i < msg23.getRESPONSE().getORDER_OBSERVATIONReps(); i++){

                    currentHeader = getObservationHeader(i, 0);
                    arraySize = headers.size();
                    if (arraySize == 0 || !currentHeader.equals(headers.get(arraySize-1))){
                        headers.add(currentHeader);
                    }

                } 
            }
            return(headers);
        }catch(Exception e){
            logger.error("Could not create header list", e);

            return(null);
        }

    }
    
    public int getOBRCount(){
    	try {
    		if (version.equals("2.2")) {
    			return(msg22.getPATIENT_RESULT().getORDER_OBSERVATIONReps());
        	}else {
        		return(msg23.getRESPONSE().getORDER_OBSERVATIONReps());	
        	}
    	} catch(Exception e){
            return(0);
        }
    	
        
    }
    

    public String getObservationHeader(int i, int j){
        try{
            //return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getAlternateIdentifier().getValue()));
        	if (version.equals("2.2")) {
            	return(getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR().getUniversalServiceID().getText().getValue()));

        	} else {
            	return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getAlternateIdentifier().getValue()));

        	}
        }catch(Exception e){
            return("");
        }
    }
    
    /*
     *  OBX METHODS
     */
    public int getOBXCount(int i){
        int count = 0;
        try{
        	if (version.equals("2.2")) {
        		count = msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATIONReps();
                // if count is 1 there may only be an nte segment and no obx segments so check
                if (count == 1){
                    String test = msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(0).getOBX().getObservationIdentifier().getText().getValue();
                    if (test == null)
                        count = 0;
                }
        	} else {
        		count = msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps();
                // if count is 1 there may only be an nte segment and no obx segments so check
                if (count == 1){
                    String test = msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(0).getOBX().getObservationIdentifier().getText().getValue();
                    if (test == null)
                        count = 0;
                }
        	}
            
        }catch(Exception e){
            logger.error("Error retrieving obx count", e);
            count = 0;
        }
        return count;
    }

    public String getOBXIdentifier(int i, int j){
        try{
        	if (version.equals("2.2")) {
                return(getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()));

        	} else {
                return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()));
	
        	}
        }catch(Exception e){
            logger.error("Error retrieving obx identifier", e);
            return("");
        }
    }

    public String getOBXName(int i, int j){
        try{
        	if (version.equals("2.2")) {
        		OBX obx = msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
        		if (!obx.getValueType().getValue().equals("CE") && !oBXHasForm(i,j))
                return(getString(obx.getObservationIdentifier().getText().getValue()));
        		else
        			return "";

        	} else {
                return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getText().getValue()));

        	}
        }catch(Exception e){
            logger.error("Error retrieving obx name", e);
            return("");
        }
    }
    
    public String getOBXValueType(int i, int j){
        try{
        	if (version.equals("2.2")) {
        		return msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue();
        	} else {
                return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue()));

        	}
        }catch(Exception e){
            logger.error("Error retrieving obx name", e);
            return("");
        }
    }
    
    public String getRequestDate(int i){
    	try{
	    	if (version.equals("2.2")) {
	    		return formatDateTime(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR().getRequestedDateTimeNotused().getTimeOfAnEvent().getValue());
	    	} else {
	    		return formatDateTime(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getRequestedDateTime().getTimeOfAnEvent().getValue());
	    	}
    	} catch(Exception e){
            logger.error("Error getRequestDate", e);
            return("");
        }    	
    }
    
    
    
    
    /** When the Test Source = ”FORM”, then REPLACE all previous  NM and ST results with the FT results.
     * When the Test Source<>”FORM”, then APPEND the FT results to the previous results.
     * **/
    public boolean oBXHasForm(int i, int j){
        try{
        	if (version.equals("2.2")) {
        		if (msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue().equals("FT")) {
        			return false;
        		}
        		String testCode= msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue();
        		for (int k=0;k < getOBXCount(i);k++) {
        			OBX obx =msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(k).getOBX();
        			if (testCode.contains(obx.getObservationIdentifier().getIdentifier().getValue()) && obx.getValueType().getValue().equals("FT") && obx.getObservationIdentifier().getComponent(5)!= null && obx.getObservationIdentifier().getComponent(5).toString().equals("FORM")) {
        				return true;
        			}
        		}
        		return false;
        	} else {
                return(false);

        	}
        }catch(Exception e){
            logger.error("Error retrieving obx name", e);
            return(false);
        }
    }

    public String getOBXResult(int i, int j){
        try{
        	if (version.equals("2.2")) {
                return(getString(Terser.get(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),5,0,1,1)));

        	} else {
                return(getString(Terser.get(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),5,0,1,1)));

        	}
        }catch(Exception e){
            logger.error("Error retrieving obx result", e);
            return("");
        }
    }

    public String getOBXReferenceRange(int i, int j){
        try{
        	if (version.equals("2.2")) {
        		return(getString(Terser.get(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),7,0,2,1)));
    	} else {
            //return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getReferencesRange().getValue()));
    		return(getString(Terser.get(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),7,0,2,1)));

    	}
        }catch(Exception e){
            logger.error("Error retrieving obx reference range", e);
            return("");
        }
    }

    public String getOBXUnits(int i, int j){
        try{
        	if (version.equals("2.2")) {
                return(getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getUnits().getIdentifier().getValue()));
    	} else {
            return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getUnits().getIdentifier().getValue()));
    	}
        }catch(Exception e){
            logger.error("Error retrieving obx units", e);
            return("");
        }
    }

    public String getOBXResultStatus(int i, int j){
        try{
        	if (version.equals("2.2")) {
                return(getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationResultStatus().getValue()));
    	} else {
            return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservResultStatus().getValue()));
    	}
        }catch(Exception e){
            logger.error("Error retrieving obx results status", e);
            return("");
        }
    }

    public int getOBXFinalResultCount(){
        try{
            int obrCount = getOBRCount();
            int obxCount;
            int count = 0;
            for (int i=0; i < obrCount; i++){
                obxCount = getOBXCount(i);
                for (int j=0; j < obxCount; j++){
                    String status = getOBXResultStatus(i, j);
                    if (status.equalsIgnoreCase("F") || status.equalsIgnoreCase("C"))
                        count++;
                }
            }


            String orderStatus = getOrderStatus();
            // add extra so final reports are always the ordered as the latest except
            // if the report has been changed in which case that report should be the latest
            if (orderStatus.equalsIgnoreCase("F"))
                count = count + 100;
            else if (orderStatus.equalsIgnoreCase("C"))
                count = count + 150;

            return count;
        }catch(Exception e){
            logger.error("Error retrieving obx final result count", e);
            return(0);
        }

    }

    public String getTimeStamp(int i, int j){
    	try{
        	if (version.equals("2.2")) {
                return formatDateTime(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue());
        	} else {
            return formatDateTime(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue());
        	}
        }catch(Exception e){
            return("");
        }
    }

    public boolean isOBXAbnormal(int i, int j){
        try{
            if (getOBXAbnormalFlag(i, j).equals("H") || getOBXAbnormalFlag(i,
            j).equals("L") || getOBXAbnormalFlag(i, j).equals("HH") ||
            getOBXAbnormalFlag(i, j).equals("LL"))
                return(true);
            else
                return(false);

        }catch(Exception e){
            return(false);
        }
    }

    public String getOBXAbnormalFlag(int i, int j){
        try{
        	if (version.equals("2.2")) {
                return(getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getAbnormalFlags(0).getValue()));
    	} else {
            return(getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getAbnormalFlags(0).getValue()));
    	}
        }catch(Exception e){
            logger.error("Error retrieving obx abnormal flag", e);
            return("");
        }
    }

    public int getOBXCommentCount(int i, int j){
        try {
        	if (version.equals("2.2")) {
                return(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getField(7).length-1);
    	} else {
            return(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTEReps());
    	}
        } catch (Exception e) {
            logger.error("Error retrieving obx comment count", e);
            return(0);
        }

    }

    public String getOBXComment(int i, int j, int k){
        try {
        	if (version.equals("2.2")) {
                Type[] field= msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getField(7);
        		if (field.length>1) {
        			String range = field[k+1].toString();
        			return(getString(range));
        		} else {
        			return "";
        		}
    	} else {
            // ICL likes to thrown reserved characters in their comments -- this is to compensate
            String obxComment = getString(Terser.get(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k),3,0,1,1))+" "+
                    getString(Terser.get(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k),3,0,2,1)).trim();

            return(obxComment);
    	}



        } catch (Exception e) {
            return("");
        }
    }

    public String getAccessionNum(){
        try{
        	int obrCount;
        	String accessionNum;
        	if (version.equals("2.2")) {
        		obrCount = msg22.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
                accessionNum = getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getFillerOrderNumber().getUniqueFillerId().getValue());
    	} else {
    		obrCount = msg23.getRESPONSE().getORDER_OBSERVATIONReps();
            accessionNum = getString(msg23.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getFillerOrderNumber().getEntityIdentifier().getValue());
    	}
            
            // if accessionNum can't be found in the OBR record of the first observation,
            //  look for it in subsequent observation records
            if (accessionNum != ""){
                return(accessionNum);
            }

            for (int j=1; j < obrCount; j++){
            	if (version.equals("2.2")) {
                accessionNum = getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(j).getOBR().getFillerOrderNumber().getUniqueFillerId().getValue());
            	} else {
                    accessionNum = getString(msg23.getRESPONSE().getORDER_OBSERVATION(j).getOBR().getFillerOrderNumber().getEntityIdentifier().getValue());
            	}
                if (accessionNum != ""){
                    return(accessionNum);
                }
            }
            // haven't found an accessionNum
            return("");

        }catch(Exception e){

            logger.error("Could not return accession number", e);
            return("");
        }
    }
    
    public String getOrderStatus(){
        // Alpha status: F - Final, P - Preliminary, C - Changed, R - Reprint
       
        try{
        	String status = "F";
        	int obrCount;
        	if (version.equals("2.2")) {
                obrCount = msg22.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
                for (int i=0; i < obrCount; i++){
                    if (getString(msg22.getPATIENT_RESULT().getORDER_OBSERVATION(i).getOBR().getResultStatus().getValue()).equals("P"))
                        status = "P";
                }
    	} else {
            obrCount = msg23.getRESPONSE().getORDER_OBSERVATIONReps();
            for (int i=0; i < obrCount; i++){
                if (getString(msg23.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getResultStatus().getValue()).equals("P"))
                    status = "P";
            }
    	}
            
            

            return(status);
        }catch(Exception e){
            logger.error("Exception retrieving order status", e);
            return("");
        }

    }
    
    public String getServiceDate(){
        try{
        	if (version.equals("2.2")) {
        		String srvcDate = msg22.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();

                if (srvcDate != null){
                    return(formatDateTime(srvcDate));
                }

                // if srvcDate can't be found in the OBR record of the first observation,
                //  look for it in subsequent observation records
                int obrCount = msg22.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
                for (int j=1; j < obrCount; j++){
                    srvcDate = msg22.getPATIENT_RESULT().getORDER_OBSERVATION(j).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();
                    if (srvcDate != null){
                        return(formatDateTime(srvcDate));
                    }
                }
                // haven't found a srvcDate in the OBR - use MSH date instead
                return(formatDateTime(msg22.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
                } else {
                	String srvcDate = msg23.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();

                    if (srvcDate != null){
                        return(formatDateTime(srvcDate));
                    }

                    // if srvcDate can't be found in the OBR record of the first observation,
                    //  look for it in subsequent observation records
                    int obrCount = msg23.getRESPONSE().getORDER_OBSERVATIONReps();
                    for (int j=1; j < obrCount; j++){
                        srvcDate = msg23.getRESPONSE().getORDER_OBSERVATION(j).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();
                        if (srvcDate != null){
                            return(formatDateTime(srvcDate));
                        }
                    }
                    // haven't found a srvcDate in the OBR - use MSH date instead
                    return(formatDateTime(msg23.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
                    }
                    }catch(Exception e){
            return("");
        }
    }
    
    public String getNteForPID() {
    try {
    	if (version.equals("2.2")) {
    		return("");
    	} else {
    		return(getString(msg23.getRESPONSE().getPATIENT().getNTE().getComment(0).getValue()));
    	}
    }catch(Exception e){
        logger.error("Could not return NTE of Patient", e);
        return("");
    }
    }
    
    
    public String getMsgType(){
        return("ALPHA");
    }

    public void debugMSHAndPid() {
        
    	StringBuilder sb = new StringBuilder();
    	
		sb.append("\n");
		sb.append("getPatientLocation:"+getPatientLocation()+"\n");
		sb.append("Msg Date:"+getMsgDate()+"\n");
		sb.append("\n");
		sb.append("First Name:"+getFirstName()+"\n");
		sb.append("Last Name:"+getLastName()+"\n");
		sb.append("DOB:"+getDOB()+"\n");
		sb.append("Sex:"+getSex()+"\n");
		sb.append("HIN:"+getHealthNum()+"\n");
		sb.append("Phone:"+getHomePhone()+"\n");
		sb.append("Alpha reqNo:"+getAlphaReqNum()+"\n");
		sb.append("Alpha client ref#:"+getClientRef()+"\n");
		sb.append("OBR Count=" + getOBRCount());
		sb.append("obxCount="+getOBXCount(0));
    	logger.debug(sb.toString());
    }
}
