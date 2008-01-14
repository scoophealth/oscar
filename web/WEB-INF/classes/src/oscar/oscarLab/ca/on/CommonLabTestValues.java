/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of CommonLabTestValues
 *
 *
 * CommonLabTestValues.java
 *
 * Created on May 4, 2005, 3:27 PM
 */

package oscar.oscarLab.ca.on;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import org.apache.log4j.Logger;
import oscar.OscarProperties;
import oscar.oscarDB.*;
import oscar.oscarLab.ca.all.parsers.*;
import oscar.util.*;

/**
 *
 * @author Jay Gallagher
 */
public class CommonLabTestValues {
    
    Logger logger = Logger.getLogger(CommonLabTestValues.class);
    
    public CommonLabTestValues() {
    }
    
    public  String getReferenceRange(String minimum,String maximum){
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
    
    
    
    
    public ArrayList findUniqueLabsForPatient(String demographic){
        OscarProperties op = OscarProperties.getInstance();
        String cml = op.getProperty("CML_LABS");
        String mds = op.getProperty("MDS_LABS");
        String pathnet = op.getProperty("PATHNET_LABS");
        String hl7text = op.getProperty("HL7TEXT_LABS");
        ArrayList labs = new ArrayList();
        if( cml != null && cml.trim().equals("yes")){
            ArrayList cmlLabs = findUniqueLabsForPatientCML(demographic);
            labs.addAll(cmlLabs);
        }
        if (mds != null && mds.trim().equals("yes")){
            ArrayList mdsLabs = findUniqueLabsForPatientMDS(demographic);
            labs.addAll(mdsLabs);
        }
        if (pathnet != null && pathnet.trim().equals("yes")){
            ArrayList pathLabs = findUniqueLabsForPatientExcelleris(demographic);
            labs.addAll(pathLabs);
        }
        if (hl7text != null && hl7text.trim().equals("yes")){
            ArrayList hl7Labs = findUniqueLabsForPatientHL7Text(demographic);
            labs.addAll(hl7Labs);
        }
        return labs;
    }
    
    //Method returns unique test names for a patient
    //List is used to compile a cummalitive lab profile
    //Hashtable return in list
    //"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE
    //"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
    //"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
    public ArrayList findUniqueLabsForPatientCML(String demographic){
        //Need to check which labs are active
        ArrayList labList = new ArrayList();
        String sql = "select  distinct p.lab_type, ltr.title, ltr.test_name "+
                "from "+
                "patientLabRouting p , "+
                "labTestResults ltr, "+
                "labPatientPhysicianInfo lpp "+
                "where p.lab_type = 'CML' "+
                "and p.demographic_no = '"+demographic+"' "+
                "and p.lab_no = ltr.labPatientPhysicianInfo_id "+
                "and ltr.labPatientPhysicianInfo_id = lpp.id and  ltr.test_name is not null  and ltr.test_name != '' "+
                "order by title";
        
        logger.info(sql);
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                String testNam = rs.getString("test_name");
                String labType = rs.getString("lab_type");
                String title = rs.getString("title");
                Hashtable h = new Hashtable();
                h.put("testName", testNam);
                h.put("labType",labType);
                h.put("title",title);
                labList.add(h);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
        }
        
        return labList;
    }
    
    //Method returns unique test names for a patient
    //List is used to compile a cummalitive lab profile
    //Hashtable return in list
    //"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE
    //"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
    //"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
    public ArrayList findUniqueLabsForPatientMDS(String demographic){
        //Need to check which labs are active
        ArrayList labList = new ArrayList();
        String sql = "select distinct p.lab_type, x.observationIden, x.observationResultStatus " +
                "from mdsOBX x, mdsMSH m, patientLabRouting p " +
                " where p.demographic_no = '"+demographic+"' " +
                "and m.segmentID = p.lab_no " +
                "and x.segmentID = m.segmentID  and p.lab_type = 'MDS' ";
        
        logger.info(sql);
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                
                String status = rs.getString("observationResultStatus");
                if (status.equals("D") || status.equals("I") || status.equals("X") || status.equals("W"))
                    continue;
                
                String testNam = "Unknown";rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));
                String labType = rs.getString("lab_type");
                String title = "";//TODO:rs.getString("title");
                
                try{
                    String obserIden = rs.getString("observationIden");//.substring(rs.getString("observationIden").indexOf('^'),rs.getString("observationIden").indexOf('^',rs.getString("observationIden").indexOf('^')));  //reportname or observationIden
                    int first = rs.getString("observationIden").indexOf('^');
                    int second = rs.getString("observationIden").indexOf('^',first+1);
                    testNam = rs.getString("observationIden").substring(first+1,second);
                }catch(Exception e){
                    logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
                }
                
                
                Hashtable h = new Hashtable();
                h.put("testName", testNam);
                h.put("labType",labType);
                h.put("title",title);
                labList.add(h);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
            
        }
        
        return labList;
    }
    public ArrayList findUniqueLabsForPatientExcelleris(String demographic){
        ArrayList labList = new ArrayList();
        String sql = "select distinct p.lab_type,x.observation_identifier "+
                "from patientLabRouting p, hl7_msh m ,hl7_pid pi, hl7_obr r,hl7_obx x  " +
                "where p.demographic_no = '"+demographic+"' " +
                "and p.lab_no = m.message_id " +
                "and pi.message_id = m.message_id " +
                "and r.pid_id = pi.pid_id " +
                "and r.obr_id = x.obr_id and p.lab_type='BCP'";
        
        logger.info(sql);
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                String testNam = rs.getString("observation_identifier").substring(1+rs.getString("observation_identifier").indexOf('^'));
                String labType = rs.getString("lab_type");
                String title = "";//TODO:rs.getString("title");
                Hashtable h = new Hashtable();
                h.put("testName", testNam);
                h.put("labType",labType);
                h.put("title",title);
                labList.add(h);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
            
        }
        
        return labList;
    }
    public ArrayList findUniqueLabsForPatientHL7Text(String demographic){
        ArrayList labList = new ArrayList();
        String sql = "SELECT lab_no FROM patientLabRouting WHERE demographic_no='"+demographic+"' AND lab_type='HL7'";
        logger.info(sql);
        Factory f = new Factory();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                MessageHandler h = f.getHandler(rs.getString("lab_no"));
                for (int i=0; i < h.getOBRCount(); i++){
                    for (int j=0; j < h.getOBXCount(i); j++){
                        
                        String status = h.getOBXResultStatus(i, j);
                        if (status.equals("DNS") || status.equals("P") || status.equals("Pending"))
                            continue;
                        
                        Hashtable t = new Hashtable();
                        t.put("testName",h.getOBXName(i, j));
                        t.put("labType","HL7");
                        t.put("title", "");//TODO... not sure what title should be
                        t.put("identCode",h.getOBXIdentifier(i, j));
                        if (!labList.contains(t))
                            labList.add(t);
                        
                    }
                }
            }
            
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
            
        }
        
        return labList;
    }
    
    /**
     *  Returns hashtables with the following characteristics
     *  //first field is lab_no,
     *  //second field is result
     *  //third field is observation date
     */
    public ArrayList findValuesByLoinc(String demographicNo, String loincCode, Connection conn){
        ArrayList labList = new ArrayList();
        
        String sql = "SELECT dataField, dateObserved, e1.val AS lab_no, e3.val AS abnormal FROM measurements m " +
                "JOIN measurementsExt e1 ON m.id = e1.measurement_id AND e1.keyval='lab_no' " +
                "JOIN measurementsExt e2 ON m.id = e2.measurement_id AND e2.keyval='identifier' " +
                "JOIN measurementsExt e3 ON m.id = e3.measurement_id AND e3.keyval='abnormal', measurementMap " +
                "WHERE e2.val = ident_code AND LOINC_CODE='"+loincCode+"' AND demographicNo='"+demographicNo+"' " +
                "ORDER BY dateObserved DESC";
        try {
            //DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //ResultSet rs = db.GetSQL(sql);
            ResultSet rs = pstmt.executeQuery();
            
            while(rs.next()){
                Hashtable h = new Hashtable();
                h.put("lab_no", rs.getString("lab_no"));
                h.put("result", rs.getString("dataField"));
                h.put("date", rs.getString("dateObserved"));
                h.put("abn", rs.getString("abnormal"));
                labList.add(h);
            }
            rs.close();
            pstmt.close();
            //db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesByLoinc()", e);
            
        }
        
        return labList;
    }
    
    /**Returns hashtable with the following characteristics
     * //first field is testName,
     * //second field is abn : abnormal or normal, A or N
     * //third field is result
     * //fourth field is unit
     * //fifth field is range
     * //sixth field is date : collection Date
     */
    public ArrayList findValuesForTest(String labType, String demographicNo, String testName){
        return findValuesForTest(labType, demographicNo, testName, "NULL");
    }
    
    public ArrayList findValuesForTest(String labType, String demographicNo, String testName, String identCode){
        HashMap accessionMap = new HashMap();
        LinkedHashMap labMap = new LinkedHashMap();
        ArrayList labList = new ArrayList();
        if (identCode != null)
            identCode = identCode.replace("_amp_", "&");
        if ( labType != null && labType.equals("CML")){
            String sql = "select p.lab_no , p.lab_type, ltr.title, ltr.test_name, ltr.result,ltr.abn, ltr.minimum, ltr.maximum, ltr.units, lpp.collection_date, lpp.accession_num " +
                    "from patientLabRouting p , labTestResults ltr, labPatientPhysicianInfo lpp " +
                    " where p.lab_type = 'CML' " +
                    " and p.demographic_no = '"+demographicNo+"' " +
                    " and p.lab_no = ltr.labPatientPhysicianInfo_id " +
                    " and ltr.test_name = '"+testName+"' " +
                    " and ltr.labPatientPhysicianInfo_id = lpp.id order by lpp.collection_date";
            
            logger.info(sql);
            
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);
                while(rs.next()){
                    String testNam = rs.getString("test_name");
                    String abn = rs.getString("abn");
                    String result = rs.getString("result");
                    String range = getReferenceRange(rs.getString("minimum"),rs.getString("maximum"));
                    String units = rs.getString("units");
                    String collDate = rs.getString("collection_date");
                    String lab_no = rs.getString("lab_no");
                    String accessionNum = rs.getString("accession_num");
                    
                    Date dateA = (Date) accessionMap.get(accessionNum);
                    Date dateB = UtilDateUtilities.getDateFromString(collDate, "dd-MMM-yy");
                    if (dateA == null || dateA.before(dateB)){
                        int monthsBetween = 0;
                        if (dateA != null){
                            monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                        }
                        if (monthsBetween < 4){
                            accessionMap.put(accessionNum, dateB);
                            Hashtable h = new Hashtable();
                            h.put("testName", testNam);
                            h.put("abn",abn);
                            h.put("result",result);
                            h.put("range",range);
                            h.put("units",units);
                            h.put("lab_no", lab_no);
                            h.put("collDate",collDate);
                            h.put("collDateDate",dateB);
                            //labList.add(h);
                            labMap.put(accessionNum, h);
                        }
                    }
                }
                rs.close();
                db.CloseConn();
                
                labList.addAll(labMap.values());
                
            }catch(Exception e){
                logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
                
            }
        } else if ( labType != null && labType.equals("MDS")){
            //String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";
            String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where p.lab_type = 'MDS' and p.demographic_no = '"+demographicNo+"' and observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID  and m.segmentID = p.lab_no order by dateTime";
            logger.info(sql);
            
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);
                while(rs.next()){
                    
                    String testNam = rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));  //reportname or observationIden
                    
                    String abn = rs.getString("abnormalFlags");            //abnormalFlags from mdsOBX
                    String result = rs.getString("observationValue");     //mdsOBX observationValue
                    String segId = rs.getString("segmentID");
                    String range = "";
                    String units = "";
                    String collDate = rs.getString("dateTime"); //mdsOBX dateTime
                    String messageConID = rs.getString("messageConID");
                    String accessionNum = messageConID.substring(0, messageConID.lastIndexOf("-"));
                    String version = messageConID.substring(messageConID.lastIndexOf("-")+1);
                    String status = rs.getString("observationResultStatus");
                    
                    // Skip the result if it is not supposed to be displayed
                    if (status.equals("I") || status.equals("W") || status.equals("X") || status.equals("D"))
                        continue;
                    
                    // Only retieve the latest measurement for each accessionNum
                    Hashtable ht = (Hashtable) accessionMap.get(accessionNum);
                    if (ht == null || Integer.parseInt((String) ht.get("mapNum")) < Integer.parseInt(version)){
                        
                        int monthsBetween = 0;
                        if (ht != null){
                            Date dateA = UtilDateUtilities.StringToDate((String) ht.get("date"), "yyyyMMdd");
                            Date dateB = UtilDateUtilities.StringToDate(collDate, "yyyyMMdd");
                            if (dateA.before(dateB)){
                                monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                            }else{
                                monthsBetween = UtilDateUtilities.getNumMonths(dateB, dateA);
                            }
                        }
                        if (monthsBetween < 4){
                            ht = new Hashtable();
                            ht.put("date", collDate);
                            ht.put("mapNum", version);
                            accessionMap.put(accessionNum, ht);
                            String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and reportName = '"+testNam+"'";
                            ResultSet rs2 = db.GetSQL(sql2);
                            
                            if(rs2.next()){
                                range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                                units = rs2.getString("units"); //mdsZMN units
                            }
                            rs2.close();
                            Hashtable h = new Hashtable();
                            h.put("testName", testNam);
                            h.put("abn",abn);
                            h.put("result",result);
                            h.put("range",range);
                            h.put("units",units);
                            h.put("lab_no", segId);
                            h.put("collDate",collDate);
                            h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
                            labMap.put(accessionNum, h);
                        }
                    }
                    
                }
                rs.close();
                db.CloseConn();
                
                labList.addAll(labMap.values());
                
            }catch(Exception e){
                logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
                
            }
        }else if ( labType != null && labType.equals("BCP")){
            String sql = "select * from patientLabRouting p, hl7_msh m ,hl7_pid pi, hl7_obr r,hl7_obx x, hl7_orc c  where p.lab_type = 'BCP' and p.demographic_no = '"+demographicNo+"' and x.observation_identifier like '%^"+testName+"' and p.lab_no = m.message_id and pi.message_id = m.message_id and r.pid_id = pi.pid_id and c.pid_id = pi.pid_id and r.obr_id = x.obr_id order by r.observation_date_time";
            logger.info(sql);
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);
                while(rs.next()){
                    // |   |  |
                    String testNam = rs.getString("observation_identifier").substring(rs.getString("observation_identifier").indexOf('^')+1);
                    
                    String abn = rs.getString("abnormal_flags");            //abnormalFlags from mdsOBX
                    String result = rs.getString("observation_results");     //mdsOBX observationValue
                    String segId = rs.getString("lab_no");
                    String range = rs.getString("reference_range");
                    String units = rs.getString("units");
                    String collDate = rs.getString("observation_date_time");
                    String accessionNum = rs.getString("filler_order_number");
                    
                    // get just the accession number
                    String[] ss = accessionNum.split("-");
                    if (ss.length == 3)
                        accessionNum =  ss[0];
                    else
                        accessionNum =  ss[1];
                    
                    Date dateA = (Date) accessionMap.get(accessionNum);
                    Date dateB = UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss");
                    if (dateA == null || dateA.before(dateB)){
                        int monthsBetween = 0;
                        if (dateA != null){
                            monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                        }
                        if (monthsBetween < 4){
                            accessionMap.put(accessionNum, dateB);
                            Hashtable h = new Hashtable();
                            h.put("testName", testNam);
                            h.put("abn",abn);
                            h.put("result",result);
                            h.put("range",range);
                            h.put("units",units);
                            h.put("lab_no", segId);
                            h.put("collDate",collDate);
                            h.put("collDateDate",dateB);
                            //labList.add(h);
                            labMap.put(accessionNum, h);
                        }
                    }
                }
                rs.close();
                db.CloseConn();
                
                labList.addAll(labMap.values());
                
            }catch(Exception e){
                logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
                
            }
            
        }else if ( labType != null && labType.equals("HL7")){
            String sql = "SELECT DISTINCT e2.val AS lab_no FROM measurements m" +
                    " JOIN measurementsExt e1 ON m.id=e1.measurement_id AND e1.keyval='identifier'" +
                    " JOIN measurementsExt e2 ON m.id=e2.measurement_id AND e2.keyval='lab_no'" +
                    " WHERE e1.val='"+identCode+"' AND m.demographicNo='"+demographicNo+"'";
            logger.info(sql);
            try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);
                while(rs.next()){
                    String lab_no = rs.getString("lab_no");
                    
                    Factory f = new Factory();
                    MessageHandler handler = f.getHandler(lab_no);
                    
                    Hashtable h = new Hashtable();
                    int i=0;
                    while ( i < handler.getOBRCount() && h.get("testName") == null){
                        for (int j=0; j < handler.getOBXCount(i); j++){
                            if (handler.getOBXIdentifier(i, j).equals(identCode)){
                                
                                String result = handler.getOBXResult(i, j);
                                
                                // only add measurements with actual results
                                if (!result.equals("")){
                                    h.put("testName", testName);
                                    h.put("abn",handler.getOBXAbnormalFlag(i, j));
                                    h.put("result",result);
                                    h.put("range",handler.getOBXReferenceRange(i, j));
                                    h.put("units",handler.getOBXUnits(i, j));
                                    String collDate = handler.getTimeStamp(i, j);
                                    h.put("lab_no",lab_no);
                                    h.put("collDate",collDate);
                                    h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
                                    labList.add(h);
                                    break;
                                }
                                
                            }
                        }
                        i++;
                    }
                }
                rs.close();
                db.CloseConn();
            }catch(Exception e){
                logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
                
            }
        }
        
        return labList;
    }
    
    /**Returns hashtable with the following characteristics
     * //first field is testName,
     * //second field is abn : abnormal or normal, A or N
     * //third field is result
     * //fourth field is unit
     * //fifth field is range
     * //sixth field is date : collection Date
     */
    public ArrayList findValuesForDemographic(String demographicNo){
        ArrayList labList = new ArrayList();
        
        String sql = "select p.lab_no , p.lab_type, ltr.title, ltr.test_name, ltr.result,ltr.abn, ltr.minimum, ltr.maximum, ltr.units, ltr.location_id, ltr.description, lpp.collection_date " +
                "from patientLabRouting p , labTestResults ltr, labPatientPhysicianInfo lpp " +
                " where p.lab_type = 'CML' " +
                " and p.demographic_no = '"+demographicNo+"' " +
                " and p.lab_no = ltr.labPatientPhysicianInfo_id " +
                " and ltr.labPatientPhysicianInfo_id = lpp.id and test_name != \"\" ";
        
        logger.info(sql);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                String testNam = rs.getString("test_name")==null ? "" : rs.getString("test_name");
                String abn = rs.getString("abn")==null ? "" : rs.getString("abn");
                String result = rs.getString("result")==null ? "" : rs.getString("result");
                String range = getReferenceRange(rs.getString("minimum"),rs.getString("maximum"));
                String units = rs.getString("units")==null ? "" : rs.getString("units");
		String location = rs.getString("location_id")==null ? "" : rs.getString("location_id");
		String description = rs.getString("description")==null ? "" : rs.getString("description");
		
                String collDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rs.getString("collection_date"),"dd-MMM-yy"),"yyyy-MM-dd");
                logger.info("This went in "+rs.getString("collection_date")+" this came out "+UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rs.getString("collection_date"),"dd-MMM-yy"),"yyyy-MM-dd"));
		
		Hashtable h = new Hashtable();
                h.put("testName", testNam);
                h.put("abn",abn);
                h.put("result",result);
                h.put("range",range);
                h.put("units",units);
		h.put("location",location);
		h.put("description",description);
                h.put("collDate",collDate);
                labList.add(h);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
        }
        
        
        sql = null;
        sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p  where p.lab_type = 'MDS' and x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";
        logger.info(sql);
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            while(rs.next()){
                
                //String testNam = rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));  //reportname or observationIden
                
                String obserIden = rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'));  //reportname or observationIden
                int first = rs.getString("observationIden").indexOf('^');
                int second = rs.getString("observationIden").substring(first+1).indexOf('^');
                String testNam = rs.getString("observationIden").substring(first+1,second+first+1);
                
                String abn = rs.getString("abnormalFlags");            //abnormalFlags from mdsOBX
                String result = rs.getString("observationValue");     //mdsOBX observationValue
                String segId = rs.getString("segmentID");
                String range = "";
                String units = "";
                //String collDate = rs.getString("dateTime");
                
                String collDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(rs.getString("dateTime"),"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");
                
                //<LabResults testDate="2004-11-17 16:26:18
                
                String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and resultMnemonic = '"+obserIden+"'";
                
                logger.info(sql2);
                ResultSet rs2 = db.GetSQL(sql2);
                
                if(rs2.next()){
                    range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                    units = rs2.getString("units"); //mdsZMN units
                    //collDate = rs2.getString("dateTime"); //mdsOBX dateTime
                }
                rs2.close();
                Hashtable h = new Hashtable();
                h.put("testName", testNam);
                h.put("abn",abn);
                h.put("result",result);
                h.put("range",range);
                h.put("units",units);
                h.put("collDate",collDate);
                labList.add(h);
            }
            rs.close();
            db.CloseConn();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
        }
        
        return labList;
    }
    
    
}
