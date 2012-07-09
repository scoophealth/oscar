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
package oscar.oscarLab.ca.on.Spire;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class SpireLabTest {
    private static Logger log = MiscUtils.getLogger();
    
    public String locationId = null; //  2. (e.g. 70 = CML Mississauga)
    public String printDate  = null; //  3. YYYYMMDD
    public String printTime  = null; //  4. HH:MM
    public String totalBType = null; //  5. number of B-type lines (= # of reports)
    public String totalCType = null; //  6. number of C-type lines
    public String totalDType = null; //  7. number of D-type lines
    
    
    public String accessionNum = null;        //  2. CML Accession number (minus first char)
    public String physicianAccountNum = null; //  3. Physician Account number
    public String serviceDate = null;         //  4. YYYYMMDD
    public String pFirstName = null;          //  5. Patient: First name
    public String pLastName = null;           //  6. Patient: Last name
    public String pSex = null;                //  7. Sex F or M
    public String pHealthNum = null;          //  8. Patient: Health number
    public String pDOB = null;                //  9. Patient: Birth date
    public String status = null;              // 10. Final or Partial F or P
    public String docNum = null;              // 11. Physician: Number
    public String docName = null;             // 12. Physician: Name
    public String docAddr1 = null;            // 13. Physician: Address line 1
    public String docAddr2 = null;            // 14. Physician: Address line 2
    public String docAddr3 = null;            // 15. Physician: Address line 3
    public String docPostal = null;           // 16. Physician: Postal code
    public String docRoute = null;            // 17. Physician: Route number
    public String comment1 = null;            // 18. Comment 1
    public String comment2 = null;            // 19. Comment 2
    public String pPhone = null;              // 20. Patient: Phone number
    public String docPhone = null;            // 21. Physician: Phone number
    public String collectionDate = null;      // 22. Collection date "DD MMM YY"
    
    public String labReportInfoId = null;
    
    public String labID= null;
    
    public ArrayList labResults = null;
    
    public String demographicNo = null;
    
    public String multiLabId = null;
    
    public SpireLabTest() {
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
        } catch (Exception e) {
        	// this is okay, either null or invalid format
        }
        return age;
    }
    
    public String getDemographicNo(){
        return demographicNo;
    }
    
    private void populateDemoNo(String labId){
        try{
            
            ResultSet rs = DBHandler.GetSQL("select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type = 'CML'");
            log.debug("select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type = 'CML'");
            if (rs.next()){
                String d = oscar.Misc.getString(rs, "demographic_no");
                log.debug("dd "+d);
                if ( !"0".equals(d)){
                    this.demographicNo = d;
                }
            }
            rs.close();
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        log.debug("going out "+this.demographicNo);
    }
    
    public void populateLab(String labid){
        labID = labid;
        
        CommonLabResultData data = new CommonLabResultData();
        this.multiLabId = data.getMatchingLabs(labid, "CML");
                
        log.debug("lab id "+labid);
        try{
            
            ResultSet rs = DBHandler.GetSQL("select * from labPatientPhysicianInfo where id = '"+labid+"'");
            
            
            if (rs.next()){
                this.labReportInfoId = oscar.Misc.getString(rs, "labReportInfo_id");
                this.accessionNum = oscar.Misc.getString(rs, "accession_num");
                this.physicianAccountNum = oscar.Misc.getString(rs, "physician_account_num");
                this.serviceDate = oscar.Misc.getString(rs, "service_date");
                this.pFirstName = oscar.Misc.getString(rs, "patient_first_name");
                this.pLastName = oscar.Misc.getString(rs, "patient_last_name");
                this.pSex = oscar.Misc.getString(rs, "patient_sex");
                this.pHealthNum = oscar.Misc.getString(rs, "patient_health_num");
                this.pDOB = oscar.Misc.getString(rs, "patient_dob");
                this.status = oscar.Misc.getString(rs, "lab_status");
                this.docNum = oscar.Misc.getString(rs, "doc_num");
                this.docName = oscar.Misc.getString(rs, "doc_name");
                this.docAddr1 = oscar.Misc.getString(rs, "doc_addr1");
                this.docAddr2 = oscar.Misc.getString(rs, "doc_addr2");
                this.docAddr3 = oscar.Misc.getString(rs, "doc_addr3");
                this.docPostal = oscar.Misc.getString(rs, "doc_postal");
                this.docRoute = oscar.Misc.getString(rs, "doc_route");
                this.comment1 = oscar.Misc.getString(rs, "comment1");
                this.comment2 = oscar.Misc.getString(rs, "comment2");
                this.pPhone = oscar.Misc.getString(rs, "patient_phone");
                this.docPhone = oscar.Misc.getString(rs, "doc_phone");
                this.collectionDate = oscar.Misc.getString(rs, "collection_date");
                log.debug(" lab id "+labReportInfoId);
            }
            
            rs.close();
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        
        if (labReportInfoId != null){
            log.debug(" filling labReport Info");
            populateLabReportInfo(labReportInfoId);
        }
        
        if (labid != null){
            log.debug("Filling lab Result DAta");
            this.labResults =  populateLabResultData(labid);
        }
        
        if (labid != null ){
            populateDemoNo(labid);
        }
        
    }
    
    
    public String getDiscipline(String labid){
        String dis = "";
        
        try{
            
            //ResultSet rs = DBHandler.GetSQL("select distinct title from labTestResults where title != '' and labPatientPhysicianInfo_id = '"+labid+"'");
            ResultSet rs = DBHandler.GetSQL("select distinct discipline from hl7TextInfo where discipline != '' and lab_no = '"+labid+"'");
            ArrayList alist = new ArrayList();
            int count = 0;
            while (rs.next()){
                String discipline = oscar.Misc.getString(rs, "discipline");
                count += discipline.length();
                alist.add(discipline);
                log.debug("line "+discipline);
            }
            
            if(alist.size() == 1 ){
                dis = (String) alist.get(0); //Only one item
            }else if(alist.size() != 0) {
                int lenAvail = 20 - ( alist.size() - 1);
                if ( lenAvail > count){
                    StringBuilder s = new StringBuilder();
                    for(int i = 0; i < alist.size(); i++){
                        s.append( (String) alist.get(i));
                        if (i < (alist.size() -1)){
                            s.append("/");
                        }
                    }
                    dis = s.toString();
                }else{//need to divide up characters
                    int charEach = lenAvail / alist.size();
                    StringBuilder s = new StringBuilder();
                    for(int i = 0; i < alist.size(); i++){
                        String str = (String) alist.get(i);
                        
                        s.append(  StringUtils.substring(str,0,charEach) );
                        if (i < (alist.size() -1)){
                            s.append("/");
                        }
                    }
                    dis = s.toString();
                }
            }
            rs.close();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return dis;
        
    }
    
    public ArrayList getStatusArray(String labid){
        CommonLabResultData comLab = new CommonLabResultData();
        return comLab.getStatusArray(labid,"CML");
    }
    
    private void populateLabReportInfo(String labid){
        //labID = labid;
        try{
            
            ResultSet rs = DBHandler.GetSQL("select * from labReportInformation where id = '"+labid+"'");
            if (rs.next()){
                this.locationId = oscar.Misc.getString(rs, "location_id");
                this.printDate = oscar.Misc.getString(rs, "print_date");
                this.printTime = oscar.Misc.getString(rs, "print_time");
                this.totalBType = oscar.Misc.getString(rs, "total_BType");
                this.totalCType = oscar.Misc.getString(rs, "total_CType");
                this.totalDType = oscar.Misc.getString(rs, "total_DType");
            }
            rs.close();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    
    private ArrayList populateLabResultData(String labid){
        ArrayList alist = new ArrayList();
        try{
            
            ResultSet rs = DBHandler.GetSQL("select * from labTestResults where labPatientPhysicianInfo_id = '"+labid+"'");
            log.debug("select * from labTestResults where labPatientPhysicianInfo_id = '"+labid+"'");
            while (rs.next()){
                String lineType = oscar.Misc.getString(rs, "line_type");
                log.debug("line "+lineType);
                if (lineType != null){
                    LabResult labRes = new LabResult();
                    
                    labRes.title = oscar.Misc.getString(rs, "title");
                    if (labRes.title == null){ labRes.title = "" ;}
                    labRes.notUsed1 = oscar.Misc.getString(rs, "notUsed1");
                    labRes.locationId = oscar.Misc.getString(rs, "location_id");
                    labRes.last = oscar.Misc.getString(rs, "last");
                    
                    if(lineType.equals("C")){
                        labRes.notUsed2 = oscar.Misc.getString(rs, "notUsed2");
                        labRes.testName = oscar.Misc.getString(rs, "test_name");
                        labRes.abn = oscar.Misc.getString(rs, "abn");
                        if(labRes.abn != null && labRes.abn.equals("N")){
                            labRes.abn = "";
                        }
                        labRes.minimum = oscar.Misc.getString(rs, "minimum");
                        labRes.maximum = oscar.Misc.getString(rs, "maximum");
                        labRes.units = oscar.Misc.getString(rs, "units");
                        labRes.result = oscar.Misc.getString(rs, "result");
                    }else if (lineType.equals("D")){
                        labRes.description = oscar.Misc.getString(rs, "description");
                        labRes.labResult = false;
                    }
                    alist.add(labRes);
                }
            }
            rs.close();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return alist;
    }
    
    public int findCMLAdnormalResults(String labId){
        int count = 0;
        String sql = null;
        try {
            
            
            sql = "select id from labTestResults where abn = 'A' and labPatientPhysicianInfo_id = '"+labId+"'";
            
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                count++;
            }
            rs.close();
        }catch(Exception e){
            log.error("sql: "+sql,e);
        }
        return count;
    }
    
    
    public class LabResult{
        
        boolean labResult = true;
        
        public boolean isLabResult(){ return labResult ;}
        public boolean isLabResultComment(){ return labResult ;}
        
        
        ///
        public String title = null;       //  2. Title
        public String notUsed1 = null;    //  3. Not used ?
        public String notUsed2 = null;    //  4. Not used ?
        public String testName = null;    //  5. Test name
        public String abn  = null;     //  6. Normal/Abnormal N or A
        public String minimum = null;     //  7. Minimum
        public String maximum = null;     //  8. Maximum
        public String units = null;       //  9. Units
        public String result = null;      // 10. Result
        public String locationId = null;  // 11. Location Id (Test performed at)
        public String last = null;        // 12. Last Y or N
        
        
        //String title = null;       // 2. Title
        //String notUsed1 = null;    // 3. not used ?
        public String description = null; // 4. Description/Comment
        //String locationId = null;  // 5. Location Id
        //String last = null;        // 6. Last Y or N
        
        ///
        public String getReferenceRange(){
            String retval ="";
            if (filled(minimum) && filled(maximum)) {
                if (minimum.equals(maximum)){
                    retval = minimum;
                }else{
                    retval = minimum + " - " + maximum;
                }
            } else if (filled(minimum)) {
            	retval = minimum;
            } else if (filled(maximum)) {
            	retval = maximum;
            }
            return retval;
        }
        
    }
    
    public class GroupResults{
        public String groupName = null;
        private ArrayList labResults = null;
        
        public void addLabResult(LabResult l){
            if (labResults == null){ labResults = new ArrayList(); }
            labResults.add(l);
        }
        
        public ArrayList getLabResults(){
            return labResults;
        }
    }
    
    public ArrayList getGroupResults(ArrayList list){
        ArrayList groups = new ArrayList();
        String currentGroup = "";
        GroupResults gResults = null;
        log.debug("start getGroupResults ... list size: "+list.size());
        for ( int i = 0; i < list.size(); i++){
            LabResult lab = (LabResult) list.get(i);
            log.debug(" lab title "+lab.title+ " currentGroup "+currentGroup);
            if ( currentGroup.equals(lab.title) && gResults != null){
                log.debug("old");
                gResults.addLabResult(lab);
                gResults.groupName =  lab.title;
            }else{
                log.debug("new");
                gResults = new GroupResults();
                gResults.groupName = currentGroup = lab.title;
                groups.add(gResults);
                gResults.addLabResult(lab);
                currentGroup = lab.title;
            }
        }
        return groups;
    }
    
    private boolean filled(String s) {
    	return !(s==null || s.trim().equals(""));
    }
}//end



