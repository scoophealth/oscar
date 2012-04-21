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
package oscar.oscarLab.ca.all;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.on.LabResultData;

public class SpireResultsData {
    
    private static Logger logger = MiscUtils.getLogger();
    
    public SpireResultsData() {
    }
    
    public static ArrayList<LabResultData> populateSpireResultsData(String providerNo, String demographicNo, String patientFirstName, String patientLastName, String patientHealthNumber, String status) {
        
        if ( providerNo == null) { providerNo = ""; }
        if ( patientFirstName == null) { patientFirstName = ""; }
        if ( patientLastName == null) { patientLastName = ""; }
        if ( status == null ) { status = ""; }
        
        patientHealthNumber=StringUtils.trimToNull(patientHealthNumber);
        
        ArrayList<LabResultData> labResults =  new ArrayList<LabResultData>();
        String sql = "";
        try {
            
            if ( demographicNo == null) {
                // note to self: lab reports not found in the providerLabRouting table will not show up - need to ensure every lab is entered in providerLabRouting, with '0'
                // for the provider number if unable to find correct provider
                
                sql = "select info.label,info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count, providerLabRouting.status " +
                        "from hl7TextInfo info, providerLabRouting " +
                        " where info.lab_no = providerLabRouting.lab_no "+
                        " AND providerLabRouting.status like '%"+status+"%' AND providerLabRouting.provider_no like '"+(providerNo.equals("")?"%":providerNo)+"'" +
                        " AND providerLabRouting.lab_type = 'HL7' " +
                        " AND info.first_name like '"+patientFirstName+"%' AND info.last_name like '"+patientLastName+"%'";
                
                if (patientHealthNumber!=null) sql=sql+" AND info.health_no like '%"+patientHealthNumber+"%'";
                
                sql=sql+" ORDER BY info.lab_no DESC";
                
            } else {
                
                sql = "select info.label,info.lab_no, info.sex, info.health_no, info.result_status, info.obr_date, info.priority, info.requesting_client, info.discipline, info.last_name, info.first_name, info.report_status, info.accessionNum, info.final_result_count " +
                        "from hl7TextInfo info, patientLabRouting " +
                        " where info.lab_no = patientLabRouting.lab_no "+
                        " AND patientLabRouting.lab_type = 'HL7' AND patientLabRouting.demographic_no='"+demographicNo+"' ORDER BY info.lab_no DESC";
            }
            
            logger.debug(sql);
            ResultSet rs = DBHandler.GetSQL(sql);
            while(rs.next()){

            	if (logger.isDebugEnabled())
            	{
            		int columns=rs.getMetaData().getColumnCount();
            		StringBuilder sb=new StringBuilder();
            		for (int i=0; i<columns; i++)
            		{
            			sb.append(rs.getString(i+1));
            			sb.append(", ");
            		}
            		logger.debug("Record found : "+sb.toString());
            	}
            	
                LabResultData lbData = new LabResultData(LabResultData.Spire);
                lbData.labType = LabResultData.Spire;
                lbData.segmentID = oscar.Misc.getString(rs, "lab_no");
                //check if any demographic is linked to this lab
                if(lbData.isMatchedToPatient()){
                    //get matched demographic no
                    //String sql2="select * from patientLabRouting plr where plr.lab_no="+Integer.parseInt(lbData.segmentID)+" and plr.lab_type='"+lbData.labType+"'";
                    String sql2="select * from patientLabRouting plr where plr.lab_no="+Integer.parseInt(lbData.segmentID)+" and plr.lab_type='HL7'";
                    logger.debug("sql2="+sql2);
                    ResultSet rs2=DBHandler.GetSQL(sql2);
                    if(rs2.next())
                        lbData.setLabPatientId(oscar.Misc.getString(rs2, "demographic_no"));
                    else
                        lbData.setLabPatientId("-1");
                }else{
                    lbData.setLabPatientId("-1");
                }
                
                if (demographicNo == null && !providerNo.equals("0")) {
                    lbData.acknowledgedStatus = oscar.Misc.getString(rs, "status");
                } else {
                    lbData.acknowledgedStatus ="U";
                }
                
                lbData.accessionNumber = oscar.Misc.getString(rs, "accessionNum");
                lbData.healthNumber = oscar.Misc.getString(rs, "health_no");
                lbData.patientName = oscar.Misc.getString(rs, "last_name")+", "+oscar.Misc.getString(rs, "first_name");
                lbData.sex = oscar.Misc.getString(rs, "sex");
                lbData.label = oscar.Misc.getString(rs, "label");
                
                lbData.resultStatus = oscar.Misc.getString(rs, "result_status");
                if (lbData.resultStatus.equals("A"))
                    lbData.abn = true;
                
                lbData.dateTime = oscar.Misc.getString(rs, "obr_date");
                
                //priority
                String priority = oscar.Misc.getString(rs, "priority");
                
                if(priority != null && !priority.equals("")){
                    switch ( priority.charAt(0) ) {
                        case 'C' : lbData.priority = "Critical"; break;
                        case 'S' : lbData.priority = "Stat/Urgent"; break;
                        case 'U' : lbData.priority = "Unclaimed"; break;
                        case 'A' : lbData.priority = "ASAP"; break;
                        case 'L' : lbData.priority = "Alert"; break;
                        default: lbData.priority = "Routine"; break;
                    }
                }else{
                    lbData.priority = "----";
                }
                
                lbData.requestingClient = oscar.Misc.getString(rs, "requesting_client");
                lbData.reportStatus =  oscar.Misc.getString(rs, "report_status");
                
                // the "C" is for corrected excelleris labs
                if (lbData.reportStatus != null && (lbData.reportStatus.equals("F") || lbData.reportStatus.equals("C"))){
                    lbData.finalRes = true;
                }else{
                    lbData.finalRes = false;
                }
                
                lbData.discipline = oscar.Misc.getString(rs, "discipline");
                lbData.finalResultsCount = rs.getInt("final_result_count");
                labResults.add(lbData);
            }
            rs.close();
        }catch(Exception e){
            logger.error("exception in Hl7Populate:", e);
        }
        return labResults;
    }
    
}

