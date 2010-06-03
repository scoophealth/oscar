/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version. * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. * * You should have
 * received a copy of the GNU General Public License along with this program; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * <OSCAR
 * TEAM> This software was written for the Department of Family Medicine McMaster University
 * Hamilton Ontario, Canada
 */
package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmLabReq07Record extends FrmRecord {
	private DemographicDao demographicDao=(DemographicDao) SpringUtils.getBean("demographicDao");
	
	public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
        	Demographic demographic=demographicDao.getDemographicById(demographicNo);
        	
            if (demographic!=null) {
                props.setProperty("demographic_no", String.valueOf(demographic.getDemographicNo()));
                props.setProperty("patientName", demographic.getLastName()+", "+ demographic.getFirstName());
                props.setProperty("healthNumber", demographic.getHin());
                props.setProperty("version", demographic.getVer());
                props.setProperty("hcType", demographic.getHcType());
                props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                        "yyyy/MM/dd"));

                //props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(), "yyyy/MM/dd"));
                java.util.Date dob = UtilDateUtilities.calcDate(demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth());
                props.setProperty("birthDate", UtilDateUtilities.DateToString(dob, "yyyy/MM/dd"));

                props.setProperty("phoneNumber", demographic.getPhone());
                props.setProperty("patientAddress", demographic.getAddress());
                props.setProperty("patientCity", demographic.getCity());
                props.setProperty("patientPC", demographic.getPostal());
                props.setProperty("province", demographic.getProvince());
                props.setProperty("sex", demographic.getSex());
                props.setProperty("demoProvider", demographic.getProviderNo());
            }

            //get local clinic information
        	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT clinic_name, clinic_address, clinic_city, clinic_province, clinic_postal, clinic_phone, clinic_fax FROM clinic";
            ResultSet rs = db.GetSQL(sql);
            if (rs.next()) {
            	props.setProperty("clinicName",db.getString(rs,"clinic_name"));
            	props.setProperty("clinicProvince",db.getString(rs,"clinic_province"));
                props.setProperty("clinicAddress", db.getString(rs,"clinic_address"));
                props.setProperty("clinicCity", db.getString(rs,"clinic_city"));
                props.setProperty("clinicPC", db.getString(rs,"clinic_postal"));
            }
            rs.close();

        } else {
            String sql = "SELECT * FROM formLabReq07 WHERE demographic_no = " + demographicNo + " AND ID = "
                    + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }

    public Properties getFormCustRecord(Properties props, String provNo) throws SQLException {
        String demoProvider = props.getProperty("demoProvider", "");
        String xmlSpecialtyCode = "<xml_p_specialty_code>";
        String xmlSpecialtyCode2 = "</xml_p_specialty_code>";
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        ResultSet rs = null;
        String sql = null;

        if (!demoProvider.equals("")) {

            if (demoProvider.equals(provNo) ) {
                // from provider table
                sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName, ohip_no, comments "
                        + "FROM provider WHERE provider_no = '" + provNo + "'";
                rs = db.GetSQL(sql);

                if (rs.next()) {
                    String comments = db.getString(rs,"comments");                    
                    String strSpecialtyCode = "00";
                    if( comments.indexOf(xmlSpecialtyCode) != -1 ) {
                        strSpecialtyCode = comments.substring(comments.indexOf(xmlSpecialtyCode) + xmlSpecialtyCode.length(), comments.indexOf(xmlSpecialtyCode2));
                        strSpecialtyCode = strSpecialtyCode.trim();
                        if( strSpecialtyCode.equals("") ) {
                            strSpecialtyCode = "00";
                        }
                    }
                    String num = db.getString(rs,"ohip_no");
                    props.setProperty("reqProvName", db.getString(rs,"provName"));
                    props.setProperty("provName", db.getString(rs,"provName"));
                    props.setProperty("practitionerNo", "0000-" + num + "-" + strSpecialtyCode);
                }
                rs.close();
            } else {
                // from provider table
                sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName, ohip_no, comments FROM provider WHERE provider_no = '"
                        + provNo + "'";
                rs = db.GetSQL(sql);
                
                String num = "";
                if (rs.next()) {
                    String comments = db.getString(rs,"comments");
                    String strSpecialtyCode = "00";
                    if( comments.indexOf(xmlSpecialtyCode) != -1 ) {
                        strSpecialtyCode = comments.substring(comments.indexOf(xmlSpecialtyCode)+xmlSpecialtyCode.length(), comments.indexOf(xmlSpecialtyCode2));
                        strSpecialtyCode = strSpecialtyCode.trim();
                        if( strSpecialtyCode.equals("") ) {
                            strSpecialtyCode = "00";
                        }
                    }
                    num = db.getString(rs,"ohip_no");
                    props.setProperty("reqProvName", db.getString(rs,"provName"));                    
                    props.setProperty("practitionerNo", "0000-" + num + "-" + strSpecialtyCode);
                }
                rs.close();

                // from provider table
                sql = "SELECT CONCAT(last_name, ', ', first_name) AS provName, ohip_no FROM provider WHERE provider_no = "
                        + demoProvider;
                rs = db.GetSQL(sql);

                if (rs.next()) {
                    if( num.equals("") ) {
                        num = db.getString(rs,"ohip_no");
                        props.setProperty("practitionerNo", "0000-"+num+"-00");
                    }
                    props.setProperty("provName", db.getString(rs,"provName"));
                    
                }
                rs.close();
            }
        }
        //get local clinic information
        sql = "SELECT clinic_name, clinic_address, clinic_city, clinic_postal, clinic_province, clinic_phone, clinic_fax FROM clinic";
        rs = db.GetSQL(sql);
        if (rs.next()) {
        	props.setProperty("clinicName",db.getString(rs,"clinic_name"));
        	props.setProperty("clinicProvince",db.getString(rs,"clinic_province"));
            props.setProperty("clinicAddress", db.getString(rs,"clinic_address"));
            props.setProperty("clinicCity", db.getString(rs,"clinic_city"));
            props.setProperty("clinicPC", db.getString(rs,"clinic_postal"));
            
        }
        rs.close();

        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formLabReq07 WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formLabReq07 WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

}
