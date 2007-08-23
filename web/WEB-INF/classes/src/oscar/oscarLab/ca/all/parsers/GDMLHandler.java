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
import ca.uhn.hl7v2.model.v23.segment.OBR;
import ca.uhn.hl7v2.model.v23.segment.OBX;
import java.sql.ResultSet;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.model.v23.datatype.*;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.parser.*;
import ca.uhn.hl7v2.validation.impl.NoValidation;

import java.text.*;


/**
 *
 * @author wrighd
 */
public class GDMLHandler implements MessageHandler {
    
    Logger logger = Logger.getLogger(GDMLHandler.class);
    static ORU_R01 msg = null;
    ArrayList headers = null;
    HashMap obrSegMap = null;
    ArrayList obrSegKeySet = null;
    
    /** Creates a new instance of CMLHandler */
    public GDMLHandler(){
    }
    
    public void init(String hl7Body) throws HL7Exception {
        Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());
        msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));
        
        ArrayList labs = getMatchingGDMLLabs(hl7Body);
        headers = new ArrayList();
        obrSegMap = new LinkedHashMap();
        obrSegKeySet = new ArrayList();
        
        logger.info("number of labs: "+labs.size());
        for (int i=0; i < labs.size(); i++){
            msg = (ORU_R01) p.parse(((String) labs.get(i)).replaceAll("\n", "\r\n"));
            int obrCount = msg.getRESPONSE().getORDER_OBSERVATIONReps();
            
            logger.info("lab("+i+") obrCount: "+obrCount);
            for (int j=0; j < obrCount; j++){
                
                // ADD OBR SEGMENTS AND THEIR OBX SEGMENTS TO THE OBRSEGMAP
                OBR obrSeg = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBR();
                ArrayList obxSegs = (ArrayList) obrSegMap.get(obrSeg);
                
                // if the obrsegment has not yet been created it will be now
                if (obxSegs == null)
                    obxSegs = new ArrayList();
                
                int obxCount = msg.getRESPONSE().getORDER_OBSERVATION(j).getOBSERVATIONReps();
                logger.info("lab("+i+") obr("+j+") obxCount: "+obxCount);
                for (int k=0; k < obxCount; k++){
                    logger.info("lab("+i+") adding obrseg("+j+") obxseg("+k+")");
                    obxSegs.add(msg.getRESPONSE().getORDER_OBSERVATION(j).getOBSERVATION(k).getOBX());
                }
                
                obrSegMap.put(obrSeg, obxSegs);
                obrSegKeySet.add(obrSeg);
                
                // ADD THE HEADER TO THE HEADERS ARRAYLIST
                String header = getString(obrSeg.getUniversalServiceIdentifier().getAlternateIdentifier().getValue());
                logger.info("lab("+i+") obr("+j+") header: "+header);
                if (!headers.contains(header)){
                    logger.info("lab("+i+") obr("+j+") adding header: "+header);
                    headers.add(header);
                }
                
            }
        }
    }
    
    private ArrayList getMatchingGDMLLabs(String hl7Body){
        Base64 base64 = new Base64();
        ArrayList ret = new ArrayList();
        int monthsBetween = 0;
        
        try{
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT m2.message, a.lab_no AS lab_no_A, b.lab_no AS lab_no_B,  a.obr_date, b.obr_date as labDate FROM hl7TextInfo a, hl7TextInfo b, hl7TextMessage m1, hl7TextMessage m2 WHERE m2.lab_id = a.lab_no AND a.accessionNum !='' AND a.accessionNum=b.accessionNum AND b.lab_no = m1.lab_id AND m1.message='"+(new String(base64.encode(hl7Body.getBytes("ASCII")), "ASCII"))+"' ORDER BY a.obr_date, a.lab_no";
            ResultSet rs = db.GetSQL(sql);
            
            while(rs.next()) {
                //Accession numbers may be recycled, accession
                //numbers for a lab should have lab dates within less than 4
                //months of eachother even this is a large timespan
                Date dateA = UtilDateUtilities.StringToDate(rs.getString("obr_date"), "yyyy-MM-dd hh:mm:ss");
                Date dateB = UtilDateUtilities.StringToDate(rs.getString("labDate"), "yyyy-MM-dd hh:mm:ss");
                if (dateA.before(dateB)){
                    monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                }else{
                    monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                }
                if (monthsBetween < 4){
                    logger.info("adding lab : "+rs.getString("lab_no_A"));
                    ret.add(new String(base64.decode(rs.getString("message").getBytes("ASCII")), "ASCII"));
                }
                
                // only return labs up to the one being initialized
                if (rs.getString("lab_no_A").equals(rs.getString("lab_no_B")))
                    break;
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("Exception in HL7 getMatchingLabs: ", e);
        }
        
        //if there have been no labs added to the database yet just return this lab
        if (ret.size() == 0)
            ret.add(hl7Body);
        return ret;
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
        return(obrSegMap.size());
    }
    
    public int getOBXCount(int i){
        return( ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).size() );
    }
    
    public String getOBRName(int i){
        return (((OBR) obrSegKeySet.get(i)).getUniversalServiceIdentifier().getText().getValue());
    }
    
    public String getTimeStamp(int i, int j){
        try{
            String ret = ((OBR) obrSegKeySet.get(i)).getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValue();
            if (ret == null)
                ret = ((OBR) obrSegKeySet.get(i)).getObservationDateTime().getTimeOfAnEvent().getValue();
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
            
            return(getString( ((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)).getAbnormalFlags(0).getValue() ));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getObservationHeader(int i, int j){
        try{
            return(getString(((OBR) obrSegKeySet.get(i)).getUniversalServiceIdentifier().getAlternateIdentifier().getValue()));
        }catch(Exception e){
            logger.error("Exception gettin header", e);
            return("");
        }
    }
    
    public String getOBXIdentifier(int i, int j){
        
        try{
            
            Terser t = new Terser(msg);
            Segment obxSeg = ((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j));
            String ident = getString(t.get(obxSeg, 3, 0, 1, 1 ));
            String subIdent = t.get(obxSeg, 3, 0, 1, 2);
            
            if (subIdent != null)
                ident = ident+"&"+subIdent;
            
            logger.info("returning obx identifier: "+ident);
            return(ident);
        }catch(Exception e){
            logger.error("error returning obx identifier", e);
            return("");
        }
    }
    
    public String getOBXName(int i, int j){
        String ret = "";
        try{
            // leave the name blank if the value type is 'FT' this is because it
            // is a comment, if the name is blank the obx segment will not be displayed
            OBX obxSeg = (OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j);
            if (!obxSeg.getValueType().getValue().equals("FT"))
                ret = getString(obxSeg.getObservationIdentifier().getText().getValue());
        }catch(Exception e){
            logger.error("Error returning OBX name", e);
        }
        
        return ret;
    }
    
    public String getOBXResult(int i, int j){
        try{
            
            Terser terser = new Terser(msg);
            return(getString(terser.get(((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)),5,0,1,1)));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXReferenceRange(int i, int j){
        String ret = "";
        try{
            Terser terser = new Terser(msg);
            
            OBX obxSeg = (OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j);
            // if there are no units specified for the obx they are stored in the
            // second componet of the reference range along with the reference range
            if (getOBXUnits(i, j).equals(""))
                ret = terser.get(obxSeg,7,0,2,1).replaceAll("\\\\\\.br\\\\", "").replaceAll("\\s", "&#160;");
            
            // may have to fall back to original reference range if the second
            // component is empty as well as the units
            if (ret == null || ret.equals("") || ret.equalsIgnoreCase("null"))
                ret = (getString(obxSeg.getReferencesRange().getValue()).replaceAll("\\\\\\.br\\\\", ""));
        }catch(Exception e){
            ret = "";
        }
        return ret;
    }
    
    public String getOBXUnits(int i, int j){
        try{
            
            return(getString(((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)).getUnits().getIdentifier().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public String getOBXResultStatus(int i, int j){
        try{
            
            // result status is stored in the wrong field.... i think
            return(getString(((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)).getNatureOfAbnormalTest().getValue()));
        }catch(Exception e){
            return("");
        }
    }
    
    public int getOBXFinalResultCount(){
        // not applicable to gdml messages
        return 0;
    }
    
    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList getHeaders(){
        return headers;
    }
    
    /**
     *  Methods to get information from observation notes
     */
    public int getOBRCommentCount(int i){
        int count = 0;
        
        for (int j=0; j < getOBXCount(i); j++){
            if (getString(((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)).getValueType().getValue()).equals("FT"))
                count++;
        }
        
        return count;
        
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
            logger.info("getOBRComment error", e);
            comment = "";
        }
        return comment;
    }
    
    /**
     *  Methods to get information from observation notes
     */
    public int getOBXCommentCount(int i, int j){
        int count = 0;
        try{
            
            Terser terser = new Terser(msg);
            String comment = "";
            OBX obxSeg = (OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j);
            while(comment != null){
                count++;
                comment = terser.get(obxSeg,7,count,1,1);
            }
            
            
        }catch(Exception e){
            return(0);
        }
        return count-1;
    }
    
    public String getOBXComment(int i, int j, int k){
        String comment = "";
        try{
            k++;
            
            Terser terser = new Terser(msg);
            
            comment = terser.get(((OBX) ((ArrayList) obrSegMap.get(obrSegKeySet.get(i))).get(j)),7,k,1,1).replaceAll("\\\\\\.br\\\\", "<br />");
            
        }catch(Exception e){
            logger.error("Cannot return comment", e);
        }
        return comment;
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
        // gdml won't send pending labs... they'll send only the final parts of the
        // labs
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
            //ignore error... it will occur when the zdr segment is not present
            //logger.error("Could not retrieve cc'd docs", e);
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
