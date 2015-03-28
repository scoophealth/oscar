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
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.LoggedInInfo;

import oscar.login.DBHelp;
import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.UtilDateUtilities;

public class FrmBCHPRecord extends FrmRecord {
    private String _dateFormat = "dd/MM/yyyy";

    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
            
            String sql = "SELECT demographic_no, last_name, first_name, phone, phone2, hin,provider_no FROM demographic WHERE demographic_no = "
                    + demographicNo;
            String providerNo = null;
            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                props.setProperty("demographic_no", rs.getString("demographic_no"));
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(),
                                _dateFormat));
                // props.setProperty("formEdited",
                // UtilDateUtilities.DateToString(new Date(),_dateFormat));
                props.setProperty("pg1_patientName", rs.getString("last_name")+", "+rs.getString("first_name"));
                props.setProperty("pg1_phn", rs.getString("hin"));
                props.setProperty("pg1_phone", rs.getString("phone") + "  " + rs.getString("phone2"));
                props.setProperty("pg1_formDate", UtilDateUtilities
                        .DateToString(new Date(), _dateFormat));
                providerNo = rs.getString("provider_no");
            }
            rs.close();
            
            if(providerNo != null && !providerNo.trim().equals("")){
                ProviderData proData = new ProviderData();
                proData.getProvider(providerNo);
                
                props.setProperty("pg1_msp",proData.getOhip_no());
                props.setProperty("pg1_md", "Dr. "+proData.getLast_name());
                
            }
        } else {
            String sql = "SELECT * FROM formBCHP WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            FrmRecordHelp frh = new FrmRecordHelp();
            frh.setDateFormat(_dateFormat);
            props = (frh).getFormRecord(sql);

            sql = "SELECT last_name, first_name, address, city, province, postal, phone,phone2, hin FROM demographic WHERE demographic_no = "
                    + demographicNo;
            ResultSet rs = DBHelp.searchDBRecord(sql);
            if (rs.next()) {
                props.setProperty("pg1_patientName_cur", rs.getString("last_name")+", "+rs.getString("first_name"));
                props.setProperty("pg1_phn_cur", rs.getString("hin"));
                props.setProperty("pg1_phone_cur", rs.getString("phone") + "  " + rs.getString("phone2"));
            }
        }
        return props;
    }

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formBCHP WHERE demographic_no=" + demographic_no + " AND ID=0";

        FrmRecordHelp frh = new FrmRecordHelp();
        frh.setDateFormat(_dateFormat);
        return ((frh).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formBCHP WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
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
