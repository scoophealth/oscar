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
 * MDSHandler.java
 *
 * Created on June 5, 2007, 9:25 AM
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
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DynamicHapiLoaderUtils;
import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;

/**
 *
 * @author wrighd
 */
public class MDSHandler implements MessageHandler {

    Object msg = null;
    Object terser;
    ArrayList obrGroups = null;
    HashMap<String,String> headerMaps = new HashMap<String,String>();
    Logger logger = Logger.getLogger(MDSHandler.class);

    /** Creates a new instance of CMLHandler */
    public MDSHandler(){
    }

    public void init(String hl7Body) throws HL7Exception {
    	hl7Body=StringUtils.trimToNull(hl7Body);
    	if (hl7Body==null)
    	{
    		logger.error("Some one called MDSHandler.init with null data");
    		return;
    	}

    	try {
    	
	        msg=DynamicHapiLoaderUtils.parseMdsMsg(hl7Body.replace( "\n", "\r\n"));
	        terser = DynamicHapiLoaderUtils.getMdsTerser(msg);

	        int obrCount = getOBRCount();
	        int obrNum;
	        boolean obrFlag;
	        String segmentName;
	        String[] segments = DynamicHapiLoaderUtils.terser_getFinder_getCurrentGroup_getNames(terser);
	        obrGroups = new ArrayList();
	        /*
	         *  Fill the OBX array list for use by future methods
	         */
	        for (int i=0; i < obrCount; i++){
	            ArrayList<String> obxSegs = new ArrayList<String>();
	            int count = 0;

	            if (i == 0){
	                try{
	                    while(DynamicHapiLoaderUtils.terserGet(terser, "/.OBX("+count+")-1-1") != null){
	                        obxSegs.add("/.OBX("+count+")");
	                        count++;
	                    }
	                }catch(Exception e){
	                    //ignore exception
	                }
	            }

	            obrNum = i+1;
	            obrFlag = false;
	            for (int k=0; k < segments.length; k++){

	                segmentName = segments[k].substring(0, 3);

	                if (obrFlag && segmentName.equals("OBX")){
	                    if (!segments[k].equals("OBX")) // would have already been added to first array
	                        obxSegs.add("/."+segments[k]);
	                }else if (obrFlag && segmentName.equals("OBR")){
	                    break;
	                }else if ( segments[k].equals("OBR"+obrNum) || ( obrNum==1 && segments[k].equals("OBR"))){
	                    obrFlag = true;
	                }

	            }
	            obrGroups.add(obxSegs);
	        }
	        /*
	        for(int i=0; i<obrGroups.size(); i++){
	            ArrayList obxSegs = (ArrayList) obrGroups.get(i);
	            for (int j=0; j < obxSegs.size(); j++){
	                String obx = (String) obxSegs.get(j);
	                //logger.info("OBRSEG("+i+") OBXSEG("+j+"): "+obx);
	            }
	        }
	         */
        } catch (Exception e) {
        	throw new HL7Exception(e);
        }
    }

    public String getMsgType(){
        return("MDS");
    }

    public String getMsgPriority(){

        int i=1;
        String priority = "R";
        try{
            priority = DynamicHapiLoaderUtils.terserGet(terser, "/.OBR-27-1");
            while(priority != null){
                i++;
                priority = DynamicHapiLoaderUtils.terserGet(terser, "/.OBR"+i+"-27-1");
                if (!priority.equalsIgnoreCase("R")){
                    break;
                }
            }
        }catch(Exception e){
            // ignore exceptions
        }

        if (priority != null && priority.startsWith("AL"))
            priority = "L";

        return priority;
    }

    /**
     *  Methods to get information about the Observation Request
     */
    public int getOBRCount(){
        if (obrGroups != null){
            return(obrGroups.size());
        }else{
            int i = 1;
            String test;
            try{
                test = DynamicHapiLoaderUtils.terserGet(terser, "/.OBR-2-1");
                while(test != null){
                    i++;
                    test = DynamicHapiLoaderUtils.terserGet(terser, "/.OBR"+i+"-2-1");
                }
            }catch(Exception e){
                // ignore exceptions
            }
            return(i-1);
        }
    }

    public String getOBRName(int i){
        /*String obrCode;
        i++;
        try{
            if (i == 1){
                obrCode = getString(terser.get("/.OBR-4-1"));
            }else{
                obrCode = getString(terser.get("/.OBR"+i+"-4-1"));
            }
            return("TEST "+i+": "+obrCode);
        }catch(Exception e){
            return("TEST "+i+":");
        }*/

        // the MDS local table is not available so the corresponding lab names are unknown
        // NO NAMES FOR MDS OBR SEGMENTS
        return("");
    }

    public String getTimeStamp(int i, int j){
        String timeStamp;
        i++;
        try{
            if (i == 1){
                timeStamp = formatDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser, "/.OBR-7-1")));
            }else{
                timeStamp = formatDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser, "/.OBR"+i+"-7-1")));
            }
            return(timeStamp);
        }catch(Exception e){
            return getMsgDate();
        }
    }

    public boolean isOBXAbnormal(int i, int j){
        if (getOBXAbnormalFlag(i, j).equals(""))
            return(false);
        else
            return(true);
    }

    public String getOBXAbnormalFlag(int i, int j){
        return(getOBXField("8-1", i, j));
    }

    public String getObservationHeader(int i, int j){

        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
        //logger.info("OBRGROUP("+i+") OBXSEG("+j+"): '"+((String) obxSegs.get(j)));
        return(matchOBXToHeader( (String) obxSegs.get(j)));

    }

    public int getOBXCount(int i){

        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
        return(obxSegs.size());
    }

    public String getOBXIdentifier(int i, int j){
        return(getOBXField("3-1", i, j));
    }


    public String getOBXValueType(int i, int j){
        return(getOBXField("2-1",i,j));
    }

    public String getOBXName(int i, int j){
        return(getOBXField("3-2", i, j));
    }

    public String getOBXResult(int i, int j){
        return(getOBXField("5-1", i, j));
    }

    public String getOBXReferenceRange(int i, int j){
        return(getOBXField("7-1", i, j));
    }

    public String getOBXUnits(int i, int j){
        return(getOBXField("6-1", i, j));
    }

    public String getOBXResultStatus(int i, int j){
        String resultStatus = getOBXField("11-1", i, j);

        switch(resultStatus.charAt(0)){
            case 'C': resultStatus = "Edited"; break;
            case 'D': resultStatus = "DNS"; break;
            case 'F': resultStatus = "Final"; break;
            case 'f': resultStatus = "Final"; break;
            case 'I': resultStatus = "Pending"; break;
            case 'P': resultStatus = "Preliminary"; break;
            case 'p': resultStatus = "Preliminary"; break;
            case 'R': resultStatus = "Entered - Not Verified"; break;
            case 'r': resultStatus = "Entered - Not Verified"; break;
            case 'S': resultStatus = "Partial"; break;
            case 's': resultStatus = "Partial"; break;
            case 'X': resultStatus = "DNS"; break; // do not show
            case 'U': resultStatus = "Changed to Final"; break;
            case 'u': resultStatus = "Changed to Final"; break;
            case 'W': resultStatus = "DNS"; break;
        }

        return (resultStatus);
    }

    public int getOBXFinalResultCount(){
        int count = 0;
        try {
            String accessionNum = getString(DynamicHapiLoaderUtils.terserGet(terser, "/.MSH-10-1"));
            count = Integer.parseInt(accessionNum.substring(accessionNum.lastIndexOf("-")+1));
        } catch (Exception e) {
            logger.error("could not retrieve message ordering number", e);
        }
        return count;
    }

    /**
     *  Retrieve the possible segment headers from the OBX fields
     */
    public ArrayList<String> getHeaders(){
        ArrayList<String> headers = new ArrayList<String>();
        String currentHeader = "";
        String nextHeader;
        String headerNum;
        String nextHeaderNum;
        int i = 0;

        try{

            nextHeaderNum = DynamicHapiLoaderUtils.terserGet(terser, "/.ZRG(0)-2-1");
            nextHeader = getString(DynamicHapiLoaderUtils.terserGet(terser, "/.ZRG(0)-7-1"));
            headerNum = nextHeaderNum;
            while(nextHeaderNum != null){

                if (headerNum.equals(nextHeaderNum)){
                    if (currentHeader.equals(""))
                        currentHeader = nextHeader;
                    else
                        currentHeader = currentHeader+"<br />"+nextHeader;
                }else{

                    if (currentHeader.equals(""))
                        currentHeader = getString(DynamicHapiLoaderUtils.terserGet(terser, "/.ZRG("+(i-1)+")-5-1"));

                    headerMaps.put(headerNum, currentHeader);
                    headers.add(currentHeader);
                    currentHeader = nextHeader;
                    headerNum = nextHeaderNum;
                }

                i++;
                nextHeaderNum = DynamicHapiLoaderUtils.terserGet(terser, "/.ZRG("+i+")-2-1");
                nextHeader = getString(DynamicHapiLoaderUtils.terserGet(terser, "/.ZRG("+i+")-7-1"));

            }
            if (currentHeader.equals(""))
                currentHeader = getString(DynamicHapiLoaderUtils.terserGet(terser, "/.ZRG("+(i-1)+")-5-1"));

            headerMaps.put(headerNum, currentHeader);
            headers.add(currentHeader);

        }catch(Exception e){

        }

        return(headers);
    }

    /**
     *  Methods to get information from observation notes
     */
    public int getOBRCommentCount(int i){
        // not needed comments will only follow OBX segments
        return(0);
    }

    public String getOBRComment(int i, int j){
        // not needed comments will only follow OBX segments
        return("");
    }

    /**
     *  Methods to get information from observation notes
     */
    public int getOBXCommentCount(int i, int j){
        try {
	        // jth obx of the ith obr

	        String[] segments = DynamicHapiLoaderUtils.terser_getFinder_getCurrentGroup_getNames(terser);
	        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
	        String obxSeg = ((String) obxSegs.get(j)).substring(2);

	        // if the obxSeg is part of the first obx array
	        if (obxSeg.charAt(3) == '('){

	            if(j+1 == obxSegs.size()){
	                obxSeg = obxSeg.substring(0, 3);
	            }else{
	                String nextObxSeg = ((String) obxSegs.get(j+1)).substring(2);
	                if (nextObxSeg.charAt(3) == '('){
	                    return(0);
	                }else{
	                    obxSeg = obxSeg.substring(0, 3);
	                }
	            }

	        }

	        int k = 0;
	        while(!obxSeg.equals(segments[k])){
	            k++;
	        }

	        int count = 0;
	        k++;
	        while (k < segments.length && segments[k].substring(0, 3).equals("NTE")){
	            k++;
	            count++;
	        }

	        return(count);
        } catch (Exception e) {
	        logger.error("Unexpected error", e);
	        return(-1);
        }
    }

    public String getOBXComment(int i, int j, int k){

    	try
    	{
	        String[] segments = DynamicHapiLoaderUtils.terser_getFinder_getCurrentGroup_getNames(terser);
	        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
	        String obxSeg = ((String) obxSegs.get(j)).substring(2);

	        // if the obxSeg is part of the first obx array
	        if (obxSeg.charAt(3) == '('){
	            obxSeg = obxSeg.substring(0, 3);
	        }

	        int l = 0;
	        while(!obxSeg.equals(segments[l])){
	            l++;
	        }

	        l = l+k+1; // at this point, l is pointing at the NTE segment

	        try{
	            Object[] nteSegs = DynamicHapiLoaderUtils.terser_getFinder_getRoot_getAll(terser,segments[l]);
	            if (getString(DynamicHapiLoaderUtils.terserGet(terser,"/."+segments[l]+"-2-1")).equals("MC")){
	              String comment = "";
	              for (int x=0; x < nteSegs.length; x++){

	                int m = 0;
	                Object nteSeg = nteSegs[x];
	                String commentCode = getString(DynamicHapiLoaderUtils.terserGet(terser,nteSeg,3,0,2,1));
	                String matchCommentCode = DynamicHapiLoaderUtils.terserGet(terser,"/.ZMC("+m+")-2-1");
	                while(matchCommentCode != null){

	                    if (matchCommentCode.equals(commentCode)){
	                        if (comment.equals(""))
	                            comment = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.ZMC("+m+")-6-1"));
	                        else
	                            comment = comment+"<br />"+getString(DynamicHapiLoaderUtils.terserGet(terser,"/.ZMC("+m+")-6-1"));
	                    }

	                    m++;
	                    matchCommentCode = DynamicHapiLoaderUtils.terserGet(terser,"/.ZMC("+m+")-2-1");
	                }
	              }
	              return(comment);

	            }else{
	                //return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/."+segments[l]+"-3-2")));
	            	String comment = null;
					for (int x=0; x < nteSegs.length; x++){
						String commentCode = getString(DynamicHapiLoaderUtils.terserGet(terser,nteSegs[x],3,0,2,1));
						comment = (comment==null) ? commentCode : comment+"<br/>"+commentCode;
					}
					return comment;
	            }
	        }catch(Exception e){
	            logger.error("Could not retrieve OBX comments", e);

	            return("");
	        }
    	} catch (Exception e) {
	        logger.error("Unexpected error", e);
	        return(null);
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
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-5-2")));
        } catch (Exception ex) {
            return("");
        }
    }

    public String getLastName(){
        try {
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-5-1")));
        } catch (Exception ex) {
            return("");
        }
    }

    public String getMiddleName(){
        try {
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-5-3")));
        } catch (Exception ex) {
            return("");
        }
    }

    public String getUnescapedName(){
        return getLastName()+"^"+getFirstName()+"^"+getMiddleName();
    }

    public String getDOB(){
        try{
            return(formatDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-7-1"))).substring(0, 10));
        }catch(Exception e){
            logger.error("Error retrieving date of birth", e);
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
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-8-1")));
        }catch(Exception e){
            return("");
        }
    }

    public String getHealthNum(){
        try{
            String healthNum = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-19-1"));
            int end = healthNum.indexOf(" ");
            if (end > 0)
                return(healthNum.substring(1, end));
            else
                return(healthNum.substring(1));
        }catch(Exception e){
            return("");
        }
    }

    public String getHealthNumVersion(){
        try{
            String healthNum = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-19-1"));
            return(healthNum.substring(healthNum.indexOf(" ")+1));
        }catch(Exception e){
            return("");
        }
    }

    public String getHomePhone(){
        try{
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.PID-13-1")));
        }catch(Exception e){
            return("");
        }
    }

    public String getWorkPhone(){
        return("N/A");
    }

    public String getPatientLocation(){
        try{
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-3-1")));
            //return(getString(terser.get("/.PV1-3-1-1")));
        }catch(Exception e){
            return("");
        }
    }

    //should be from the 1st OBR always.
    public String getServiceDate(){
        try{
        	Date mshDate = UtilDateUtilities.StringToDate(getRequestDate(0), "yyyy-MM-dd hh:mm:ss");
            return( UtilDateUtilities.DateToString(mshDate, "dd-MMM-yyyy") );
        }catch(Exception e){
            return("");
        }
    }

    public String getRequestDate(int i){
        String requestDate;
        i++;
        try{
            if (i == 1){
            	requestDate = formatDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser, "/.OBR-14-1")));
            }else{
            	requestDate = formatDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser, "/.OBR"+i+"-14-1")));
            }
            return(requestDate);
        }catch(Exception e){
            return getMsgDate();
        }
    }

    public String getOrderStatus(){

        String ret = "F";
        try{
            if (getString(DynamicHapiLoaderUtils.terserGet(terser,"/.ZFR-3-1")).equals("0"))
                return("P");

            String status = "";
            int i=0;

            // If one of the zfr segments says partial, the lab should be marked
            // as a partial lab
            while ((status = DynamicHapiLoaderUtils.terserGet(terser,"/.ZFR("+i+")-3-1")) != null){
                if (status.equals("0")){
                    ret = "P";
                    break;
                }
                i++;
            }
        }catch(Exception e){
            logger.error("Exception retrieving order status", e);
        }
        return ret;
    }

    public String getClientRef(){
        try{
            String clientNum = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-10-1"));
            int firstDash = clientNum.indexOf("-");
            return(clientNum.substring(0, firstDash));
        }catch(Exception e){
            return("");
        }
    }

    public String getDocNum(){
        try{
            return(DynamicHapiLoaderUtils.terserGet(terser,"/.PV1-8-1").replace("-", ""));
        }catch(Exception e){
            return("");
        }
    }

    public String getAccessionNum(){
        try{
            String accessionNum = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-10-1"));
            int firstDash = accessionNum.indexOf("-");
            int secondDash = accessionNum.indexOf("-", firstDash+1);
            return(accessionNum.substring(firstDash+1, secondDash));
        }catch(Exception e){
            return("");
        }
    }

    public String getDocName(){
        try{
            return(getFullDocName("/.PV1-8-"));
        }catch(Exception e){
            return("");
        }
    }

    public String getCCDocs(){
        String docs = "";
        try {
        	String tmp = "";
        	int x=0;
        	do {
        		tmp = getFullDocName("/.PV1-9("+x+")-");
        		if(tmp.length()>0) {
        			if(docs.length()>0) {
        				docs = docs+", " + tmp;
        			} else {
        				docs = tmp;
        			}
        		}
        		x++;
        	}while(!tmp.equals(""));
            if (docs.equals(""))
                docs = getFullDocName("/.PV1-17-");
            else
                docs = docs+", "+getFullDocName("/.PV1-17-");
            return(docs);
        } catch (Exception e) {
            return("");
        }
    }

    public ArrayList<String> getDocNums(){
        ArrayList<String> nums = new ArrayList<String>();
        String docNum;
        try{
            if ((docNum = DynamicHapiLoaderUtils.terserGet(terser,"/.PV1-8-1")) != null){
                nums.add(docNum.replace("-", ""));
            }
            if ((docNum = DynamicHapiLoaderUtils.terserGet(terser,"/.PV1-9-1")) != null){
                nums.add(docNum.replace("-", ""));
            }
            if ((docNum = DynamicHapiLoaderUtils.terserGet(terser,"/.PV1-17-1")) != null){
                nums.add(docNum.replace("-", ""));
            }
        }catch(Exception e){
            logger.error("Could not retrieve doctor numbers", e);
        }
        return(nums);
    }

    /**
     *  Methods specific to the MDSHandler
     */
    public String getFormType(){
        String retVal = "";

        try{
            String typeField = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-10-1"));
            char typeNum = typeField.charAt(typeField.indexOf('-', typeField.indexOf('-')+1)+1);

            switch(typeNum){
                case '1': retVal = "S"; break;
                case '4': retVal = "X"; break;
                case '5': retVal = "C"; break;
                case '6': retVal = "H"; break;
                case '7': retVal = "A"; break;
                case '9': retVal = "M"; break;
            }

        }catch(Exception e){
            logger.error("Could not retrieve form type", e);

        }

        return(retVal);
    }

    public String getMsgDate(){

        try{
            String dateString = formatDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-7-1")));
            return(dateString);
        }catch(Exception e){
            return("");
        }

    }

    public Date getMsgDateAsDate(){
        Date date = null;
        try{
            date = getDateTime(getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-7-1")));
        }catch(Exception e){
            //Not sure what to do here
            MiscUtils.getLogger().error("Error", e);
        }
        return date;
    }

    private String getOBXField(String field, int i, int j){
        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
        String obxSeg = (String) obxSegs.get(j);

        try{
            return(getString(DynamicHapiLoaderUtils.terserGet(terser,obxSeg+"-"+field)));
        }catch(Exception e){
            return("");
        }
    }

    private String matchOBXToHeader(String obxSeg){

        int i=0;
        String headerNum = "null";
        try{
            String zmnNum = DynamicHapiLoaderUtils.terserGet(terser,"/.ZMN(0)-8-1");

            String obxNum = DynamicHapiLoaderUtils.terserGet(terser,obxSeg+"-4-1");
            // we only need the last section of the headerNum
            obxNum = obxNum.substring(obxNum.indexOf("-", obxNum.indexOf("-")+1)+1);

            while(zmnNum != null){
                if (zmnNum.equals(obxNum)){
                    headerNum = DynamicHapiLoaderUtils.terserGet(terser,"/.ZMN("+i+")-10-1");
                    break;
                }
                i++;
                zmnNum = DynamicHapiLoaderUtils.terserGet(terser,"/.ZMN("+i+")-8-1");
            }

        }catch(Exception e){
            logger.error("error retrieving header", e);

        }


        return(headerMaps.get(headerNum) );

    }

    private String getFullDocName(String docSeg){
        //docSeg = "/.PV1-8-"
        String docName = "";
        String temp;

        try {
	        // get name prefix ie/ DR.
	        temp = DynamicHapiLoaderUtils.terserGet(terser,docSeg+"6");
	        if(temp != null)
	            docName = temp;

	        // get the name
	        temp = DynamicHapiLoaderUtils.terserGet(terser,docSeg+"2");
	        if(temp != null){
	            if (docName.equals("")){
	                docName = temp;
	            }else{
	                docName = docName +" "+ temp;
	            }
	        }
        } catch (Exception e) {
	        logger.error("Unexpected Error.", e);
        }

        return (docName);
    }

    private Date getDateTime(String plain){
        String dateFormat = "yyyyMMddHHmmss";
        dateFormat = dateFormat.substring(0, plain.length());
        Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
        return date;
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
            retrieve.replaceAll("^", " ");
            return(retrieve.trim());
        }else{
            return("");
        }
    }

    String delimiter = "  ";
    char bl = ' ';


    public  String getAuditLine(String procDate, String procTime, String logId,String formStatus, String formType, String accession, String hcNum, String hcVerCode, String patientName,String orderingClient,String  messageDate,String messageTime){
        logger.info("Getting Audit Line");



        return getPaddedString(procDate,11,bl)+delimiter+
               getPaddedString(procTime,8,bl)+delimiter+
               getPaddedString(logId,7,bl)+delimiter+
               getPaddedString(formStatus,1,bl)+delimiter+
               getPaddedString(formType,1,bl)+delimiter+
               getPaddedString(accession,9,bl)+delimiter+
               getPaddedString(hcNum,10,bl)+delimiter+
               getPaddedString(hcVerCode,2,bl)+delimiter+
               getPaddedString(patientName,61,bl)+delimiter+
               getPaddedString(orderingClient,8,bl)+delimiter+
               getPaddedString(messageDate,11,bl)+delimiter+
               getPaddedString(messageTime,8,bl)+"\n\r";

	}

	String getPaddedString(String originalString, int length, char paddingChar){
           StringBuilder str = new StringBuilder(length);
           str.append(originalString);

           for (int i = str.length(); i < length; i++){
             str.append(paddingChar);
           }

           return str.substring(0,length);
        }

    public String audit(){
        String retVal = "";

        java.util.Date date = new java.util.Date();
        SimpleDateFormat dayFormatter =  new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat timeFormatter =  new SimpleDateFormat("HH:mm:ss");

        String procDate = dayFormatter.format(date);
        String procTime = timeFormatter.format(date);

        String messageDate = "";
        String messageTime = "";
        try{
            messageDate = dayFormatter.format(getMsgDateAsDate());
            messageTime = timeFormatter.format(getMsgDateAsDate());
        }catch(Exception e){MiscUtils.getLogger().error("Error", e);}

        retVal = getAuditLine(procDate, procTime, "REC",getOrderStatus(), getFormType(), getAccessionNum(), getHealthNum(), getHealthNumVersion(), getUnescapedName(),getClientRef(),messageDate, messageTime);

        return retVal;
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
