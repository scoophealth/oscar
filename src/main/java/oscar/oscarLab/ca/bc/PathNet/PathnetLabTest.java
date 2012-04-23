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

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarMDS.data.ReportStatus;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class PathnetLabTest {

    Logger logger = Logger.getLogger(PathnetLabTest.class);

    String
//select_pid_information = "SELECT patient_name, external_id, date_of_birth, patient_address, sex, home_number  FROM hl7_pid WHERE hl7_pid.pid_id='@pid'",
            select_header_information = "SELECT DISTINCT filler_order_number, requested_date_time, observation_date_time, ordering_provider, result_copies_to FROM hl7_obr WHERE pid_id = '@pid'",
            select_lab_results = "SELECT hl7_obr.filler_order_number, hl7_obr.universal_service_id, hl7_obr.ordering_provider, hl7_obr.results_report_status_change, hl7_obr.diagnostic_service_sect_id, hl7_obr.result_status, hl7_obr.result_copies_to, hl7_obr.note as obrnote, hl7_obx.set_id, hl7_obx.observation_identifier, hl7_obx.observation_results, hl7_obx.units, hl7_obx.reference_range, hl7_obx.abnormal_flags, hl7_obx.observation_result_status, hl7_obx.note as obxnote FROM hl7_obr left join hl7_obx on hl7_obr.obr_id=hl7_obx.obr_id WHERE hl7_obr.pid_id='@pid' ORDER BY hl7_obr.diagnostic_service_sect_id",
            select_signed = "SELECT hl7_pid.pid_id, hl7_link.status, hl7_link.provider_no, hl7_link.signed_on, provider.last_name, provider.first_name FROM hl7_pid left join hl7_link on hl7_pid.pid_id=hl7_link.pid_id left join provider on provider.provider_no=hl7_link.provider_no WHERE hl7_pid.pid_id='@pid';",
            update_lab_report_signed = "UPDATE hl7_link SET hl7_link.status='S', hl7_link.provider_no='@provider_no', hl7_link.signed_on=NOW() WHERE hl7_link.pid_id='@pid';",
            update_lab_report_viewed = "UPDATE hl7_link SET hl7_link.status='A' WHERE hl7_link.pid_id='@pid' AND hl7_link.status!='S';",
            select_doc_notes = "SELECT hl7_message.notes FROM hl7_pid, hl7_message WHERE hl7_pid.pid_id='@pid' AND hl7_pid.message_id=hl7_message.message_id;",
            update_doc_notes = "UPDATE hl7_pid, hl7_message SET hl7_message.notes='@notes' WHERE hl7_pid.pid_id='@pid' AND hl7_pid.message_id=hl7_message.message_id;";


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

//ArrayList labs = gResults.getLabResults();
//for ( int l =0 ; l < labs.size() ; l++){
//                            CMLLabTest.LabResult thisResult = (CMLLabTest.LabResult) labs.get(l);
//                                <td valign="top" align="left"><a href="labValues.jsp?testName=<%=thisResult.testName%>&demo=<%=lab.getDemographicNo()%>&labType=BCP"><%=thisResult.testName %></a></td>
//                        <%}/*for lab.size*/%>
//                            <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '../../../oscarMDS/SearchPatient.do?labType=BCP&segmentID=<%= request.getParameter("segmentID")%>&name=<%=java.net.URLEncoder.encode(lab.pLastName+", "+lab.pFirstName )%>', 'searchPatientWindow')">



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
        String ret = null;
        try{

            String select_demoNo = "select demographic_no from patientLabRouting where lab_type = 'BCP' and lab_no = '"+id+"'";
            ResultSet rs  = DBHandler.GetSQL(select_demoNo);
            if(rs.next()){
                ret = oscar.Misc.getString(rs, "demographic_no");
            }
            if (ret != null && ret.equals("0")){
                ret = null;
            }
            rs.close();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return ret;
    }

    public void populateLab(String labid){

        PathnetResultsData data = new PathnetResultsData();
        multiLabId = data.getMatchingLabs(labid);
        demographicNo = getDemographicNumByLabId(labid);
        try{

            String select_pid_information = "SELECT pid_id, patient_name, external_id, date_of_birth, patient_address, sex, home_number,sending_facility  FROM hl7_pid, hl7_msh WHERE hl7_pid.message_id = '"+labid+"' and hl7_msh.message_id = hl7_pid.message_id";
            ResultSet rs = DBHandler.GetSQL(select_pid_information);
            if(rs.next()){
                pName = removeCarat(oscar.Misc.getString(rs, "patient_name"));
                pSex = oscar.Misc.getString(rs, "sex");
                pHealthNum = oscar.Misc.getString(rs, "external_id");
                pDOB = getFirstValSpace( oscar.Misc.getString(rs, "date_of_birth") );
                pPhone = oscar.Misc.getString(rs, "home_number");
                patientLocation = oscar.Misc.getString(rs, "sending_facility");
                pid = oscar.Misc.getString(rs, "pid_id");
            }
            rs.close();
            String select_obr_information = "SELECT * from hl7_obr WHERE pid_id = '"+pid+"'";
            rs = DBHandler.GetSQL(select_obr_information);
            if(rs.next()){
                serviceDate = oscar.Misc.getString(rs, "results_report_status_change");
                status = oscar.Misc.getString(rs, "result_status"); //.equals("F") ? "Final" : "Partial")
                Properties p = sepDocNameNum(oscar.Misc.getString(rs, "ordering_provider"));
                docNum = p.getProperty("num","");
                accessionNum = justGetAccessionNumber(oscar.Misc.getString(rs, "filler_order_number"));
                docName = p.getProperty("name",oscar.Misc.getString(rs, "ordering_provider"));
                String ccs = oscar.Misc.getString(rs, "result_copies_to");
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

            //ResultSet rs = DBHandler.GetSQL(select_lab_results.replaceAll("@pid", pid));
            ResultSet rs = DBHandler.GetSQL("select diagnostic_service_sect_id, universal_service_id, set_id, obr_id from hl7_obr where pid_id = '"+pid+"' ");

            logger.info("select diagnostic_service_sect_id, universal_service_id, set_id, obr_id from hl7_obr where pid_id = '"+pid+"' ");

            while(rs.next()){
                GroupResults gr = new GroupResults();
                gr.groupName = oscar.Misc.getString(rs, "diagnostic_service_sect_id")+ " " +oscar.Misc.getString(rs, "universal_service_id").substring(oscar.Misc.getString(rs, "universal_service_id").indexOf("^"));
                String obrId = oscar.Misc.getString(rs, "obr_id");

                ResultSet rs2 = DBHandler.GetSQL("select set_id, observation_date_time,observation_result_status, observation_identifier, observation_results, units, reference_range, abnormal_flags, observation_result_status, note as obxnote from hl7_obx where obr_id = '"+obrId+"'");
                logger.info("select set_id, observation_identifier, observation_results, units, reference_range, abnormal_flags, observation_result_status, note as obxnote from hl7_obx where obr_id = '"+obrId+"'");
                while(rs2.next()){
                    LabResult l = new LabResult();
                    l.testName = rs2.getString("observation_identifier").substring(rs2.getString("observation_identifier").indexOf("^")+1);
                    l.result = rs2.getString("observation_results");
                    l.abn = rs2.getString("abnormal_flags");
                    l.minimum = rs2.getString("reference_range");
                    l.maximum =rs2.getString("reference_range");
                    l.units =rs2.getString("units");
                    l.timeStamp = rs2.getString("observation_date_time");
                    l.resultStatus = rs2.getString("observation_result_status");
                    l.notes = rs2.getString("obxnote");
                    if( l.notes != null ){
                        l.notes = l.notes.replaceAll("\\\\\\.br\\\\", " ");
                    }
                    gr.addLabResult(l);
                }
                rs2.close();
                list.add(gr);
            }
            rs.close();
        }catch(Exception e){MiscUtils.getLogger().error("Error", e);}
        return list;
    }

    public ArrayList<GroupResults> getResults(String pid){
        ArrayList<GroupResults> list = new ArrayList<GroupResults>();
        try{

            //ResultSet rs = DBHandler.GetSQL(select_lab_results.replaceAll("@pid", pid));
            ResultSet rs = DBHandler.GetSQL("select diagnostic_service_sect_id, universal_service_id, set_id, obr_id,note from hl7_obr where pid_id = '"+pid+"' ");
            logger.info("select diagnostic_service_sect_id, universal_service_id, set_id, obr_id,note from hl7_obr where pid_id = '"+pid+"' ");

            GroupResults gr = null;
            while(rs.next()){
                String gName = oscar.Misc.getString(rs, "diagnostic_service_sect_id");
                if (gr == null || !gName.equals(gr.groupName)){
                    gr = new GroupResults();
                    gr.groupName = gName; //oscar.Misc.getString(rs,"diagnostic_service_sect_id"); //+ " " +oscar.Misc.getString(rs,"universal_service_id").substring(oscar.Misc.getString(rs,"universal_service_id").indexOf(" "));
                    list.add(gr);
                }
                gr.addHeaderResults(oscar.Misc.getString(rs, "note"));
                String obrId = oscar.Misc.getString(rs, "obr_id");
                ResultSet rs2 = DBHandler.GetSQL("select x.set_id, universal_service_id, x.observation_date_time, x.observation_result_status, x.observation_identifier, x.observation_results, x.units, x.reference_range, x.abnormal_flags, x.observation_result_status, x.note as obxnote from hl7_obx x, hl7_obr where hl7_obr.obr_id = '"+obrId+"' and hl7_obr.obr_id = x.obr_id ");
                logger.info("select x.set_id, universal_service_id, x.observation_date_time, x.observation_result_status, x.observation_identifier, x.observation_results, x.units, x.reference_range, x.abnormal_flags, x.observation_result_status, x.note as obxnote from hl7_obx x, hl7_obr where hl7_obr.obr_id = '"+obrId+"' and hl7_obr.obr_id = x.obr_id ");
                while(rs2.next()){
                    LabResult l = new LabResult();
                    l.service_name = rs2.getString("universal_service_id").substring(rs2.getString("universal_service_id").indexOf("^")+1);
                    l.testName = rs2.getString("observation_identifier").substring(rs2.getString("observation_identifier").indexOf("^")+1);
                    l.result = rs2.getString("observation_results");
                    if( l.result != null ){
                        l.result = l.result.replaceAll("\\\\\\.br\\\\", "<br/>");
                    }
                    l.abn = rs2.getString("abnormal_flags");
                    l.minimum = rs2.getString("reference_range");
                    l.maximum =rs2.getString("reference_range");
                    l.units =rs2.getString("units");
                    l.timeStamp = rs2.getString("observation_date_time");
                    l.resultStatus = rs2.getString("observation_result_status");
                    l.notes = rs2.getString("obxnote");
                    if( l.notes != null ){
                        l.notes = l.notes.replaceAll("\\\\\\.br\\\\", "<br/>");
                    }
                    gr.addLabResult(l);
                }
                rs2.close();
            }
            rs.close();
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
