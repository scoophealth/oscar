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

import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmDischargeSummaryRecord extends FrmRecord {

	public Properties getCaisiFormRecord(int demographicNo, int existingID, int providerNo, int programNo) throws SQLException  {
        Properties props = new Properties();
        if (existingID <= 0) {


            /**************will delete this section later *****************************/
            /* For Client Report in Seaton House */
            /* ******************

            String sql_cr1 = "SELECT demographic_no, last_name, first_name, CONCAT(date_of_birth,'/',month_of_birth,'/',year_of_birth) as dob from demographic";
            ResultSet rs_cr1 = DBHandler.GetSQL(sql_cr1);
            while(rs_cr1.next()) {
            	String fusion47_yn = "";
                String rotary12_yn = "";
                String programId = "";
                String program_name = "";
                String c_fusion47 = "";
                String c_rotary12 = "";
                String fusion47 = "";

            	String demographic_no = rs_cr1.getString("demographic_no");
            	String last_name = rs_cr1.getString("last_name");
            	String first_name = rs_cr1.getString("first_name");
            	String dob = rs_cr1.getString("dob");

            	String sql_cr2 = "select program_id from admission where client_id="+demographic_no+" and admission_status='current' ";
            	ResultSet rs_cr2 = DBHandler.GetSQL(sql_cr2);
        		while(rs_cr2.next()) {
        			programId = rs_cr2.getString("program_id");

        			String sql_cr22 = "select name from program where id="+programId+" and type='Bed' ";
            	//String sql_cr2 = "select p.name as program_name from program p, admission a where p.id=a.program_id and a.client_id="+demographic_no+" and a.admission_status='current' and p.type='Bed' ";
        			ResultSet rs_cr22 = DBHandler.GetSQL(sql_cr22);
        			if(rs_cr22.next()) {
        				program_name = rs_cr22.getString("name");
        			}
        			rs_cr22.close();
        		}

        		rs_cr2.close();

            	String sql_cr3 = "select count(*) as c_fusion47 from admission a where a.client_id="+demographic_no +" and a.admission_status='current' and a.program_id=47";
            	ResultSet rs_cr3 = DBHandler.GetSQL(sql_cr3);
        		if(rs_cr3.next()) {
        			c_fusion47 = rs_cr3.getString("c_fusion47");
        			if(c_fusion47.equals("0")) {
        				fusion47_yn = "No";
        			}
        			else
        				fusion47_yn = "Yes";
        		}
        		rs_cr3.close();

        		String sql_cr4 = "select count(*) as c_rotary12 from admission a where a.client_id="+demographic_no+" and a.admission_status='current' and a.program_id=12";
        		ResultSet rs_cr4 = DBHandler.GetSQL(sql_cr4);
        		if(rs_cr4.next()) {
        			c_rotary12 = rs_cr4.getString("c_rotary12");
        			if(c_rotary12.equals("0")) {
        				rotary12_yn = "No";
        			}
        			else
        				rotary12_yn = "Yes";
        		}
        		rs_cr4.close();

        		String sql_cr5 = "select i.code as code ,i.description as descr, ci.resolved as resolved,ci.type as type from casemgmt_issue ci, issue i where ci.demographic_no="+demographic_no+" and ci.issue_id=i.issue_id";
        		ResultSet rs_cr5 = DBHandler.GetSQL(sql_cr5);
        		while(rs_cr5.next()) {
        			String issue_code = rs_cr5.getString("code");
        			String issue_description = rs_cr5.getString("descr");
        			String casemgmt_issue_resolved = rs_cr5.getString("resolved");
        			String casemgmt_issue_type = rs_cr5.getString("type");
        			//issue_description.replace("'","'\'");
        			//UtilMisc.charEscape(issue_description,'\'');
        			//String sql_insert = "insert into clientReport values('"+program_name+"','"+demographic_no+"','"+last_name+"','"+first_name+"','"+dob+"','"+issue_code+"','"+issue_desription+"','"+casemgmt_issue_resolved+"','"+casemgmt_issue_type+"','"+fusion47_yn+"','"+rotary12_yn+"')";
        			String sql_insert = "insert into clientReport values('"+UtilMisc.charEscape(program_name,'\'')+"','"+UtilMisc.charEscape(demographic_no,'\'')+"','"+UtilMisc.charEscape(last_name,'\'')+"','"+UtilMisc.charEscape(first_name,'\'')+"','"+dob+"','"+UtilMisc.charEscape(issue_code,'\'')+"','"+UtilMisc.charEscape(issue_description,'\'')+"','"+casemgmt_issue_resolved+"','"+casemgmt_issue_type+"','"+fusion47_yn+"','"+rotary12_yn+"')";
        			MiscUtils.getLogger().debug(sql_insert);
        			DBHandler.RunSQL(sql_insert);

        		}
        		rs_cr5.close();

            }
            rs_cr1.close();

            *******************end *************************/

            String sql0 = "SELECT name AS programName FROM program WHERE id='"+programNo+"'";
            ResultSet rs0 = DBHandler.GetSQL(sql0);
            if(rs0.next()) {
            	props.setProperty("programName",oscar.Misc.getString(rs0, "programName"));
            }
            rs0.close();

            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth, CONCAT(hin,ver) AS ohip FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"),
                        oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        "yyyy/MM/dd"));
                //props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy-MM-dd
                // HH:mm:ss"));
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));
                props.setProperty("clientName", oscar.Misc.getString(rs, "clientName"));
                props.setProperty("ohip",oscar.Misc.getString(rs, "ohip"));
            }
            rs.close();

            String sql1 = "SELECT CONCAT(CONCAT(last_name,', '),first_name) AS providerName FROM provider WHERE provider_no='"+providerNo+"'";
            ResultSet rs1 = DBHandler.GetSQL(sql1);
            if(rs1.next()) {
            	props.setProperty("providerName",rs1.getString("providerName"));
            }
            rs1.close();

            //String sql2 = "SELECT admission_date,discharge_date,admission_notes FROM admission where client_id="+demographicNo+" and program_id="+programNo+" and admission_status='discharged' ORDER BY discharge_date DESC ";
            String sql2 = "SELECT admission_date,discharge_date,admission_notes FROM admission where client_id="+demographicNo+" and program_id="+programNo+" and admission_status='current' ORDER BY admission_date DESC ";
            ResultSet rs2 = DBHandler.GetSQL(sql2);
            if(rs2.next()){
            	if(rs2.isFirst()) {
            		String admitDate = oscar.Misc.getString(rs2, "admission_date").substring(0,10);
            		String admitDate_r = admitDate.replace("-", "/");
            		props.setProperty("admitDate",admitDate_r);

            	}
            }
            rs2.close();

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
            ResultSet rs4 = DBHandler.GetSQL(sql4);
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
            props.setProperty("currentIssues",issues.toString());
            rs4.close();

            StringBuilder prescriptions = new StringBuilder();
            //select d.rx_date,d.special,d.end_date, d.archived,d.BN,d.GCN_SEQNO,d.customName from Drug d where d.demographic_no = ? ORDER BY d.rx_date DESC, d.id DESC";
            String sql5 = "SELECT special from drugs where demographic_no="+demographicNo+" and archived=0 ORDER BY rx_date DESC, drugid DESC";
            ResultSet rs5 = DBHandler.GetSQL(sql5);
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
        } else {
            String sql = "SELECT * FROM formDischargeSummary WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }




	public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();
        if (existingID <= 0) {

            String sql = "SELECT demographic_no, CONCAT(CONCAT(last_name, ', '), first_name) AS clientName, year_of_birth, month_of_birth, date_of_birth FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"),
                        oscar.Misc.getString(rs, "date_of_birth"));
                props.setProperty("demographic_no", oscar.Misc.getString(rs, "demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        "yyyy/MM/dd"));
                //props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy-MM-dd
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
