/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmDischargeSummaryRecord extends FrmRecord {
	private static Logger logger=MiscUtils.getLogger();
	public Properties getCaisiFormRecord(int demographicNo, int existingID, int providerNo, int programNo) {
        Properties props = new Properties();
        if (existingID <= 0) {

            String sql0 = "SELECT name AS programName FROM program WHERE id='"+programNo+"'";
            
            try {
	            ResultSet rs0 = DBHandler.GetSQL(sql0);
	            if(rs0.next()) {
	            	props.setProperty("programName",oscar.Misc.getString(rs0, "programName"));
	            }
	            rs0.close();
            } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            logger.error("",e);
            }
            
            
            

            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth, CONCAT(hin,ver) AS ohip FROM demographic WHERE demographic_no = "
                    + demographicNo;
            
            
            
            try {
	            ResultSet rs = DBHandler.GetSQL(sql);
	            if (rs.next()) {
	                Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"),
	                        oscar.Misc.getString(rs, "date_of_birth"));
	                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
	                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(),
	                        "yyyy/MM/dd"));
	                //props.setProperty("formEdited",
	                // UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd
	                // HH:mm:ss"));
	                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
	                props.setProperty("clientName", oscar.Misc.getString(rs, "clientName"));
	                props.setProperty("ohip",oscar.Misc.getString(rs, "ohip"));
	            }
	            rs.close();
            } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            logger.error("",e);
            }
            
            
            

            String sql1 = "SELECT CONCAT(CONCAT(last_name,', '),first_name) AS providerName FROM provider WHERE provider_no='"+providerNo+"'";
            
            
            
            try {
	            ResultSet rs1 = DBHandler.GetSQL(sql1);
	            if(rs1.next()) {
	            	props.setProperty("providerName",rs1.getString("providerName"));
	            }
	            rs1.close();
            } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            logger.error("",e);
            }
            
            

            String sql2 = "SELECT admission_date,discharge_date,admission_notes FROM admission where client_id="+demographicNo+" and program_id="+programNo+" and admission_status='current' ORDER BY admission_date DESC ";
           
            
            try {
	            ResultSet rs2 = DBHandler.GetSQL(sql2);
	            if(rs2.next()){
	            	if(rs2.isFirst()) {
	            		String admitDate = oscar.Misc.getString(rs2, "admission_date").substring(0,10);
	            		String admitDate_r = admitDate.replace("-", "/");
	            		props.setProperty("admitDate",admitDate_r);

	            	}
	            }
	            rs2.close();
            } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            logger.error("",e);
            }
            
            

            AllergyDao allergyDao=(AllergyDao) SpringUtils.getBean("allergyDao");
            List<Allergy> allergies=allergyDao.findAllergies(demographicNo);
			StringBuilder allergiesString = new StringBuilder();
			for (Allergy allergy : allergies) {
				if (allergiesString.length() != 0) {
					allergiesString.append(",");
				}

				allergiesString.append(allergy.getDescription());
			}

            props.setProperty("allergies",allergiesString.toString());

            StringBuilder issues = new StringBuilder();
            
            
            String sql4 = "SELECT issue_id from casemgmt_issue where demographic_no="+demographicNo+" and resolved=0";
             
            ResultSet rs4 = null;
            
            try {
	            rs4 = DBHandler.GetSQL(sql4);
	            while(rs4.next()) {
	            	
	            	String sql5 = "SELECT description from issue where issue_id="+oscar.Misc.getString(rs4, "issue_id");
	            	ResultSet rs5 = DBHandler.GetSQL(sql5);
	            	if(rs5.next()) {
	            		if(rs4.isFirst()) {
	            			issues.append(oscar.Misc.getString(rs5, "description"));
	            		}
	            		else {
	            			issues.append(";");
	            			issues.append(oscar.Misc.getString(rs5, "description"));
	            		}
	            	}
	            	rs5.close();    	
	            	
	            }
            } catch (SQLException e) {

	            logger.error("",e);
            } finally {
                props.setProperty("currentIssues",issues.toString());
	            if(rs4 != null) {
	                try {
		                rs4.close();
	                } catch (SQLException e) {

		                logger.error("",e);
	                }
	            }
            }
            


            StringBuilder prescriptions = new StringBuilder();
            String sql5 = "SELECT special from drugs where demographic_no="+demographicNo+" and archived=0 ORDER BY rx_date DESC, drugid DESC";
            ResultSet rs5 = null;
            
            try {
	            rs5 = DBHandler.GetSQL(sql5);
	            while(rs5.next()) {
	            	if(rs5.isFirst()) {
	            		prescriptions.append(oscar.Misc.getString(rs5, "special"));
	            	}
	            	else {
	            		//prescriptions.append(";");
	            		prescriptions.append(oscar.Misc.getString(rs5, "special"));
	            	}
	            }
	            props.setProperty("prescriptionSummary", prescriptions.toString());
	            rs5.close();
            } catch (SQLException e) {

	            logger.error("",e);
            } finally {
            	
            	if(rs5 != null) {
	                try {
		                rs5.close();
	                } catch (SQLException e) {

		                logger.error("",e);
	                }
	            }
            }
            
            
            
        } else {
        	
        	    	
            String sql = "SELECT * FROM formDischargeSummary WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            
            try {
	            props = (new FrmRecordHelp()).getFormRecord(sql);
            } catch (SQLException e) {
            	logger.error("", e);
            }
            
        }
        
        
        

        return props;
    }




	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();
        if (existingID <= 0) {

            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"),
                        oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(),
                        "yyyy/MM/dd"));
                //props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(new Date(), "yyyy-MM-dd
                // HH:mm:ss"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("clientName", oscar.Misc.getString(rs, "pName"));
            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formDischargeSummary WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        //
        String sql = "SELECT * FROM formDischargeSummary WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public String findActionValue(String submit) throws SQLException {
        if (submit != null && submit.equalsIgnoreCase("print")) {
            return "print";
        } else if (submit != null && submit.equalsIgnoreCase("save")) {
            return "save";
        } else if (submit != null && submit.equalsIgnoreCase("exit")) {
            return "exit";
        } else {
            return "failure";
        }
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        String temp = null;

        if (action.equalsIgnoreCase("print")) {
            temp = where + "?demoNo=" + demoId + "&formId=" + formId; // + "&study_no=" + studyId +
                                                                      // "&study_link" + studyLink;
        } else if (action.equalsIgnoreCase("save")) {
            temp = where + "?demographic_no=" + demoId + "&formId=" + formId; // "&study_no=" +
                                                                              // studyId +
                                                                              // "&study_link" +
                                                                              // studyLink; //+
        } else if (action.equalsIgnoreCase("exit")) {
            temp = where;
        } else {
            temp = where;
        }

        return temp;
    }

}
