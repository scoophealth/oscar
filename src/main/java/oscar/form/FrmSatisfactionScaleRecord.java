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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;


/*
 CREATE TABLE `formSatisfactionScale` (
  `ID` int(10) NOT NULL auto_increment,
  `demographic_no` int(10) NOT NULL default '0',
  `provider_no` int(10) default NULL,
  `formCreated` date default NULL,
  `formEdited` timestamp(14) NOT NULL,
  `studyID` varchar(20) NOT NULL default 'N/A',
  `believe1Y` tinyint(1) default NULL,
  `believe1N` tinyint(1) default NULL,
  `receive2Y` tinyint(1) default NULL,
  `receive2N` tinyint(1) default NULL,
  `receiveOT3Y` tinyint(1) default NULL,
  `receiveP3Y` tinyint(1) default NULL,
  `receiveB3Y` tinyint(1) default NULL,
  `otTreats4` char(4) default NULL,
  `ptTreats4` char(4) default NULL,
  `explaining1` char(2) default NULL,
  `everythingNeeded2` char(2) default NULL,
  `perfect3` char(2) default NULL,
  `wonder4` char(2) default NULL,
  `confident5` char(2) default NULL,
  `careful6` char(2) default NULL,
  `afford7` char(2) default NULL,
  `easyaccess8` char(2) default NULL,
  `toolong9` char(2) default NULL,
  `businesslike10` char(2) default NULL,
  `veryfriendly11` char(2) default NULL,
  `hurrytoomuch12` char(2) default NULL,
  `ignore13` char(2) default NULL,
  `doubtability14` char(2) default NULL,
  `plentyoftime15` char(2) default NULL,
  `hardtogetanappointment16` char(2) default NULL,
  `dissatisfied17` char(2) default NULL,
  `abletogetrehabilitation18` char(2) default NULL,
  PRIMARY KEY  (`ID`)
) TYPE=MyISAM 
 */

public class FrmSatisfactionScaleRecord extends FrmRecord {
	private String _dateFormat = "yyyy/MM/dd";

	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID)
		throws SQLException {
		Properties props = new Properties();
                
                
                ResultSet rs;
                String sql;

		if (existingID <= 0) {			
			sql = "SELECT demographic_no, sex, year_of_birth, month_of_birth, date_of_birth, phone FROM demographic WHERE demographic_no = "
                              + demographicNo;
			rs = DBHandler.GetSQL(sql);
			if (rs.next()) {
                                java.util.Date dob = UtilDateUtilities.calcDate(oscar.Misc.getString(rs, "year_of_birth"), oscar.Misc.getString(rs, "month_of_birth"), oscar.Misc.getString(rs, "date_of_birth"));
				props.setProperty(
					"demographic_no",
					oscar.Misc.getString(rs, "demographic_no"));
				props.setProperty(
					"formCreated",
					UtilDateUtilities.DateToString(
						new Date(),
						_dateFormat));	
                                props.setProperty("dob", UtilDateUtilities.DateToString(dob,"yyyy/MM/dd"));
                                props.setProperty("sex", oscar.Misc.getString(rs, "sex"));
                                props.setProperty("phone", oscar.Misc.getString(rs, "phone"));
			}
			rs.close();
                        sql = "SELECT studyID FROM rehabStudy2004 WHERE demographic_no='"+demographicNo + "'";
                        rs = DBHandler.GetSQL(sql);
                        if (rs.next()){
                            props.setProperty("studyID", oscar.Misc.getString(rs, "studyID"));
                        }
                        else{
                            props.setProperty("studyID", "N/A");
                        }
                        rs.close();
		} else {
			sql =
				"SELECT * FROM formSatisfactionScale WHERE demographic_no = "
					+ demographicNo
					+ " AND ID = "
					+ existingID;
			rs = DBHandler.GetSQL(sql);

                        if(rs.next())
                        {
                            MiscUtils.getLogger().debug("getting metaData");
                            ResultSetMetaData md = rs.getMetaData();

                            for(int i=1; i<=md.getColumnCount(); i++)
                            {
                                String name = md.getColumnName(i);

                                String value;
                                    MiscUtils.getLogger().debug(" name = "+name+" type = "+md.getColumnTypeName(i)+" scale = "+md.getScale(i));
                                if(md.getColumnTypeName(i).equalsIgnoreCase("TINY"))           
                                {

                                    if(rs.getInt(i)==1)
                                    {
                                        value = "checked='checked'";
                                        MiscUtils.getLogger().debug("checking "+name);
                                    }
                                    else
                                    {
                                        value = "";
                                        MiscUtils.getLogger().debug("not checking "+name);
                                    }
                                }
                                else
                                {
                                    if(md.getColumnTypeName(i).equalsIgnoreCase("date"))
                                    {
                                        value = UtilDateUtilities.DateToString(rs.getDate(i),"yyyy/MM/dd");
                                    }
                                    else
                                    {
                                        value = oscar.Misc.getString(rs, i);
                                    }
                                }

                                if(value!=null)
                                {
                                    props.setProperty(name, value);
                                }
                            }
                        }
                        rs.close();
                    }
                    return props;
	}

	public int saveFormRecord(Properties props) throws SQLException {
		String demographic_no = props.getProperty("demographic_no");
		String sql =
			"SELECT * FROM formSatisfactionScale WHERE demographic_no="
				+ demographic_no
				+ " AND ID=0";

		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).saveFormRecord(props, sql));
	}

	public Properties getPrintRecord(int demographicNo, int existingID)
		throws SQLException {
		String sql =
			"SELECT * FROM formSatisfactionScale WHERE demographic_no = "
				+ demographicNo
				+ " AND ID = "
				+ existingID;
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).getPrintRecord(sql));
	}

	public String findActionValue(String submit) throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).findActionValue(submit));
	}

	public String createActionURL(
		String where,
		String action,
		String demoId,
		String formId)
		throws SQLException {
		FrmRecordHelp frh = new FrmRecordHelp();
		frh.setDateFormat(_dateFormat);
		return ((frh).createActionURL(where, action, demoId, formId));
	}

}
