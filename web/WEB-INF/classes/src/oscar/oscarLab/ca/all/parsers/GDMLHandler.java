/*
 * GDMLHandler.java
 *
 * Created on June 7, 2007, 12:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.parsers;

import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import org.apache.log4j.Logger;
import oscar.util.UtilDateUtilities;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.datatype.*;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.parser.*;
import ca.uhn.hl7v2.validation.impl.NoValidation;

import java.text.*;
import java.util.ArrayList;


/**
 *
 * @author wrighd
 */
public class GDMLHandler implements MessageHandler {
    
    Logger logger = Logger.getLogger(GDMLHandler.class);
    static ORU_R01 msg = null;
    
    /** Creates a new instance of CMLHandler */
    public GDMLHandler(){
    }
    
    public void init(String hl7Body) throws HL7Exception {
        Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());
        msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
    }
    
    public String getMsgType(){
        return("GDML");
    }
    
    public String getMsgDate(){
        return(formatDateTime(msg.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue()));
    }
    
    public String getMsgPriority(){
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
        return(msg.getRESPONSE().getORDER_OBSERVATIONReps());
    }
    
    public int getOBXCount(int i){
        int count = 0;
        try{
            
            int trueCount = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps();
            // an OBX segment marked with a value type of "FT" is a comment and
            // should not be counted, segments marked with "CE" should also not
            // be countedit only occurs as the last OBX in an OBR group
            for (int j=0; j < trueCount; j++){
                String valueType = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX().getValueType().getValue();
                if (valueType != null && !valueType.equals("FT") && !valueType.equals("CE"))
                    count++;
            }
            
        }catch(Exception e){
            logger.error("GDMLHandler getOBXCount error", e);
        }
        
        return count;
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
            String ret = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValue();
            if (ret == null)
                ret = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getObservationDateTime().getTimeOfAnEvent().getValue();
            return(formatDateTime(getString(ret)));
        }catch(Exception e){
            return("");
        }
    }
    
    public boolean isOBXAbnormal(int i, int j){
        if (getOBXAbnormalFlag(i, j).equals("N"))
            return(false);
        else
            return(true);
    }
    
    public String getOBXAbnormalFlag(int i, int j){
        
        try{
            int obxNum = findOBXNum(i, j);            
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX().getAbnormalFlags(0).getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getObservationHeader(int i, int j){
        try{
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBR().getUniversalServiceIdentifier().getAlternateIdentifier().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXIdentifier(int i, int j){
        
        try{
            int obxNum = findOBXNum(i, j);
            Terser t = new Terser(msg);
            Segment obxSeg = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX();
            String ident = getString(t.get(obxSeg, 3, 0, 1, 1 ));
            String subIdent = t.get(obxSeg, 3, 0, 1, 2);
            
            if (subIdent != null)
                ident = ident+"&"+subIdent;
            
            return(ident);
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXName(int i, int j){
        try{
            int obxNum = findOBXNum(i, j);
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX().getObservationIdentifier().getText().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXResult(int i, int j){
        try{
            int obxNum = findOBXNum(i, j);
            Terser terser = new Terser(msg);
            return(getString(terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX(),5,0,1,1)));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXReferenceRange(int i, int j){
        try{
            int obxNum = findOBXNum(i, j);
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX().getReferencesRange().getValue()).replaceAll("\\\\\\.br\\\\", ""));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXUnits(int i, int j){
        try{
            int obxNum = findOBXNum(i, j);
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX().getUnits().getIdentifier().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXResultStatus(int i, int j){
        try{
            int obxNum = findOBXNum(i, j);
            // result status is stored in the wrong field.... i think
            return(getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX().getNatureOfAbnormalTest().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public int getOBXFinalResultCount(){
        int obrCount = getOBRCount();
        int obxCount;
        int count = 0;
        for (int i=0; i < obrCount; i++){
            obxCount = getOBXCount(i);
            for (int j=0; j < obxCount; j++){
                if (!getOBXResultStatus(i, j).equalsIgnoreCase("P"))
                    count++;
            }
        }
        return count;
    }
    
    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList getHeaders(){
        int i;
        int j;
        int k = 0;
        ArrayList headers = new ArrayList();
        String currentHeader;
        try{
            for (i=0; i < msg.getRESPONSE().getORDER_OBSERVATIONReps(); i++){
                
                currentHeader = getObservationHeader(i, 0);
                
                k = 0;
                while(k < headers.size() && !currentHeader.equals(headers.get(k))){
                    k++;
                }
                if (k == headers.size()){
                    logger.info("Adding header: '"+currentHeader+"' to list");
                    headers.add(currentHeader);
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
        try {
            int lastOBX = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps() - 1;
            //if (lastOBX == 0 && getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(lastOBX).getOBX().getValueType().getValue()).equals("FT"))
            if (getOBXCount(i) == 0 && getString(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(lastOBX).getOBX().getValueType().getValue()).equals("FT"))
                return(1);
            else
                return(0);
        } catch (Exception e) {
            return(0);
        }
        
    }
    
    public String getOBRComment(int i, int j){
        String comment = "";
        try {
            Terser terser = new Terser(msg);
            
            int k = 0;
            int lastOBX = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps() - 1;
            String nextComment = terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(lastOBX).getOBX(),5,k,1,1);
            while(nextComment != null){
                comment = comment + nextComment.replaceAll("\\\\\\.br\\\\", "<br />");
                k++;
                nextComment = terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(lastOBX).getOBX(),5,k,1,1);
            }
            
        } catch (Exception e) {
            logger.info("getOBRComment error", e);
            comment = "";
        }
        return comment;
    }
    
    /**
     *  Methods to get information from observation notes
     */
    public int getOBXCommentCount(int i, int j){
        try{
            int obxNum = findOBXNum(i, j);
            Terser terser = new Terser(msg);
            int trueOBXCount = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps();
            int obxCount = getOBXCount(i);
            
            if ( obxNum+1 == obxCount && obxCount != trueOBXCount )
                return(1);
            else if(terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX(),7,1,1,1) != null)
                return(1);
            else
                return(0);
        }catch(Exception e){
            return(0);
        }
    }
    
    public String getOBXComment(int i, int j, int k){
        try{
            k++;
            int obxNum = findOBXNum(i, j);
            Terser terser = new Terser(msg);
            String comment = "";
            String nextComment = terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX(),7,k,1,1);
            
            // check the reference range for comments first
            while(nextComment != null){
                comment = comment + nextComment.replaceAll("\\\\\\.br\\\\", "<br />");
                k++;
                nextComment = terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum).getOBX(),7,k,1,1);
            }
            
            // check the results field for the comment
            if (comment.equals("")){
                k = 0;
                nextComment = terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum+1).getOBX(),5,k,1,1);
                while(nextComment != null){
                    comment = comment + nextComment.replaceAll("\\\\\\.br\\\\", "<br />");
                    k++;
                    nextComment = terser.get(msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(obxNum+1).getOBX(),5,k,1,1);
                }
            }
            
            return(comment);
            
        }catch(Exception e){
            logger.error("Cannot return comment", e);
            
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
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            java.util.Date date = (java.util.Date)formatter.parse(dob);
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
            return("");
        }
    }
    
    public String getOrderStatus(){
        // no order status specified, assuming final
        return("F");
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
            logger.error("Could not retrieve cc'd docs", e);
            return("");
        }
        
    }
    
    public ArrayList getDocNums(){
        String docNum = "";
        ArrayList nums = new ArrayList();
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
    
    private int findOBXNum(int i, int j){
        int count = 0;
        try{
            int trueCount = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATIONReps();
            int k = 0;
            
            while (k < trueCount && count != j){
                String valueType = msg.getRESPONSE().getORDER_OBSERVATION(i).getOBSERVATION(k).getOBX().getValueType().getValue();
                if (valueType != null && !valueType.equals("FT") && !valueType.equals("CE"))
                    count++;
                
                k++;
            }
        }catch(Exception e){
            logger.error("Error retrieveing obx num", e);
        }
        return count;
    }
    
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
    
    
    private String formatDateTime(String plain){
        if (!plain.equals("")){
            String formatted = plain.substring(0, 4)+"-"+plain.substring(4, 6)+"-"+plain.substring(6);
            if (plain.length() > 8)
                formatted = formatted.substring(0, 10)+" "+formatted.substring(10, 12)+":"+formatted.substring(12, 14)+":00";
            else
                formatted = formatted+" 00:00:00";
            return (formatted);
        }else{
            return (plain);
        }
    }
    
    private String getString(String retrieve){
        if (retrieve != null){
            return(retrieve.trim());
        }else{
            return("");
        }
    }
    
}