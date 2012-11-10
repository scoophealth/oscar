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

import oscar.oscarDB.DBHandler;

/**
 * @author Yi Li
 * 
 * @deprecated Use appropriate DAO class instead.
 */
@Deprecated
public class BillingONDataHelp {


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
