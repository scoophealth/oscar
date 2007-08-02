/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarMDS.data;

import org.apache.log4j.Logger;
import oscar.oscarDB.*;
import java.util.*;
import java.sql.*;
import java.text.SimpleDateFormat;


public class MDSSegmentData {
    
    Logger logger = Logger.getLogger(MDSSegmentData.class);
    
    public String segmentID;
    
    public String reportDate;
    public String reportStatus;
    public String clientNo;
    public String accessionNo;
    public ProviderData providers;
    public ArrayList headersArray = new ArrayList();
    public ArrayList statusArray = new ArrayList();
    
    
    public void populateMDSSegmentData(String SID) {
               
        this.segmentID=SID;
        String sql =null;
        String associatedOBR = "";
        String queryString = "";
        String labID = "";
        int mdsOBXNum = 0;
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            
            // Get the header info
            
            sql = "select mdsMSH.dateTime, mdsMSH.messageConID, min(mdsZFR.reportFormStatus) as reportFormStatus from mdsMSH, mdsZFR where "+
                    "mdsMSH.segmentID = mdsZFR.segmentID and mdsMSH.segmentID='"+segmentID+"' group by mdsMSH.segmentID";
            
            rs = db.GetSQL(sql);
            if (rs.next()) {
                GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String d = rs.getString("dateTime");
                
                // boneheaded calendar numbers months from 0
                cal.set(Integer.parseInt(d.substring(0,4)), Integer.parseInt(d.substring(5,7))-1, Integer.parseInt(d.substring(8,10)));
                
                reportDate = dateFormat.format(cal.getTime());
                reportStatus = ( rs.getString("reportFormStatus").equals("1") ? "Final" : "Partial" );
                clientNo = rs.getString("messageConID").split("-")[0];
                accessionNo = rs.getString("messageConID").split("-")[1];
            }
            rs.close();
            
            
            // Get the lab ID
            
            sql = "select labID from mdsZLB where segmentID='"+this.segmentID+"'";
            
            rs = db.GetSQL(sql);
            while(rs.next()){
                labID = rs.getString("labID");
            }
            rs.close();
            
            // Get the providers
            
            sql = "select * from mdsPV1 where segmentID='"+this.segmentID+"'";
            
            rs = db.GetSQL(sql);
            if (rs.next()) {
                providers = new ProviderData(rs.getString("refDoctor"), rs.getString("conDoctor"), rs.getString("admDoctor"));
            } else {
                providers = new ProviderData("", "", "");
            }
            rs.close();
            
            // Get the lab status
            
            sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='"+this.segmentID+"' and providerLabRouting.lab_type='MDS'";
            
            rs = db.GetSQL(sql);
            while(rs.next()){
                statusArray.add( new ReportStatus(rs.getString("first_name")+" "+rs.getString("last_name"), rs.getString("provider_no"), descriptiveStatus(rs.getString("status")), rs.getString("comment"), rs.getString("timestamp"), this.segmentID ) );
            }
            rs.close();
            
            // Get item descriptions and ranges and read into a hashtable
            
            Hashtable mnemonics = new Hashtable();
            sql = "select * from mdsZMN where segmentID='"+this.segmentID+"'";
            
            rs = db.GetSQL(sql);
            while(rs.next()){
                mnemonics.put(rs.getString("resultMnemonic"), new Mnemonics(rs.getString("reportName"), rs.getString("units"), rs.getString("referenceRange")));
            }
            rs.close();
            
            // Process the notes
            
            Hashtable notes = new Hashtable();
            sql = "select * from mdsNTE where segmentID='"+this.segmentID+"'";
            
            rs = db.GetSQL(sql);
            while (rs.next()){
                if (notes.get(rs.getString("associatedOBX")) == null) {
                    notes.put(rs.getString("associatedOBX"), new ArrayList());
                }
                if (rs.getString("sourceOfComment").equals("M")) {
                    
                    ((ArrayList)notes.get(rs.getString("associatedOBX"))).add(new String(rs.getString("comment").substring(3)));
                } else {
                    if (rs.getString("sourceOfComment").equals("MC")) {
                        sql = "select * from mdsZMC where segmentID='"+this.segmentID+"' and setID='"+rs.getString("comment").substring(1)+"'";
                        ResultSet rs2;
                        rs2 = db.GetSQL(sql);
                        if (rs2.getFetchSize() == 0) {
                            sql = "select * from mdsZMC where segmentID='"+this.segmentID+"' and setID like '%"+rs.getString("comment").substring(1, rs.getString("comment").length()-1)+"%'";
                            rs2 = db.GetSQL(sql);
                        }
                        while (rs2.next()) {
                            ((ArrayList)notes.get(rs.getString("associatedOBX"))).add(new String(rs2.getString("messageCodeDesc")));
                        }
                        rs2.close();
                        
                        //logger.info("Found message note in MC format.  Comment:"+rs.getString("comment"));
                        // ((ArrayList)notes.get(rs.getString("associatedOBX"))).add(new String("MC - not yet implemented"));
                    } else {
                        logger.info("Found message note in unknown format.  Format:"+rs.getString("sourceOfComment"));
                        ((ArrayList)notes.get(rs.getString("associatedOBX"))).add(new String("Unknown note format!"));
                    }
                }
            }
            
            
            // Get the report section names
            
            sql = "select reportGroupDesc,reportGroupID,count(reportGroupID),reportGroupHeading,reportSequence from mdsZRG where segmentID='"+this.segmentID+"' group by reportGroupDesc, reportGroupID order by reportSequence";
            
            
            rs = db.GetSQL(sql);
            while(rs.next()){
                if (rs.getInt("count(reportGroupID)") == 1 && !rs.getString("reportGroupHeading").equals("")) {
                    String[] rGH = { rs.getString("reportGroupHeading") };
                    headersArray.add(new Headers(rs.getString("reportGroupDesc"),rs.getString("reportGroupID"), rGH));
                } else if (rs.getInt("count(reportGroupID)") > 1) {
                    sql = "select reportGroupHeading from mdsZRG where segmentID='"+this.segmentID+"' and reportGroupID='"+rs.getString("reportGroupID")+"' order by reportSequence";
                    ResultSet rs2;
                    rs2 = db.GetSQL(sql);
                    ArrayList tempArray = new ArrayList();
                    while (rs2.next()) {
                        tempArray.add(rs2.getString("reportGroupHeading"));
                    }
                    rs2.close();
                    String[] reportGroupHeading = new String[tempArray.size()];
                    reportGroupHeading = (String[]) tempArray.toArray(reportGroupHeading);
                    headersArray.add(new Headers(rs.getString("reportGroupDesc"),rs.getString("reportGroupID"), reportGroupHeading));
                } else {
                    headersArray.add(new Headers(rs.getString("reportGroupDesc"),rs.getString("reportGroupID"), null));
                }
            }
            rs.close();
            
            // Create the data structures for each section, grouped by OBR
            
            for(int i = 0;i< headersArray.size();i++){
                
                sql = "select resultCode from mdsZMN where segmentID='"+this.segmentID+"' and reportGroup='"+((Headers)headersArray.get(i)).reportSequence+"'";
                rs=db.GetSQL(sql);
                queryString = "";
                while(rs.next()){
                    if (queryString.length() == 0) {
                        queryString = "observationSubID like '%"+rs.getString("resultCode")+"%'";
                    } else {
                        queryString = queryString.concat(" or observationSubID like '%"+rs.getString("resultCode")+"%'");
                    }
                }
                rs.close();
                
                sql = "select distinct mdsOBX.associatedOBR, mdsOBR.observationDateTime from mdsOBX, mdsOBR where mdsOBX.segmentID = mdsOBR.segmentID and mdsOBX.associatedOBR = mdsOBR.obrID and mdsOBX.segmentID='"+this.segmentID+"' and ("+queryString+") order by associatedOBR";
                
                rs=db.GetSQL(sql);
                while(rs.next()){
                    ((Headers)headersArray.get(i)).groupedReportsArray.add(new GroupedReports(rs.getString("associatedOBR"), rs.getString("observationDateTime"), queryString));
                }
                rs.close();
            }
            
            // Get the actual results
            
            Mnemonics thisOBXMnemonics = new Mnemonics();
            
            for(int i=0 ; i< (headersArray.size());i++){
                for (int j =0 ; j< ((Headers)headersArray.get(i)).groupedReportsArray.size();j++){
                    associatedOBR =((GroupedReports)((Headers)headersArray.get(i)).groupedReportsArray.get(j)).associatedOBR;
                    queryString =((GroupedReports)((Headers)headersArray.get(i)).groupedReportsArray.get(j)).queryString;
                    sql = "select * from mdsOBX where segmentID='"+this.segmentID+"' and associatedOBR='"+associatedOBR+"' and ("+queryString+")";
                    rs = db.GetSQL(sql);
                    while(rs.next()){
                        
                        mdsOBXNum=Integer.parseInt(rs.getString("obxID"));
                        thisOBXMnemonics.update((Mnemonics)mnemonics.get(rs.getString("observationIden").substring(1,rs.getString("observationIden").indexOf('^'))));
                        
                        
                        ((GroupedReports)((Headers)headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.add(
                                new Results(thisOBXMnemonics.reportName,
                                thisOBXMnemonics.referenceRange,
                                thisOBXMnemonics.units,
                                rs.getString("observationValue"),
                                rs.getString("abnormalFlags"),
                                rs.getString("observationIden"),
                                rs.getString("observationResultStatus"),
                                (ArrayList)notes.get(Integer.toString(mdsOBXNum)),
                                rs.getString("producersID").substring(0,rs.getString("producersID").indexOf('^')) ));
                        
                    }
                    rs.close();
                }
            }
            db.CloseConn();
        }catch(SQLException e){
            logger.error("In MDS Segment Data: SQL: "+sql, e);
        }
    }
    
    public String descriptiveStatus(String status) {
        switch (status.charAt(0)) {
            case 'A' : return "Acknowledged";
            case 'U' : return "N/A";
            default  : return "Not Acknowledged";
        }
    }
    
    // returns true if provider has already acknowledged this lab; false otherwise
    public boolean getAcknowledgedStatus(String providerNo) {
        
        for (int i=0; i < statusArray.size(); i++) {
            
            if ( ((ReportStatus) statusArray.get(i)).getProviderNo().equals(providerNo) ) {
                // logger.info("Status of "+i+" is : "+ ((ReportStatus) statusArray.get(i)).getStatus() );
                return ( ((ReportStatus) statusArray.get(i)).getStatus().startsWith("Ack") );
            }
        }
        return false;
    }
    
}

