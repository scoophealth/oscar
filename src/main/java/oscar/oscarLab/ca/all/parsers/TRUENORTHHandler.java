/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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

import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.OBR;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class TRUENORTHHandler implements MessageHandler {
    Logger logger = Logger.getLogger(TRUENORTHHandler.class);	
	ORU_R01 msg = null;
    ArrayList<String> headers = null;
    HashMap<OBR, ArrayList<OBX>> obrSegMap = null;
    ArrayList<OBR> obrSegKeySet = null;
    
    public TRUENORTHHandler() {
    }
    
    public void init(String hl7Body) throws HL7Exception {

    	Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());
        msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
        String accessionNumber = getAccessionNum(msg);
        String firstName = getFirstName();
        String lastName = getLastName();
        String hin = getHealthNum();
        
        headers = new ArrayList<String>();
        obrSegMap = new LinkedHashMap<OBR,ArrayList<OBX>>();
        obrSegKeySet = new ArrayList<OBR>();
        msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
		int obrCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();

		for (int j = 0; j < obrCount; j++) {

			// ADD OBR SEGMENTS AND THEIR OBX SEGMENTS TO THE OBRSEGMAP
			OBR obrSeg = msg.getRESPONSE().getORDER_OBSERVATION(j)
					.getOBR();
			ArrayList<OBX> obxSegs = obrSegMap.get(obrSeg);

			// if the obrsegment has not yet been created it will be now
			if (obxSegs == null)
				obxSegs = new ArrayList<OBX>();

			int obxCount = msg.getRESPONSE().getORDER_OBSERVATION(j)
					.getOBSERVATIONReps();
			for (int k = 0; k < obxCount; k++) {
				obxSegs.add(msg.getRESPONSE().getORDER_OBSERVATION(j)
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
    
    public String getMsgType(){
    	return "TRUENORTH";
    }
    
    public String getMsgDate() {
		return (formatDateTime(msg.getMSH().getDateTimeOfMessage()
				.getTimeOfAnEvent().getValue()));
	}
    
    public String getMsgPriority() {
    	String priority = "R";
        for (int i=0; i < getOBRCount(); i++){
            try{
                if (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getPriority().getValue()).equals("S")){
                    priority="S";
                    break;
                }
            }catch(Exception e){
                logger.error("Error finding priority", e);
            }
        }

        return priority;
	}
    
    /**
     *  Methods to get information about the Observation Request
     */
    public int getOBRCount(){
        return(obrSegMap.size());
    }

    public int getOBXCount(int i){
        return( (obrSegMap.get(obrSegKeySet.get(i))).size() );
    }

    public String getOBRName(int i){
        return (( obrSegKeySet.get(i)).getUniversalServiceIdentifier().getText().getValue());
    }

    public String getTimeStamp(int i, int j){
        try{
            String ret = ( obrSegKeySet.get(i)).getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValue();
            if (ret == null)
                ret = ( obrSegKeySet.get(i)).getObservationDateTime().getTimeOfAnEvent().getValue();
            if (ret == null)
                ret = ( obrSegKeySet.get(i)).getRequestedDateTime().getTimeOfAnEvent().getValue();
            return(formatDateTime(getString(ret)));
        }catch(Exception e){
            logger.error("Exception retrieving timestamp", e);
            return("");
        }
    }

    public boolean isOBXAbnormal(int i, int j){
        String abnormalFlag = getOBXAbnormalFlag(i, j);
        if (abnormalFlag.equals("") || abnormalFlag.equals("N"))
            return(false);
        else
            return(true);
    }


    public String getOBXAbnormalFlag(int i, int j){

        try{

            return(getString( ( ( obrSegMap.get(obrSegKeySet.get(i))).get(j)).getAbnormalFlags(0).getValue() ));
        }catch(Exception e){
            logger.error("Exception retrieving abnormal flag", e);
            return("");
        }
    }

    public String getObservationHeader(int i, int j){
        try{
            return(getString(( obrSegKeySet.get(i)).getUniversalServiceIdentifier().getText().getValue()));
        }catch(Exception e){
            logger.error("Exception gettin header", e);
            return("");
        }
    }

    public String getOBXIdentifier(int i, int j){

        try{ // no value for TRUENORTH
            Segment obxSeg = (( obrSegMap.get(obrSegKeySet.get(i))).get(j));
            String ident = getString(Terser.get(obxSeg, 3, 0, 1, 1 ));
            String subIdent = Terser.get(obxSeg, 3, 0, 1, 2);

            if (subIdent != null)
                ident = ident+"&"+subIdent;

            logger.debug("returning obx identifier: "+ident);
            return(ident);
        }catch(Exception e){
            logger.error("error returning obx identifier", e);
            return("");
        }
    }


    public String getOBXValueType(int i, int j){
        String ret = "";
        try{
            OBX obxSeg = ( obrSegMap.get(obrSegKeySet.get(i))).get(j);
            ret = obxSeg.getValueType().getValue();
        }catch(Exception e){
            logger.error("Error returning OBX name", e);
        }

        return ret;
    }

    public String getOBXName(int i, int j){
        String ret = "";
        try{
            // leave the name blank if the value type is 'FT' this is because it
            // is a comment, if the name is blank the obx segment will not be displayed
            OBX obxSeg =  ( obrSegMap.get(obrSegKeySet.get(i))).get(j);
            if (!obxSeg.getValueType().getValue().equals("FT"))
                ret = getString(obxSeg.getObservationIdentifier().getText().getValue());
        }catch(Exception e){
            logger.error("Error returning OBX name", e);
        }

        return ret;
    }

    public String getOBXResult(int i, int j){

        String result = "";
        try{

            result = getString(Terser.get((( obrSegMap.get(obrSegKeySet.get(i))).get(j)),5,0,1,1));

            // format the result
            if (result.endsWith("."))
                result = result.substring(0, result.length()-1);

        }catch(Exception e){
            logger.error("Exception returning result", e);
        }
        return result;
    }

    public String getOBXReferenceRange(int i, int j){// not relevant to TRUENORTH
        String ret = "";
        try{ 
            OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);

            // If the units are not specified use the formatted reference range
            // which will usually contain the units as well

            if (getOBXUnits(i, j).equals(""))
                ret = getString(Terser.get(obxSeg,7,0,2,1));

            // may have to fall back to original reference range if the second
            // component is empty
            if (ret.equals("") ){
                ret = getString(obxSeg.getReferencesRange().getValue());
                if (!ret.equals("")){
                    // format the reference range if using the unformatted one
                    String[] ranges = ret.split("-");
                    for (int k=0; k < ranges.length; k++){
                        if (ranges[k].endsWith("."))
                            ranges[k] = ranges[k].substring(0, ranges[k].length()-1);
                    }

                    if (ranges.length > 1){
                        if (ranges[0].contains(">") || ranges[0].contains("<"))
                            ret = ranges[0]+"= "+ranges[1];
                        else
                            ret = ranges[0]+" - "+ranges[1];
                    }else if (ranges.length == 1){
                        ret = ranges[0]+" -";
                    }
                }
            }
        }catch(Exception e){
            logger.error("Exception retrieving reception range", e);
        }
        return ret.replaceAll("\\\\\\.br\\\\", "");
    }

    public String getOBXUnits(int i, int j){ // not relevant to TRUENORTH
        String ret = "";
        try{
            OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
            ret = getString(obxSeg.getUnits().getIdentifier().getValue());

            // if there are no units specified check the formatted reference
            // range for the units
            if (ret.equals("")){
                ret = getString(Terser.get(obxSeg,7,0,2,1));

                // only display units from the formatted reference range if they
                // have not already been displayed as the reference range
                if (ret.contains("-") || ret.contains("<") || ret.contains(">") || ret.contains("NEGATIVE"))
                    ret = "";
            }
        }catch(Exception e){
            logger.error("Exception retrieving units", e);
        }
        return ret.replaceAll("\\\\\\.br\\\\", "");
    }

    public String getOBXResultStatus(int i, int j){ // not relevant to TRUENORTH
        try{

            // result status is stored in the wrong field.... i think
            return(getString(( (obrSegMap.get(obrSegKeySet.get(i))).get(j)).getObservResultStatus().getValue()));
        }catch(Exception e){
            logger.error("Exception retrieving results status", e);
            return("");
        }
    }

    public int getOBXFinalResultCount(){
        // not applicable to gdml labs
        return 0;
    }

    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList<String> getHeaders(){
        return headers;
    }

    /**
     *  Methods to get information from observation notes
     */
    public int getOBRCommentCount(int i){
        int count = 0;

        for (int j=0; j < getOBXCount(i); j++){
            if (getString((( obrSegMap.get(obrSegKeySet.get(i))).get(j)).getValueType().getValue()).equals("FT"))
                count++;
        }

        return count;

    }

    public String getOBRComment(int i, int j){
        String comment = "";

        // update j to the number of the comment not the index of a comment array
        j++;
        try {
            int obxCount = getOBXCount(i);
            int count = 0;
            int l = 0;
            OBX obxSeg = null;

            while ( l < obxCount && count < j){

                obxSeg = ( obrSegMap.get(obrSegKeySet.get(i))).get(l);
                if (getString(obxSeg.getValueType().getValue()).equals("FT")){
                    count++;
                }
                l++;

            }
            l--;

            int k = 0;

            String nextComment = Terser.get(obxSeg,5,k,1,1);
            
            while(nextComment != null){

            	//in case there are "&" in field, & = sub-component separator in HL7
                int subIndex = 2;
            	String nextCommentSub = Terser.get(obxSeg,5,k,1,subIndex);
            	while(nextCommentSub != null){
            		nextComment += "&" + nextCommentSub;
            		nextCommentSub = Terser.get(obxSeg,5,k,1,++subIndex);
            	}
            	
                comment = comment + nextComment.replaceAll("(        )+", "<br />");
                k++;
                nextComment = Terser.get(obxSeg,5,k,1,1);
            }

        } catch (Exception e) {
            logger.error("getOBRComment error", e);
            comment = "";
        }
        return comment;
    }

    /**
     *  Methods to get information from observation notes
     */
    public int getOBXCommentCount(int i, int j){ // not relevant to TRUENORTH
        int count = 0;
        try{
            String comment = "";
            OBX obxSeg = ( obrSegMap.get(obrSegKeySet.get(i))).get(j);
            while(comment != null){
                count++;
                comment = Terser.get(obxSeg,7,count,1,1);
                if (comment == null)
                    comment = Terser.get(obxSeg,7,count,2,1);
            }
            count--;

        }catch(Exception e){
            logger.error("Exception retrieving obx comment count", e);
            count = 0;
        }
        return count;
    }

    public String getOBXComment(int i, int j, int k){ // not relevant to TRUENORTH
        String comment = "";
        try{
            k++;

            OBX obxSeg = ( obrSegMap.get(obrSegKeySet.get(i))).get(j);
            comment = Terser.get(obxSeg,7,k,1,1);
            if (comment == null)
                comment = Terser.get(obxSeg,7,k,2,1);

        }catch(Exception e){
            logger.error("Cannot return comment", e);
        }
        return comment.replaceAll("\\\\\\.br\\\\", "<br />");
    }
	
    /**
     *  Methods to get information about the patient
     */
    public String getPatientName(){
        return(getFirstName()+" "+getLastName());
    }

    public String getFirstName(){
    	return getFirstName(msg);
    }
    
    private String getFirstName(ORU_R01 msg){
        return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getGivenName().getValue()));
    }

    public String getLastName(){
    	return getLastName(msg);
    }
    
    private String getLastName(ORU_R01 msg){
        return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientName().getFamilyName().getValue()));
    }

    public String getDOB(){
        try{
            return(formatDateTime(getString(msg.getRESPONSE().getPATIENT().getPID().getDateOfBirth().getTimeOfAnEvent().getValue())).substring(0, 10));
        }catch(Exception e){
            logger.error("Exception retrieving DOB", e);
            return("");
        }
    }

    public String getAge(){
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

    public String getSex(){
        return(getString(msg.getRESPONSE().getPATIENT().getPID().getSex().getValue()));
    }

    public String getHealthNum(){
    	return getHealthNum(msg);
    }
    	
    private String getHealthNum(ORU_R01 msg){
        try {
	        return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDInternalID(0).getID().getValue()));
        } catch (HL7Exception e) {
        	logger.error("Could not get hin", e);
        	return "";
        }
    }

    public String getServiceDate(){
        try{
            return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getRequestedDateTime().getTimeOfAnEvent().getValue())));
        }catch(Exception e){
            logger.error("Exception retrieving service date", e);
            return("");
        }
    }

    public String getRequestDate(int i){
        try{
            String ret = ( obrSegKeySet.get(i)).getRequestedDateTime().getTimeOfAnEvent().getValue();
            return(formatDateTime(getString(ret)));
        }catch(Exception e){
            logger.error("Exception retrieving request date", e);
            return("");
        }
    }

    public String getOrderStatus(){
        // TRUENORTH won't send pending labs... they'll send only the final parts of the
        // labs
        return("F");
    }
    
    public String getAccessionNum(){
    	return getAccessionNum(msg);
    }
    
    private String getAccessionNum(ORU_R01 msg){
        try{
            return(getString(msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getVisitNumber().getID().getValue()));
        }catch(Exception e){
            logger.error("Could not return accession num: ", e);
            return("");
        }
    }
    
    public String getDocName(){
        String docName = "";
        int i=0;
        try{
            while(!getFullDocName(msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getReferringDoctor(i)).equals("")){
                if (i==0){
                    docName = getFullDocName(msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getReferringDoctor(i));
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

        String docNames = "";

        /*try {
            Terser terser = new Terser(msg);

            String givenName = terser.get("/.ZDR(0)-4-1");
            String middleName = terser.get("/.ZDR(0)-4-3");
            String familyName = terser.get("/.ZDR(0)-4-2");

            int i=1;
            while (givenName != null){

                if (i==1)
                    docNames = givenName;
                else
                    docNames = docNames+", "+givenName;

                if (middleName != null)
                    docNames = docNames+" "+middleName;
                if (familyName != null)
                    docNames = docNames+" "+familyName;

                givenName = terser.get("/.ZDR("+i+")-4-1");
                middleName = terser.get("/.ZDR("+i+")-4-3");
                familyName = terser.get("/.ZDR("+i+")-4-2");

                i++;
            }

            return(docNames);

        } catch (Exception e) {
            //ignore error... it will occur when the zdr segment is not present
            //logger.error("Could not retrieve cc'd docs", e);
            return("");
        }*/
        return(docNames);
    }

    public ArrayList<String> getDocNums(){
        String docNum = "";
        ArrayList<String> nums = new ArrayList<String>();
        int i=0;
        try{

            //requesting client number
            docNum = msg.getRESPONSE().getPATIENT().getVISIT().getPV1().getReferringDoctor(i).getIDNumber().getValue();
            if (docNum != null){
               nums.add(docNum);
            }

            //cc'd docs numbers
            String num = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue();
            i=1;
            while (num != null){
                if (!num.equals(docNum))
                    nums.add(num);
                num = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue();
                i++;
            }

        }catch(Exception e){
            //ignore error... it will occur when the zdr segment is not present
            //logger.error("Could not return numbers", e);
        }
        return(nums);
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
    
    private String formatDateTime(String plain){
    	if (plain==null || plain.trim().equals("")) return "";

        String dateFormat = "yyyyMMddHHmmss";
        dateFormat = dateFormat.substring(0, plain.length());
        String stringFormat = "yyyy-MM-dd HH:mm:ss";
        stringFormat = stringFormat.substring(0, stringFormat.lastIndexOf(dateFormat.charAt(dateFormat.length()-1))+1);

        Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
        return UtilDateUtilities.DateToString(date, stringFormat);
    }

    private String getString(String retrieve){
        if (retrieve != null){
            return(retrieve.trim());
        }else{
            return("");
        }
    }

	@Override
    public String getHomePhone() {
	    return "";
    }

	@Override
    public String getWorkPhone() {
	    return "";
    }

	@Override
    public String getPatientLocation() {
		return(getString(msg.getMSH().getSendingFacility().getNamespaceID().getValue()));
    }

	@Override
    public String getClientRef() {
	    return "";
    }

	@Override
    public String getNteForPID() {
	    // TODO Auto-generated method stub
	    return "";
    }
}
