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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

public class FrmONAREnhancedRecord extends FrmRecord {
	
	  
	 protected String _newDateFormat = "yyyy-MM-dd"; //handles both date formats, but yyyy/MM/dd is displayed to avoid deprecation

	
	public FrmONAREnhancedRecord() {
		this.dateFormat = "yyyy/MM/dd";
	}
	
	public Properties getFormRecord(LoggedInInfo loggedInInfo, int demographicNo, int existingID) throws SQLException {
		Properties props = new Properties();

		if (existingID <= 0) {

			this.setDemoProperties(loggedInInfo, demographicNo, props);
			props.setProperty("c_lastName", StringUtils.trimToEmpty(demographic.getLastName()));
			props.setProperty("c_firstName", StringUtils.trimToEmpty(demographic.getFirstName()));
			props.setProperty("c_hin", demographic.getHin());

			if ( "ON".equals(demographic.getHcType()) ) {
				props.setProperty("c_hinType", "OHIP");
			} else if ( "QC".equals(demographic.getHcType()) ) {
				props.setProperty("c_hinType", "RAMQ");
			} else {
				props.setProperty("c_hinType", "OTHER");
			}
			props.setProperty("c_fileNo", StringUtils.trimToEmpty(demographic.getChartNo()));

			props.setProperty("pg1_homePhone", StringUtils.trimToEmpty(demographic.getPhone()));
			props.setProperty("pg1_workPhone", StringUtils.trimToEmpty(demographic.getPhone2()));

			props.setProperty("formCreated", UtilDateUtilities.DateToString(new Date(), dateFormat));
			props.setProperty("pg1_formDate", UtilDateUtilities.DateToString(new Date(), dateFormat));
		} 
		else {
			//join it up so the resulting props have values from all 3 tables
			String sql = "SELECT * FROM formONAREnhancedRecord rec, formONAREnhancedRecordExt1 ext1, formONAREnhancedRecordExt2 ext2 WHERE rec.ID = ext1.ID and rec.ID = ext2.ID and rec.demographic_no = " + demographicNo + " AND rec.ID = " + existingID;
			props = (new FrmRecordHelp()).getFormRecord(sql);
		}

		return props;
	}

    public int saveFormRecord(Properties props) throws SQLException {
    	//get the names/types of each column in the 3 tables
        List<String> namesA = getColumnNames("formONAREnhancedRecord");
        List<String> namesB = getColumnNames("formONAREnhancedRecordExt1");
        List<String> namesC = getColumnNames("formONAREnhancedRecordExt2");
        
        //insert the initial record, and grab the ID to do the inserts on the other 2 tables
        int id = addRecord(props, "formONAREnhancedRecord", namesA,null);
        addRecord(props, "formONAREnhancedRecordExt1", namesB,id);
        addRecord(props, "formONAREnhancedRecordExt2", namesC,id);
         
        return id;
    }

    public Properties getPrintRecord(int demographicNo, int existingID) throws SQLException {
    	//join the 3 tables
    	String sql = "SELECT * FROM formONAREnhancedRecord rec, formONAREnhancedRecordExt1 ext1, formONAREnhancedRecordExt2 ext2 WHERE rec.ID = ext1.ID and rec.ID = ext2.ID and rec.demographic_no = " + demographicNo + " AND rec.ID = " + existingID;
		return ((new FrmRecordHelp()).getPrintRecord(sql));
    }

    public String findActionValue(String submit) throws SQLException {
        return ((new FrmRecordHelp()).findActionValue(submit));
    }

    public String createActionURL(String where, String action, String demoId, String formId) throws SQLException {
        return ((new FrmRecordHelp()).createActionURL(where, action, demoId, formId));
    }

	public boolean isSendToPing(String demoNo)  {
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

	
	private List<String> getColumnNames(String table) throws SQLException  {
		List<String> result = new ArrayList<String>();
		ResultSet rs2 = null; 

		try {
			rs2 = DBHandler.GetSQL("select * from " + table + " limit 1");
			
			ResultSetMetaData md = rs2.getMetaData();
			
			for (int i = 1; i <= md.getColumnCount(); i++) {
		    	String name = md.getColumnName(i);
		    	String type = md.getColumnTypeName(i);
		    	result.add(name + "|" + type);
			}
		}finally {
			if(rs2 != null)
				rs2.close();
		}
		
		return result;
	}
	
	int addRecord(Properties props,String table, List<String> namesA, Integer id) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "+ table+ " (");
		for(String name:namesA) {
			sb.append(name.split("\\|")[0] + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		
		sb.append(") VALUES (");
		
		for(String name:namesA) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(");");
		
		PreparedStatement preparedStmt = null;
		try {
			preparedStmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);	
			
			for(int x=0;x<namesA.size();x++) {
				String t = namesA.get(x);
				String theName = t.split("\\|")[0];
				String type = t.split("\\|")[1];
				
				if(theName.equals("ID")) {
					if(id == null) {
						preparedStmt.setNull(x+1, Types.INTEGER);
					} else {
						preparedStmt.setInt(x+1, id.intValue());
					}
					continue;
				}
				
				
				if(type.equals("VARCHAR") || type.equals("CHAR")) {
					String value = props.getProperty(theName);
					if(value == null) {
						preparedStmt.setNull(x+1, getType(type));
					} else {
						preparedStmt.setString(x+1,value);
					}
				}
				else if(type.equals("INT") || type.equals("TINYINT")) {
					String value = props.getProperty(theName);
					if(value != null && value.isEmpty()) {
						MiscUtils.getLogger().info("empty value for "+ theName);
					}
					if(value == null || value.isEmpty()) {
						value = "0";
					}
					else if (value.equalsIgnoreCase("on") || value.equalsIgnoreCase("checked='checked'")) {
	                    value = "1";
					}
					preparedStmt.setInt(x+1, Integer.parseInt(value));
				} else if(type.equals("DATE")) {
					String  value = props.getProperty(theName);
					Date d= null;
					
					if (theName.equalsIgnoreCase("formEdited")) {
	                    d = new Date();
	                } else {
	                    if ((value == null) || (value.indexOf('/') != -1))
	                        d = UtilDateUtilities.StringToDate(value, dateFormat);
	                    else
	                        d = UtilDateUtilities.StringToDate(value, _newDateFormat);
	                }
	                if (d == null)
	                    preparedStmt.setNull(x+1, Types.DATE);
	                else
	                	preparedStmt.setDate(x+1,  new java.sql.Date(d.getTime()));
	                				
				} else if(type.equals("TIMESTAMP")) {
					Date d;
	                if (theName.equalsIgnoreCase("formEdited")) {
	                    d = new Date();
	                } else {
	                    d = UtilDateUtilities.StringToDate(props.getProperty(theName), "yyyyMMddHHmmss");
	                }
	                if (d == null)
	                	 preparedStmt.setNull(x+1, Types.TIMESTAMP);
	                else
	                	preparedStmt.setTimestamp(x+1, new java.sql.Timestamp(d.getTime()));
	               
					
				} else {
					MiscUtils.getLogger().error("missing type handler for this column " + theName, new Exception());
				}
				
			}
			
			preparedStmt.executeUpdate();
			
			if(id == null) {
				ResultSet rs = null;
				try {
					 rs = preparedStmt.getGeneratedKeys();
					 
			         if(rs.next()) {
			             id = rs.getInt(1);
			         }
			     } finally {
			    	 if(rs != null)
			    		 rs.close();
			     }
		         
			}
		} finally {
			if(preparedStmt != null) {
				preparedStmt.close();
			}
		}
         
         return id;
	}




	private int getType(String type) {
		if(type.equals("VARCHAR")) {
			return Types.VARCHAR;
		}
		if(type.equals("CHAR")) {
			return Types.CHAR;
		}
		return -1;
	}
	
}

