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


package oscar.oscarLab.ca.bc.PathNet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.Hl7ObrDao;
import org.oscarehr.billing.CA.BC.dao.Hl7ObxDao;
import org.oscarehr.billing.CA.BC.dao.Hl7PidDao;
import org.oscarehr.billing.CA.BC.model.Hl7Msh;
import org.oscarehr.billing.CA.BC.model.Hl7Obr;
import org.oscarehr.billing.CA.BC.model.Hl7Obx;
import org.oscarehr.billing.CA.BC.model.Hl7Pid;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarMDS.data.ReportStatus;
import oscar.util.ConversionUtils;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class PathnetLabTest {

    Logger logger = Logger.getLogger(PathnetLabTest.class);

    public String pName = "";          //  5. Patient: First name
    public String pSex = "";                //  7. Sex F or M
    public String pHealthNum = "";          //  8. Patient: Health number
    public String pDOB = "";                //  9. Patient: Birth date
    public String pPhone = "";
    public String patientLocation = "";
    public String pid = null;

    public String serviceDate = "";
    public String status = ""; //.equals("F") ? "Final" : "Partial")
    public String docNum = "";
    public String accessionNum = "";
    public String docName = "";
    public String demographicNo = null ;
    public String ccedDocs = "";
    public String locationId = "";
    public String multiLabId = "";

    public ArrayList labResults = new ArrayList();

    public PathnetLabTest() {
    }

    public ArrayList<ReportStatus> getStatusArray(String labID){
        CommonLabResultData comLab = new CommonLabResultData();
        return comLab.getStatusArray(labID,"BCP");
    }

    public ArrayList<GroupResults> getGroupResults(ArrayList<GroupResults> labResults){
        return new ArrayList<GroupResults>();
    }

    public String getDemographicNo(){
        return demographicNo;
    }

    public Properties sepDocNameNum(String s){
        Properties h = new Properties();
        int i = s.indexOf("^");
        if (i != -1){
            String num = s.substring(0,i);
            String name = s.substring(i+1).replaceAll("\\^", " ");
            h.put("num", num);
            h.put("name",name);
        }
        return h;
    }

    public String justGetDocName(String s){
        String ret = s;
        int i = s.indexOf("^");
        if (i != -1){
            ret = s.substring(i+1).replaceAll("\\^", " ");
        }
        return ret;
    }

    public String getFirstVal(String s,String delimiter){
        String ret = s;
        int i = s.indexOf(delimiter);
        if (i != -1){
            ret = s.substring(0,i);
        }
        return ret;
    }

    public String getFirstValDash(String s){
        return getFirstVal(s,"-");
    }

    public String getFirstValSpace(String s){
        return getFirstVal(s," ");
    }

    public String getDemographicNumByLabId(String id){
        PatientLabRoutingDao dao = SpringUtils.getBean(PatientLabRoutingDao.class);
        for(PatientLabRouting r : dao.findByLabNoAndLabType(ConversionUtils.fromIntString(id), "BCP")) {
        	return "" + r.getDemographicNo();        	
        }
        return null;        
    }

    public void populateLab(String labid){
        PathnetResultsData data = new PathnetResultsData();
        multiLabId = data.getMatchingLabs(labid);
        demographicNo = getDemographicNumByLabId(labid);
        
        Hl7PidDao dao = SpringUtils.getBean(Hl7PidDao.class);
        Hl7ObrDao obrDao = SpringUtils.getBean(Hl7ObrDao.class); 
        
        try{
            
            for(Object[] o : dao.findPidsAndMshByMessageId(0)) {
            	Hl7Pid p = (Hl7Pid) o[0];
            	Hl7Msh msh = (Hl7Msh) o[1];
				
                pName = removeCarat(p.getPatientName());
                pSex = p.getSex();
                pHealthNum = p.getExternalId();
                pDOB = getFirstValSpace(ConversionUtils.toDateString(p.getDateOfBirth()));
                pPhone = p.getHomeNumber();
                patientLocation = msh.getSendingFacility();
                pid = p.getId().toString();
            }
            
            
            List<Hl7Obr> obrs = obrDao.findByPid(ConversionUtils.fromIntString(pid));

            for(Hl7Obr obr : obrs) {
                serviceDate = ConversionUtils.toDateString(obr.getResultsReportStatusChange());
                status = obr.getResultStatus(); //.equals("F") ? "Final" : "Partial")
                Properties p = sepDocNameNum(obr.getOrderingProvider());
                docNum = p.getProperty("num","");
                accessionNum = justGetAccessionNumber(obr.getFillerOrderNumber());
                docName = p.getProperty("name",obr.getOrderingProvider());
                String ccs = obr.getResultCopiesTo();
                String docs[] = ccs.split("~");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < docs.length; i++){
                    sb.append(justGetDocName(docs[i]));
                    if (i < (docs.length - 1)){
                        sb.append(", ");
                    }
                }
                ccedDocs = sb.toString();
            }
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }


    public String getAge(){
        return getAge(this.pDOB);
    }

    public String getAge(String s){
        String age = "N/A";
        try {
            // Some examples
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            java.util.Date date = formatter.parse(s);
            age = UtilDateUtilities.calcAge(date);
        } catch (ParseException e) {
        	//empty
        }
        return age;
    }

    public static String removeCarat(String s){
        if(s!=null){
            return s.replaceAll("\\^", " ");
        }
        return s;
    }

    public String justGetAccessionNumber(String s){
        String[] ss = s.split("-");
        if (ss.length == 3)
            return ss[0];
        else
            return ss[1];
    }


    public ArrayList<GroupResults> getResultsOld(String pid){
        ArrayList<GroupResults> list = new ArrayList<GroupResults>();
        try{

            Hl7ObrDao dao = SpringUtils.getBean(Hl7ObrDao.class);
            Hl7ObxDao obxDao = SpringUtils.getBean(Hl7ObxDao.class);
            
            for(Hl7Obr o : dao.findByPid(ConversionUtils.fromIntString(pid))) {
                GroupResults gr = new GroupResults();
                gr.groupName = o.getDiagnosticServiceSectId() + " " +o.getUniversalServiceId().substring(o.getUniversalServiceId().indexOf("^"));
                String obrId = "" + o.getId();
                
                for(Hl7Obx obx : obxDao.findByObrId(ConversionUtils.fromIntString(obrId))) {
                    LabResult l = new LabResult();
                    l.testName = obx.getObservationIdentifier().substring(obx.getObservationIdentifier().indexOf("^")+1);
                    l.result = obx.getObservationResults();
                    l.abn = obx.getAbnormalFlags();
                    l.minimum = obx.getReferenceRange();
                    l.maximum =obx.getReferenceRange();
                    l.units =obx.getUnits();
                    l.timeStamp = ConversionUtils.toDateString(obx.getObservationDateTime());
                    l.resultStatus = obx.getObservationResultStatus();
                    l.notes = obx.getNote();
                    if( l.notes != null ){
                        l.notes = l.notes.replaceAll("\\\\\\.br\\\\", " ");
                    }
                    gr.addLabResult(l);
                }
                list.add(gr);
            }
        }catch(Exception e){MiscUtils.getLogger().error("Error", e);}
        return list;
    }

    public ArrayList<GroupResults> getResults(String pid){
        ArrayList<GroupResults> list = new ArrayList<GroupResults>();
        Hl7ObrDao dao = SpringUtils.getBean(Hl7ObrDao.class);
        Hl7ObxDao obxDao = SpringUtils.getBean(Hl7ObxDao.class);
        try{
            GroupResults gr = null;
            for(Hl7Obr obr : dao.findByPid(ConversionUtils.fromIntString(pid))) {
                String gName = obr.getDiagnosticServiceSectId(); 
                
                if (gr == null || !gName.equals(gr.groupName)){
                    gr = new GroupResults();
                    gr.groupName = gName;
                    list.add(gr);
                }
                gr.addHeaderResults(obr.getNote());
                String obrId = "" + obr.getId();
                
               for(Object[] o : obxDao.findObxAndObrByObrId(ConversionUtils.fromIntString(obrId))) {
            	   Hl7Obx obx2 = (Hl7Obx) o[0];
            	   Hl7Obr obr2 = (Hl7Obr) o[1];
            	   
                    LabResult l = new LabResult();
                    l.service_name = obr2.getUniversalServiceId().substring(obr2.getUniversalServiceId().indexOf("^")+1);
                    l.testName = obx2.getObservationIdentifier().substring(obx2.getObservationIdentifier().indexOf("^")+1);
                    l.result = obx2.getObservationResults();
                    if( l.result != null ){
                        l.result = l.result.replaceAll("\\\\\\.br\\\\", "<br/>");
                    }
                    l.abn = obx2.getAbnormalFlags();
                    l.minimum = obx2.getReferenceRange();
                    l.maximum =obx2.getReferenceRange();
                    l.units =obx2.getUnits();
                    l.timeStamp = ConversionUtils.toDateString(obx2.getObservationDateTime());
                    l.resultStatus = obx2.getObservationResultStatus();
                    l.notes = obx2.getNote();
                    if( l.notes != null ){
                        l.notes = l.notes.replaceAll("\\\\\\.br\\\\", "<br/>");
                    }
                    gr.addLabResult(l);
                }
            }
        }catch(Exception e){MiscUtils.getLogger().error("Error", e);}
        return list;
    }



    public class LabResult{

        boolean labResult = true;

        public boolean isLabResult(){ return labResult ;}
        public boolean isLabResultComment(){ return labResult ;}


        ///
        public String service_name = null;
        public String title = null;       //  2. Title
        public String notUsed1 = null;    //  3. Not used ?
        public String notUsed2 = null;    //  4. Not used ?
        public String testName = null;    //  5. Test name
        public String abn  = null;     //  6. Normal/Abnormal N or A
        public String minimum = null;     //  7. Minimum
        public String maximum = null;     //  8. Maximum
        public String units = null;       //  9. Units
        public String result = null;      // 10. Result
        public String locationId = "";  // 11. Location Id (Test performed at )
        public String last = null;        // 12. Last Y or N

        public String timeStamp = "";
        public String resultStatus = "";
        //String title = null;       // 2. Title
        //String notUsed1 = null;    // 3. not used ?
        public String description = null; // 4. Description/Comment
        //String locationId = null;  // 5. Location Id
        //String last = null;        // 6. Last Y or N

        public String notes = null;


        public String getReferenceRange(){
            String retval ="";
            if (minimum != null && maximum != null){
                if (!minimum.equals("") && !maximum.equals("")){
                    if (minimum.equals(maximum)){
                        retval = minimum;
                    }else{
                        retval = minimum + " - " + maximum;
                    }
                }
            }
            return retval;
        }

    }

    public class GroupResults{
        public String groupName = null;
        private ArrayList<LabResult> labResults = null;
        public ArrayList<String> headerResults = null;

        public void addLabResult(LabResult l){
            if (labResults == null){ labResults = new ArrayList<LabResult>(); }
            labResults.add(l);
        }

        public void addHeaderResults(String s){
            boolean add = true;

            if (headerResults == null){ headerResults = new ArrayList<String>(); }
            if ( headerResults.size() > 0){
                String h = headerResults.get((headerResults.size()-1));

                if(h.equals(s.replaceAll("\\\\\\.br\\\\", "<br/>"))){
                    add = false;
                }
            }
            if(add){
                headerResults.add(s.replaceAll("\\\\\\.br\\\\", "<br/>"));
            }
        }

        public ArrayList<LabResult> getLabResults(){
            if (labResults == null){ labResults = new ArrayList<LabResult>(); }
            return labResults;
        }

        public ArrayList<String> getHeaderResults(){
            if (headerResults == null){ headerResults = new ArrayList<String>(); }
            return headerResults;
        }

    }

    public String getLocationId() {
        return locationId;
    }
}
