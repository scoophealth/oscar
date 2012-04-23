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


package oscar.oscarMDS.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;


public class MDSSegmentData {

    Logger logger = Logger.getLogger(MDSSegmentData.class);

    public String segmentID;

    public String reportDate;
    public String reportStatus;
    public String clientNo;
    public String accessionNo;
    public ProviderData providers;
    public ArrayList<Headers> headersArray = new ArrayList<Headers>();
    public ArrayList<ReportStatus> statusArray = new ArrayList<ReportStatus>();


    public void populateMDSSegmentData(String SID) {

        this.segmentID=SID;
        String sql =null;
        String associatedOBR = "";
        String queryString = "";
        String labID = "";
        int mdsOBXNum = 0;
        try{

            ResultSet rs;

            // Get the header info

            sql = "select mdsMSH.dateTime, mdsMSH.messageConID, min(mdsZFR.reportFormStatus) as reportFormStatus from mdsMSH, mdsZFR where "+
                    "mdsMSH.segmentID = mdsZFR.segmentID and mdsMSH.segmentID='"+segmentID+"' group by mdsMSH.segmentID";

            rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String d = oscar.Misc.getString(rs, "dateTime");

                // boneheaded calendar numbers months from 0
                cal.set(Integer.parseInt(d.substring(0,4)), Integer.parseInt(d.substring(5,7))-1, Integer.parseInt(d.substring(8,10)));

                reportDate = dateFormat.format(cal.getTime());
                reportStatus = ( oscar.Misc.getString(rs, "reportFormStatus").equals("1") ? "Final" : "Partial" );
                clientNo = oscar.Misc.getString(rs, "messageConID").split("-")[0];
                accessionNo = oscar.Misc.getString(rs, "messageConID").split("-")[1];
            }
            rs.close();


            // Get the lab ID

            sql = "select labID from mdsZLB where segmentID='"+this.segmentID+"'";

            rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                labID = oscar.Misc.getString(rs, "labID");
            }
            rs.close();

            // Get the providers

            sql = "select * from mdsPV1 where segmentID='"+this.segmentID+"'";

            rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                providers = new ProviderData(oscar.Misc.getString(rs, "refDoctor"), oscar.Misc.getString(rs, "conDoctor"), oscar.Misc.getString(rs, "admDoctor"));
            } else {
                providers = new ProviderData("", "", "");
            }
            rs.close();

            // Get the lab status

            sql = "select provider.first_name, provider.last_name, provider.provider_no, providerLabRouting.status, providerLabRouting.comment, providerLabRouting.timestamp from provider, providerLabRouting where provider.provider_no = providerLabRouting.provider_no and providerLabRouting.lab_no='"+this.segmentID+"' and providerLabRouting.lab_type='MDS'";

            rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                statusArray.add( new ReportStatus(oscar.Misc.getString(rs, "first_name")+" "+oscar.Misc.getString(rs, "last_name"), oscar.Misc.getString(rs, "provider_no"), descriptiveStatus(oscar.Misc.getString(rs, "status")), oscar.Misc.getString(rs, "comment"), oscar.Misc.getString(rs, "timestamp"), this.segmentID ) );
            }
            rs.close();

            // Get item descriptions and ranges and read into a hashtable

            Hashtable mnemonics = new Hashtable();
            sql = "select * from mdsZMN where segmentID='"+this.segmentID+"'";

            rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                mnemonics.put(oscar.Misc.getString(rs, "resultMnemonic"), new Mnemonics(oscar.Misc.getString(rs, "reportName"), oscar.Misc.getString(rs, "units"), oscar.Misc.getString(rs, "referenceRange")));
            }
            rs.close();

            // Process the notes

            Hashtable notes = new Hashtable();
            sql = "select * from mdsNTE where segmentID='"+this.segmentID+"'";

            rs = DBHandler.GetSQL(sql);
            while (rs.next()){
                if (notes.get(oscar.Misc.getString(rs, "associatedOBX")) == null) {
                    notes.put(oscar.Misc.getString(rs, "associatedOBX"), new ArrayList());
                }
                if (oscar.Misc.getString(rs, "sourceOfComment").equals("M")) {

                    ((ArrayList)notes.get(oscar.Misc.getString(rs, "associatedOBX"))).add(new String(oscar.Misc.getString(rs, "comment").substring(3)));
                } else {
                    if (oscar.Misc.getString(rs, "sourceOfComment").equals("MC")) {
                        sql = "select * from mdsZMC where segmentID='"+this.segmentID+"' and setID='"+oscar.Misc.getString(rs, "comment").substring(1)+"'";
                        ResultSet rs2;
                        rs2 = DBHandler.GetSQL(sql);
                        if (rs2.getFetchSize() == 0) {
                            sql = "select * from mdsZMC where segmentID='"+this.segmentID+"' and setID like '%"+oscar.Misc.getString(rs, "comment").substring(1, oscar.Misc.getString(rs, "comment").length()-1)+"%'";
                            rs2 = DBHandler.GetSQL(sql);
                        }
                        while (rs2.next()) {
                            ((ArrayList)notes.get(oscar.Misc.getString(rs, "associatedOBX"))).add(new String(rs2.getString("messageCodeDesc")));
                        }
                        rs2.close();

                        //logger.info("Found message note in MC format.  Comment:"+oscar.Misc.getString(rs,"comment"));
                        // ((ArrayList)notes.get(oscar.Misc.getString(rs,"associatedOBX"))).add(new String("MC - not yet implemented"));
                    } else {
                        logger.info("Found message note in unknown format.  Format:"+oscar.Misc.getString(rs, "sourceOfComment"));
                        ((ArrayList)notes.get(oscar.Misc.getString(rs, "associatedOBX"))).add(new String("Unknown note format!"));
                    }
                }
            }


            // Get the report section names

            sql = "select reportGroupDesc,reportGroupID,count(reportGroupID),reportGroupHeading,reportSequence from mdsZRG where segmentID='"+this.segmentID+"' group by reportGroupDesc, reportGroupID order by reportSequence";


            rs = DBHandler.GetSQL(sql);
            while(rs.next()){
                if (rs.getInt("count(reportGroupID)") == 1 && !oscar.Misc.getString(rs, "reportGroupHeading").equals("")) {
                    String[] rGH = { oscar.Misc.getString(rs, "reportGroupHeading") };
                    headersArray.add(new Headers(oscar.Misc.getString(rs, "reportGroupDesc"),oscar.Misc.getString(rs, "reportGroupID"), rGH));
                } else if (rs.getInt("count(reportGroupID)") > 1) {
                    sql = "select reportGroupHeading from mdsZRG where segmentID='"+this.segmentID+"' and reportGroupID='"+oscar.Misc.getString(rs, "reportGroupID")+"' order by reportSequence";
                    ResultSet rs2;
                    rs2 = DBHandler.GetSQL(sql);
                    ArrayList<String> tempArray = new ArrayList<String>();
                    while (rs2.next()) {
                        tempArray.add(rs2.getString("reportGroupHeading"));
                    }
                    rs2.close();
                    String[] reportGroupHeading = new String[tempArray.size()];
                    reportGroupHeading = tempArray.toArray(reportGroupHeading);
                    headersArray.add(new Headers(oscar.Misc.getString(rs, "reportGroupDesc"),oscar.Misc.getString(rs, "reportGroupID"), reportGroupHeading));
                } else {
                    headersArray.add(new Headers(oscar.Misc.getString(rs, "reportGroupDesc"),oscar.Misc.getString(rs, "reportGroupID"), null));
                }
            }
            rs.close();

            // Create the data structures for each section, grouped by OBR

            for(int i = 0;i< headersArray.size();i++){

                sql = "select resultCode from mdsZMN where segmentID='"+this.segmentID+"' and reportGroup='"+(headersArray.get(i)).reportSequence+"'";
                rs=DBHandler.GetSQL(sql);
                queryString = "";
                while(rs.next()){
                    if (queryString.length() == 0) {
                        queryString = "observationSubID like '%"+oscar.Misc.getString(rs, "resultCode")+"%'";
                    } else {
                        queryString = queryString.concat(" or observationSubID like '%"+oscar.Misc.getString(rs, "resultCode")+"%'");
                    }
                }
                rs.close();

                sql = "select distinct mdsOBX.associatedOBR, mdsOBR.observationDateTime from mdsOBX, mdsOBR where mdsOBX.segmentID = mdsOBR.segmentID and mdsOBX.associatedOBR = mdsOBR.obrID and mdsOBX.segmentID='"+this.segmentID+"' and ("+queryString+") order by associatedOBR";

                rs=DBHandler.GetSQL(sql);
                while(rs.next()){
                    (headersArray.get(i)).groupedReportsArray.add(new GroupedReports(oscar.Misc.getString(rs, "associatedOBR"), oscar.Misc.getString(rs, "observationDateTime"), queryString));
                }
                rs.close();
            }

            // Get the actual results

            Mnemonics thisOBXMnemonics = new Mnemonics();

            for(int i=0 ; i< (headersArray.size());i++){
                for (int j =0 ; j< (headersArray.get(i)).groupedReportsArray.size();j++){
                    associatedOBR =((headersArray.get(i)).groupedReportsArray.get(j)).associatedOBR;
                    queryString =((headersArray.get(i)).groupedReportsArray.get(j)).queryString;
                    sql = "select * from mdsOBX where segmentID='"+this.segmentID+"' and associatedOBR='"+associatedOBR+"' and ("+queryString+")";
                    rs = DBHandler.GetSQL(sql);
                    while(rs.next()){

                        mdsOBXNum=Integer.parseInt(oscar.Misc.getString(rs, "obxID"));
                        thisOBXMnemonics.update((Mnemonics)mnemonics.get(oscar.Misc.getString(rs, "observationIden").substring(1,oscar.Misc.getString(rs, "observationIden").indexOf('^'))));


                        ((headersArray.get(i)).groupedReportsArray.get(j)).resultsArray.add(
                                new Results(thisOBXMnemonics.reportName,
                                thisOBXMnemonics.referenceRange,
                                thisOBXMnemonics.units,
                                oscar.Misc.getString(rs, "observationValue"),
                                oscar.Misc.getString(rs, "abnormalFlags"),
                                oscar.Misc.getString(rs, "observationIden"),
                                oscar.Misc.getString(rs, "observationResultStatus"),
                                (ArrayList)notes.get(Integer.toString(mdsOBXNum)),
                                oscar.Misc.getString(rs, "producersID").substring(0,oscar.Misc.getString(rs, "producersID").indexOf('^')) ));

                    }
                    rs.close();
                }
            }
        }catch(SQLException e){
            logger.error("In MDS Segment Data: SQL: "+sql, e);
        }
    }

    public String descriptiveStatus(String status) {
        switch (status.charAt(0)) {
            case 'A' : return "Acknowledged";
            case 'F' : return "Filed but not acknowledged";
            case 'U' : return "N/A";
            default  : return "Not Acknowledged";
        }
    }

    // returns true if provider has already acknowledged this lab; false otherwise
    public boolean getAcknowledgedStatus(String providerNo) {

        for (int i=0; i < statusArray.size(); i++) {

            if ( ( statusArray.get(i)).getProviderNo().equals(providerNo) ) {
                // logger.info("Status of "+i+" is : "+ ((ReportStatus) statusArray.get(i)).getStatus() );
                return ( ( statusArray.get(i)).getStatus().startsWith("Ack") );
            }
        }
        return false;
    }

}
