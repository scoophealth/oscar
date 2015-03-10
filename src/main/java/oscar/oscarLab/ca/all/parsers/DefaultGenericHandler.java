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
 * DefaultGenericHandler.java
 *
 * Created on June 8, 2007, 2:17 PM
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

import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

/**
 *
 * @author wrighd
 */
public class DefaultGenericHandler implements MessageHandler {

    Logger logger = Logger.getLogger(DefaultGenericHandler.class);

    protected Message msg = null;
    protected Terser terser;
    protected ArrayList<ArrayList<Segment>> obrGroups = null;

    /** Creates a new instance of CMLHandler */
    public DefaultGenericHandler(){
    }

    public void init(String hl7Body) throws HL7Exception {

        Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());

        // force parsing as a generic message by changing the message structure
        hl7Body = hl7Body.replaceAll("R01", "");
        msg = p.parse(hl7Body.replaceAll( "\n", "\r\n"));

        terser = new Terser(msg);

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

    }

    public String getMsgType(){
        return(null);
    }

    public String getMsgDate(){

        try{
            String dateString = formatDateTime(getString(terser.get("/.MSH-7-1")));
            return(dateString);
        }catch(Exception e){
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

        if (obrGroups != null){
            return(obrGroups.size());
        }else{
            int i = 1;
            //String test;
            Segment test;
            try{

                test = terser.getSegment("/.OBR");
                while(test != null){
                    i++;
                    test = (Segment) terser.getFinder().getRoot().get("OBR"+i);
                }

            }catch(Exception e){
                //ignore exceptions
            }

            return(i-1);
        }
    }

    public String getOBRName(int i){

        String obrName;
        i++;
        try{
            if (i == 1){

                obrName = getString(terser.get("/.OBR-4-2"));
                if (obrName.equals(""))
                    obrName = getString(terser.get("/.OBR-4-1"));

            }else{
                Segment obrSeg = (Segment) terser.getFinder().getRoot().get("OBR"+i);
                obrName = getString(Terser.get(obrSeg,4,0,2,1));
                if (obrName.equals(""))
                    obrName = getString(Terser.get(obrSeg,4,0,1,1));

            }

            return(obrName);

        }catch(Exception e){
            return("");
        }
    }

    public String getTimeStamp(int i, int j){
        String timeStamp;
        i++;
        try{
            if (i == 1){
                timeStamp = formatDateTime(getString(terser.get("/.OBR-7-1")));
            }else{
                Segment obrSeg = (Segment) terser.getFinder().getRoot().get("OBR"+i);
                timeStamp = formatDateTime(getString(Terser.get(obrSeg,7,0,1,1)));
            }
            return(timeStamp);
        }catch(Exception e){
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
        return(getOBXField(i, j, 8, 0, 1));
    }

    public String getObservationHeader(int i, int j){
        //stored in different places for different messages
        return("");

    }

    public int getOBXCount(int i){
        ArrayList<Segment> obxSegs = obrGroups.get(i);
        return(obxSegs.size());
    }

    public String getOBXValueType(int i, int j){
        return(getOBXField(i, j, 2, 0, 1));
    }

    public String getOBXIdentifier(int i, int j){
        return(getOBXField(i, j, 3, 0, 1));
    }

    public String getOBXName(int i, int j){
        return(getOBXField(i, j, 3, 0, 2));
    }

    public String getOBXResult(int i, int j){
        return(getOBXField(i, j, 5, 0, 1));
    }

    public String getOBXReferenceRange(int i, int j){
        return(getOBXField(i, j, 7, 0, 1));
    }

    public String getOBXUnits(int i, int j){
        return(getOBXField(i, j, 6, 0, 1));
    }

    public String getOBXResultStatus(int i, int j){
        return(getOBXField(i, j, 11, 0, 1));
    }

    public int getOBXFinalResultCount(){
        int obrCount = getOBRCount();
        int obxCount;
        int count = 0;
        String status;
        for (int i=0; i < obrCount; i++){
            obxCount = getOBXCount(i);
            for (int j=0; j < obxCount; j++){
                status = getOBXResultStatus(i, j);
                if (status.startsWith("F") || status.startsWith("f"))
                    count++;
            }
        }
        return count;
    }

    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList<String> getHeaders(){
        //  stored in different places for different messages,
        //  a list must still be returned though
        ArrayList<String> headers = new ArrayList<String>();
        headers.add("");
        return(headers);
    }

    /**
     *  Methods to get information from observation notes
     */
    public int getOBRCommentCount(int i){

        try{
            String[] segments = terser.getFinder().getRoot().getNames();
            int k = getNTELocation(i, -1);
            int count = 0;

            // make sure to count all the nte segments in the group
            if (k < segments.length && segments[k].substring(0, 3).equals("NTE")){
                Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
                for (int l=0; l < nteSegs.length; l++){
                    count++;
                }
            }

            return(count);
        }catch(Exception e){
            logger.error("OBR Comment count error", e);

            return(0);
        }

    }

    public String getOBRComment(int i, int j){

        try{
            String[] segments = terser.getFinder().getRoot().getNames();
            int k = getNTELocation(i, -1);

            Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
            Segment nteSeg = (Segment) nteSegs[j];
            return(getString(Terser.get(nteSeg,3,0,1,1)));

        }catch(Exception e){
            logger.error("Could not retrieve OBX comments", e);

            return("");
        }
    }

    /**
     *  Methods to get information from observation notes
     */
    public int getOBXCommentCount(int i, int j){
        // jth obx of the ith obr

        try{

            String[] segments = terser.getFinder().getRoot().getNames();
            int k = getNTELocation(i, j);

            int count = 0;
            if (k < segments.length && segments[k].substring(0, 3).equals("NTE")){
                Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
                for (int l=0; l < nteSegs.length; l++){
                    count++;
                }
            }

            return(count);
        }catch(Exception e){
            logger.error("OBR Comment count error", e);

            return(0);
        }

    }

    public String getOBXComment(int i, int j, int nteNum){


        try{

            String[] segments = terser.getFinder().getRoot().getNames();
            int k = getNTELocation(i, j);


            Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
            Segment nteSeg = (Segment) nteSegs[nteNum];
            return(getString(Terser.get(nteSeg,3,0,1,1)));

        }catch(Exception e){
            logger.error("Could not retrieve OBX comments", e);

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
        try {
            return(getString(terser.get("/.PID-5-2")));
        } catch (HL7Exception ex) {
            return("");
        }
    }

    public String getLastName(){
        try {
            return(getString(terser.get("/.PID-5-1")));
        } catch (HL7Exception ex) {
            return("");
        }
    }

    public String getDOB(){
        try{
            return(formatDateTime(getString(terser.get("/.PID-7-1"))).substring(0, 10));
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
        try{
            return(getString(terser.get("/.PID-8-1")));
        }catch(Exception e){
            return("");
        }
    }

    public String getHealthNum(){
        String healthNum;

        try{

            //Try finding the health number in the external ID
            healthNum = getString(terser.get("/.PID-2-1"));
            if (healthNum.length() == 10)
                return(healthNum);

            //Try finding the health number in the alternate patient ID
            healthNum = getString(terser.get("/.PID-4-1"));
            if (healthNum.length() == 10)
                return(healthNum);

            //Try finding the health number in the internal ID
            healthNum = getString(terser.get("/.PID-3-1"));
            if (healthNum.length() == 10)
                return(healthNum);

            //Try finding the health number in the SSN field
            healthNum = getString(terser.get("/.PID-19-1"));
            if (healthNum.length() == 10)
                return(healthNum);
        }catch(Exception e){
            //ignore exceptions
        }

        return("");
    }

    public String getHomePhone(){
        try{
            return(getString(terser.get("/.PID-13-1")));
        }catch(Exception e){
            return("");
        }
    }

    public String getWorkPhone(){
        return("");
    }

    public String getPatientLocation(){
        try{
            return(getString(terser.get("/.MSH-4-1")));
        }catch(Exception e){
            return("");
        }
    }

    public String getServiceDate(){
        //usually a message type specific location
        return("");
    }

    public String getRequestDate(int i){
        //usually a message type specific location
        return("");
    }

    public String getOrderStatus(){
        //usually a message type specific location
        return("");
    }

    public String getClientRef(){
        try{
            return(getString(terser.get("/.OBR-16-1")));
        }catch(Exception e){
            return("");
        }
    }

    public String getAccessionNum(){
        //usually a message type specific location
        return("");
    }

    public String getDocName(){
        try{
            return(getFullDocName("/.OBR-16-"));
        }catch(Exception e){
            return("");
        }
    }

    public String getCCDocs(){

        try {
            int i=0;
            String docs = getFullDocName("/.OBR-28("+i+")-");
            i++;
            String nextDoc = getFullDocName("/.OBR-28("+i+")-");

            while(!nextDoc.equals("")){
                docs = docs+", "+nextDoc;
                i++;
                nextDoc = getFullDocName("/.OBR-28("+i+")-");
            }

            return(docs);
        } catch (Exception e) {
            return("");
        }
    }

    public ArrayList<String> getDocNums(){
        ArrayList<String> nums = new ArrayList<String>();
        String docNum;
        try{
            if ((docNum = terser.get("/.OBR-16-1")) != null)
                nums.add(docNum);

            int i=0;
            while((docNum = terser.get("/.OBR-28("+i+")-1")) != null){
                nums.add(docNum);
                i++;
            }

        }catch(Exception e){
        	//empty
        }

        return(nums);
    }


    public String audit(){
        return "";
    }

    protected String getOBXField(int i, int j, int field, int rep, int comp){
        ArrayList<Segment> obxSegs = obrGroups.get(i);

        try{
            Segment obxSeg = obxSegs.get(j);
            return (getString(Terser.get(obxSeg, field, rep, comp, 1 )));
        }catch(Exception e){
            return("");
        }
    }

    private int getNTELocation(int i, int j) throws HL7Exception{
        int k = 0;
        int obrCount = 0;
        int obxCount = 0;
        String[] segments = terser.getFinder().getRoot().getNames();

        while (k != segments.length && obrCount != i+1){
            if (segments[k].substring(0, 3).equals("OBR"))
                obrCount++;
            k++;
        }

        Structure[] obxSegs;
        while (k != segments.length && obxCount != j+1){


            if (segments[k].substring(0, 3).equals(("OBX"))){
                obxSegs = terser.getFinder().getRoot().getAll(segments[k]);
                obxCount = obxCount + obxSegs.length;
            }
            k++;
        }

        return(k);
    }


    private String getFullDocName(String docSeg) throws HL7Exception{
        String docName = "";
        String temp;

        // get name prefix ie/ DR.
        temp = terser.get(docSeg+"6");
        if(temp != null)
            docName = temp;

        // get the name
        temp = terser.get(docSeg+"3");
        if(temp != null){
            if (docName.equals("")){
                docName = temp;
            }else{
                docName = docName +" "+ temp;
            }
        }

        if(terser.get(docSeg+"4") != null)
            docName = docName +" "+ terser.get(docSeg+"4");
        if(terser.get(docSeg+"2") != null)
            docName = docName +" "+ terser.get(docSeg+"2");
        if(terser.get(docSeg+"5")!= null)
            docName = docName +" "+ terser.get(docSeg+"5");
        if(terser.get(docSeg+"7") != null)
            docName = docName +" "+ terser.get(docSeg+"7");

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

    public String getNteForPID() {
    	return "";
    }
}
