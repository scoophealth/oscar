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


package oscar.oscarLab.ca.on;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author Jay Gallagher
 */
public class CommonLabTestValues {

    private static Logger logger = MiscUtils.getLogger();

    private CommonLabTestValues() {
    	// prevent instantiation
    }

    public static String getReferenceRange(String minimum,String maximum){
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




    public static ArrayList<HashMap<String, Serializable>> findUniqueLabsForPatient(String demographic){
        OscarProperties op = OscarProperties.getInstance();
        String cml = op.getProperty("CML_LABS");
        String mds = op.getProperty("MDS_LABS");
        String pathnet = op.getProperty("PATHNET_LABS");
        String hl7text = op.getProperty("HL7TEXT_LABS");
        ArrayList<HashMap<String, Serializable>> labs = new ArrayList<HashMap<String, Serializable>>();
        if( cml != null && cml.trim().equals("yes")){
            ArrayList<HashMap<String, Serializable>> cmlLabs = findUniqueLabsForPatientCML(demographic);
            labs.addAll(cmlLabs);
        }
        if (mds != null && mds.trim().equals("yes")){
            ArrayList<HashMap<String, Serializable>> mdsLabs = findUniqueLabsForPatientMDS(demographic);
            labs.addAll(mdsLabs);
        }
        if (pathnet != null && pathnet.trim().equals("yes")){
            ArrayList<HashMap<String, Serializable>> pathLabs = findUniqueLabsForPatientExcelleris(demographic);
            labs.addAll(pathLabs);
        }
        if (hl7text != null && hl7text.trim().equals("yes")){
            ArrayList<HashMap<String, Serializable>> hl7Labs = findUniqueLabsForPatientHL7Text(demographic);
            labs.addAll(hl7Labs);
        }
        return labs;
    }

    //Method returns unique test names for a patient
    //List is used to compile a cummalitive lab profile
    //HashMap return in list
    //"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE
    //"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
    //"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
    public static ArrayList<HashMap<String, Serializable>> findUniqueLabsForPatientCML(String demographic){
        //Need to check which labs are active
        ArrayList<HashMap<String, Serializable>> labList = new ArrayList<HashMap<String, Serializable>>();
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

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                String testNam = oscar.Misc.getString(rs, "test_name");
                String labType = oscar.Misc.getString(rs, "lab_type");
                String title = oscar.Misc.getString(rs, "title");
                HashMap<String, Serializable> h = new HashMap<String, Serializable>();
                h.put("testName", testNam);
                h.put("labType",labType);
                h.put("title",title);
                labList.add(h);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
        }

        return labList;
    }

    //Method returns unique test names for a patient
    //List is used to compile a cummalitive lab profile
    //HashMap return in list
    //"testName" : Name of test eg. CHOL/HDL RATIO, CHOLESTEROL, CREATININE
    //"labType" : Vendor of lab eg. MDS, CML, BCP(Excelleris)
    //"title" : Heading of lab group eg. CHEMISTRY, HEMATOLOGY
    public static ArrayList<HashMap<String, Serializable>> findUniqueLabsForPatientMDS(String demographic){
        //Need to check which labs are active
        ArrayList<HashMap<String, Serializable>> labList = new ArrayList<HashMap<String, Serializable>>();
        String sql = "select distinct p.lab_type, x.observationIden, x.observationResultStatus " +
                "from mdsOBX x, mdsMSH m, patientLabRouting p " +
                " where p.demographic_no = '"+demographic+"' " +
                "and m.segmentID = p.lab_no " +
                "and x.segmentID = m.segmentID  and p.lab_type = 'MDS' ";

        logger.info(sql);

        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){

                String status = oscar.Misc.getString(rs, "observationResultStatus");
                if (status.equals("D") || status.equals("I") || status.equals("X") || status.equals("W"))
                    continue;

                String testNam = "Unknown";oscar.Misc.getString(rs, "observationIden").substring(1,oscar.Misc.getString(rs, "observationIden").indexOf('^'));
                String labType = oscar.Misc.getString(rs, "lab_type");
                String title = "";//TODO:oscar.Misc.getString(rs,"title");

                try{
                    String obserIden = oscar.Misc.getString(rs, "observationIden");//.substring(oscar.Misc.getString(rs,"observationIden").indexOf('^'),oscar.Misc.getString(rs,"observationIden").indexOf('^',oscar.Misc.getString(rs,"observationIden").indexOf('^')));  //reportname or observationIden
                    int first = oscar.Misc.getString(rs, "observationIden").indexOf('^');
                    int second = oscar.Misc.getString(rs, "observationIden").indexOf('^',first+1);
                    testNam = oscar.Misc.getString(rs, "observationIden").substring(first+1,second);
                }catch(Exception e){
                    logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
                }


                HashMap<String, Serializable> h = new HashMap<String, Serializable>();
                h.put("testName", testNam);
                h.put("labType",labType);
                h.put("title",title);
                labList.add(h);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);

        }

        return labList;
    }
    public static ArrayList<HashMap<String, Serializable>> findUniqueLabsForPatientExcelleris(String demographic){
        ArrayList<HashMap<String, Serializable>> labList = new ArrayList<HashMap<String, Serializable>>();
        String sql = "select distinct p.lab_type,x.observation_identifier "+
                "from patientLabRouting p, hl7_msh m ,hl7_pid pi, hl7_obr r,hl7_obx x  " +
                "where p.demographic_no = '"+demographic+"' " +
                "and p.lab_no = m.message_id " +
                "and pi.message_id = m.message_id " +
                "and r.pid_id = pi.pid_id " +
                "and r.obr_id = x.obr_id and p.lab_type='BCP'";

        logger.info(sql);

        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                String testNam = oscar.Misc.getString(rs, "observation_identifier").substring(1+oscar.Misc.getString(rs, "observation_identifier").indexOf('^'));
                String labType = oscar.Misc.getString(rs, "lab_type");
                String title = "";//TODO:oscar.Misc.getString(rs,"title");
                HashMap<String, Serializable> h = new HashMap<String, Serializable>();
                h.put("testName", testNam);
                h.put("labType",labType);
                h.put("title",title);
                labList.add(h);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);

        }

        return labList;
    }

    public static ArrayList<HashMap<String, Serializable>> findUniqueLabsForPatientHL7Text(String demographic){
        ArrayList<HashMap<String, Serializable>> labList = new ArrayList<HashMap<String, Serializable>>();
        String sql = "SELECT lab_no FROM patientLabRouting WHERE demographic_no='"+demographic+"' AND lab_type='HL7'";
        logger.info(sql);
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                MessageHandler h = Factory.getHandler(oscar.Misc.getString(rs, "lab_no"));
                for (int i=0; i < h.getOBRCount(); i++){
                    for (int j=0; j < h.getOBXCount(i); j++){

                        String status = h.getOBXResultStatus(i, j);
                        if (status.equals("DNS") || status.equals("P") || status.equals("Pending"))
                            continue;

                        HashMap<String, Serializable> t = new HashMap<String, Serializable>();
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
    public static ArrayList<HashMap<String, Serializable>> findValuesByLoinc(String demographicNo, String loincCode){
        ArrayList<HashMap<String, Serializable>> labList = new ArrayList<HashMap<String, Serializable>>();

        String sql = "SELECT dataField, dateObserved, e1.val AS lab_no, e3.val AS abnormal FROM measurements m " +
                "JOIN measurementsExt e1 ON m.id = e1.measurement_id AND e1.keyval='lab_no' " +
                "JOIN measurementsExt e2 ON m.id = e2.measurement_id AND e2.keyval='identifier' " +
                "JOIN measurementsExt e3 ON m.id = e3.measurement_id AND e3.keyval='abnormal', measurementMap " +
                "WHERE e2.val = ident_code AND LOINC_CODE='"+loincCode+"' AND demographicNo='"+demographicNo+"' " +
                "ORDER BY dateObserved DESC";
        try {
            Connection conn=DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
            	HashMap<String, Serializable> h = new HashMap<String, Serializable>();
                h.put("lab_no", oscar.Misc.getString(rs,"lab_no"));
                h.put("result", oscar.Misc.getString(rs,"dataField"));
                h.put("date", oscar.Misc.getString(rs,"dateObserved"));
                h.put("abn", oscar.Misc.getString(rs,"abnormal"));
                h.put("collDateDate",rs.getDate("dateObserved"));
                labList.add(h);
            }
            rs.close();
            pstmt.close();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesByLoinc()", e);

        }

        return labList;
    }


    //WITH MORE DATA MERGE WITH 1 AFTER April 09
     public static ArrayList<Map<String, Serializable>> findValuesByLoinc2(String demographicNo, String loincCode, Connection conn){
    	 ArrayList<Map<String, Serializable>> labList = new ArrayList<Map<String, Serializable>>();

        String sql = "SELECT dataField, dateObserved, e1.val AS lab_no, e3.val AS abnormal, e4.val as units FROM measurements m " +
                "JOIN measurementsExt e1 ON m.id = e1.measurement_id AND e1.keyval='lab_no' " +
                "JOIN measurementsExt e2 ON m.id = e2.measurement_id AND e2.keyval='identifier' " +
                "JOIN measurementsExt e4 ON m.id = e4.measurement_id AND e4.keyval='identifier' " +
                "JOIN measurementsExt e3 ON m.id = e3.measurement_id AND e3.keyval='abnormal', measurementMap " +
                "WHERE e2.val = ident_code AND LOINC_CODE='"+loincCode+"' AND demographicNo='"+demographicNo+"' " +
                "ORDER BY dateObserved DESC";
        try {
            //
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //ResultSet rs = DBHandler.GetSQL(sql);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
            	HashMap<String, Serializable> h = new HashMap<String, Serializable>();
                h.put("lab_no", oscar.Misc.getString(rs,"lab_no"));
                h.put("result", oscar.Misc.getString(rs,"dataField"));
                h.put("date", oscar.Misc.getString(rs,"dateObserved"));
                h.put("abn", oscar.Misc.getString(rs,"abnormal"));
                h.put("collDateDate",rs.getDate("dateObserved"));
                h.put("units",oscar.Misc.getString(rs,"units"));
                labList.add(h);
            }
            rs.close();
            pstmt.close();
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
    public static ArrayList<Map<String, Serializable>> findValuesForTest(String labType, String demographicNo, String testName){
        return findValuesForTest(labType, demographicNo, testName, "NULL");
    }

    public static ArrayList<Map<String, Serializable>> findValuesForTest(String labType, String demographicNo, String testName, String identCode){
        HashMap<String, Serializable> accessionMap = new HashMap<String, Serializable>();
        LinkedHashMap<String,Map<String, Serializable>> labMap = new LinkedHashMap<String,Map<String, Serializable>>();
        ArrayList<Map<String, Serializable>> labList = new ArrayList<Map<String, Serializable>>();
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

                ResultSet rs = DBHandler.GetSQL(sql);
                while(rs.next()){
                    String testNam = oscar.Misc.getString(rs, "test_name");
                    String abn = oscar.Misc.getString(rs, "abn");
                    String result = oscar.Misc.getString(rs, "result");
                    String range = getReferenceRange(oscar.Misc.getString(rs, "minimum"),oscar.Misc.getString(rs, "maximum"));
                    String units = oscar.Misc.getString(rs, "units");
                    String collDate = oscar.Misc.getString(rs, "collection_date");
                    String lab_no = oscar.Misc.getString(rs, "lab_no");
                    String accessionNum = oscar.Misc.getString(rs, "accession_num");

                    Date dateA = (Date) accessionMap.get(accessionNum);
                    Date dateB = UtilDateUtilities.getDateFromString(collDate, "dd-MMM-yy");
                    if (dateA == null || dateA.before(dateB)){
                        int monthsBetween = 0;
                        if (dateA != null){
                            monthsBetween = UtilDateUtilities.getNumMonths(dateA, dateB);
                        }
                        if (monthsBetween < 4){
                            accessionMap.put(accessionNum, dateB);
                            HashMap<String, Serializable> h = new HashMap<String, Serializable>();
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
                labList.addAll(labMap.values());

            }catch(Exception e){
                logger.error("exception in CommonLabTestValues.findValuesForTest()", e);

            }
        } else if ( labType != null && labType.equals("MDS")){
            //String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";
            String sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p where p.lab_type = 'MDS' and p.demographic_no = '"+demographicNo+"' and observationIden like '%^"+testName+"%' and  x.segmentID = m.segmentID  and m.segmentID = p.lab_no order by dateTime";
            logger.info(sql);

            try {

                ResultSet rs = DBHandler.GetSQL(sql);
                while(rs.next()){

                    String testNam = oscar.Misc.getString(rs, "observationIden").substring(1,oscar.Misc.getString(rs, "observationIden").indexOf('^'));  //reportname or observationIden

                    String abn = oscar.Misc.getString(rs, "abnormalFlags");            //abnormalFlags from mdsOBX
                    String result = oscar.Misc.getString(rs, "observationValue");     //mdsOBX observationValue
                    String segId = oscar.Misc.getString(rs, "segmentID");
                    String range = "";
                    String units = "";
                    String collDate = oscar.Misc.getString(rs, "dateTime"); //mdsOBX dateTime
                    String messageConID = oscar.Misc.getString(rs, "messageConID");
                    String accessionNum = messageConID.substring(0, messageConID.lastIndexOf("-"));
                    String version = messageConID.substring(messageConID.lastIndexOf("-")+1);
                    String status = oscar.Misc.getString(rs, "observationResultStatus");

                    // Skip the result if it is not supposed to be displayed
                    if (status.equals("I") || status.equals("W") || status.equals("X") || status.equals("D"))
                        continue;

                    // Only retieve the latest measurement for each accessionNum
                    HashMap<String, Serializable> ht = (HashMap<String, Serializable>) accessionMap.get(accessionNum);
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
                            ht = new HashMap<String, Serializable>();
                            ht.put("date", collDate);
                            ht.put("mapNum", version);
                            accessionMap.put(accessionNum, ht);
                            String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and reportName = '"+testNam+"'";
                            ResultSet rs2 = DBHandler.GetSQL(sql2);

                            if(rs2.next()){
                                range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                                units = rs2.getString("units"); //mdsZMN units
                            }
                            rs2.close();
                            HashMap<String, Serializable> h = new HashMap<String, Serializable>();
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
                labList.addAll(labMap.values());

            }catch(Exception e){
                logger.error("exception in CommonLabTestValues.findValuesForTest()", e);

            }
        }else if ( labType != null && labType.equals("BCP")){
            String sql = "select * from patientLabRouting p, hl7_msh m ,hl7_pid pi, hl7_obr r,hl7_obx x, hl7_orc c  where p.lab_type = 'BCP' and p.demographic_no = '"+demographicNo+"' and x.observation_identifier like '%^"+testName+"' and p.lab_no = m.message_id and pi.message_id = m.message_id and r.pid_id = pi.pid_id and c.pid_id = pi.pid_id and r.obr_id = x.obr_id order by r.observation_date_time";
            logger.info(sql);
            try {

                ResultSet rs = DBHandler.GetSQL(sql);
                while(rs.next()){
                    // |   |  |
                    String testNam = oscar.Misc.getString(rs, "observation_identifier").substring(oscar.Misc.getString(rs, "observation_identifier").indexOf('^')+1);

                    String abn = oscar.Misc.getString(rs, "abnormal_flags");            //abnormalFlags from mdsOBX
                    String result = oscar.Misc.getString(rs, "observation_results");     //mdsOBX observationValue
                    String segId = oscar.Misc.getString(rs, "lab_no");
                    String range = oscar.Misc.getString(rs, "reference_range");
                    String units = oscar.Misc.getString(rs, "units");
                    String collDate = oscar.Misc.getString(rs, "observation_date_time");
                    String accessionNum = oscar.Misc.getString(rs, "filler_order_number");

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
                            HashMap<String, Serializable> h = new HashMap<String, Serializable>();
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

                ResultSet rs = DBHandler.GetSQL(sql);
                while(rs.next()){
                    String lab_no = oscar.Misc.getString(rs, "lab_no");

                    MessageHandler handler = Factory.getHandler(lab_no);

                    HashMap<String, Serializable> h = new HashMap<String, Serializable>();
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
                                    MiscUtils.getLogger().debug("COLLDATE "+collDate);
                                    if (collDate.length() == 10){
                                       h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd"));
                                    }else{
                                       h.put("collDateDate",UtilDateUtilities.getDateFromString(collDate, "yyyy-MM-dd HH:mm:ss"));
                                    }
                                    labList.add(h);
                                    break;
                                }

                            }
                        }
                        i++;
                    }
                }
                rs.close();
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
    public static ArrayList<HashMap<String, Serializable>> findValuesForDemographic(String demographicNo){
        ArrayList<HashMap<String, Serializable>> labList = new ArrayList<HashMap<String, Serializable>>();

        String sql = "select p.lab_no , p.lab_type, ltr.id, ltr.test_name, ltr.result,ltr.abn, ltr.minimum, ltr.maximum, ltr.units, ltr.location_id, ltr.description, lpp.accession_num, lpp.collection_date " +
                "from patientLabRouting p , labTestResults ltr, labPatientPhysicianInfo lpp " +
                " where p.lab_type = 'CML' " +
                " and p.demographic_no = '"+demographicNo+"' " +
                " and p.lab_no = ltr.labPatientPhysicianInfo_id " +
                " and ltr.labPatientPhysicianInfo_id = lpp.id and test_name != ''";

        logger.info(sql);
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){
		Integer id = rs.getInt("id");
                String testNam = oscar.Misc.getString(rs, "test_name")==null ? "" : oscar.Misc.getString(rs, "test_name");
                String abn = oscar.Misc.getString(rs, "abn")==null ? "" : oscar.Misc.getString(rs, "abn");
                String result = oscar.Misc.getString(rs, "result")==null ? "" : oscar.Misc.getString(rs, "result");
                String range = getReferenceRange(oscar.Misc.getString(rs, "minimum"),oscar.Misc.getString(rs, "maximum"));
		String min = oscar.Misc.getString(rs, "minimum");
		String max = oscar.Misc.getString(rs, "maximum");
                String units = oscar.Misc.getString(rs, "units")==null ? "" : oscar.Misc.getString(rs, "units");
		String location = oscar.Misc.getString(rs, "location_id")==null ? "" : oscar.Misc.getString(rs, "location_id");
		String description = oscar.Misc.getString(rs, "description")==null ? "" : oscar.Misc.getString(rs, "description");
		String accession = oscar.Misc.getString(rs, "accession_num")==null ? "" : oscar.Misc.getString(rs, "accession_num");

                String collDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "collection_date"),"dd-MMM-yy"),"yyyy-MM-dd");
                logger.info("This went in "+oscar.Misc.getString(rs, "collection_date")+" this came out "+UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "collection_date"),"dd-MMM-yy"),"yyyy-MM-dd"));

                HashMap<String, Serializable> h = new HashMap<String, Serializable>();
		h.put("id", id);
                h.put("testName", testNam);
                h.put("abn",abn);
                h.put("result",result);
                h.put("range",range);
		h.put("min",min);
		h.put("max",max);
                h.put("units",units);
		h.put("location",location);
		h.put("description",description);
		h.put("accession",accession);
                h.put("collDate",collDate);
                labList.add(h);
            }
            rs.close();
        }catch(Exception e){

            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
        }


        sql = null;
        sql = "select *   from mdsOBX x, mdsMSH m, patientLabRouting p  where p.lab_type = 'MDS' and x.segmentID = m.segmentID and p.demographic_no = '"+demographicNo+"' and m.segmentID = p.lab_no";
        logger.info(sql);

        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){

                //String testNam = oscar.Misc.getString(rs,"observationIden").substring(1,oscar.Misc.getString(rs,"observationIden").indexOf('^'));  //reportname or observationIden

                String obserIden = oscar.Misc.getString(rs, "observationIden").substring(1,oscar.Misc.getString(rs, "observationIden").indexOf('^'));  //reportname or observationIden
                int first = oscar.Misc.getString(rs, "observationIden").indexOf('^');
                int second = oscar.Misc.getString(rs, "observationIden").substring(first+1).indexOf('^');
                String testNam = oscar.Misc.getString(rs, "observationIden").substring(first+1,second+first+1);

                String abn = oscar.Misc.getString(rs, "abnormalFlags");            //abnormalFlags from mdsOBX
                String result = oscar.Misc.getString(rs, "observationValue");     //mdsOBX observationValue
                String segId = oscar.Misc.getString(rs, "segmentID");
                String range = "";
                String units = "";
                //String collDate = oscar.Misc.getString(rs,"dateTime");

                String collDate = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(oscar.Misc.getString(rs, "dateTime"),"yyyy-MM-dd hh:mm:ss"),"yyyy-MM-dd");

                //<LabResults testDate="2004-11-17 16:26:18

                String sql2 = "select * from mdsZMN where segmentID = '"+segId+"' and resultMnemonic = '"+obserIden+"'";

                logger.info(sql2);
                ResultSet rs2 = DBHandler.GetSQL(sql2);

                if(rs2.next()){
                    range = rs2.getString("referenceRange");  // mdsZMN referenceRange
                    units = rs2.getString("units"); //mdsZMN units
                    //collDate = rs2.getString("dateTime"); //mdsOBX dateTime
                }
                rs2.close();
                HashMap<String, Serializable> h = new HashMap<String, Serializable>();
                h.put("testName", testNam);
                h.put("abn",abn);
                h.put("result",result);
                h.put("range",range);
                h.put("units",units);
                h.put("collDate",collDate);
                labList.add(h);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CommonLabTestValues.findValuesForTest()", e);
        }

        return labList;
    }
}
