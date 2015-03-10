/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
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
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.segment.OBR;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;



public class PFHTHandler implements MessageHandler {

	Logger logger = Logger.getLogger(PFHTHandler.class);

	private ORU_R01 msg = null;
	//private MDM_R01 mdmMsg = null;

	private ArrayList<String> headers = null;
	private HashMap<OBR, ArrayList<OBX>> obrSegMap = null;
	private ArrayList<OBR> obrSegKeySet = null;



	public PFHTHandler() {
		logger.info("NEW PFHTHandler UPLOAD HANDLER instance just instantiated. ");
	}

	 public void init(String hl7Body) throws HL7Exception {

	        Parser p = new PipeParser();
	        p.setValidationContext(new NoValidation());
	      //  msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));


	        	msg = (ORU_R01) p.parse(hl7Body);


	        ArrayList<String> labs = getMatchingPFHTlabs(hl7Body);
	        headers = new ArrayList<String>();
	        obrSegMap = new LinkedHashMap<OBR, ArrayList<OBX>>();
	        obrSegKeySet = new ArrayList<OBR>();


	        for (int i=0; i < labs.size(); i++){
	            msg = (ORU_R01) p.parse(( labs.get(i)).replaceAll("\n", "\r\n"));
	            int obrCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();


	            for (int j=0; j < obrCount; j++){

	                // ADD OBR SEGMENTS AND THEIR OBX SEGMENTS TO THE OBRSEGMAP
	                OBR obrSeg = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBR();
	                ArrayList<OBX> obxSegs =  obrSegMap.get(obrSeg);


	                // if the obrsegment has not yet been created it will be now
	                if (obxSegs == null)
	                    obxSegs = new ArrayList<OBX>();

	                int obxCount = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBSERVATIONReps();

	                logger.info("obxCount = "+obxCount);
	                for (int k=0; k < obxCount; k++){
	                //	int obxNteCount = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBSERVATION(k).getNTEReps();
	                	OBX curObxSeg = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBSERVATION(k).getOBX();
	                	obxSegs.add(curObxSeg);
	                }

	                obrSegMap.put(obrSeg, obxSegs);
	                obrSegKeySet.add(obrSeg);

	                // ADD THE HEADER TO THE HEADERS ARRAYLIST
	                String header = getString(obrSeg.getUniversalServiceIdentifier().getIdentifier().getValue());
	                logger.info("PFHT header ="+header);
	                if (!headers.contains(header)){
	                    headers.add(header);
	                }

	            }
	        }
	    }

	    private ArrayList<String> getMatchingPFHTlabs(String hl7Body){
	        Base64 base64 = new Base64(0);
	        ArrayList<String> ret = new ArrayList<String>();
	        int monthsBetween = 0;
	        Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
	        try{
	        	List<Hl7TextMessageInfo> matchingLabs = hl7TextInfoDao.getMatchingLabs(hl7Body);
				for ( Hl7TextMessageInfo l: matchingLabs ) {
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


	        }catch(Exception e){
	            logger.error("Exception in HL7 getMatchingLabs: ", e);
	        }

	        //if there have been no labs added to the database yet just return this lab
	        if (ret.size() == 0)
	            ret.add(hl7Body);
	        return ret;
	    }

	    public String getMsgType(){
	        return("PFHT");
	    }

	    public String getMsgDate(){
	        return(formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
	    }

	    public String getMsgPriority(){
	        String priority = "R";
	        for (int i=0; i < getOBRCount(); i++){
	            try{
	               /* if (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getQuantityTiming().getPriority().getValue()).equals("S")){
	                    priority="S";
	                    break;
	                }*/
	            	priority= getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getQuantityTiming().getPriority().getValue());
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
	        return ((obrSegKeySet.get(i)).getUniversalServiceIdentifier().getText().getValue());
	    }

	    public String getTimeStamp(int i, int j){
	        try{
	            String ret = (obrSegKeySet.get(i)).getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValue();
	            if (ret == null)
	                ret = obrSegKeySet.get(i).getObservationDateTime().getTimeOfAnEvent().getValue();
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

	            return(getString( ( (obrSegMap.get(obrSegKeySet.get(i))).get(j)).getAbnormalFlags(0).getValue() ));
	        }catch(Exception e){
	            logger.error("Exception retrieving abnormal flag", e);
	            return("");
	        }
	    }

	    public String getObservationHeader(int i, int j){
	        try{
	            return(getString((obrSegKeySet.get(i)).getUniversalServiceIdentifier().getIdentifier().getValue()));
	        }catch(Exception e){
	            logger.error("Exception getting header", e);
	            return("");
	        }
	    }

	    public String getOBXIdentifier(int i, int j){

	        try{

	            Segment obxSeg = ((obrSegMap.get(obrSegKeySet.get(i))).get(j));
	            String ident = getString(Terser.get(obxSeg, 3, 0, 1, 1 ));
	            String subIdent = Terser.get(obxSeg, 3, 0, 1, 2);

	            if (subIdent != null)
	                ident = ident+"&"+subIdent;

	            logger.info("returning obx identifier: "+ident);
	            return(ident);
	        }catch(Exception e){
	            logger.error("error returning obx identifier", e);
	            return("");
	        }
	    }


	    public String getOBXValueType(int i, int j){
	        String ret = "";
	        try{
	            OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
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
	            OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);
	            logger.info("271 Num of obxsegs ="+(obrSegMap.get(obrSegKeySet.get(i))).size());
	            logger.info("OBX value type"+obxSeg.getValueType().getValue()+ "i= "+i+" j= "+j);
	            if (obxSeg.getValueType().getValue()!=null && (!obxSeg.getValueType().getValue().equals("FT")))
	                ret = getString(obxSeg.getObservationIdentifier().getText().getValue());
	        }catch(Exception e){
	            logger.error("Error returning OBX name", e);
	        }
	        logger.info("OBX Name = "+ret);
	        return ret;
	    }

	    public String getOBXResult(int i, int j){

	        String result = "";
	        try{

	            result = getString(Terser.get(((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)),5,0,1,1));

	            // format the result
	            if (result.endsWith("."))
	                result = result.substring(0, result.length()-1);

	        }catch(Exception e){
	            logger.error("Exception returning result", e);
	        }
	        logger.info("OBX Result = "+result);
	        return result.replaceAll("\\\\\\.br\\\\", "<br />");
	    }

	    public String getOBXReferenceRange(int i, int j){
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

	    public String getOBXUnits(int i, int j){
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

	    public String getOBXResultStatus(int i, int j){
	        try{

	            // result status is stored in the wrong field.... i think
	            return(getString((( obrSegMap.get(obrSegKeySet.get(i))).get(j)).getNatureOfAbnormalTest().getValue()));
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
	        /*j++;
	        try {
	            Terser terser = new Terser(msg);

	            int obxCount = getOBXCount(i);
	            int count = 0;
	            int l = 0;
	            OBX obxSeg = null;

	            while ( l < obxCount && count < j){

	                obxSeg = (OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(l);
	                if (getString(obxSeg.getValueType().getValue()).equals("FT")){
	                    count++;
	                }
	                l++;

	            }
	            l--;

	            int k = 0;
	            String nextComment = terser.get(obxSeg,5,k,1,1);
	            while(nextComment != null){
	                comment = comment + nextComment.replaceAll("\\\\\\.br\\\\", "<br />");
	                k++;
	                nextComment = terser.get(obxSeg,5,k,1,1);
	            }

	        } catch (Exception e) {
	            logger.error("getOBRComment error", e);
	            comment = "";
	        }*/

	        return comment;
	    }

	    /**
	     * Returns the comment in NTE-3 for the preceding OBX segment
	     */
	    public String getNteForOBX(int i, int j){
	    	String ret = "";
	    	try{

	    	 ret= msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE().getComment(0).toString();
	    	 if (ret==null) ret="";
	    	 logger.info("NTE for OBR="+i+" ,OBX="+j+" is "+ret);

	    	} catch (Exception e) {
	    		logger.error("Error accessing the NTE segment for OBX"+j+" "+e);
	    	}

			return ret.replaceAll("\\\\\\.br\\\\", "<br />");
		}

	    /**
	     *  Methods to get information from observation notes
	     */
	    public int getOBXCommentCount(int i, int j){
	        int count = 0;
	        try{

	            int obxcount;
	            OBX obxSeg = (obrSegMap.get(obrSegKeySet.get(i))).get(j);

	            obxcount = (obrSegMap.get(obrSegKeySet.get(i))).size();
	            for (int k=0;k<obxcount;k++) {
	            	if (Terser.get(obxSeg,7,k,1,1)!=null && !Terser.get(obxSeg,7,k,1,1).equals("") && (Terser.get(obxSeg,7,count,2,1)!=null && !Terser.get(obxSeg,7,count,2,1).equals(""))) {
	            		count++;
	            	}
	            }


	        }catch(Exception e){
	            logger.error("Exception retrieving obx comment count", e);
	            count = 0;
	        }
	        return count;
	    }

	    public String getOBXComment(int i, int j, int k){
	        String comment = "";
	        try{

	            OBX obxSeg =  ( obrSegMap.get(obrSegKeySet.get(i))).get(j);
	            comment = Terser.get(obxSeg,7,k,1,1);
	            if (comment == null)
	                comment = Terser.get(obxSeg,7,k,2,1);

	        }catch(Exception e){
	            logger.error("Cannot return comment", e);
	        }
	        logger.info("OBX comment"+comment.replaceAll("\\\\\\.br\\\\", "<br />"));
	        return comment.replaceAll("\\\\\\.br\\\\", "<br />");
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
	            java.util.Date date = formatter.parse(dob);
	            age = UtilDateUtilities.calcAge(date);
	        } catch (ParseException e) {
	            logger.error("Could not get age", e);

	        }
	        return age;
	    }

	    public String getSex(){
	        return(getString(msg.getRESPONSE().getPATIENT().getPID().getSex().getValue()));
	    }

	    public String getHealthNum(){
	        return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDExternalID().getID().getValue()));
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
	            return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue())));
	        }catch(Exception e){
	            logger.error("Exception retrieving service date", e);
	            return("");
	        }
	    }

	    public String getOrderStatus(){
	    	/*try{
	            String status = "F";
	            int obrCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();

	            for (int i=0; i < obrCount; i++){
	                if (getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getResultStatus().getValue()).equals("P"))
	                    status = "P";
	            }

	            return(status);
	        }catch(Exception e){
	            logger.error("Exception retrieving order status", e);
	            return("");
	        }*/
	    	return ("F"); //PFHT result status is Transcribed, Auth(Verified) so just return F
	    }

	    public String getClientRef(){

	        try{
	            return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDInternalID(0).getAssigningAuthority().getNamespaceID().getValue()));
	        }catch(Exception e){
	            logger.error("Could not return accession num: ", e);
	            return("");
	        }
	    }

	    public String getAccessionNum(){
	        try{
	            return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDInternalID(0).getID().getValue()));
	        }catch(Exception e){
	            logger.error("Could not return accession num: ", e);
	            return("");
	        }
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

	        String docNames = "";

	        try {
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
	        }

	    }

	    public ArrayList<String> getDocNums(){
	        String docNum = "";
	        ArrayList<String> nums = new ArrayList<String>();
	        int i=0;
	        try{

	            //requesting client number
	            docNum = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getOrderingProvider(i).getIDNumber().getValue();
	            nums.add(docNum);

	            //cc'd docs numbers
	            Terser terser = new Terser(msg);
	            String num = terser.get("/.ZDR(0)-3-1");
	            i=1;
	            while (num != null){
	                if (!num.equals(docNum))
	                    nums.add(num);
	                num = terser.get("/.ZDR("+i+")-3-1");
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


	    private String formatDateTime(String plain){
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

	    public String getFillerOrderNumber(){

			return "";
		}

	    public String getEncounterId(){
	    	return "";
	    }

	    public String getRadiologistInfo(){
			return "";
		}
	    public String getObservationName(int i, int j){
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
