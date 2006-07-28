import java.util.*;
import oscar.oscarMDSLab.dbUtil.*;
import java.sql.*;
import java.text.*;
import java.io.*;
import java.util.logging.*;

/**
 * parses a desired MDS HL7 lab results file and stores the details
 * of the parse in an array list. From the list the file details can
 * be written to accomplish the MDS audit function or inserted into a
 * database.
 */
public class BulkFileParse {
    
    Logger logger = Logger.getLogger("mdsFileManagement.BulkFileParse");
   
    public ArrayList aList = new ArrayList();
    private FileLineReader fReader = new FileLineReader();
    private String currentLine = null;
    private StringTokenizer stringToken;
    private String thisSegment=null;
    private String replaces;
    
    /**
     *Used to escape Sql
     */
    public  String es(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("'", "''");
    }
    
    
    /**
     * This function gets the next segment from the tokenized
     * currentline held in StringToken.  It returns null at
     * exceptions.  This is handy because at this time the MDS
     * implementation of HL7 is a little wonky i.e. no termination
     * of end fields that may or may not be present.
     *
     */
    public String getSegment(StringTokenizer thisToken, int i){
        try{
            String thisSegment=null;
            int zk=0;
            for (zk=0;zk<i;zk++){
                thisSegment= thisToken.nextToken();
            }
            return thisSegment.trim();
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    
    // This is a version of getSegment modified to return the rest of the line, delimiters and all
    // Needed so that the delimiter character can be used in comments
    public String getRemaining(StringTokenizer thisToken){
        try{
            String thisSegment=null;
            thisSegment= thisToken.nextToken("");  // null delimiter
            return thisSegment.trim().substring(1); // trim leading delimiter
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
    
    public void auditHelper(int insertID,String auditLogFile)    {
        AuditLine al = new AuditLine();
        String procDate = null;   //make date up
        String procTime = null;   //make time up
        String logId = "REC";
        String[] formStatus;   //table mdsZFR column reportFormStatus
        String[] formType;     //mySQL table mdsZFR column reportForm   last char of the field have to convert from numeric to Letter version
        String accession  = null;
        String hcNum      = null;
        String hcVerCode  = null;
        String patientName = null;
        String orderingClient = null;
        String messageDate    = null;
        String messageTime    = null;
        
        //Calendar cal = Calendar.getInstance() ;
        
        // procDate = cal.get(Calendar.DAY_OF_MONTH)+"-"+cal.get(Calendar.
        
        java.util.Date date = new java.util.Date();
        SimpleDateFormat formatter =  new SimpleDateFormat("dd-MMM-yyyy");
        procDate = formatter.format(date);
        
        procTime = new SimpleDateFormat("HH:mm:ss").format(date);
        formStatus = al.auditFormStatus(insertID);
        formType   = al.auditFormType(insertID);
        accession  = al.auditAccession(insertID);
        orderingClient = al.auditClientNumber(insertID);
        String[] healthCard = al.auditHealthCard(insertID);
        hcNum = healthCard[0];
        hcVerCode = healthCard[1];
        
        java.util.Date mdsDate = al.auditMessageDate(insertID);
        
        patientName = al.auditPatientName(insertID);
        messageDate = new SimpleDateFormat("dd-MMM-yyyy").format(mdsDate);
        messageTime = new SimpleDateFormat("HH:mm:ss").format(mdsDate);
        
        for (int i = 0; i < formStatus.length; i++) {
            al.addToAuditFile(al.getAuditLine(procDate, procTime, logId, formStatus[i], formType[i], accession, hcNum, hcVerCode, patientName, orderingClient, messageDate, messageTime), auditLogFile);        
            System.out.println(al.getAuditLine(procDate, procTime, logId, formStatus[i], formType[i], accession, hcNum, hcVerCode, patientName, orderingClient, messageDate, messageTime));
        }
    }
    
    
    
    public void processResult(String auditLogFile,String dupsHL7dir){
        logger.info("Proccessing Result");
        int insertID;
        for(int i=0;i<aList.size();i++){
            MDSHL7Message segm = (MDSHL7Message) aList.get(i);

            int dupResult = checkForDuplicates(segm) ;
            if (dupResult == 0) {
                insertID = commitSegmentToDB(i);
                auditHelper(insertID,auditLogFile);
                providerRouteReport(insertID);
                patientRouteReport(insertID);
            } else {
                logger.info("Duplicates found");

                Long lon = new Long(Calendar.getInstance().getTimeInMillis());

                String str = new String(dupsHL7dir+"/"+dupResult+"."+lon.toString() );
                printSegment("Seems to be copy of "+dupResult, str,  segm);

            }
        }
    }
    
    public String getProviderNoFromOhipNo(String providerMinistryNo){
       String ret = null;
       String sql = "select provider_no from provider where ohip_no='"+providerMinistryNo+"'";
       System.out.println("\n\n"+sql+"\n\n");
       boolean hasNext = false;
       DBHandler db = new DBHandler(DBHandler.MDS_DATA);                   
       try {
          ResultSet rsr = db.GetSQL(sql);	               
          if (!rsr.next()){
             sql = "select provider_no from provider where ohip_no='0"+providerMinistryNo+"'";
             System.out.println("\n\n"+sql+"\n\n");
             rsr = db.GetSQL(sql);	               
             if (rsr.next()){
                hasNext = true;
             }
          }else{
             hasNext = true;
          }
          if(hasNext){
             ret = rsr.getString("provider_no");
          }
          rsr.close();
          db.CloseConn();
       }catch(Exception e){
          e.printStackTrace();  
       }
       return ret;
    }
    
    
    public void providerRouteReportOLD (int segmentID) {                
        try {
            
            
            DBHandler db = new DBHandler(DBHandler.MDS_DATA);            
            String sql;
        
            try {
                String providerMinistryNo;
                String[] subStrings;
                String[] conDoctors;                                
                
                sql ="select refDoctor, conDoctor, admDoctor from mdsPV1 where segmentID='"+segmentID+"'";
                ResultSet rs = db.GetSQL(sql);                
                boolean addedToProviderLabRouting = false;
                
                if ( rs.next() ) {
                    
                    // route lab first to admitting doctor
                    subStrings = rs.getString("admDoctor").split("\\^");
                    providerMinistryNo = subStrings[0].substring(1, subStrings[0].length());
                    // check that this is a legal provider
                    sql = "select provider_no from provider where ohip_no='"+providerMinistryNo+"'";
                    ResultSet rs2 = db.GetSQL(sql);
	            sql = "foo";
                    if ( rs2.next() ) {  // provider found in database
                        sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+rs2.getString("provider_no")+"', '"+segmentID+"', 'N','MDS')";
                        db.RunSQL(sql);
                        addedToProviderLabRouting =true;
                    }  // provider not found                     
                    rs2.close();
                    
                    // next route to consulting doctor(s)
                    if ( ! rs.getString("conDoctor").equals("") ) {
                        conDoctors = rs.getString("conDoctor").split("~");
                        for (int i = 1; i <= conDoctors.length; i++) {
                            subStrings = conDoctors[i-1].split("\\^");
                            providerMinistryNo = subStrings[0].substring(1, subStrings[0].length());
                            // check that this is a legal provider
                            sql = "select provider_no from provider where ohip_no='"+providerMinistryNo+"'";
                            ResultSet rs3 = db.GetSQL(sql);
                            if ( rs3.next() ) {  // provider found in database
                                // ignore duplicates in case admitting doctor == consulting doctor
                                sql ="insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+rs3.getString("provider_no")+"', '"+segmentID+"', 'N','MDS')";
                                db.RunSQL(sql);
                                addedToProviderLabRouting =true;
                            }   // provider not found                              
                            rs3.close();
                        }
                    }
                    
                    // next route to referring doctor(s)
                    if ( ! rs.getString("refDoctor").equals("") ) {
                       subStrings = rs.getString("refDoctor").split("\\^");
                       providerMinistryNo = subStrings[0].substring(1, subStrings[0].length());
                       // check that this is a legal provider
                       sql = "select provider_no from provider where ohip_no='"+providerMinistryNo+"'";
                       System.out.println("\n\n"+sql+"\n\n");
                       ResultSet rsr = db.GetSQL(sql);	               
                       if (!rsr.next()){
                          sql = "select provider_no from provider where ohip_no='0"+providerMinistryNo+"'";
                          System.out.println("\n\n"+sql+"\n\n");
                          rsr = db.GetSQL(sql);	               
                       }
                       if ( rsr.next() ) {  // provider found in database
                          sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+rsr.getString("provider_no")+"', '"+segmentID+"', 'N','MDS')";
                          db.RunSQL(sql);
                          addedToProviderLabRouting =true;
                       }  // provider not found                        
                       rsr.close();
                                              
                    }
                    
                    if(!addedToProviderLabRouting){
                       sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('0', '"+segmentID+"', 'N','MDS')";
                       db.RunSQL(sql);                        
                    }
                    
                } else { // major error
                    throw new Exception("Corresponding PV1 entry not found!");
                }                
                rs.close();                                
            } catch (Exception e) {
                System.out.println("Error in providerRouteReport:"+e);
                
                sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('0', '"+segmentID+"', 'N','MDS')";
                db.RunSQL(sql);
            }
            db.CloseConn();            
        } catch (Exception e) {
            System.out.println("Database error in providerRouteReport:"+e);
        }        
    }    
/////////
    public void providerRouteReport (int segmentID) {                
        try {                        
            DBHandler db = new DBHandler(DBHandler.MDS_DATA);            
            String sql;        
            try {
                String providerMinistryNo;
                String[] subStrings;
                String[] conDoctors;                                
                String providerNo = null;
                
                ArrayList listOfProviderNo = new ArrayList();
                
                sql ="select refDoctor, conDoctor, admDoctor from mdsPV1 where segmentID='"+segmentID+"'";
                ResultSet rs = db.GetSQL(sql);                
                boolean addedToProviderLabRouting = false;                
                if ( rs.next() ) {                    
                    // route lab first to admitting doctor
                    subStrings = rs.getString("admDoctor").split("\\^");
                    providerMinistryNo = subStrings[0].substring(1, subStrings[0].length());
                    // check that this is a legal provider
                    providerNo = getProviderNoFromOhipNo(providerMinistryNo);                    
                    if ( providerNo != null) {  // provider found in database
                        ///sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+providerNo+"', '"+segmentID+"', 'N','MDS')";
                        ///db.RunSQL(sql);                        
                        listOfProviderNo.add(providerNo);                                                
                        addedToProviderLabRouting =true;
                    }  // provider not found                                         
                    
                    // next route to consulting doctor(s)
                    if ( ! rs.getString("conDoctor").equals("") ) {
                        conDoctors = rs.getString("conDoctor").split("~");
                        for (int i = 1; i <= conDoctors.length; i++) {
                            subStrings = conDoctors[i-1].split("\\^");
                            providerMinistryNo = subStrings[0].substring(1, subStrings[0].length());
                            // check that this is a legal provider
                            providerNo = getProviderNoFromOhipNo(providerMinistryNo);                                                
                            if ( providerNo != null) {  // provider found in database
                                // ignore duplicates in case admitting doctor == consulting doctor
                                ///sql ="insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+providerNo+"', '"+segmentID+"', 'N','MDS')";
                                ///db.RunSQL(sql);                                                               
                                if (!listOfProviderNo.contains(providerNo)){
                                   listOfProviderNo.add(providerNo);
                                }                           
                                addedToProviderLabRouting =true;
                            }   // provider not found                                                          
                        }
                    }
                    
                    // next route to referring doctor(s)
                    if ( ! rs.getString("refDoctor").equals("") ) {
                       subStrings = rs.getString("refDoctor").split("\\^");
                       providerMinistryNo = subStrings[0].substring(1, subStrings[0].length());
                       // check that this is a legal provider
                       providerNo = getProviderNoFromOhipNo(providerMinistryNo);                    
                       if ( providerNo != null) {  // provider found in database
                          if (!listOfProviderNo.contains(providerNo)){
                             listOfProviderNo.add(providerNo);
                          }                           
                          //sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+providerNo+"', '"+segmentID+"', 'N','MDS')";
                          //db.RunSQL(sql);
                          addedToProviderLabRouting =true;
                       }  // provider not found                        
                       
                                              
                    }
                    
                    if (listOfProviderNo.size() > 0) {  // provider found in database
                       for(int p = 0; p < listOfProviderNo.size(); p++){
                          String prov = (String) listOfProviderNo.get(p);
                          sql ="insert ignore into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+prov+"', '"+segmentID+"', 'N','MDS')";
                          System.out.println(" size "+listOfProviderNo.size()+" "+sql);
                          db.RunSQL(sql);
                       }
                       addedToProviderLabRouting =true;
                    }   // provider not found                                                          
                    
                    if(!addedToProviderLabRouting){
                       sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('0', '"+segmentID+"', 'N','MDS')";
                       db.RunSQL(sql);                        
                    }
                    
                } else { // major error
                    throw new Exception("Corresponding PV1 entry not found!");
                }                
                rs.close();                
            } catch (Exception e) {
                System.out.println("Error in providerRouteReport:"+e);
                
                sql ="insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('0', '"+segmentID+"', 'N','MDS')";
                db.RunSQL(sql);
            }
            db.CloseConn();            
        } catch (Exception e) {
            System.out.println("Database error in providerRouteReport:"+e);
        }        
    }    

/////////
    public void patientRouteReport (int segmentID) {                
        try {
            
            
            DBHandler db = new DBHandler(DBHandler.MDS_DATA);            
            String sql;
        
            try {                            
                sql ="select healthNumber, patientName, dOB, sex from mdsPID where segmentID='"+segmentID+"'";
                ResultSet rs = db.GetSQL(sql);
                                                
                if ( rs.next() ) {
                    String lastName = rs.getString("patientName").split("\\^")[0].toUpperCase();
                    String firstName = rs.getString("patientName").split("\\^")[1].toUpperCase();
                    String dobYear = rs.getString("dOB").substring(0,4);
                    String dobMonth = rs.getString("dOB").substring(4,6);
                    String dobDay = rs.getString("dOB").substring(6,8);
                    String demoNo = null;
                    if ( rs.getString("healthNumber").toUpperCase().startsWith("X") ) {
                        // patient's health number is known - check initials, DOB match
                        sql = "select demographic_no from demographic where hin='"+rs.getString("healthNumber").substring(1,11)+"' and " +
                              "last_name like '"+lastName.substring(0,1)+"%' and " +
                              "first_name like '"+firstName.substring(0,1)+"%' and year_of_birth='"+dobYear+"' and " +
                              "month_of_birth='"+dobMonth+"' and date_of_birth='"+dobDay+"' and sex like '"+rs.getString("sex").toUpperCase()+"%' and " +
                              "patient_status='AC'";
                        ResultSet rs2 = db.GetSQL(sql);                        
                        if ( rs2.next() ) {
                            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('"+rs2.getString("demographic_no")+"', '"+segmentID+"','MDS')";                            
                            demoNo = rs2.getString("demographic_no");
                        } else {
                            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('0', '"+segmentID+"','MDS')";                            
                        }
                        db.RunSQL(sql);
                    } else {                        
                        // patient's health number is unknown - search by name, DOB, sex
                        sql = "select demographic_no from demographic where last_name='"+lastName+"' and " +
                              "first_name like '"+firstName+"%' and year_of_birth='"+dobYear+"' and " +
                              "month_of_birth='"+dobMonth+"' and date_of_birth='"+dobDay+"' and sex like '"+rs.getString("sex").toUpperCase()+"%' and " +
                              "patient_status='AC'";
                        ResultSet rs3 = db.GetSQL(sql);
                        if ( rs3.next() ) {
                            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('"+rs3.getString("demographic_no")+"', '"+segmentID+"','MDS')";                            
                            demoNo = rs3.getString("demographic_no");
                        } else {
                            sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('0', '"+segmentID+"','MDS')";                            
                        }
                        db.RunSQL(sql);
                    }
                    
                    if (demoNo != null){
                       patientProviderRoute(""+segmentID,demoNo);
                    }
                } else { // major error
                    throw new Exception("Corresponding PID entry not found!");
                }                
                rs.close();                
            } catch (Exception e) {
                System.out.println("Error in patientRouteReport:"+e);
                                
                sql = "insert into patientLabRouting (demographic_no, lab_no,lab_type) values ('0', '"+segmentID+"','MDS')";                            
                db.RunSQL(sql);
            }
            db.CloseConn();            
        } catch (Exception e) {
            System.out.println("Database error in patientRouteReport:"+e);
        }        
    }    
    
    public void patientProviderRoute(String lab_no, String demographic_no){
       try {                        
          DBHandler db = new DBHandler(DBHandler.MDS_DATA);            
          String sql;
          sql ="select provider_no from demographic where demographic_no = '"+demographic_no+"'";
          ResultSet rs = db.GetSQL(sql);
                                                
          if ( rs.next() ) {
             String prov_no  = rs.getString("provider_no");                                        
             if ( prov_no != null ){
                sql = "select status from providerLabRouting where lab_type = 'MDS' and provider_no = '"+prov_no+"' and lab_no = '"+lab_no+"'";                               
                ResultSet rs2 = db.GetSQL(sql);
                if ( !rs2.next() ) {                            
                   sql = "insert into providerLabRouting (provider_no, lab_no, status,lab_type) VALUES ('"+prov_no+"', '"+lab_no+"', 'N','MDS')";
                   db.RunSQL(sql);
                } 
                rs2.close();                      
             }                 
          }
          rs.close();                
          db.CloseConn();            
       } catch (Exception e) {
          System.out.println("Database error in patientProviderRoute:"+e);
       }        
    }
    
    public int commitFileToDB(String fileName){
        logger.info("Commiting File to Database: "+fileName);
        String sql = null;
        int insertID=0;
        java.sql.ResultSet rs;
        int i=-1;//the MSH header counter
        int j=0; //the ZMN counter
        int k=0; //the ZRG counter
        int l=0; //the ZFR counter
        int m=0; //the ZMC counter
        int n=0; //the ZCT counter
        int o=0; //the OBX counter
        int p=0; //the NTE counter
        //db.RunSQL(sql);
        this.parseFile(fileName);
        DBHandler db =null;
        try {
            db = new DBHandler(DBHandler.MDS_DATA);
            
            for(i=0;i<aList.size();i++) {
                //Insert the current MSHTag info into mdsMSH
                sql ="insert into mdsMSH (sendingApp,dateTime,type,messageConID,processingID,versionID,acceptAckType,appAckType) VALUES ('"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getSendingApp() )+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getDateTime())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getType())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getMessageConID())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getProcessingID())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getVersionID())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getAcceptAckType())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getAppAckType())+"')";
                db.RunSQL(sql);
                //Grab the unique insert ID of the MSH tag we just put in the mdsMSH table
                rs = db.GetSQL("SELECT LAST_INSERT_ID()");
                if(rs.next()) {
                    insertID =  rs.getInt(1) ;
                }
                //Insert the PIDTag info into the pIDSegment table
                sql ="insert into mdsPID (segmentID,intPatientID,altPatientID,patientName,dOB,sex,homePhone,healthNumber) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getIntPatientID())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getAltPatientID())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getPatientName())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getDOB())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getSex())+"','"+ es(((MDSHL7Message)aList.get(i)).PIDTag.getHomePhone())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getHealthNumber())+"')";
                db.RunSQL(sql);
                //Insert the PV1Tag info into the pV1Segment table
                sql ="insert into mdsPV1 (segmentID,patientClass,patientLocation,refDoctor,conDoctor,admDoctor,vNumber,accStatus,admDateTime) VALUES ("+insertID +",'"+ es(((MDSHL7Message)aList.get(i)).PV1Tag.getPatientClass())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getPatientLocation())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getRefDoctor())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getConDoctor())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getAdmDoctor())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getVNumber())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getAccStatus())+"','"+es(  ((MDSHL7Message)aList.get(i)).PV1Tag.getAdmDateTime())+"')";
                db.RunSQL(sql);
                //Insert the ZLBTag info into the zLBSegment table
                sql ="insert into mdsZLB (segmentID, labID,labIDVersion,labAddress,primaryLab,primaryLabVersion,MDSLU,MDSLV) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getLabID())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getLabIDVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getLabAddress())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getPrimaryLab())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getPrimaryLabVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getMDSLU())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getMDSLV())+"')";
                db.RunSQL(sql);
                //Insert the ZMNTag info into the zMNSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getZMNArrayListSize();j++) {
                    sql ="insert into mdsZMN (segmentID, resultMnemonic,resultMnemonicVersion,reportName,units,cumulativeSequence,referenceRange,resultCode,reportForm,reportGroup,reportGroupVersion) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultMnemonic())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultMnemonicVersion())+"','"+ es(((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportName())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getUnits())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getCumulativeSequence())+"','"+ es(((MDSHL7Message)aList.get(i)).getZMNNode(j).getReferenceRange())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultCode())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportForm())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportGroup())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportGroupVersion())+"')";
                    db.RunSQL(sql);
                }
                //Insert the ZRGTag info into the zMNSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getZRGArrayListSize();j++) {
                    sql ="insert into mdsZRG (segmentID,reportSequence,reportGroupID,reportGroupVersion,reportFlags,reportGroupDesc,mDSindex, reportGroupHeading) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportSequence())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupID())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportFlags())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupDesc())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getMDSIndex())+"','" +es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupHeading())+"')";
                    db.RunSQL(sql);
                }
                //Insert the ZFRTag info into the zMNSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getZFRArrayListSize();j++) {
                    sql ="insert into mdsZFR (segmentID, reportForm,reportFormStatus,testingLab,medicalDirector,editFlag,abnormalFlag) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getReportForm())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getReportFormStatus())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getTestingLab())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getMedicalDirector())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getEditFlag())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getAbnormalFlag())+"')";
                    db.RunSQL(sql);
                }
                //Insert the ZMCTag info into the zMNSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getZMCArrayListSize();j++) {
                    sql ="insert into mdsZMC (segmentID, setID,messageCodeIdentifier,messageCodeVersion,noMessageCodeDescLines,sigFlag,messageCodeDesc) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getSetID())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeIdentifier())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getNoMessageCodeDescLines())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getSigFlag())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeDesc())+"')";
                    db.RunSQL(sql);
                }
                //Insert the ZCL tag
                sql ="insert into mdsZCL (segmentID, setID,consultDoc,clientAddress,route,stop,area,reportSet,clientType,clientModemPool,clientFaxNumber,clientBakFax) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getSetID())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getConsultDoc())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientAddress())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getRoute())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getStop())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getArea())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getReportSet())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientType())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientModemPool())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientFaxNumber())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientBakFax())+"')";
                db.RunSQL(sql);
                //Insert the ZCTTag info into the zMNSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getZCTArrayListSize();j++) {
                    sql ="insert into mdsZCT (segmentID, barCodeIdentifier, placerGroupNo, observationDateTime) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZCTNode(j).getBarCodeIdentifier())+"','"+es( ((MDSHL7Message)aList.get(i)).getZCTNode(j).getPlacerGroupNo())+"','"+es( ((MDSHL7Message)aList.get(i)).getZCTNode(j).getObservationDateTime())+"')";
                    db.RunSQL(sql);
                }
                
                //Insert the OBRTag info into the zMNSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getOBRArrayListSize();j++) {
                    sql ="insert into mdsOBR (segmentID, placerOrderNo, universalServiceID,observationDateTime, specimenRecDateTime,fillerFieldOne,quantityTiming,obrID) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getPlacerOrderNo())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getUniversalServiceID())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getObservationDateTime())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getSpecimenRecDateTime())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getFillerFieldOne())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getQuantityTiming())+"','"+((MDSHL7Message)aList.get(i)).getOBRNode(j).getObrSegmentID()+"')";
                    db.RunSQL(sql);
                }
                
                //Insert the OBXTag info into the oBXSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getOBXArrayListSize();j++) {
                    sql ="insert into mdsOBX (segmentID,valueType,observationIden,observationSubID,observationValue,abnormalFlags,observationResultStatus,producersID,associatedOBR,obxID) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getValueType())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationIden())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationSubID())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationValue())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getAbnormalFlags())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationResultStatus())+"','"+es(((MDSHL7Message)aList.get(i)).getOBXNode(j).getProducersID())+"','"+((MDSHL7Message)aList.get(i)).getOBXNode(j).getAssociatedOBR()+"','"+((MDSHL7Message)aList.get(i)).getOBXNode(j).getObxSegmentID()+"')";
                    db.RunSQL(sql);
                }
                
                //Insert the NTETag info into the oBRSegment table
                for(j=0;j<((MDSHL7Message)aList.get(i)).getNTEArrayListSize();j++) {
                    sql ="insert into mdsNTE (segmentID,sourceOfComment,comment,associatedOBX) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getNTENode(j).getSourceOfComment())+"','"+es( ((MDSHL7Message)aList.get(i)).getNTENode(j).getComment())+"','"+ ((MDSHL7Message)aList.get(i)).getNTENode(j).getAssociatedOBX()+"')";
                    db.RunSQL(sql);
                }
                
                
                
            }
            db.CloseConn();
        }catch(Exception e) {
            logger.severe("Error Inside the BulkFileParse::"+e.getMessage());
            try{
                if(db !=null){
                    db.CloseConn();
                }
            }
            catch(Exception f){
                logger.severe("Error Inside the BulkFileParse::"+f.getMessage());
            }
        }
        return insertID;
    }
    
    
    
    
    public int checkForDuplicates(MDSHL7Message segm){
        int retval = 0;
        String messageControlId = segm.MSHTag.getMessageConID();
        String dateTime = segm.MSHTag.getDateTime();
        String sql = null;
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);
            ResultSet rs;
            sql ="select segmentID from mdsMSH where messageConID = '"+es(messageControlId)+"' and date_format(dateTime, '%Y%m%d%H%i%s') = '"+es(dateTime)+"'";
            
            rs = db.GetSQL(sql);
            if(rs.next()){
                retval = Integer.parseInt(rs.getString("segmentID"));
            }
            db.CloseConn();
        }catch(Exception e){
            try{ if(db !=null){ db.CloseConn(); } }
            catch(Exception f){ System.out.println("Inside the checkForDuplicates::"+f);  }
        }
        return retval;
    }
    
    public int commitSegmentToDB(int i){
        
        logger.info("COMMITTING SEGMENT TO DataBase");
        String sql = null;
        int insertID=0;
        java.sql.ResultSet rs;
        
        //i=-1;//the MSH header counter
        int j=0; //the ZMN counter
        int k=0; //the ZRG counter
        int l=0; //the ZFR counter
        int m=0; //the ZMC counter
        int n=0; //the ZCT counter
        int o=0; //the OBX counter
        int p=0; //the NTE counter
        
        
        DBHandler db =null;
        try{
            db = new DBHandler(DBHandler.MDS_DATA);
            
            //Insert the current MSHTag info into mdsMSH
            sql ="insert into mdsMSH (sendingApp,dateTime,type,messageConID,processingID,versionID,acceptAckType,appAckType) VALUES ('"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getSendingApp())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getDateTime())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getType())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getMessageConID())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getProcessingID())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getVersionID())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getAcceptAckType())+"','"+es( ((MDSHL7Message)aList.get(i)).MSHTag.getAppAckType())+"')";
            db.RunSQL(sql);
            
            //Grab the unique insert ID of the MSH tag we just put in the mdsMSH table
            rs = db.GetSQL("SELECT LAST_INSERT_ID()");
            if(rs.next()){
                insertID =  rs.getInt(1) ;
            }
            
            //Insert the PIDTag info into the pIDSegment table
            sql ="insert into mdsPID (segmentID,intPatientID,altPatientID,patientName,dOB,sex,homePhone,healthNumber) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getIntPatientID())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getAltPatientID())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getPatientName())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getDOB())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getSex())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getHomePhone())+"','"+es( ((MDSHL7Message)aList.get(i)).PIDTag.getHealthNumber())+"')";
            db.RunSQL(sql);
            
            //Insert the PV1Tag info into the pV1Segment table
            sql ="insert into mdsPV1 (segmentID,patientClass,patientLocation,refDoctor,conDoctor,admDoctor,vNumber,accStatus,admDateTime) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getPatientClass())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getPatientLocation())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getRefDoctor())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getConDoctor())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getAdmDoctor())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getVNumber())+"','"+es( ((MDSHL7Message)aList.get(i)).PV1Tag.getAccStatus())+"','"+es(  ((MDSHL7Message)aList.get(i)).PV1Tag.getAdmDateTime())+"')";
            db.RunSQL(sql);
            
            //Insert the ZLBTag info into the zLBSegment table
            sql ="insert into mdsZLB (segmentID, labID,labIDVersion,labAddress,primaryLab,primaryLabVersion,MDSLU,MDSLV) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getLabID())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getLabIDVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getLabAddress())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getPrimaryLab())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getPrimaryLabVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getMDSLU())+"','"+es( ((MDSHL7Message)aList.get(i)).ZLBTag.getMDSLV())+"')";
            db.RunSQL(sql);
            
            //Insert the ZMNTag info into the zMNSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZMNArrayListSize();j++){
                sql ="insert into mdsZMN (segmentID, resultMnemonic,resultMnemonicVersion,reportName,units,cumulativeSequence,referenceRange,resultCode,reportForm,reportGroup,reportGroupVersion) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultMnemonic())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultMnemonicVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportName())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getUnits())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getCumulativeSequence())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReferenceRange())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultCode())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportForm())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportGroup())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportGroupVersion())+"')";
                db.RunSQL(sql);
            }
            
            //Insert the ZRGTag info into the zMNSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZRGArrayListSize();j++){
                sql ="insert into mdsZRG (segmentID,reportSequence,reportGroupID,reportGroupVersion,reportFlags,reportGroupDesc,mDSindex, reportGroupHeading) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportSequence())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupID())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportFlags())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupDesc())+"','"+es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getMDSIndex())+"','" +es( ((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupHeading())+"')";
                db.RunSQL(sql);
            }
            
            //Insert the ZFRTag info into the zMNSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZFRArrayListSize();j++){
                sql ="insert into mdsZFR (segmentID, reportForm,reportFormStatus,testingLab,medicalDirector,editFlag,abnormalFlag) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getReportForm())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getReportFormStatus())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getTestingLab())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getMedicalDirector())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getEditFlag())+"','"+es( ((MDSHL7Message)aList.get(i)).getZFRNode(j).getAbnormalFlag())+"')";
                db.RunSQL(sql);
            }
            
            //Insert the ZMCTag info into the zMNSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZMCArrayListSize();j++){
                sql ="insert into mdsZMC (segmentID, setID,messageCodeIdentifier,messageCodeVersion,noMessageCodeDescLines,sigFlag,messageCodeDesc) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getSetID())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeIdentifier())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeVersion())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getNoMessageCodeDescLines())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getSigFlag())+"','"+es( ((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeDesc())+"')";
                db.RunSQL(sql);
            }
            
            //Insert the ZCL tag
            sql ="insert into mdsZCL (segmentID, setID,consultDoc,clientAddress,route,stop,area,reportSet,clientType,clientModemPool,clientFaxNumber,clientBakFax) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getSetID())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getConsultDoc())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientAddress())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getRoute())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getStop())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getArea())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getReportSet())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientType())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientModemPool())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientFaxNumber())+"','"+es( ((MDSHL7Message)aList.get(i)).ZCLTag.getClientBakFax())+"')";
            db.RunSQL(sql);
            
            //Insert the ZCTTag info into the zMNSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZCTArrayListSize();j++){
                sql ="insert into mdsZCT (segmentID, barCodeIdentifier, placerGroupNo, observationDateTime) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getZCTNode(j).getBarCodeIdentifier())+"','"+es( ((MDSHL7Message)aList.get(i)).getZCTNode(j).getPlacerGroupNo())+"','"+es( ((MDSHL7Message)aList.get(i)).getZCTNode(j).getObservationDateTime())+"')";
                db.RunSQL(sql);
            }
            
            //Insert the OBRTag info into the zMNSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getOBRArrayListSize();j++){
                sql ="insert into mdsOBR (segmentID, placerOrderNo, universalServiceID,observationDateTime, specimenRecDateTime,fillerFieldOne,quantityTiming,obrID) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getPlacerOrderNo())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getUniversalServiceID())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getObservationDateTime())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getSpecimenRecDateTime())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getFillerFieldOne())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBRNode(j).getQuantityTiming())+"','"+((MDSHL7Message)aList.get(i)).getOBRNode(j).getObrSegmentID()+"')";
                db.RunSQL(sql);
            }
            
            //Insert the OBXTag info into the oBXSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getOBXArrayListSize();j++){
                sql ="insert into mdsOBX (segmentID,valueType,observationIden,observationSubID,observationValue,abnormalFlags,observationResultStatus,producersID,associatedOBR,obxID) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getValueType())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationIden())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationSubID())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationValue())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getAbnormalFlags())+"','"+es( ((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationResultStatus())+"','"+es(((MDSHL7Message)aList.get(i)).getOBXNode(j).getProducersID())+"','"+((MDSHL7Message)aList.get(i)).getOBXNode(j).getAssociatedOBR()+"','"+((MDSHL7Message)aList.get(i)).getOBXNode(j).getObxSegmentID()+"')";
                db.RunSQL(sql);
            }
            
            //Insert the NTETag info into the oBRSegment table
            for(j=0;j<((MDSHL7Message)aList.get(i)).getNTEArrayListSize();j++){
                sql ="insert into mdsNTE (segmentID,sourceOfComment,comment,associatedOBX) VALUES ("+insertID +",'"+es( ((MDSHL7Message)aList.get(i)).getNTENode(j).getSourceOfComment())+"','"+es( ((MDSHL7Message)aList.get(i)).getNTENode(j).getComment())+"','"+ ((MDSHL7Message)aList.get(i)).getNTENode(j).getAssociatedOBX()+"')";
                db.RunSQL(sql);
            }
            
            db.CloseConn();
        }catch(Exception e){
            logger.info("Error writing Segment to Database");
            try{
                if(db !=null){
                    db.CloseConn();
                }
            }
            catch(Exception f){
                logger.info("Error closing connection");
            }
        }
        return insertID;
    }
    
    
    public boolean parseFile(String fileName){       
        logger.info("Parsing File "+fileName);
        boolean retval = true;
        try{
            int i=-1;//the MSH header counter
            int j=0; //the ZMN counter
            int k=0; //the ZRG counter
            int l=0; //the ZFR counter
            int m=0; //the ZMC counter
            int n=0; //the ZCT counter
            int o=0; //the OBX counter
            int p=0; //the NTE counter
            int q=0; //the OBR counter
            
            int obr=0;
            boolean tag = false;
            fReader.openFile(fileName);
            while ((currentLine = fReader.getNextLine())!=null){
                
                tag=false;
                //condition the HL7 file from MDS so it can be taken apart
                //by the StringTokenizer
                currentLine = currentLine.replaceAll("\\x7c"," |");
                //MDS sometimes drops the last field and a pipe or two may go
                //missing.  The MDS parser looks for the number of pipes and
                //does some kind of for loop based on that number, this parser
                //instead splits on the pipes without counting the number.  In
                //order to gaurantee a null field at the end of a line insead of
                //a no.such.element error the following line pads the MDS input.
                //This is sort of a hack, but what can you do.
                // if ( ! currentLine.startsWith("ZMC") ) {
                currentLine = currentLine.concat(" | | | | | | |");
                // }
                //System.out.println(currentLine);
                stringToken = new StringTokenizer(currentLine,"|");
                thisSegment = getSegment(stringToken,1);
                
                if (thisSegment.equals("MSH")){
                    i++;//Start counting the MSH segments that go into the ArrayList aList
                    j=0;//Reset the ZMN count for each MSH segment
                    k=0;//Reset the ZRG count for each MSH segment
                    l=0;//Reset the ZFR count for each MSH segment
                    m=0;//Reset the ZMC count for each MSH segment
                    n=0;//Reset the ZCT count for each MSH segment
                    o=0;//Reset the OBX count for each MSH segment
                    p=0;//Reset the NTE count for each MSH segment
                    q=0;//Reset the OBR count for each MSH segment
                    aList.add(i,new MDSHL7Message());
                    ((MDSHL7Message)aList.get(i)).MSHTag.setMSHCheck(thisSegment);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setSendingApp(thisSegment);
                    thisSegment = getSegment(stringToken,4);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setDateTime(thisSegment);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setType(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setMessageConID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setProcessingID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setVersionID(thisSegment);
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setAcceptAckType(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).MSHTag.setAppAckType(thisSegment);
                    tag=true;
                }
                
                if (thisSegment.equals("PID")){
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setIntPatientID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setAltPatientID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setPatientName(thisSegment);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setDOB(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setSex(thisSegment);
                    thisSegment = getSegment(stringToken,5);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setHomePhone(thisSegment);
                    thisSegment = getSegment(stringToken,6);
                    ((MDSHL7Message)aList.get(i)).PIDTag.setHealthNumber(thisSegment);
                    tag=true;
                }
                
                if (thisSegment.equals("PV1")){
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setPatientClass(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setPatientLocation(thisSegment);
                    thisSegment = getSegment(stringToken,5);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setRefDoctor(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setConDoctor(thisSegment);
                    thisSegment = getSegment(stringToken,8);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setAdmDoctor(thisSegment);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setVNumber(thisSegment);
                    thisSegment = getSegment(stringToken,22);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setAccStatus(thisSegment);
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).PV1Tag.setAdmDateTime(thisSegment);
                    tag=true;
                }
                
                if (thisSegment.equals("ZLB")){
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setLabID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setLabIDVersion(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setLabAddress(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setPrimaryLab(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setPrimaryLabVersion(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setMDSLU(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZLBTag.setMDSLV(thisSegment);
                    tag=true;
                }
                
                if (thisSegment.equals("ZMN")){
                    ((MDSHL7Message)aList.get(i)).addZMNNode(j);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setResultMnemonic(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setResultMnemonicVersion(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setReportName(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setUnits(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setCumulativeSequence(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setReferenceRange(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setResultCode(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setReportForm(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setReportGroup(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMNNode(j).setReportGroupVersion(thisSegment);
                    //Increment j to prepare for the next pass through this block for this
                    //MSH segment
                    j++;
                    tag=true;
                }
                
                if (thisSegment.equals("ZRG")){
                    ((MDSHL7Message)aList.get(i)).addZRGNode(k);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setReportSequence(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setReportGroupID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setReportGroupVersion(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setReportFlags(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setReportGroupDesc(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setMDSIndex(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZRGNode(k).setReportGroupHeading(thisSegment);
                    //Increment k to prepare for the next pass through this block for this
                    //MSH segment
                    k++;
                    tag=true;
                }
                if (thisSegment.equals("ZFR")){
                    ((MDSHL7Message)aList.get(i)).addZFRNode(l);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getZFRNode(l).setReportForm(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZFRNode(l).setReportFormStatus(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZFRNode(l).setTestingLab(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZFRNode(l).setMedicalDirector(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZFRNode(l).setEditFlag(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZFRNode(l).setAbnormalFlag(thisSegment);
                    //Increment l to prepare for the next pass through this block for this
                    //MSH segment
                    l++;
                    tag=true;
                }
                if (thisSegment.equals("ZMC")){
                    ((MDSHL7Message)aList.get(i)).addZMCNode(m);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getZMCNode(m).setSetID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMCNode(m).setMessageCodeIdentifier(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMCNode(m).setNoMessageCodeDescLines(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMCNode(m).setSigFlag(thisSegment);
                    // thisSegment = getRemaining(stringToken);  // Treat the rest of the line as the comment
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getZMCNode(m).setMessageCodeDesc(thisSegment);
                    //Increment l to prepare for the next pass through this block for this
                    //MSH segment
                    m++;
                    tag=true;
                }
                
                if (thisSegment.equals("ZCL")){
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setSetID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setConsultDoc(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setClientAddress(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setRoute(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setStop(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setArea(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setReportSet(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setClientType(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setClientModemPool(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setClientFaxNumber(thisSegment);
                    //Sometimes the last token in the line is there and sometime it isn't
                    //if not an exception is thrown, this avoids that nasty little spill.
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).ZCLTag.setClientBakFax(thisSegment);
                    tag=true;
                }
                
                if (thisSegment.equals("ZCT")){
                    ((MDSHL7Message)aList.get(i)).addZCTNode(n);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getZCTNode(n).setBarCodeIdentifier(thisSegment);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getZCTNode(n).setPlacerGroupNo(thisSegment);
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).getZCTNode(n).setObservationDateTime(thisSegment);
                    //Increment l to prepare for the next pass through this block for this
                    //MSH segment
                    n++;
                    tag=true;
                }
                
                if (thisSegment.equals("OBR")){
                    ((MDSHL7Message)aList.get(i)).addOBRNode(q);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setObrSegmentID(q+1);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setPlacerOrderNo(thisSegment);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setUniversalServiceID(thisSegment);
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setObservationDateTime(thisSegment);
                    thisSegment = getSegment(stringToken,7);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setSpecimenRecDateTime(thisSegment);
                    thisSegment = getSegment(stringToken,6);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setFillerFieldOne(thisSegment);
                    thisSegment = getSegment(stringToken,7);
                    ((MDSHL7Message)aList.get(i)).getOBRNode(q).setQuantityTiming(thisSegment);
                    //Increment l to prepare for the next pass through this block for this
                    //MSH segment
                    q++;
                    tag=true;
                }
                
                if (thisSegment.equals("OBX")){
                    ((MDSHL7Message)aList.get(i)).addOBXNode(o);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setObxSegmentID(o+1);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setValueType(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setObservationIden(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setObservationSubID(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setObservationValue(thisSegment);
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setAbnormalFlags(thisSegment);
                    thisSegment = getSegment(stringToken,3);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setObservationResultStatus(thisSegment);
                    thisSegment = getSegment(stringToken,4);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setProducersID(thisSegment);
                    ((MDSHL7Message)aList.get(i)).getOBXNode(o).setAssociatedOBR(q);
                    //Increment l to prepare for the next pass through this block for this
                    //MSH segment
                    o++;
                    tag=true;
                }
                if (thisSegment.equals("NTE")){
                    ((MDSHL7Message)aList.get(i)).addNTENode(p);
                    thisSegment = getSegment(stringToken,2);
                    ((MDSHL7Message)aList.get(i)).getNTENode(p).setSourceOfComment(thisSegment);
                    thisSegment = getSegment(stringToken,1);
                    ((MDSHL7Message)aList.get(i)).getNTENode(p).setComment(thisSegment);
                    ((MDSHL7Message)aList.get(i)).getNTENode(p).setAssociatedOBX(o);
                    //Increment l to prepare for the next pass through this block for this
                    //MSH segment
                    p++;
                    tag=true;
                }
                if (!tag){
                    System.out.println("No Tag found");
                    retval = true;
                }
            }
            fReader.closeFile();
        }catch (Exception e){
            System.out.println("System caught on "+thisSegment);
            System.out.println(e);
            retval = false;
        }
        return retval;
    }
    
    public void printAll() {
        logger.info("Printing All to Std out");
        int i = 0;
        int j = 0;
        
        for(i=0;i<aList.size();i++) {
            System.out.println(" ");
            //MSHtag test
            System.out.println("***********MSH tag results**************" );
            System.out.println("This Line is      " +((MDSHL7Message)aList.get(i)).MSHTag.getMSHCheck()+ "  sendingApp is  " +((MDSHL7Message)aList.get(i)).MSHTag.getSendingApp() );
            System.out.println("dateTime          " +((MDSHL7Message)aList.get(i)).MSHTag.getDateTime()+ "  type is " +((MDSHL7Message)aList.get(i)).MSHTag.getType() );
            System.out.println("MessageConID is   " +((MDSHL7Message)aList.get(i)).MSHTag.getMessageConID()+ "  processingID is   " +((MDSHL7Message)aList.get(i)).MSHTag.getProcessingID() );
            System.out.println("versionID         " +((MDSHL7Message)aList.get(i)).MSHTag.getVersionID()+ "  acceptAckType " +((MDSHL7Message)aList.get(i)).MSHTag.getAcceptAckType() );
            System.out.println("appAckType        " +((MDSHL7Message)aList.get(i)).MSHTag.getAppAckType() );
            //PIDtag test
            System.out.println("***********PID tag results**************" );
            System.out.println("intPatientID is   " +((MDSHL7Message)aList.get(i)).PIDTag.getIntPatientID()+ "  altPatientID is  " +((MDSHL7Message)aList.get(i)).PIDTag.getAltPatientID() );
            System.out.println("patientName  is   " +((MDSHL7Message)aList.get(i)).PIDTag.getPatientName()+ "   dOB is " +((MDSHL7Message)aList.get(i)).PIDTag.getDOB() );
            System.out.println("sex is            " +((MDSHL7Message)aList.get(i)).PIDTag.getSex() + "   homePhone   " +((MDSHL7Message)aList.get(i)).PIDTag.getHomePhone() );
            System.out.println("healthNumber      " +((MDSHL7Message)aList.get(i)).PIDTag.getHealthNumber());
            //PV1tag test
            System.out.println("***********PV1 tag results**************" );
            System.out.println("patientClass      " +((MDSHL7Message)aList.get(i)).PV1Tag.getPatientClass());
            System.out.println("patientLocation   " +((MDSHL7Message)aList.get(i)).PV1Tag.getPatientLocation());
            System.out.println("refDoctor         " +((MDSHL7Message)aList.get(i)).PV1Tag.getRefDoctor());
            System.out.println("conDoctor         " +((MDSHL7Message)aList.get(i)).PV1Tag.getConDoctor());
            System.out.println("admDoctor         " +((MDSHL7Message)aList.get(i)).PV1Tag.getAdmDoctor());
            System.out.println("vNumber           " +((MDSHL7Message)aList.get(i)).PV1Tag.getVNumber());
            System.out.println("accStatus         " +((MDSHL7Message)aList.get(i)).PV1Tag.getAccStatus());
            System.out.println("admDateTime       " +((MDSHL7Message)aList.get(i)).PV1Tag.getAdmDateTime());
            //ZLBtag test
            System.out.println("***********ZLB tag results**************" );
            System.out.println("labID             " +((MDSHL7Message)aList.get(i)).ZLBTag.getLabID());
            System.out.println("labIDVersion      " +((MDSHL7Message)aList.get(i)).ZLBTag.getLabIDVersion());
            System.out.println("labAddress        " +((MDSHL7Message)aList.get(i)).ZLBTag.getLabAddress());
            System.out.println("primaryLab        " +((MDSHL7Message)aList.get(i)).ZLBTag.getPrimaryLab());
            System.out.println("primaryLabVersion " +((MDSHL7Message)aList.get(i)).ZLBTag.getPrimaryLabVersion());
            System.out.println("MDSLU             " +((MDSHL7Message)aList.get(i)).ZLBTag.getMDSLU());
            System.out.println("MDSLV             " +((MDSHL7Message)aList.get(i)).ZLBTag.getMDSLV());
            //ZMNtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZMNArrayListSize();j++) {
                System.out.println("***********ZMN tag results**************" + j);
                System.out.println("resultMnemonic    " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultMnemonic());
                System.out.println("resultMnemonicversion " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultMnemonicVersion());
                System.out.println("reportName        " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportName());
                System.out.println("units             " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getUnits());
                System.out.println("cumulativeSequence " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getCumulativeSequence());
                System.out.println("referenceRange    " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getReferenceRange());
                System.out.println("resultCode        " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getResultCode());
                System.out.println("reportForm        " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportForm());
                System.out.println("reportGroup       " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportGroup());
                System.out.println("reportGroupVersion " +((MDSHL7Message)aList.get(i)).getZMNNode(j).getReportGroupVersion());
            }
            //ZRGtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZRGArrayListSize();j++) {
                System.out.println("***********ZRG tag results**************" + j);
                System.out.println("reportSequence     " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportSequence());
                System.out.println("reportGroupID      " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupID());
                System.out.println("reportGroupVersion " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupVersion());
                System.out.println("reportFlags        " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportFlags());
                System.out.println("reportGroupDesc    " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupDesc());
                System.out.println("index              " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getMDSIndex());
                
                System.out.println("reportGroupHeader  " +((MDSHL7Message)aList.get(i)).getZRGNode(j).getReportGroupHeading());
            }
            //ZFRtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZFRArrayListSize();j++) {
                System.out.println("***********ZFR tag results**************" + j);
                System.out.println("reportForm         " +((MDSHL7Message)aList.get(i)).getZFRNode(j).getReportForm());
                System.out.println("reportFormStatus   " +((MDSHL7Message)aList.get(i)).getZFRNode(j).getReportFormStatus());
                System.out.println("testingLab         " +((MDSHL7Message)aList.get(i)).getZFRNode(j).getTestingLab());
                System.out.println("medicalDirector    " +((MDSHL7Message)aList.get(i)).getZFRNode(j).getMedicalDirector());
                System.out.println("editFlag           " +((MDSHL7Message)aList.get(i)).getZFRNode(j).getEditFlag());
                System.out.println("abnormalFlag       " +((MDSHL7Message)aList.get(i)).getZFRNode(j).getAbnormalFlag());
            }
            
            //ZMCtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZMCArrayListSize();j++) {
                System.out.println("***********ZMC tag results**************" + j);
                System.out.println("setID              " +((MDSHL7Message)aList.get(i)).getZMCNode(j).getSetID());
                System.out.println("codeIdentifier     " +((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeIdentifier());
                System.out.println("codeVersion        " +((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeVersion());
                System.out.println("codeDescLines      " +((MDSHL7Message)aList.get(i)).getZMCNode(j).getNoMessageCodeDescLines());
                System.out.println("sigFlag            " +((MDSHL7Message)aList.get(i)).getZMCNode(j).getSigFlag());
                System.out.println("codeDesc           " +((MDSHL7Message)aList.get(i)).getZMCNode(j).getMessageCodeDesc());
            }
            
            //ZCLtag test
            System.out.println("***********ZCL tag results**************");
            System.out.println("setId              " +((MDSHL7Message)aList.get(i)).ZCLTag.getSetID());
            System.out.println("consultDoc         " +((MDSHL7Message)aList.get(i)).ZCLTag.getConsultDoc());
            System.out.println("clientAddress      " +((MDSHL7Message)aList.get(i)).ZCLTag.getClientAddress());
            System.out.println("route              " +((MDSHL7Message)aList.get(i)).ZCLTag.getRoute());
            System.out.println("stop               " +((MDSHL7Message)aList.get(i)).ZCLTag.getStop());
            System.out.println("area               " +((MDSHL7Message)aList.get(i)).ZCLTag.getArea());
            System.out.println("reportSet          " +((MDSHL7Message)aList.get(i)).ZCLTag.getReportSet());
            System.out.println("clientType         " +((MDSHL7Message)aList.get(i)).ZCLTag.getClientType());
            System.out.println("clientModemPool    " +((MDSHL7Message)aList.get(i)).ZCLTag.getClientModemPool());
            System.out.println("clientFaxPool      " +((MDSHL7Message)aList.get(i)).ZCLTag.getClientFaxNumber());
            System.out.println("clientBakFax       " +((MDSHL7Message)aList.get(i)).ZCLTag.getClientBakFax());
            
            //ZCTtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getZCTArrayListSize();j++) {
                System.out.println("***********ZCT tag results**************" + j);
                System.out.println("barCodeIdentifier  " +((MDSHL7Message)aList.get(i)).getZCTNode(j).getBarCodeIdentifier());
                System.out.println("placerGroupNo      " +((MDSHL7Message)aList.get(i)).getZCTNode(j).getPlacerGroupNo());
                System.out.println("observationTimeDate " +((MDSHL7Message)aList.get(i)).getZCTNode(j).getObservationDateTime());
            }
            
            //OBRtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getOBRArrayListSize();j++) {
                System.out.println("***********OBR tag results**************" + j);
                System.out.println("placerOrderNo      " +((MDSHL7Message)aList.get(i)).getOBRNode(j).getPlacerOrderNo());
                System.out.println("universalServiceID " +((MDSHL7Message)aList.get(i)).getOBRNode(j).getUniversalServiceID());
                System.out.println("observationDateTime " +((MDSHL7Message)aList.get(i)).getOBRNode(j).getObservationDateTime());
                System.out.println("specimenRecDateTime " +((MDSHL7Message)aList.get(i)).getOBRNode(j).getSpecimenRecDateTime());
                System.out.println("fillerFieldOne     " +((MDSHL7Message)aList.get(i)).getOBRNode(j).getFillerFieldOne());
                System.out.println("quantityTiming     " +((MDSHL7Message)aList.get(i)).getOBRNode(j).getQuantityTiming());
            }
            
            //OBXtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getOBXArrayListSize();j++) {
                System.out.println("***********OBX tag results**************" + j);
                System.out.println("valueType          " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getValueType());
                System.out.println("observationIden    " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationIden());
                System.out.println("observationSubID   " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationSubID());
                System.out.println("observationValue   " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationValue());
                System.out.println("abnormalFlags      " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getAbnormalFlags());
                System.out.println("observationResults " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getObservationResultStatus());
                System.out.println("producersID        " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getProducersID());
                System.out.println("associatedOBR      " +((MDSHL7Message)aList.get(i)).getOBXNode(j).getAssociatedOBR());
            }
            //NTEtag test
            for(j=0;j<((MDSHL7Message)aList.get(i)).getNTEArrayListSize();j++) {
                System.out.println("***********NTE tag results**************" + j);
                System.out.println("sourceOfComment    " +((MDSHL7Message)aList.get(i)).getNTENode(j).getSourceOfComment());
                System.out.println("comment            " +((MDSHL7Message)aList.get(i)).getNTENode(j).getComment());
                System.out.println("associatedOBX      " +((MDSHL7Message)aList.get(i)).getNTENode(j).getAssociatedOBX());
            }
        }
    }
    
    public void printSegment(String title,String filename, MDSHL7Message segm){
        logger.info("Printing Segment to file :"+filename);
        int i = 0;
        int j = 0;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename));
            out.write(title);
            
            
            
            out.write(" ");
            //MSHtag test
            out.write("***********MSH tag results**************" );
            out.write("This Line is      " +segm.MSHTag.getMSHCheck()+ "  sendingApp is  " +segm.MSHTag.getSendingApp() );
            out.write("dateTime          " +segm.MSHTag.getDateTime()+ "  type is " +segm.MSHTag.getType() );
            out.write("MessageConID is   " +segm.MSHTag.getMessageConID()+ "  processingID is   " +segm.MSHTag.getProcessingID() );
            out.write("versionID         " +segm.MSHTag.getVersionID()+ "  acceptAckType " +segm.MSHTag.getAcceptAckType() );
            out.write("appAckType        " +segm.MSHTag.getAppAckType() );
            //PIDtag test
            out.write("***********PID tag results**************" );
            out.write("intPatientID is   " +segm.PIDTag.getIntPatientID()+ "  altPatientID is  " +segm.PIDTag.getAltPatientID() );
            out.write("patientName  is   " +segm.PIDTag.getPatientName()+ "   dOB is " +segm.PIDTag.getDOB() );
            out.write("sex is            " +segm.PIDTag.getSex() + "   homePhone   " +segm.PIDTag.getHomePhone() );
            out.write("healthNumber      " +segm.PIDTag.getHealthNumber());
            //PV1tag test
            out.write("***********PV1 tag results**************" );
            out.write("patientClass      " +segm.PV1Tag.getPatientClass());
            out.write("patientLocation   " +segm.PV1Tag.getPatientLocation());
            out.write("refDoctor         " +segm.PV1Tag.getRefDoctor());
            out.write("conDoctor         " +segm.PV1Tag.getConDoctor());
            out.write("admDoctor         " +segm.PV1Tag.getAdmDoctor());
            out.write("vNumber           " +segm.PV1Tag.getVNumber());
            out.write("accStatus         " +segm.PV1Tag.getAccStatus());
            out.write("admDateTime       " +segm.PV1Tag.getAdmDateTime());
            //ZLBtag test
            out.write("***********ZLB tag results**************" );
            out.write("labID             " +segm.ZLBTag.getLabID());
            out.write("labIDVersion      " +segm.ZLBTag.getLabIDVersion());
            out.write("labAddress        " +segm.ZLBTag.getLabAddress());
            out.write("primaryLab        " +segm.ZLBTag.getPrimaryLab());
            out.write("primaryLabVersion " +segm.ZLBTag.getPrimaryLabVersion());
            out.write("MDSLU             " +segm.ZLBTag.getMDSLU());
            out.write("MDSLV             " +segm.ZLBTag.getMDSLV());
            //ZMNtag test
            for(j=0;j<segm.getZMNArrayListSize();j++) {
                out.write("***********ZMN tag results**************" + j);
                out.write("resultMnemonic    " +segm.getZMNNode(j).getResultMnemonic());
                out.write("resultMnemonicversion " +segm.getZMNNode(j).getResultMnemonicVersion());
                out.write("reportName        " +segm.getZMNNode(j).getReportName());
                out.write("units             " +segm.getZMNNode(j).getUnits());
                out.write("cumulativeSequence " +segm.getZMNNode(j).getCumulativeSequence());
                out.write("referenceRange    " +segm.getZMNNode(j).getReferenceRange());
                out.write("resultCode        " +segm.getZMNNode(j).getResultCode());
                out.write("reportForm        " +segm.getZMNNode(j).getReportForm());
                out.write("reportGroup       " +segm.getZMNNode(j).getReportGroup());
                out.write("reportGroupVersion " +segm.getZMNNode(j).getReportGroupVersion());
            }
            //ZRGtag test
            for(j=0;j<segm.getZRGArrayListSize();j++) {
                out.write("***********ZRG tag results**************" + j);
                out.write("reportSequence     " +segm.getZRGNode(j).getReportSequence());
                out.write("reportGroupID      " +segm.getZRGNode(j).getReportGroupID());
                out.write("reportGroupVersion " +segm.getZRGNode(j).getReportGroupVersion());
                out.write("reportFlags        " +segm.getZRGNode(j).getReportFlags());
                out.write("reportGroupDesc    " +segm.getZRGNode(j).getReportGroupDesc());
                out.write("index              " +segm.getZRGNode(j).getMDSIndex());
                
                out.write("reportGroupHeader  " +segm.getZRGNode(j).getReportGroupHeading());
            }
            //ZFRtag test
            for(j=0;j<segm.getZFRArrayListSize();j++) {
                out.write("***********ZFR tag results**************" + j);
                out.write("reportForm         " +segm.getZFRNode(j).getReportForm());
                out.write("reportFormStatus   " +segm.getZFRNode(j).getReportFormStatus());
                out.write("testingLab         " +segm.getZFRNode(j).getTestingLab());
                out.write("medicalDirector    " +segm.getZFRNode(j).getMedicalDirector());
                out.write("editFlag           " +segm.getZFRNode(j).getEditFlag());
                out.write("abnormalFlag       " +segm.getZFRNode(j).getAbnormalFlag());
            }
            
            //ZMCtag test
            for(j=0;j<segm.getZMCArrayListSize();j++) {
                out.write("***********ZMC tag results**************" + j);
                out.write("setID              " +segm.getZMCNode(j).getSetID());
                out.write("codeIdentifier     " +segm.getZMCNode(j).getMessageCodeIdentifier());
                out.write("codeVersion        " +segm.getZMCNode(j).getMessageCodeVersion());
                out.write("codeDescLines      " +segm.getZMCNode(j).getNoMessageCodeDescLines());
                out.write("sigFlag            " +segm.getZMCNode(j).getSigFlag());
                out.write("codeDesc           " +segm.getZMCNode(j).getMessageCodeDesc());
            }
            
            //ZCLtag test
            out.write("***********ZCL tag results**************");
            out.write("setId              " +segm.ZCLTag.getSetID());
            out.write("consultDoc         " +segm.ZCLTag.getConsultDoc());
            out.write("clientAddress      " +segm.ZCLTag.getClientAddress());
            out.write("route              " +segm.ZCLTag.getRoute());
            out.write("stop               " +segm.ZCLTag.getStop());
            out.write("area               " +segm.ZCLTag.getArea());
            out.write("reportSet          " +segm.ZCLTag.getReportSet());
            out.write("clientType         " +segm.ZCLTag.getClientType());
            out.write("clientModemPool    " +segm.ZCLTag.getClientModemPool());
            out.write("clientFaxPool      " +segm.ZCLTag.getClientFaxNumber());
            out.write("clientBakFax       " +segm.ZCLTag.getClientBakFax());
            
            //ZCTtag test
            for(j=0;j<segm.getZCTArrayListSize();j++) {
                out.write("***********ZCT tag results**************" + j);
                out.write("barCodeIdentifier  " +segm.getZCTNode(j).getBarCodeIdentifier());
                out.write("placerGroupNo      " +segm.getZCTNode(j).getPlacerGroupNo());
                out.write("observationTimeDate " +segm.getZCTNode(j).getObservationDateTime());
            }
            
            //OBRtag test
            for(j=0;j<segm.getOBRArrayListSize();j++) {
                out.write("***********OBR tag results**************" + j);
                out.write("placerOrderNo      " +segm.getOBRNode(j).getPlacerOrderNo());
                out.write("universalServiceID " +segm.getOBRNode(j).getUniversalServiceID());
                out.write("observationDateTime " +segm.getOBRNode(j).getObservationDateTime());
                out.write("specimenRecDateTime " +segm.getOBRNode(j).getSpecimenRecDateTime());
                out.write("fillerFieldOne     " +segm.getOBRNode(j).getFillerFieldOne());
                out.write("quantityTiming     " +segm.getOBRNode(j).getQuantityTiming());
            }
            
            //OBXtag test
            for(j=0;j<segm.getOBXArrayListSize();j++) {
                out.write("***********OBX tag results**************" + j);
                out.write("valueType          " +segm.getOBXNode(j).getValueType());
                out.write("observationIden    " +segm.getOBXNode(j).getObservationIden());
                out.write("observationSubID   " +segm.getOBXNode(j).getObservationSubID());
                out.write("observationValue   " +segm.getOBXNode(j).getObservationValue());
                out.write("abnormalFlags      " +segm.getOBXNode(j).getAbnormalFlags());
                out.write("observationResults " +segm.getOBXNode(j).getObservationResultStatus());
                out.write("producersID        " +segm.getOBXNode(j).getProducersID());
                out.write("associatedOBR      " +segm.getOBXNode(j).getAssociatedOBR());
            }
            //NTEtag test
            for(j=0;j<segm.getNTEArrayListSize();j++) {
                out.write("***********NTE tag results**************" + j);
                out.write("sourceOfComment    " +segm.getNTENode(j).getSourceOfComment());
                out.write("comment            " +segm.getNTENode(j).getComment());
                out.write("associatedOBX      " +segm.getNTENode(j).getAssociatedOBX());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    /**
     * The object stored in ArrayList al that holds the data specific
     * to each HL7 MSH tag within the MDS lab results file being parsed.
     * Specific data includes all the data in tags that have a one to one
     * relation with the MSH tag
     * @author  unascribed
     */
    public class MDSHL7Message {
        private MSHSegment MSHTag = new MSHSegment();
        private PIDSegment PIDTag = new PIDSegment();
        private PV1Segment PV1Tag = new PV1Segment();
        private ZLBSegment ZLBTag = new ZLBSegment();
        private ZCLSegment ZCLTag = new ZCLSegment();
        private ArrayList zMNList = new ArrayList();
        private ArrayList zRGList = new ArrayList();
        private ArrayList zFRList = new ArrayList();
        private ArrayList zMCList = new ArrayList();
        private ArrayList zCTList = new ArrayList();
        private ArrayList oBRList = new ArrayList();
        private ArrayList oBXList = new ArrayList();
        private ArrayList nTEList = new ArrayList();
        //ZMN Segment Collection Utils
        public void addZMNNode(int i) {
            zMNList.add(i,new ZMNSegment());
            return;
        }
        public ZMNSegment getZMNNode(int i) {
            return (ZMNSegment)(this.zMNList.get(i));
        }
        public int getZMNArrayListSize() {
            return zMNList.size();
        }
        //ZRG Segment Collection Utils
        public void addZRGNode(int i) {
            zRGList.add(i,new ZRGSegment());
            return;
        }
        public ZRGSegment getZRGNode(int i) {
            return (ZRGSegment)(this.zRGList.get(i));
        }
        public int getZRGArrayListSize() {
            return zRGList.size();
        }
        
        //ZRG Segment Collection Utils
        public void addZFRNode(int i) {
            zFRList.add(i,new ZFRSegment());
            return;
        }
        public ZFRSegment getZFRNode(int i) {
            return (ZFRSegment)(this.zFRList.get(i));
        }
        public int getZFRArrayListSize() {
            return zFRList.size();
        }
        
        //ZMC Segment Collection Utils
        public void addZMCNode(int i) {
            zMCList.add(i,new ZMCSegment());
            return;
        }
        public ZMCSegment getZMCNode(int i) {
            return (ZMCSegment)(this.zMCList.get(i));
        }
        public int getZMCArrayListSize() {
            return zMCList.size();
        }
        
        //ZCT Segment Collection Utils
        public void addZCTNode(int i) {
            zCTList.add(i,new ZCTSegment());
            return;
        }
        public ZCTSegment getZCTNode(int i) {
            return (ZCTSegment)(this.zCTList.get(i));
        }
        public int getZCTArrayListSize() {
            return zCTList.size();
        }
        
        //OBX Segment Collection Utils
        public void addOBXNode(int i) {
            oBXList.add(i,new OBXSegment());
            return;
        }
        public OBXSegment getOBXNode(int i) {
            return (OBXSegment)(this.oBXList.get(i));
        }
        public int getOBXArrayListSize() {
            return oBXList.size();
        }
        
        //OBX Segment Collection Utils
        public void addNTENode(int i) {
            nTEList.add(i,new NTESegment());
            return;
        }
        public NTESegment getNTENode(int i) {
            return (NTESegment)(this.nTEList.get(i));
        }
        public int getNTEArrayListSize() {
            return nTEList.size();
        }
        
        //OBR Segment Collection Utils
        public void addOBRNode(int i) {
            oBRList.add(i,new OBRSegment());
            return;
        }
        public OBRSegment getOBRNode(int i) {
            return (OBRSegment)(this.oBRList.get(i));
        }
        public int getOBRArrayListSize() {
            return oBRList.size();
        }
        
        private class MSHSegment {
            //MSH Segment Specific Info
            private String MSHCheck;
            private String sendingApp;
            private String dateTime;
            private String type;
            private String messageConID;
            private String processingID;
            private String versionID;
            private String acceptAckType;
            private String appAckType;
            
            public String getMSHCheck() {
                return this.MSHCheck;
            }
            public void setMSHCheck(String RHS) {
                this.MSHCheck = RHS;
                return;
            }
            
            public String getSendingApp() {
                return this.sendingApp;
            }
            public void setSendingApp(String RHS) {
                this.sendingApp = RHS;
                return;
            }
            
            public String getDateTime() {
                return this.dateTime;
            }
            public void setDateTime(String RHS) {
                this.dateTime = RHS;
                return;
            }
            
            public String getType() {
                return this.type;
            }
            public void setType(String RHS) {
                this.type = RHS;
                return;
            }
            
            public String getMessageConID() {
                return this.messageConID;
            }
            public void setMessageConID(String RHS) {
                this.messageConID = RHS;
                return;
            }
            
            public String getProcessingID() {
                return this.processingID;
            }
            public void setProcessingID(String RHS) {
                this.processingID = RHS;
                return;
            }
            
            public String getVersionID() {
                return this.versionID;
            }
            public void setVersionID(String RHS) {
                this.versionID = RHS;
                return;
            }
            
            public String getAcceptAckType() {
                return this.acceptAckType;
            }
            public void setAcceptAckType(String RHS) {
                this.acceptAckType = RHS;
                return;
            }
            
            public String getAppAckType() {
                return this.appAckType;
            }
            public void setAppAckType(String RHS) {
                this.appAckType = RHS;
                return;
            }
        }
        
        class PIDSegment {
            //PID Tag info
            private String intPatientID;
            private String altPatientID;
            private String patientName;
            private String dOB;
            private String sex;
            private String homePhone;
            private String healthNumber;
            
            public void setIntPatientID(String RHS) {
                this.intPatientID=RHS;
                return;
            }
            public String getIntPatientID() {
                return this.intPatientID;
            }
            
            public void setAltPatientID(String RHS) {
                this.altPatientID=RHS;
                return;
            }
            public String getAltPatientID() {
                return this.altPatientID;
            }
            
            public void setPatientName(String RHS) {
                this.patientName=RHS;
                return;
            }
            public String getPatientName() {
                return this.patientName;
            }
            
            public void setDOB(String RHS) {
                this.dOB=RHS;
                return;
            }
            public String getDOB() {
                return this.dOB;
            }
            
            public void setSex(String RHS) {
                this.sex=RHS;
                return;
            }
            public String getSex() {
                return this.sex;
            }
            
            public void setHomePhone(String RHS) {
                this.homePhone=RHS;
                return;
            }
            public String getHomePhone() {
                return this.homePhone;
            }
            
            public void setHealthNumber(String RHS) {
                this.healthNumber=RHS;
                return;
            }
            public String getHealthNumber() {
                return this.healthNumber;
            }
        }
        
        class PV1Segment {
            //PV1 Tag Info
            private String patientClass;
            private String patientLocation;
            private String refDoctor;
            private String conDoctor;
            private String admDoctor;
            private String vNumber;
            private String accStatus;
            private String admDateTime;
            
            public void setPatientClass(String RHS) {
                this.patientClass=RHS;
                return;
            }
            public String getPatientClass() {
                return this.patientClass;
            }
            
            public void setPatientLocation(String RHS) {
                this.patientLocation=RHS;
                return;
            }
            public String getPatientLocation() {
                return this.patientLocation;
            }
            
            public void setRefDoctor(String RHS) {
                this.refDoctor=RHS;
                return;
            }
            public String getRefDoctor() {
                return this.refDoctor;
            }
            
            public void setConDoctor(String RHS) {
                this.conDoctor=RHS;
                return;
            }
            public String getConDoctor() {
                return this.conDoctor;
            }
            
            
            public String getAdmDoctor() {
                return this.admDoctor;
            }
            
            public void setAdmDoctor(String RHS) {
                this.admDoctor=RHS;
                return;
            }
            
            public String getVNumber() {
                return this.vNumber;
            }
            
            public void setVNumber(String RHS) {
                this.vNumber=RHS;
                return;
            }
            public String getAccStatus() {
                return this.accStatus;
            }
            
            public void setAccStatus(String RHS) {
                this.accStatus=RHS;
                return;
            }
            public String getAdmDateTime() {
                return this.admDateTime;
            }
            
            public void setAdmDateTime(String RHS) {
                this.admDateTime=RHS;
                return;
            }
        }
        
        class ZLBSegment {
            //ZLB Tag Info
            private String labID;
            private String labIDVersion;
            private String labAddress;
            private String primaryLab;
            private String primaryLabVersion;
            private String MDSLU;
            private String MDSLV;
            
            public void setLabID(String RHS) {
                this.labID=RHS;
                return;
            }
            public String getLabID() {
                return this.labID;
            }
            
            public void setLabIDVersion(String RHS) {
                this.labIDVersion=RHS;
                return;
            }
            public String getLabIDVersion() {
                return this.labIDVersion;
            }
            
            public void setLabAddress(String RHS) {
                this.labAddress=RHS;
                return;
            }
            public String getLabAddress() {
                return this.labAddress;
            }
            
            public void setPrimaryLab(String RHS) {
                this.primaryLab=RHS;
                return;
            }
            public String getPrimaryLab() {
                return this.primaryLab;
            }
            
            public void setPrimaryLabVersion(String RHS) {
                this.primaryLabVersion=RHS;
                return;
            }
            public String getPrimaryLabVersion() {
                return this.primaryLabVersion;
            }
            
            public void setMDSLU(String RHS) {
                this.MDSLU=RHS;
                return;
            }
            public String getMDSLU() {
                return this.MDSLU;
            }
            
            public void setMDSLV(String RHS) {
                this.MDSLV=RHS;
                return;
            }
            public String getMDSLV() {
                return this.MDSLV;
            }
        }
        
        class ZMNSegment {
            //ZMN Tag info
            private String resultMnemonic;
            private String resultMnemonicVersion;
            private String reportName;
            private String units;
            private String cumulativeSequence;
            private String referenceRange;
            private String resultCode;
            private String reportForm;
            private String reportGroup;
            private String reportGroupVersion;
            
            public void setResultMnemonic(String RHS) {
                this.resultMnemonic=RHS;
                return;
            }
            public String getResultMnemonic() {
                return this.resultMnemonic;
            }
            
            public void setResultMnemonicVersion(String RHS) {
                this.resultMnemonicVersion=RHS;
                return;
            }
            public String getResultMnemonicVersion() {
                return this.resultMnemonicVersion;
            }
            
            public void setReportName(String RHS) {
                this.reportName=RHS;
                return;
            }
            public String getReportName() {
                return this.reportName;
            }
            
            public void setUnits(String RHS) {
                this.units=RHS;
                return;
            }
            public String getUnits() {
                return this.units;
            }
            
            public void setCumulativeSequence(String RHS) {
                this.cumulativeSequence=RHS;
                return;
            }
            public String getCumulativeSequence() {
                return this.cumulativeSequence;
            }
            
            public void setReferenceRange(String RHS) {
                this.referenceRange=RHS;
                return;
            }
            public String getReferenceRange() {
                return this.referenceRange;
            }
            
            public void setResultCode(String RHS) {
                this.resultCode=RHS;
                return;
            }
            public String getResultCode() {
                return this.resultCode;
            }
            
            public void setReportForm(String RHS) {
                this.reportForm=RHS;
                return;
            }
            public String getReportForm() {
                return this.reportForm;
            }
            
            public void setReportGroup(String RHS) {
                this.reportGroup=RHS;
                return;
            }
            public String getReportGroup() {
                return this.reportGroup;
            }
            
            public void setReportGroupVersion(String RHS) {
                this.reportGroupVersion=RHS;
                return;
            }
            public String getReportGroupVersion() {
                return this.reportGroupVersion;
            }
        }
        
        class ZRGSegment {
            //ZRG Tag info
            
            private String reportSequence;
            private String reportGroupID;
            private String reportGroupVersion;
            private String reportFlags;
            private String reportGroupDesc;
            private String mDSindex;
            private String reportGroupHeading;
            public void setReportGroupHeading(String RHS) {
                this.reportGroupHeading=RHS;
                return;
            }
            public String getReportGroupHeading() {
                return this.reportGroupHeading;
            }
            
            public void setReportSequence(String RHS) {
                this.reportSequence=RHS;
                return;
            }
            public String getReportSequence() {
                return this.reportSequence;
            }
            
            public void setReportGroupID(String RHS) {
                this.reportGroupID=RHS;
                return;
            }
            public String getReportGroupID() {
                return this.reportGroupID;
            }
            
            public void setReportGroupVersion(String RHS) {
                this.reportGroupVersion=RHS;
                return;
            }
            public String getReportGroupVersion() {
                return this.reportGroupVersion;
            }
            
            public void setReportFlags(String RHS) {
                this.reportFlags=RHS;
                return;
            }
            public String getReportFlags() {
                return this.reportFlags;
            }
            
            public void setReportGroupDesc(String RHS) {
                this.reportGroupDesc=RHS;
                return;
            }
            public String getReportGroupDesc() {
                return this.reportGroupDesc;
            }
            
            public void setMDSIndex(String RHS) {
                this.mDSindex=RHS;
                return;
            }
            public String getMDSIndex() {
                return this.mDSindex;
            }
            
        }
        
        class ZFRSegment {
            private String reportForm;
            private String reportFormStatus;
            private String testingLab;
            private String medicalDirector;
            private String editFlag;
            private String abnormalFlag;
            
            public void setReportForm(String RHS) {
                this.reportForm=RHS;
                return;
            }
            public String getReportForm() {
                return this.reportForm;
            }
            
            public void setAbnormalFlag(String RHS) {
                this.abnormalFlag=RHS;
                return;
            }
            public String getAbnormalFlag() {
                return this.abnormalFlag;
            }
            
            public void setEditFlag(String RHS) {
                this.editFlag=RHS;
                return;
            }
            public String getEditFlag() {
                return this.editFlag;
            }
            
            public void setMedicalDirector(String RHS) {
                this.medicalDirector=RHS;
                return;
            }
            public String getMedicalDirector() {
                return this.medicalDirector;
            }
            
            public void setTestingLab(String RHS) {
                this.testingLab=RHS;
                return;
            }
            public String getTestingLab() {
                return this.testingLab;
            }
            
            public void setReportFormStatus(String RHS) {
                this.reportFormStatus=RHS;
                return;
            }
            public String getReportFormStatus() {
                return this.reportFormStatus;
            }
        }
        
        class ZMCSegment {
            private String setID;
            private String messageCodeIdentifier;
            private String messageCodeVersion;
            private String noMessageCodeDescLines;
            private String sigFlag;
            private String messageCodeDesc;
            
            public void setSetID(String RHS) {
                this.setID = RHS;
                return;
            }
            public String getSetID() {
                return this.setID;
            }
            
            public void setMessageCodeIdentifier(String RHS) {
                this.messageCodeIdentifier = RHS;
                return;
            }
            public String getMessageCodeIdentifier() {
                return this.messageCodeIdentifier;
            }
            
            public void setMessageCodeVersion(String RHS) {
                this.messageCodeVersion = RHS;
                return;
            }
            public String getMessageCodeVersion() {
                return this.messageCodeVersion;
            }
            
            public void setNoMessageCodeDescLines(String RHS) {
                this.noMessageCodeDescLines = RHS;
                return;
            }
            public String getNoMessageCodeDescLines() {
                return this.noMessageCodeDescLines;
            }
            
            public void setSigFlag(String RHS) {
                this.sigFlag = RHS;
                return;
            }
            public String getSigFlag() {
                return this.sigFlag;
            }
            
            public void setMessageCodeDesc(String RHS) {
                this.messageCodeDesc = RHS;
                return;
            }
            public String getMessageCodeDesc() {
                return this.messageCodeDesc;
            }
        }
        
        class ZCLSegment {
            private String setID;
            private String consultDoc;
            private String clientAddress;
            private String route;
            private String stop;
            private String area;
            private String reportSet;
            private String clientType;
            private String clientModemPool;
            private String clientFaxNumber;
            private String clientBakFax;
            
            public void setSetID(String RHS) {
                this.setID = RHS;
                return;
            }
            public String getSetID() {
                return this.setID;
            }
            
            public void setConsultDoc(String RHS) {
                this.consultDoc = RHS;
                return;
            }
            public String getConsultDoc() {
                return this.consultDoc;
            }
            
            public void setClientAddress(String RHS) {
                this.clientAddress = RHS;
                return;
            }
            public String getClientAddress() {
                return this.clientAddress;
            }
            
            public void setRoute(String RHS) {
                this.route = RHS;
                return;
            }
            public String getRoute() {
                return this.route;
            }
            
            public void setStop(String RHS) {
                this.stop = RHS;
                return;
            }
            public String getStop() {
                return this.stop;
            }
            
            public void setArea(String RHS) {
                this.area = RHS;
                return;
            }
            public String getArea() {
                return this.area;
            }
            
            public void setReportSet(String RHS) {
                this.reportSet = RHS;
                return;
            }
            public String getReportSet() {
                return this.reportSet;
            }
            
            public void setClientType(String RHS) {
                this.clientType = RHS;
                return;
            }
            public String getClientType() {
                return this.clientType;
            }
            
            public void setClientModemPool(String RHS) {
                this.clientModemPool = RHS;
                return;
            }
            public String getClientModemPool() {
                return this.clientModemPool;
            }
            
            public void setClientFaxNumber(String RHS) {
                this.clientFaxNumber = RHS;
                return;
            }
            public String getClientFaxNumber() {
                return this.clientFaxNumber;
            }
            
            public void setClientBakFax(String RHS) {
                this.clientBakFax = RHS;
                return;
            }
            public String getClientBakFax() {
                return this.clientBakFax;
            }
        }
        public class ZCTSegment {
            private String barCodeIdentifier;
            private String placerGroupNo;
            private String observationDateTime;
            
            public void setBarCodeIdentifier(String RHS) {
                this.barCodeIdentifier=RHS;
                return;
            }
            public String getBarCodeIdentifier() {
                return this.barCodeIdentifier;
            }
            public void setPlacerGroupNo(String RHS) {
                this.placerGroupNo=RHS;
                return;
            }
            public String getPlacerGroupNo() {
                return this.placerGroupNo;
            }
            public void setObservationDateTime(String RHS) {
                this.observationDateTime=RHS;
                return;
            }
            public String getObservationDateTime() {
                return this.observationDateTime;
            }
            
        }
        public class OBRSegment {
            
            private String  placerOrderNo;
            private String  universalServiceID;
            private String  observationDateTime;
            private String  specimenRecDateTime;
            private String  fillerFieldOne;
            private String  quantityTiming;
            private int     obrSegmentID;
            
            public void setObrSegmentID(int obrSegmentID) {
                this.obrSegmentID = obrSegmentID;
                return;
            }
            public int getObrSegmentID() {
                return this.obrSegmentID;
            }
            
            
            public void setPlacerOrderNo(String RHS) {
                this.placerOrderNo=RHS;
                return;
            }
            public String getPlacerOrderNo() {
                return this.placerOrderNo;
            }
            public void setUniversalServiceID(String RHS) {
                this.universalServiceID=RHS;
                return;
            }
            public String getUniversalServiceID() {
                return this.universalServiceID;
            }
            public void setObservationDateTime(String RHS) {
                this.observationDateTime=RHS;
                return;
            }
            public String getObservationDateTime() {
                return this.observationDateTime;
            }
            public void setSpecimenRecDateTime(String RHS) {
                this.specimenRecDateTime=RHS;
                return;
            }
            public String getSpecimenRecDateTime() {
                return this.specimenRecDateTime;
            }
            public void setFillerFieldOne(String RHS) {
                this.fillerFieldOne=RHS;
                return;
            }
            public String getFillerFieldOne() {
                return this.fillerFieldOne;
            }
            public void setQuantityTiming(String RHS) {
                this.quantityTiming=RHS;
                return;
            }
            public String getQuantityTiming() {
                return this.quantityTiming;
            }
        }
        
        public class OBXSegment {
            private String valueType;
            private String observationIden;
            private String observationSubID;
            private String observationValue;
            private String abnormalFlags;
            private String observationResultStatus;
            private String producersID;
            private int    associatedOBR;
            private int    obxSegmentID;
            
            public void setObxSegmentID(int obxSegmentID) {
                this.obxSegmentID = obxSegmentID;
                return;
            }
            public int getObxSegmentID() {
                return this.obxSegmentID;
            }
            public void setAssociatedOBR(int associatedOBR) {
                this.associatedOBR = associatedOBR;
                return;
            }
            public int getAssociatedOBR() {
                return this.associatedOBR;
            }
            public void setValueType(String RHS) {
                this.valueType = RHS;
                return;
            }
            public String getValueType() {
                return this.valueType;
            }
            public void setObservationIden(String RHS) {
                this.observationIden = RHS;
                return;
            }
            public String getObservationIden() {
                return this.observationIden;
            }
            public void setObservationSubID(String RHS) {
                this.observationSubID = RHS;
                return;
            }
            public String getObservationSubID() {
                return this.observationSubID;
            }
            public void setObservationValue(String RHS) {
                this.observationValue = RHS;
                return;
            }
            public String getObservationValue() {
                return this.observationValue;
            }
            public void setAbnormalFlags(String RHS) {
                this.abnormalFlags = RHS;
                return;
            }
            public String getAbnormalFlags() {
                return this.abnormalFlags;
            }
            public void setObservationResultStatus(String RHS) {
                this.observationResultStatus = RHS;
                return;
            }
            public String getObservationResultStatus() {
                return this.observationResultStatus;
            }
            public void setProducersID(String RHS) {
                this.producersID = RHS;
                return;
            }
            public String getProducersID() {
                return this.producersID;
            }
            
        }
        
        class NTESegment {
            private String sourceOfComment;
            private String comment;
            private int associatedOBX;
            public void setSourceOfComment(String RHS) {
                this.sourceOfComment=RHS;
                return;
            }
            public String getSourceOfComment() {
                return this.sourceOfComment;
            }
            public void setComment(String RHS) {
                this.comment=RHS;
                return;
            }
            public String getComment() {
                return this.comment;
            }
            public void setAssociatedOBX(int RHS) {
                this.associatedOBX = RHS;
                return;
            }
            public int getAssociatedOBX() {
                return this.associatedOBX;
            }
        }
    }
}
