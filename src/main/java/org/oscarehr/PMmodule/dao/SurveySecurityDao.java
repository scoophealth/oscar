/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.dao;

import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class SurveySecurityDao {

	//switch the quatro security manager when available
	//true = allowed
	//false = restricted
	public boolean checkPrivilege(String formName, String providerNo) throws SQLException {
        java.sql.ResultSet rs;
        
        //check to see if there's a privilege defined
        String sqlCheck = "select count(*) as count from secObjPrivilege where objectName = '_ucf." + formName + "'";
        rs = DBHandler.GetSQL(sqlCheck);
        if(!rs.next()) {
        	rs.close();
        	return true;
        }
        if(rs.getInt("count") == 0) {
        	rs.close();
        	return true;
        }
        rs.close();
        
        String sql = new String("select r.provider_no,roleUserGroup,privilege from secObjPrivilege p ,secUserRole r where p.roleUserGroup=r.role_name and p.objectName='_ucf."+formName+"' and p.privilege='x' and r.provider_no='"+providerNo+"'");
        rs = DBHandler.GetSQL(sql);
		if(rs.next()) {
			rs.close();
			return true;
		} else {
			rs.close();
			return false;
		}
	}
}
