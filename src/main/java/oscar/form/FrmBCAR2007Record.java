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


package oscar.form;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;
import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmBCAR2007Record extends FrmRecord {
    private String _dateFormat = "dd/MM/yyyy";
    private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

    public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {

            String sql = "SELECT demographic_no, last_name, first_name, sex, address, city, province, postal, phone, phone2, year_of_birth, month_of_birth, date_of_birth, hin, family_doctor FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                java.util.Date date = UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs
                        .getString("month_of_birth"), rs.getString("date_of_birth"));
                props.setProperty("demographic_no", rs.getString("demographic_no"));
                props
                        .setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(),
                                _dateFormat));
                // props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(UtilDateUtilities.Today(),_dateFormat));
                props.setProperty("c_surname", rs.getString("last_name"));
                props.setProperty("c_givenName", rs.getString("first_name"));
                props.setProperty("c_address", rs.getString("address"));
                props.setProperty("c_city", rs.getString("city"));
                props.setProperty("c_province", rs.getString("province"));
                props.setProperty("c_postal", rs.getString("postal"));
                props.setProperty("c_phn", rs.getString("hin"));
                props.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date, _dateFormat));
                props.setProperty("pg1_age", String.valueOf(UtilDateUtilities.calcAge(date)));
                props.setProperty("c_phone", rs.getString("phone"));
                props.setProperty("c_phoneAlt1", rs.getString("phone2"));
                props.setProperty("pg1_formDate", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), _dateFormat));
                props.setProperty("pg2_formDate", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), _dateFormat));
                props.setProperty("pg3_formDate", UtilDateUtilities
                        .DateToString(UtilDateUtilities.Today(), _dateFormat));
                String rd = SxmlMisc.getXmlContent(rs.getString("family_doctor"), "rd");
                rd = rd != null ? rd : "";
                props.setProperty("pg1_famPhy", rd);

                Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(""+demographicNo);
                String cell = demoExt.get("demo_cell");
                if ( cell != null ){
                    props.setProperty("c_phoneAlt2",cell );
                }

            }
            rs.close();
        } else {
            String sql = "SELECT * FROM formBCAR2007 WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            FrmRecordHelp frh = new FrmRecordHelp();
            frh.setDateFormat(_dateFormat);
            props = (frh).getFormRecord(sql);

            sql = "SELECT last_name, first_name, address, city, province, postal, phone,phone2, hin FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHelp.searchDBRecord(sql);
            if (rs.next()) {
                props.setProperty("c_surname_cur", rs.getString("last_name"));
                props.setProperty("c_givenName_cur", rs.getString("first_name"));
                props.setProperty("c_address_cur", rs.getString("address"));
                props.setProperty("c_city_cur", rs.getString("city"));
                props.setProperty("c_province_cur", rs.getString("province"));
                props.setProperty("c_postal_cur", rs.getString("postal"));
                props.setProperty("c_phn_cur", rs.getString("hin"));
                props.setProperty("c_phone_cur", rs.getString("phone"));
                props.setProperty("c_phoneAlt1_cur", rs.getString("phone2"));
                Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(""+demographicNo);
                String cell = demoExt.get("demo_cell");
                if ( cell != null ){
                    props.setProperty("c_phoneAlt2_cur",cell );
                }
            }
        }
        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formBCAR2007 WHERE demographic_no=" + demographic_no + " AND ID=0";

        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formBCAR2007 WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).createActionURL(where, action, demoId, formId));
    }

}
