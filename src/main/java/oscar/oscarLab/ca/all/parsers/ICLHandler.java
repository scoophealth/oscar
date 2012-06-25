/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

/*
 * ICLHandler.java
 * Created on Mar 28, 2009
 * Modified by David Daley, Ithream
 * Derived from PATH7Handler.java, by wrighd
 */

package oscar.oscarLab.ca.all.parsers;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.datatype.XCN;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;


/*
 * @author David Daley, Ithream
 */
public class ICLHandler extends DefaultGenericHandler implements MessageHandler {

    Logger logger = Logger.getLogger(ICLHandler.class);
    ORU_R01 msg = null;

    /** Creates a new instance of ICLHandler */
    public ICLHandler(){
    }

    public void init(String hl7Body) throws HL7Exception {
        Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());
        msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
    }

    public String getMsgType(){
        return("ICL");
    }

    public String getMsgPriority(){
        return("");
    }
    /*
     *  MSH METHODS
     */

    public String getMsgDate(){
        return(formatDateTime(getString(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue())));
    }

    /*
     *  PID METHODS
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
				try {
            return(getString(msg.getRESPONSE().getPATIENT().getPID().getSex().getValue()));
				} catch (Exception e) {
				    logger.error("Exception finding sex", e);
						return("");
				}
    }

    public String getHealthNum(){
				try {
            return(getString(msg.getRESPONSE().getPATIENT().getPID().getPatientIDInternalID(0).getID().getValue()));
				} catch (Exception e) {
				    logger.error("Exception finding health number", e);
						return("");
				}
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
            logger.error("Exception finding phone number", e);
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
            logger.error("Exception finding work phone number", e);
            return("");
        }
    }

    public String getPatientLocation(){
        return(getString(msg.getMSH().getSendingFacility().getNamespaceID().getValue()));
    }

    /*
     *  OBC METHODS
     */
    public String getAccessionNum(){
        try{
            int obrCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();
            String accessionNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getFillerOrderNumber().getEntityIdentifier().getValue());

            // if accessionNum can't be found in the OBR record of the first observation,
            //  look for it in subsequent observation records
            if (accessionNum != ""){
                return(accessionNum.substring(0,accessionNum.indexOf("-")));
            }

            for (int j=1; j < obrCount; j++){
                accessionNum = getString(msg.getRESPONSE().getORDER_OBSERVATION(j).getOBR().getFillerOrderNumber().getEntityIdentifier().getValue());
                if (accessionNum != ""){
                    return(accessionNum.substring(0,accessionNum.indexOf("-")));
                }
            }
            // haven't found an accessionNum
            return("");

        }catch(Exception e){

            logger.error("Could not return accession number", e);
            return("");
        }
    }

    /*
     *  OBR METHODS
     */

    public int getOBRCount(){
        return(msg.getRESPONSE().getORDER_OBSERVATIONReps());
    }

    public String getOBRName(int i){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getText().getValue()));
        }catch(Exception e){
            return("");
        }
    }

    public String getObservationHeader(int i, int j){
        try{
            //return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getAlternateIdentifier().getValue()));
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getText().getValue()));
        }catch(Exception e){
            return("");
        }
    }

    public int getOBRCommentCount(int i){
        try {
            int count = msg.getRESPONSE().getORDER_OBSERVATION(i).getNTEReps();
            return(count);
        } catch (Exception e) {
            return(0);
        }
    }

    public String getOBRComment(int i, int j){
        try {

            // ICL likes to thrown reserved characters in their comments -- this is to compensate
            String obrComment = getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getNTE(j),3,0,1,1))+" "+
                    getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getNTE(j),3,0,2,1)).trim();

            return(obrComment);

        } catch (Exception e) {
            return("");
        }

    }

    public String getServiceDate(){
        try{
            String srvcDate = msg.getRESPONSE().getORDER_OBSERVATION(0).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();

            if (srvcDate != null){
                return(formatDateTime(srvcDate));
            }

            // if srvcDate can't be found in the OBR record of the first observation,
            //  look for it in subsequent observation records
            int obrCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();
            for (int j=1; j < obrCount; j++){
                srvcDate = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();
                if (srvcDate != null){
                    return(formatDateTime(srvcDate));
                }
            }
            // haven't found a srvcDate in the OBR - use MSH date instead
            return(formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
        }catch(Exception e){
            return("");
        }
    }

    public String getOrderStatus(){
        // ICL status: U - Updated Final, F - Final, P - Partial
        // would like to return the least of the statuses, but 'U' not recognized upstream, so return 'F' if not 'P'
        try{
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
        }

    }

    public String getClientRef(){
        String docNum = "";
        try{
            Terser terser = new Terser(msg);
            docNum = terser.get("/.ORC-12-1");
            return(docNum);
        }catch(Exception e){
            logger.error("Exception returning client ref number", e);
            return("");
        }
    }

    public String getDocName(){
        String docLastName = "";
        String docFirstName = "";
        try{
            Terser terser = new Terser(msg);
            docLastName = terser.get("/.ORC-12-2");
            docFirstName = terser.get("/.ORC-12-3");
            return(docFirstName + " " + docLastName);
        }catch(Exception e){
            logger.error("Exception returning doctor name", e);
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
            logger.error("Exception returning cc'ed doctors", e);
            return("");
        }
    }

    public ArrayList<String> getDocNums(){
        ArrayList<String> nums = new ArrayList<String>();
        String docNum;
        try{
            Terser terser = new Terser(msg);
            if ((docNum = terser.get("/.ORC-12-1")) != null){
                nums.add(docNum);
            }

            int i=0;
            while((docNum = terser.get("/.OBR-28("+i+")-1")) != null){
                nums.add(docNum);
                i++;
            }

        }catch(Exception e){
            logger.error("Exception returning docNums", e);
        }

        return(nums);
    }


    /*
     *  OBX METHODS
     */
    public int getOBXCount(int i){
        int count = 0;
        try{
            count = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps();
            // if count is 1 there may only be an nte segment and no obx segments so check
            if (count == 1){
                String test = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(0).getOBX().getObservationIdentifier().getText().getValue();
                if (test == null)
                    count = 0;
            }
        }catch(Exception e){
            logger.error("Error retrieving obx count", e);
            count = 0;
        }
        return count;
    }

    public String getOBXIdentifier(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getIdentifier().getValue()));
        }catch(Exception e){
            logger.error("Error retrieving obx identifier", e);
            return("");
        }
    }

    public String getOBXName(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservationIdentifier().getText().getValue()));
        }catch(Exception e){
            logger.error("Error retrieving obx name", e);
            return("");
        }
    }

    public String getOBXResult(int i, int j){
        try{
            return(getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX(),5,0,1,1)));
        }catch(Exception e){
            logger.error("Error retrieving obx result", e);
            return("");
        }
    }

    public String getOBXReferenceRange(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getReferencesRange().getValue()));
        }catch(Exception e){
            logger.error("Error retrieving obx reference range", e);
            return("");
        }
    }

    public String getOBXUnits(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getUnits().getIdentifier().getValue()));
        }catch(Exception e){
            logger.error("Error retrieving obx units", e);
            return("");
        }
    }

    public String getOBXResultStatus(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getObservResultStatus().getValue()));
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
            return(formatDateTime(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getDateTimeOfTheObservation().getTimeOfAnEvent().getValue())));
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
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getAbnormalFlags(0).getValue()));
        }catch(Exception e){
            logger.error("Error retrieving obx abnormal flag", e);
            return("");
        }
    }

    public int getOBXCommentCount(int i, int j){
        try {
            int count = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTEReps();
            return(count);
        } catch (Exception e) {
            logger.error("Error retrieving obx comment count", e);
            return(0);
        }

    }

    public String getOBXComment(int i, int j, int k){
        try {
            // ICL likes to thrown reserved characters in their comments -- this is to compensate
            String obxComment = getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k),3,0,1,1))+" "+
                    getString(Terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getNTE(k),3,0,2,1)).trim();

            return(obxComment);


        } catch (Exception e) {
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
            for (i=0; i < msg.getRESPONSE().getORDER_OBSERVATIONReps(); i++){

                currentHeader = getObservationHeader(i, 0);
                arraySize = headers.size();
                if (arraySize == 0 || !currentHeader.equals(headers.get(arraySize-1))){
                    headers.add(currentHeader);
                }

            }
            return(headers);
        }catch(Exception e){
            logger.error("Could not create header list", e);

            return(null);
        }

    }

    public String audit(){
        return "";
    }

    /*
     *  END OF PUBLIC METHODS
     */


    private String getFullDocName(XCN docSeg){
        String docName = "";

        if(docSeg.getPrefixEgDR().getValue() != null)
            docName = docSeg.getPrefixEgDR().getValue();

        if(docSeg.getGivenName().getValue() != null){
            if (docName.equals("")){
                docName = docSeg.getGivenName().getValue();
            }else{
                docName = docName +" "+ docSeg.getGivenName().getValue();
            }
        }
        if(docSeg.getMiddleInitialOrName().getValue() != null)
            docName = docName +" "+ docSeg.getMiddleInitialOrName().getValue();
        if(docSeg.getFamilyName().getValue() != null)
            docName = docName +" "+ docSeg.getFamilyName().getValue();
        if(docSeg.getSuffixEgJRorIII().getValue() != null)
            docName = docName +" "+ docSeg.getSuffixEgJRorIII().getValue();
        if(docSeg.getDegreeEgMD().getValue() != null)
            docName = docName +" "+ docSeg.getDegreeEgMD().getValue();

        return (docName);
    }




    protected String getString(String retrieve){
        if (retrieve != null){
            return(retrieve.trim().replaceAll("\\\\\\.br\\\\", "<br />"));
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

}
