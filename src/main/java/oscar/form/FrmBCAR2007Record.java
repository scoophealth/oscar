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

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.oscarehr.common.dao.DemographicExtDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;
import oscar.util.UtilDateUtilities;

public class FrmBCAR2007Record extends FrmRecord {
    private String _dateFormat = "dd/MM/yyyy";

    private DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);

    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
        
    	Demographic demo = null;
    	Properties props = new Properties();

        if (existingID <= 0) {

            demo = demographicManager.getDemographic(loggedInInfo, demographicNo);

            if (demo != null) {
                java.util.Date date = UtilDateUtilities.calcDate(demo.getYearOfBirth(), demo.getMonthOfBirth(), demo.getDateOfBirth());
                props.setProperty("demographic_no", demo.getDemographicNo().toString());
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(),_dateFormat));

                props.setProperty("c_surname", demo.getLastName());
                props.setProperty("c_givenName", demo.getFirstName());
                props.setProperty("c_address", demo.getAddress());
                props.setProperty("c_city", demo.getCity());
                props.setProperty("c_province", demo.getProvince());
                props.setProperty("c_postal", demo.getPostal());
                props.setProperty("c_phn", demo.getHin());
                props.setProperty("pg1_dateOfBirth", UtilDateUtilities.DateToString(date, _dateFormat));
                props.setProperty("pg1_age", String.valueOf(UtilDateUtilities.calcAge(date)));
                props.setProperty("c_phone", demo.getPhone());
                props.setProperty("c_phoneAlt1", demo.getPhone2());
                props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(new Date(), _dateFormat));
                props.setProperty("pg2_formDate", UtilDateUtilities.DateToString(new Date(), _dateFormat));
                props.setProperty("pg3_formDate", UtilDateUtilities.DateToString(new Date(), _dateFormat));
                
                String rd = SxmlMisc.getXmlContent(demo.getFamilyDoctor(), "rd");
                rd = rd != null ? rd : "";
                props.setProperty("pg1_famPhy", rd);

                Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(demographicNo);
                String cell = demoExt.get("demo_cell");
                if ( cell != null ){
                    props.setProperty("c_phoneAlt2",cell );
                }
            }
        } else {
            String sql = "SELECT * FROM formBCAR2007 WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            FrmRecordHelp frh = new FrmRecordHelp();
            frh.setDateFormat(_dateFormat);
            props = (frh).getFormRecord(sql);

            demo = demographicManager.getDemographic(loggedInInfo, demographicNo);
            
            if (demo != null) {
                props.setProperty("c_surname_cur", demo.getLastName());
                props.setProperty("c_givenName_cur", demo.getFirstName());
                props.setProperty("c_address_cur", demo.getAddress());
                props.setProperty("c_city_cur", demo.getCity());
                props.setProperty("c_province_cur", demo.getProvince());
                props.setProperty("c_postal_cur", demo.getPostal());
                props.setProperty("c_phn_cur", demo.getHin());
                props.setProperty("c_phone_cur", demo.getPhone());
                props.setProperty("c_phoneAlt1_cur", demo.getPhone2());
                Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(demographicNo);
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
