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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.integration.hl7.model.PatientId;
import org.oscarehr.util.MiscUtils;

import oscar.Misc;
import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
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
public class HHSEmrDownloadHandler extends DefaultGenericHandler implements MessageHandler {

    Logger logger = Logger.getLogger(HHSEmrDownloadHandler.class);
    ArrayList<String> headerList = null;
 //   Message msg = null;
 //   Terser terser;
 //   ArrayList obrGroups = null;

    /** Creates a new instance of CMLHandler */
    public HHSEmrDownloadHandler(){
        super();


    }

    @Override
     public String getMsgType(){
        return("HHSEMR");
    }

    @Override
    public ArrayList<String> getHeaders(){
       headerList = new ArrayList<String>();

       String[] noms = msg.getNames();
       for(String s:noms){
           logger.debug(s);
       }


       for (int i = 0; i < getOBRCount();i++){
            headerList.add(getOBRName(i));
            logger.debug("ADDING to header "+getOBRName(i));
       }
       logger.debug("AFTER");
       try{

       logger.debug("Current Group "+terser.getFinder().getCurrentGroup().getName());

       Structure[] strs =   terser.getFinder().getCurrentChildReps(); //((Group) terser.getFinder().getCurrentGroup().get("RESPONSE")).getAll("ORDER_OBSERVATION");
        for(Structure str:strs){
            logger.debug(str.getClass().getName()+"  "+str.getName());
            Group obrseg = (Group) str;

            Structure[] structChilds = obrseg.getAll("OBX");
            for(Structure ss:structChilds){
                logger.debug(ss.getClass().getName()+" "+str.getName());
            }
        }
       }catch(Exception e){
           logger.debug("debug", e);

       }
       return headerList;
    }

     public String getObservationHeader(int i, int j){
         return headerList.get(i);
     }

     private void findOBX(Group group,ArrayList<Structure> list) throws Exception{
        String[] noms = group.getNames();
        for (String nom:noms){
            Structure[] obxS = group.getAll(nom);
            for(Structure ss : obxS){
                if (ss instanceof Segment){
                    if ("OBX".equals(ss.getName())){
                        list.add(ss);
                    }
                }else{
                    findOBX((Group) ss,list);
                }
            }
        }
    }


    public void init(String hl7Body) throws HL7Exception {

        Parser p = new PipeParser();
        p.setValidationContext(new NoValidation());

        // force parsing as a generic message by changing the message structure

        msg = p.parse(hl7Body.replaceAll( "\n", "\r\n"));

        terser = new Terser(msg);

        int obrCount = getOBRCount();

        obrGroups = new ArrayList();

        /*
         *  Fill the OBX array list for use by future methods
         */

        logger.debug("Current Group "+terser.getFinder().getCurrentGroup().getName());



        try{
        Structure[] strs = ((Group) terser.getFinder().getRoot().get("RESPONSE")).getAll("ORDER_OBSERVATION");
        for (Structure str:strs){
            logger.debug("NEW OBX");
            ArrayList obrGroup = new ArrayList();
            findOBX((Group) str,obrGroup);
            obrGroups.add(obrGroup);
        }
        }catch(Exception e){
            logger.error("Error in adding OBX elements to obrGroup ", e);
        }


    //Group root = terser.getFinder().getRoot();
    //groupDisplay(root,"");
    logger.debug("END GROUP PRINT");



    /*
        logger.debug("obr Count "+obrCount);
        for (int i=0; i < obrCount; i++){
            ArrayList obxSegs = new ArrayList();
            count = 0;

            obrNum = i+1;
            obrFlag = false;
            logger.debug("segment len "+segments.length);

            for (int k=0; k < segments.length; k++){

                segmentName = segments[k].substring(0, 3);
                logger.debug("Segment Name "+segmentName+"  OBR flag "+obrFlag);
                if (obrFlag && segmentName.equals("OBX")){

                    // make sure to count all of the obx segments in the group
                    Structure[] segs = terser.getFinder().getRoot().getAll(segments[k]);
                    logger.debug("segment length "+segs.length);
                    for (int l=0; l < segs.length; l++){
                        Segment obxSeg = (Segment) segs[l];
                        obxSegs.add(obxSeg);
                    }

                }else if (obrFlag && segmentName.equals("OBR")){
                    logger.debug("breaking");
                    break;
                }else if ( segments[k].equals("OBR"+obrNum) || ( obrNum==1 && segments[k].equals("OBR"))){
                    logger.debug("setting obr Flag true obrNum "+obrNum+" segment  "+segments[k]);
                    obrFlag = true;
                }

            }
            logger.debug("obxSegs size "+obxSegs.size() );
            obrGroups.add(obxSegs);
        }
        */

    }
//
//    public String getMsgType(){
//        return(null);
//    }
//
//    public String getMsgDate(){
//
//        try{
//            String dateString = formatDateTime(getString(terser.get("/.MSH-7-1")));
//            return(dateString);
//        }catch(Exception e){
//            return("");
//        }
//
//    }
//
//    public String getMsgPriority(){
//        return("");
//    }
//
//    /**
//     *  Methods to get information about the Observation Request
//     */
//    public int getOBRCount(){
//
//        if (obrGroups != null){
//            return(obrGroups.size());
//        }else{
//            int i = 1;
//            //String test;
//            Segment test;
//            try{
//
//                test = terser.getSegment("/.OBR");
//                while(test != null){
//                    i++;
//                    test = (Segment) terser.getFinder().getRoot().get("OBR"+i);
//                }
//
//            }catch(Exception e){
//                //ignore exceptions
//            }
//
//            return(i-1);
//        }
//    }
//
      public String getOBRName(int i){
          String addToEnd = "";
          try{
              addToEnd = getString(terser.get("/.OBR-19-2"));
          }catch(Exception e){
        	  //empty
          }

          return super.getOBRName(i) + addToEnd;
      }
//
//        String obrName;
//        i++;
//        try{
//            if (i == 1){
//
//                obrName = getString(terser.get("/.OBR-4-2"));
//                if (obrName.equals(""))
//                    obrName = getString(terser.get("/.OBR-4-1"));
//
//            }else{
//                Segment obrSeg = (Segment) terser.getFinder().getRoot().get("OBR"+i);
//                obrName = getString(terser.get(obrSeg,4,0,2,1));
//                if (obrName.equals(""))
//                    obrName = getString(terser.get(obrSeg,4,0,1,1));
//
//            }
//
//            return(obrName);
//
//        }catch(Exception e){
//            return("");
//        }
//    }
//
//    public String getTimeStamp(int i, int j){
//        String timeStamp;
//        i++;
//        try{
//            if (i == 1){
//                timeStamp = formatDateTime(getString(terser.get("/.OBR-7-1")));
//            }else{
//                Segment obrSeg = (Segment) terser.getFinder().getRoot().get("OBR"+i);
//                timeStamp = formatDateTime(getString(terser.get(obrSeg,7,0,1,1)));
//            }
//            return(timeStamp);
//        }catch(Exception e){
//            return("");
//        }
//    }
//
//    public boolean isOBXAbnormal(int i, int j){
//        String abnormalFlag = getOBXAbnormalFlag(i, j);
//        if (abnormalFlag.equals("") || abnormalFlag.equals("N"))
//            return(false);
//        else
//            return(true);
//    }
//
//    public String getOBXAbnormalFlag(int i, int j){
//        return(getOBXField(i, j, 8, 0, 1));
//    }
//
//    public String getObservationHeader(int i, int j){
//        //stored in different places for different messages
//        return("");
//
//    }
//
//    public int getOBXCount(int i){
//        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
//        return(obxSegs.size());
//    }
//
//    public String getOBXIdentifier(int i, int j){
//        return(getOBXField(i, j, 3, 0, 1));
//    }
//
    public String getOBXName(int i, int j){
    	if (getOBXField(i,j,2,0,1).equals("FT")) {
    		return "";
    	} else
        return(getOBXField(i, j, 3, 0, 2));
    }
//
//    public String getOBXResult(int i, int j){
//        return(getOBXField(i, j, 5, 0, 1));
//    }
//
    public String getOBXReferenceRange(int i, int j){
        String tmp = getOBXField(i, j, 7, 0, 3);
        if(tmp.equals("")) {
        	//try the first component
        	tmp = getOBXField(i, j, 7, 0, 1);
        }
        return tmp;
    }
//
//    public String getOBXUnits(int i, int j){
//        return(getOBXField(i, j, 6, 0, 1));
//    }
//
//    public String getOBXResultStatus(int i, int j){
//        return(getOBXField(i, j, 11, 0, 1));
//    }
//
//    public int getOBXFinalResultCount(){
//        int obrCount = getOBRCount();
//        int obxCount;
//        int count = 0;
//        String status;
//        for (int i=0; i < obrCount; i++){
//            obxCount = getOBXCount(i);
//            for (int j=0; j < obxCount; j++){
//                status = getOBXResultStatus(i, j);
//                if (status.startsWith("F") || status.startsWith("f"))
//                    count++;
//            }
//        }
//        return count;
//    }
//
//    /**
//     *  Retrieve the possible segment headers from the OBX fields
//     */
//    public ArrayList getHeaders(){
//        //  stored in different places for different messages,
//        //  a list must still be returned though
//        ArrayList headers = new ArrayList();
//        headers.add("");
//        return(headers);
//    }
//
//    /**
//     *  Methods to get information from observation notes
//     */
//    public int getOBRCommentCount(int i){
//
//        try{
//            String[] segments = terser.getFinder().getRoot().getNames();
//            int k = getNTELocation(i, -1);
//            int count = 0;
//
//            // make sure to count all the nte segments in the group
//            if (k < segments.length && segments[k].substring(0, 3).equals("NTE")){
//                Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
//                for (int l=0; l < nteSegs.length; l++){
//                    count++;
//                }
//            }
//
//            return(count);
//        }catch(Exception e){
//            logger.error("OBR Comment count error", e);
//
//            return(0);
//        }
//
//    }
//
//    public String getOBRComment(int i, int j){
//
//        try{
//            String[] segments = terser.getFinder().getRoot().getNames();
//            int k = getNTELocation(i, -1);
//
//            Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
//            Segment nteSeg = (Segment) nteSegs[j];
//            return(getString(terser.get(nteSeg,3,0,1,1)));
//
//        }catch(Exception e){
//            logger.error("Could not retrieve OBX comments", e);
//
//            return("");
//        }
//    }
//
//    /**
//     *  Methods to get information from observation notes
//     */
//    public int getOBXCommentCount(int i, int j){
//        // jth obx of the ith obr
//
//        try{
//
//            String[] segments = terser.getFinder().getRoot().getNames();
//            int k = getNTELocation(i, j);
//
//            int count = 0;
//            if (k < segments.length && segments[k].substring(0, 3).equals("NTE")){
//                Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
//                for (int l=0; l < nteSegs.length; l++){
//                    count++;
//                }
//            }
//
//            return(count);
//        }catch(Exception e){
//            logger.error("OBR Comment count error", e);
//
//            return(0);
//        }
//
//    }
//
//    public String getOBXComment(int i, int j, int nteNum){
//
//
//        try{
//
//            String[] segments = terser.getFinder().getRoot().getNames();
//            int k = getNTELocation(i, j);
//
//            int count = 0;
//
//            Structure[] nteSegs = terser.getFinder().getRoot().getAll(segments[k]);
//            Segment nteSeg = (Segment) nteSegs[nteNum];
//            return(getString(terser.get(nteSeg,3,0,1,1)));
//
//        }catch(Exception e){
//            logger.error("Could not retrieve OBX comments", e);
//
//            return("");
//        }
//    }
//
//
//    /**
//     *  Methods to get information about the patient
//     */
//    public String getPatientName(){
//        return(getFirstName()+" "+getLastName());
//    }
//
//    public String getFirstName(){
//        try {
//            return(getString(terser.get("/.PID-5-2")));
//        } catch (HL7Exception ex) {
//            return("");
//        }
//    }
//
//    public String getLastName(){
//        try {
//            return(getString(terser.get("/.PID-5-1")));
//        } catch (HL7Exception ex) {
//            return("");
//        }
//    }
//
    public String getDOB(){
    	String dob = "",year="",mon="",day="";

        try{
        	dob = getString(terser.get("/.PID-7-1"));
        	Date date = new Date(dob);
        	year = UtilDateUtilities.justYear(date);
        	mon = UtilDateUtilities.justMonth(date);
        	day = UtilDateUtilities.justDay(date);

            return(formatDateTime(year+mon+day));
        }catch(Exception e){
            return("");
        }
    }
//
//    public String getAge(){
//        String age = "N/A";
//        String dob = getDOB();
//        try {
//            // Some examples
//            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            java.util.Date date = (java.util.Date)formatter.parse(dob);
//            age = UtilDateUtilities.calcAge(date);
//        } catch (ParseException e) {
//            logger.error("Could not get age", e);
//
//        }
//        return age;
//    }
//
//    public String getSex(){
//        try{
//            return(getString(terser.get("/.PID-8-1")));
//        }catch(Exception e){
//            return("");
//        }
//    }
//
    public String getHealthNum(){
        String healthNum;
        try{
            healthNum = getString(terser.get("/.PID-3-1"));
                return(healthNum);
        }catch(Exception e){
            //ignore exceptions
        }

        return("");
    }

//    public String getHomePhone(){
//        try{
//            return(getString(terser.get("/.PID-13-1")));
//        }catch(Exception e){
//            return("");
//        }
//    }
//
//    public String getWorkPhone(){
//        return("");
//    }
//
//    public String getPatientLocation(){
//        try{
//            return(getString(terser.get("/.MSH-4-1")));
//        }catch(Exception e){
//            return("");
//        }
//    }
//
    public String getServiceDate(){
    	String serdate = "",year="",mon="",day="", time="";
        //usually a message type specific location
    	try{
    		serdate = getString(terser.get("/.OBR-7-1"));
        	Date date = new Date(serdate);
        	year = UtilDateUtilities.justYear(date);
        	mon = UtilDateUtilities.justMonth(date);
        	day = UtilDateUtilities.justDay(date);
        	time = justTime(date);
            serdate = (year+"-"+mon+"-"+day+" "+time);
            logger.info("serdate = "+serdate);
            return serdate.toString();
        }catch(Exception e){
            return("");
        }
    }
//
//    public String getOrderStatus(){
//        //usually a message type specific location
//        return("");
//    }
//
    public String getOrderStatus(){
        String status = "F";

        for(int x=0;x<this.getOBRCount();x++) {
        	for(int y=0;y<this.getOBXCount(x);y++) {
        		String val = this.getOBXResultStatus(x, y);
        		if(!val.equals("F")) {
        			return val;
        		}
        	}
        }

        return(status);
    }
//
//    public String getClientRef(){
//        try{
//            return(getString(terser.get("/.OBR-16-1")));
//        }catch(Exception e){
//            return("");
//        }
//    }


public String getAccessionNum(){
	String useOrderNumber = OscarProperties.getInstance().getProperty("hhs.emr.handler.accession.use_order_number","false");
	if(useOrderNumber.equals("true")) {
		try{
            String accessionNum = getString(terser.get("/.OBR-3-1"));
            return accessionNum;
        }catch(Exception e){
            return("");
        }
	}

        try{
            String accessionNum = getString(terser.get("/.MSH-10-1"));
            return accessionNum;
        }catch(Exception e){
            return("");
        }
    }


//    public String getAccessionNum(){
//        //usually a message type specific location
//        return("");
//    }
//
//    public String getDocName(){
//        try{
//            return(getFullDocName("/.OBR-16-"));
//        }catch(Exception e){
//            return("");
//        }
//    }
//
//    public String getCCDocs(){
//
//        try {
//            int i=0;
//            String docs = getFullDocName("/.OBR-28("+i+")-");
//            i++;
//            String nextDoc = getFullDocName("/.OBR-28("+i+")-");
//
//            while(!nextDoc.equals("")){
//                docs = docs+", "+nextDoc;
//                i++;
//                nextDoc = getFullDocName("/.OBR-28("+i+")-");
//            }
//
//            return(docs);
//        } catch (Exception e) {
//            return("");
//        }
//    }
//

    /*
     * Custom segment added by medseek because the HL7 messages did not contain "who" that we can route to the right provider in oscar.
     *
     */
    @Override
    public ArrayList<String> getDocNums(){
        ArrayList<String> nums = new ArrayList<String>();
        String docNum;
        try{
            if ((docNum = terser.get("/.Z01-1-1")) != null){
                MiscUtils.getLogger().debug("Adding doc Num"+Misc.forwardZero(docNum, 6));
                nums.add(Misc.forwardZero(docNum, 6));
            } else {
            	return super.getDocNums();
            }

        }catch(Exception e){
        	return super.getDocNums();
        }

        return(nums);
    }
//
//
//    public String audit(){
//        return "";
//    }
//
//    private String getOBXField(int i, int j, int field, int rep, int comp){
//        ArrayList obxSegs = (ArrayList) obrGroups.get(i);
//
//        try{
//            Segment obxSeg = (Segment) obxSegs.get(j);
//            return (getString(terser.get(obxSeg, field, rep, comp, 1 )));
//        }catch(Exception e){
//            return("");
//        }
//    }
//
//    private int getNTELocation(int i, int j) throws HL7Exception{
//        int k = 0;
//        int obrCount = 0;
//        int obxCount = 0;
//        String[] segments = terser.getFinder().getRoot().getNames();
//
//        while (k != segments.length && obrCount != i+1){
//            if (segments[k].substring(0, 3).equals("OBR"))
//                obrCount++;
//            k++;
//        }
//
//        Structure[] obxSegs;
//        while (k != segments.length && obxCount != j+1){
//
//
//            if (segments[k].substring(0, 3).equals(("OBX"))){
//                obxSegs = terser.getFinder().getRoot().getAll(segments[k]);
//                obxCount = obxCount + obxSegs.length;
//            }
//            k++;
//        }
//
//        return(k);
//    }
//
//
//    private String getFullDocName(String docSeg) throws HL7Exception{
//        String docName = "";
//        String temp;
//
//        // get name prefix ie/ DR.
//        temp = terser.get(docSeg+"6");
//        if(temp != null)
//            docName = temp;
//
//        // get the name
//        temp = terser.get(docSeg+"3");
//        if(temp != null){
//            if (docName.equals("")){
//                docName = temp;
//            }else{
//                docName = docName +" "+ temp;
//            }
//        }
//
//        if(terser.get(docSeg+"4") != null)
//            docName = docName +" "+ terser.get(docSeg+"4");
//        if(terser.get(docSeg+"2") != null)
//            docName = docName +" "+ terser.get(docSeg+"2");
//        if(terser.get(docSeg+"5")!= null)
//            docName = docName +" "+ terser.get(docSeg+"5");
//        if(terser.get(docSeg+"7") != null)
//            docName = docName +" "+ terser.get(docSeg+"7");
//
//        return (docName);
//    }
//
//
//    private String formatDateTime(String plain){
//
//        String dateFormat = "yyyyMMddHHmmss";
//        dateFormat = dateFormat.substring(0, plain.length());
//        String stringFormat = "yyyy-MM-dd HH:mm:ss";
//        stringFormat = stringFormat.substring(0, stringFormat.lastIndexOf(dateFormat.charAt(dateFormat.length()-1))+1);
//
//        Date date = UtilDateUtilities.StringToDate(plain, dateFormat);
//        return UtilDateUtilities.DateToString(date, stringFormat);
//    }
//
//    private String getString(String retrieve){
//        if (retrieve != null){
//            retrieve.replaceAll("^", " ");
//            return(retrieve.trim().replaceAll("\\\\\\.br\\\\", "<br />"));
//        }else{
//            return("");
//        }
//    }

    public String getPatientIdByType(String type) throws HL7Exception {
    	PatientId id = extractInternalPatientIds().get(type);
    	if(id != null) {
    		return id.getId();
    	}
    	return null;
    }

	protected Map<String,PatientId> extractInternalPatientIds() throws HL7Exception {
		Map<String,PatientId> ids = new LinkedHashMap<String,PatientId>();
		int x=0;
		while(true) {
			String identifier = terser.get("PID-3("+x+")-1");
			String authority = terser.get("PID-3("+x+")-4");
			String typeId = terser.get("PID-3("+x+")-5");

			if(identifier != null) {
				PatientId tmp = new PatientId(identifier, authority, typeId);
				ids.put(typeId,tmp);
			}

			if(identifier == null && terser.get("PID-3("+(x+1)+")-1")==null) {
				break;
			}
			x++;
		}
		return ids;
	}

	private String justTime(Date date){
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm");
        return simpledateformat.format(date);
    }
}
