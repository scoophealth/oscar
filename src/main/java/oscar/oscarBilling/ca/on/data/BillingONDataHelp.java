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


/*
 * Created on Mar 14, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;

/**
 * @author Yi Li
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class BillingONDataHelp {
	public synchronized int saveBillingRecord(String sql) {
		int ret = 0;
		
		try {
			
			DBHandler.RunSQL(sql);

			/*
			 * if db_type = mysql return LAST_INSERT_ID() but if db_type =
			 * postgresql, return a prepared statement, since here we dont know
			 * which sequence will be used
			 */
			String db_type = OscarProperties.getInstance() != null ? OscarProperties.getInstance().getProperty(
					"db_type", "") : "";
			if (db_type.equals("") || db_type.equalsIgnoreCase("mysql")) {
				sql = "SELECT LAST_INSERT_ID()";
			} else if (db_type.equalsIgnoreCase("postgresql")) {
				sql = "SELECT CURRVAL('?')";
			} else {
				throw new SQLException("ERROR: Database " + db_type + " unrecognized.");
			}
			ResultSet rs = DBHandler.GetSQL(sql);
			if (rs.next())
				ret = rs.getInt(1);
			rs.close();
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return ret;
	}

	public synchronized boolean updateDBRecord(String sql) {
		boolean ret = false;
		
		try {
			
			ret = DBHandler.RunSQL(sql);
			ret = true;
		} catch (SQLException e) {
			ret = false;
			MiscUtils.getLogger().error("Error", e);
		}
		return ret;
	}

	public synchronized ResultSet searchDBRecord(String sql) {
		ResultSet ret = null;
		
		try {
			
			ret = DBHandler.GetSQL(sql);
		} catch (SQLException e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return ret;
	}
}
