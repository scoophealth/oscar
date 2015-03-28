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
import java.util.Properties;

import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.UtilDateUtilities;

public class FrmPositionHazardRecord extends FrmRecord {
    
    private ClinicDAO clinicDao = (ClinicDAO) SpringUtils.getBean("clinicDAO");
                
    public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
        Properties props = new Properties();

        if (existingID <= 0) {
        	Demographic demographic=demographicManager.getDemographic(loggedInInfo, demographicNo);

            if (demographic!=null) {
                props.setProperty("demographic_no", String.valueOf(demographic.getDemographicNo()));
                props.setProperty("patientName", demographic.getLastName()+", "+ demographic.getFirstName());
                props.setProperty("healthNumber", demographic.getHin());
                props.setProperty("version", demographic.getVer());
                props.setProperty("hcType", demographic.getHcType());
                props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), "yyyy/MM/dd"));

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
            Clinic clinic = clinicDao.getClinic();
            props.setProperty("clinicName",clinic.getClinicName());
            props.setProperty("clinicProvince",clinic.getClinicProvince());
            props.setProperty("clinicAddress", clinic.getClinicAddress());
            props.setProperty("clinicCity", clinic.getClinicCity());
            props.setProperty("clinicPC", clinic.getClinicPostal());

        } else {
            String sql = "SELECT * FROM formPositionHazard WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
            props = (new FrmRecordHelp()).getFormRecord(sql);
        }

        return props;
    }
        
    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formPositionHazard WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formPositionHazard WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }
}
