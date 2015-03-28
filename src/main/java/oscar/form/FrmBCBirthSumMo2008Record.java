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

import org.oscarehr.util.LoggedInInfo;

import oscar.util.UtilDateUtilities;

public class FrmBCBirthSumMo2008Record extends FrmRecord {
	
	public FrmBCBirthSumMo2008Record() {
		this.dateFormat = "dd/MM/yyyy";
	}

	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {

		Properties props = new Properties();

		if (existingID <= 0) {
			this.setDemoProperties(loggedInInfo, demographicNo, props);
			
			props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), dateFormat));
			props.setProperty("formEdited", UtilDateUtilities.DateToString(new Date(), dateFormat));
			props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(new Date(), dateFormat));
		} 
		else {
			String sql = "SELECT * FROM formBCBirthSumMo2008 WHERE demographic_no = " + demographicNo + " AND ID = " + existingID;
			FrmRecordHelp frh = new FrmRecordHelp();
			frh.setDateFormat(dateFormat);
			props = (frh).getFormRecord(sql);
			
			this.setDemoCurProperties(loggedInInfo, demographicNo, props);
		}
		return props;
	}

    public int saveFormRecord(Properties props) throws SQLException {
        String demographic_no = props.getProperty("demographic_no");
        String sql = "SELECT * FROM formBCBirthSumMo2008 WHERE demographic_no=" +demographic_no +" AND ID=0";

		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
		return ((frh).saveFormRecord(props, sql));
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException  {
        String sql = "SELECT * FROM formBCBirthSumMo2008 WHERE demographic_no = " +demographicNo +" AND ID = " +existingID ;
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
		return ((frh).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
 		return ((frh).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(dateFormat);
 		return ((frh).createActionURL(where, action, demoId, formId));
    }

}
