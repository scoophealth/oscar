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
 *  Ontario, Canada   Creates a new instance of CommonLabResultData
 *
 *
 *
 * CommonLabResultData.java
 *
 * Created on April 21, 2005, 4:25 PM
 */

package oscar.oscarLab.ca.on;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DocumentResultsDao;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDB.ArchiveDeletedRecords;
import oscar.oscarDB.DBHandler;
import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.upload.ProviderLabRouting;
import oscar.oscarLab.ca.bc.PathNet.PathnetResultsData;
import oscar.oscarMDS.data.MDSResultsData;
import oscar.oscarMDS.data.ReportStatus;
/**
 *
 * @author Jay Gallagher
 */
public class CommonLabResultData {

    Logger logger = Logger.getLogger(CommonLabResultData.class);

    public static final boolean ATTACHED = true;
    public static final boolean UNATTACHED = false;

    public static final String NOT_ASSIGNED_PROVIDER_NO="0";

    public CommonLabResultData() {

    }

    public static String[] getLabTypes(){
        return new String[] {"MDS","CML","BCP","HL7","DOC"};
    }

    public ArrayList<LabResultData> populateLabResultsData(String demographicNo, String reqId, boolean attach) {
        ArrayList<LabResultData> labs = new ArrayList<LabResultData>();
        oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

        OscarProperties op = OscarProperties.getInstance();

        String cml = op.getProperty("CML_LABS");
        String mds = op.getProperty("MDS_LABS");
        String pathnet = op.getProperty("PATHNET_LABS");
        String hl7text = op.getProperty("HL7TEXT_LABS");


        if( cml != null && cml.trim().equals("yes")){
            ArrayList<LabResultData> cmlLabs = mDSData.populateCMLResultsData(demographicNo, reqId, attach);
            labs.addAll(cmlLabs);
        }
        if (mds != null && mds.trim().equals("yes")){
            ArrayList<LabResultData> mdsLabs = mDSData.populateMDSResultsData2(demographicNo, reqId, attach);
            labs.addAll(mdsLabs);
        }
        if (pathnet != null && pathnet.trim().equals("yes")){
            PathnetResultsData pathData = new PathnetResultsData();
            ArrayList<LabResultData> pathLabs = pathData.populatePathnetResultsData(demographicNo, reqId, attach);
            labs.addAll(pathLabs);
        }
        if (hl7text != null && hl7text.trim().equals("yes")){
            Hl7textResultsData hl7Data = new Hl7textResultsData();
            ArrayList<LabResultData> hl7Labs = hl7Data.populateHL7ResultsData(demographicNo, reqId, attach);
            labs.addAll(hl7Labs);
        }

        return labs;
    }

    public ArrayList populateLabResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        return populateLabResultsData( providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber,status, "I");
    }
    public ArrayList populateLabResultsDataInboxIndexPage(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status,String scannedDocStatus) {
        ArrayList labs = new ArrayList();
        oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

        OscarProperties op = OscarProperties.getInstance();

        String cml = op.getProperty("CML_LABS");
        String mds = op.getProperty("MDS_LABS");
        String pathnet = op.getProperty("PATHNET_LABS");
        String hl7text = op.getProperty("HL7TEXT_LABS");

        if(scannedDocStatus !=null && (scannedDocStatus.equals("N")  || scannedDocStatus.equals("I")  || scannedDocStatus.equals(""))){

            if( cml != null && cml.trim().equals("yes")){
                ArrayList cmlLabs = mDSData.populateCMLResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(cmlLabs);
            }
            if (mds != null && mds.trim().equals("yes")){
                ArrayList mdsLabs = mDSData.populateMDSResultsData2(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(mdsLabs);
            }
            if (pathnet != null && pathnet.trim().equals("yes")){
                PathnetResultsData pathData = new PathnetResultsData();
                ArrayList pathLabs = pathData.populatePathnetResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(pathLabs);
            }
            if (hl7text != null && hl7text.trim().equals("yes")){

                Hl7textResultsData hl7Data = new Hl7textResultsData();
                ArrayList hl7Labs = hl7Data.populateHl7ResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(hl7Labs);
            }
        }

        if(scannedDocStatus !=null && (scannedDocStatus.equals("O")  || scannedDocStatus.equals("I")  || scannedDocStatus.equals(""))){

            DocumentResultsDao documentResultsDao= (DocumentResultsDao) SpringUtils.getBean("documentResultsDao");
            ArrayList docs = documentResultsDao.populateDocumentResultsDataOfAllProviders(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
            labs.addAll(docs);
        }

        return labs;
    }
    public ArrayList populateLabResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status,String scannedDocStatus) {
        ArrayList labs = new ArrayList();
        oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();

        OscarProperties op = OscarProperties.getInstance();

        String cml = op.getProperty("CML_LABS");
        String mds = op.getProperty("MDS_LABS");
        String pathnet = op.getProperty("PATHNET_LABS");
        String hl7text = op.getProperty("HL7TEXT_LABS");

        if(scannedDocStatus !=null && (scannedDocStatus.equals("N")  || scannedDocStatus.equals("I")  || scannedDocStatus.equals(""))){

            if( cml != null && cml.trim().equals("yes")){
                ArrayList cmlLabs = mDSData.populateCMLResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(cmlLabs);
            }
            if (mds != null && mds.trim().equals("yes")){
                ArrayList mdsLabs = mDSData.populateMDSResultsData2(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(mdsLabs);
            }
            if (pathnet != null && pathnet.trim().equals("yes")){
                PathnetResultsData pathData = new PathnetResultsData();
                ArrayList pathLabs = pathData.populatePathnetResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(pathLabs);
            }
            if (hl7text != null && hl7text.trim().equals("yes")){

                Hl7textResultsData hl7Data = new Hl7textResultsData();
                ArrayList hl7Labs = hl7Data.populateHl7ResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
                labs.addAll(hl7Labs);
            }
        }

        if(scannedDocStatus !=null && (scannedDocStatus.equals("O")  || scannedDocStatus.equals("I")  || scannedDocStatus.equals(""))){          

            DocumentResultsDao documentResultsDao= (DocumentResultsDao) SpringUtils.getBean("documentResultsDao");
            ArrayList docs = documentResultsDao.populateDocumentResultsData(providerNo, demographicNo, patientFirstName, patientLastName, patientHealthNumber, status);
            labs.addAll(docs);
        }

        return labs;
    }

    public static boolean updateReportStatus(Properties props, int labNo, String providerNo, char status, String comment, String labType) {


        try {
            DBPreparedHandler db = new DBPreparedHandler();
            // handles the case where this provider/lab combination is not already in providerLabRouting table
            String sql = "select id, status from providerLabRouting where lab_type = '"+labType+"' and provider_no = '"+providerNo+"' and lab_no = '"+labNo+"'";

            ResultSet rs = db.queryResults(sql);

            if(rs.next()){  //

                String id = oscar.Misc.getString(rs, "id");
                sql = "update providerLabRouting set status='"+status+"', comment=? where id = '"+id+"'";
                if (!oscar.Misc.getString(rs, "status").equals("A")){

                    db.queryExecute(sql, new String[] { comment });

                }
            }else{

                sql = "insert ignore into providerLabRouting (provider_no, lab_no, status, comment,lab_type) values ('"+providerNo+"', '"+labNo+"', '"+status+"', ?,'"+labType+"')";
                db.queryExecute(sql, new String[] { comment });
            }

            if ( !"0".equals(providerNo)){
                String recordsToDeleteSql = "select * from providerLabRouting where provider_no='0' and lab_no='"+labNo+"' and lab_type = '"+labType+"'";
                sql = "delete from providerLabRouting where provider_no='0' and lab_no=? and lab_type = '"+labType+"'";
                ArchiveDeletedRecords adr = new ArchiveDeletedRecords();
                adr.recordRowsToBeDeleted(recordsToDeleteSql, ""+providerNo,"providerLabRouting");
                db.queryExecute(sql, new String[] { Integer.toString(labNo) });
            }
            return true;
        }catch(Exception e){
            Logger l = Logger.getLogger(CommonLabResultData.class);
            l.error("exception in MDSResultsData.updateReportStatus()",e);
            return false;
        }
    }




    public ArrayList getStatusArray(String labId, String labType){

        ArrayList statusArray = new ArrayList();

        String sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='"+labId+"' and providerLabRouting.lab_type = '"+labType+"'";
        try{
            
            ResultSet rs = DBHandler.GetSQL(sql);
            logger.info(sql);
            while(rs.next()){
                statusArray.add( new ReportStatus(oscar.Misc.getString(rs, "first_name")+" "+oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "provider_no"), descriptiveStatus(oscar.Misc.getString(rs, "status")), oscar.Misc.getString(rs, "comment"), oscar.Misc.getString(rs, "timestamp"), labId ) );
                //statusArray.add( new ReportStatus(oscar.Misc.getString(rs,"first_name")+" "+oscar.Misc.getString(rs,"last_name"), oscar.Misc.getString(rs,"provider_no"), descriptiveStatus(oscar.Misc.getString(rs,"status")), oscar.Misc.getString(rs,"comment"), rs.getTimestamp("timestamp").getTime(), labId ) );
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in CommonLabResultData.getStatusArray()",e);
        }
        return statusArray;
    }

    public String descriptiveStatus(String status) {
        switch (status.charAt(0)) {
            case 'A' : return "Acknowledged";
            case 'F' : return "Filed but not acknowledged";
            case 'U' : return "N/A";
            default  : return "Not Acknowledged";
        }
    }

    public static String searchPatient(String labNo,String labType) {
        String retval = "0";
        try {
            

            String sql = "select demographic_no from patientLabRouting where lab_no='"+labNo+"' and lab_type = '"+labType+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                retval = oscar.Misc.getString(rs, "demographic_no");
            }
        }catch(Exception e){
            Logger l = Logger.getLogger(CommonLabResultData.class);
            l.error("exception in CommonLabResultData.searchPatient()",e);
            return "0";
        }
        return retval;
    }

    public static boolean updatePatientLabRouting(String labNo, String demographicNo,String labType) {
        boolean result = false;

        try {
            

            // update pateintLabRouting for labs with the same accession number
            CommonLabResultData data = new CommonLabResultData();
            String[] labArray =  data.getMatchingLabs(labNo, labType).split(",");
            for (int i=0; i < labArray.length; i++){

                // delete old entries
                String sql = "delete from patientLabRouting where lab_no='"+labArray[i]+"' and lab_type = '"+labType+"'";
                result = DBHandler.RunSQL(sql);

                // add new entries
                sql = "insert into patientLabRouting (lab_no, demographic_no,lab_type) values ('"+labArray[i]+"', '"+demographicNo+"','"+labType+"')";
                result = DBHandler.RunSQL(sql);

                // add labs to measurements table
                populateMeasurementsTable(labArray[i], demographicNo, labType);

            }

            return result;

        }catch(Exception e){
            Logger l = Logger.getLogger(CommonLabResultData.class);
            l.error("exception in CommonLabResultData.updateLabRouting()",e);
            return false;
        }
    }


    public static boolean updateLabRouting(ArrayList flaggedLabs, String selectedProviders) {
        boolean result;

        try {
            

            String[] providersArray = selectedProviders.split(",");
           
            CommonLabResultData data = new CommonLabResultData();
            ProviderLabRouting plr = new ProviderLabRouting();
           //MiscUtils.getLogger().info(flaggedLabs.size()+"--");
            for (int i=0; i < flaggedLabs.size(); i++) {
                String[] strarr = (String[]) flaggedLabs.get(i);
                String lab = strarr[0];
                String labType = strarr[1];

                // Forward all versions of the lab
                String matchingLabs = data.getMatchingLabs(lab, labType);
                String[] labIds = matchingLabs.split(",");
                //MiscUtils.getLogger().info(labIds.length+"labIds --");
                for (int k=0; k < labIds.length; k++){

                    for (int j=0; j < providersArray.length; j++) {
                        /*if (!insertString.equals("")) {
                            insertString = insertString + ", ";
                        }
                        insertString = insertString + "('" + providersArray[j] + "','" + labIds[k]+ "','N','"+labType+"')";
                         */
                        plr.route(labIds[k], providersArray[j], DbConnectionFilter.getThreadLocalDbConnection(), labType);
                    }

                    // delete old entries
                    String sql = "delete from providerLabRouting where provider_no='0' and lab_type= '"+labType+"' and lab_no = '"+labIds[k]+"'";

                    result = DBHandler.RunSQL(sql);

                }

            }



            // add new entries
            //String sql = "insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) values "+insertString;
            //result = DBHandler.RunSQL(sql);
            return true;
        }catch(Exception e){
            Logger l = Logger.getLogger(CommonLabResultData.class);
            l.error("exception in CommonLabResultData.updateLabRouting()",e);
            return false;
        }
    }

    ////
    public static boolean fileLabs(ArrayList flaggedLabs, String provider) {
        
        CommonLabResultData data = new CommonLabResultData();

        Properties props = OscarProperties.getInstance();
        for (int i=0; i < flaggedLabs.size(); i++){
            String[] strarr = (String[]) flaggedLabs.get(i);
            String lab = strarr[0];
            String labType = strarr[1];
            String labs = data.getMatchingLabs(lab, labType);

            if (labs != null && !labs.equals("")){
                String[] labArray = labs.split(",");
                for (int j=0; j < labArray.length; j++){
                    updateReportStatus(props, Integer.parseInt(labArray[j]), provider, 'F', "", labType);
                }

            }else{
                updateReportStatus(props, Integer.parseInt(lab), provider, 'F', "",labType);
            }
        }
        return true;
    }
    ////

    public String getMatchingLabs(String lab_no, String lab_type){
        String labs = null;
        if (lab_type.equals(LabResultData.HL7TEXT)){
            Hl7textResultsData data = new Hl7textResultsData();
            labs = data.getMatchingLabs(lab_no);
        }else if (lab_type.equals(LabResultData.MDS)){
            MDSResultsData data = new MDSResultsData();
            labs = data.getMatchingLabs(lab_no);
        }else if (lab_type.equals((LabResultData.EXCELLERIS))){
            PathnetResultsData data = new PathnetResultsData();
            labs = data.getMatchingLabs(lab_no);
        }else if (lab_type.equals(LabResultData.CML)){
            MDSResultsData data = new MDSResultsData();
            labs = data.getMatchingCMLLabs(lab_no);
        }else if (lab_type.equals(LabResultData.DOCUMENT)){
            labs = lab_no;//one document is only linked to one patient.
        }

        return labs;
    }

    public String getDemographicNo(String labId,String labType){
        String demoNo = null;
        try{
            
            ResultSet rs = DBHandler.GetSQL("select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type = '"+labType+"'");
            if (rs.next()){
                String d = oscar.Misc.getString(rs, "demographic_no");
                if ( !"0".equals(d)){
                    demoNo = d;
                }
            }
            rs.close();

        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return demoNo;
    }

    public boolean isDocLinkedWithPatient(String labId,String labType){
        boolean ret=false;
        try{
            String sql="select module_id from ctl_document where document_no="+labId+" and module='demographic'";
            ResultSet rs=DBHandler.GetSQL(sql);
            if(rs.next()){
                String mi=oscar.Misc.getString(rs, "module_id");
                if(mi!=null && !mi.trim().equals("-1")){
                    ret=true;
                }
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in isDocLinkedWithPatient",e);
        }
        return ret;
    }
    public boolean isLabLinkedWithPatient(String labId,String labType){
        boolean ret = false;
        try {
            
            String sql = "select demographic_no from patientLabRouting where lab_no = '"+labId+"' and lab_type  = '"+labType+"' ";

            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                String demo =  oscar.Misc.getString(rs, "demographic_no");
                if(demo != null && !demo.trim().equals("0") ){
                    ret = true;
                }
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in isLabLinkedWithPatient",e);

        }
        return ret;
    }

    public int getAckCount(String labId, String labType){
        int ret = 0;
        try {
            
            String sql = "select count(*) from providerLabRouting where lab_no = '"+labId+"' and lab_type  = '"+labType+"' and status='A'";
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()){
                ret = rs.getInt(1);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in getAckCount",e);

        }
        return ret;
    }

    public static void populateMeasurementsTable(String labId, String demographicNo, String labType){
        if (labType.equals(LabResultData.HL7TEXT)){
            Hl7textResultsData rd = new Hl7textResultsData();
            rd.populateMeasurementsTable(labId, demographicNo);
        }
    }


}
