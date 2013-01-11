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
import java.util.Properties;

import oscar.OscarProperties;
import oscar.util.UtilDateUtilities;

public class FrmONAREnhancedRecord extends FrmRecord {
	
	public FrmONAREnhancedRecord() {
		this.dateFormat = "yyyy/MM/dd";
	}
	
	public Properties getFormRecord(int demographicNo, int existingID) throws SQLException {
		Properties props = new Properties();

		if (existingID <= 0) {

			this.setDemoProperties(demographicNo, props);
			props.setProperty("c_lastName", demographic.getLastName());
			props.setProperty("c_firstName", demographic.getFirstName());
			props.setProperty("c_hin", demographic.getHin());

			if (demographic.getHcType().equals("ON")) {
				props.setProperty("c_hinType", "OHIP");
			} else if (demographic.getHcType().equals("QC")) {
				props.setProperty("c_hinType", "RAMQ");
			} else {
				props.setProperty("c_hinType", "OTHER");
			}
			props.setProperty("c_fileNo", demographic.getChartNo());

			props.setProperty("pg1_homePhone", demographic.getPhone());
			props.setProperty("pg1_workPhone", demographic.getPhone2());

			props.setProperty("formCreated", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), dateFormat));
			props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(UtilDateUtilities.Today(), dateFormat));
		} 
		else {
			String sql = "SELECT * FROM formONAREnhanced WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
		}

		return props;
	}

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formONAREnhanced WHERE demographic_no=" + demographic_no + " AND ID=0";

        return ((new FrmRecordHelp()).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
        String sql = "SELECT * FROM formONAREnhanced WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
        return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

	public boolean isSendToPing(String demoNo) throws SQLException {
		boolean ret = false;

		if ("yes".equalsIgnoreCase(OscarProperties.getInstance().getProperty("PHR", ""))) {

			if (this.demographic != null) {
				String demoEmail = this.demographic.getEmail();
				if (demoEmail != null && demoEmail.length() > 5 && demoEmail.matches(".*@.*")) {
					ret = true;
				}
			}
		}
		return ret;
	}

}
