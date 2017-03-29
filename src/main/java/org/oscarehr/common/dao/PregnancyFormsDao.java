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
package org.oscarehr.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.login.DBHelp;

/**
 * using OSCAR Forms...so DBHelp it is.
 * @author marc
 *
 */
public class PregnancyFormsDao {

	public static Integer getLatestFormIdByPregnancy(Integer episodeId) {
		String sql = "SELECT id from formONAREnhancedRecord WHERE episodeId="+episodeId+" ORDER BY formEdited DESC";                
        ResultSet rs = DBHelp.searchDBRecord(sql);
        try {
	        if(rs.next()) {
	        	Integer id = rs.getInt("id");
	        	return id;
	        }
        }catch(SQLException e) {
        	MiscUtils.getLogger().error("Error",e);
        	return 0;
        }
		return 0;
	}
	
	public static Integer getLatestFormIdByDemographicNo(Integer demographicNo) {
		String sql = "SELECT id from formONAREnhancedRecord WHERE demographic_no="+demographicNo+" ORDER BY formEdited DESC";                
        ResultSet rs = DBHelp.searchDBRecord(sql);
        try {
	        if(rs.next()) {
	        	Integer id = rs.getInt("id");
	        	return id;
	        }
        }catch(SQLException e) {
        	MiscUtils.getLogger().error("Error",e);
        	return 0;
        }
		return 0;
	}
	
	public static Integer getLatestAR2005FormIdByDemographicNo(Integer demographicNo) {
		String sql = "SELECT id from formONAR WHERE demographic_no="+demographicNo+" ORDER BY formEdited DESC";                
        ResultSet rs = DBHelp.searchDBRecord(sql);
        try {
	        if(rs != null && rs.next()) {
	        	Integer id = rs.getInt("id");
	        	return id;
	        }
        }catch(SQLException e) {
        	MiscUtils.getLogger().error("Error",e);
        	return 0;
        }
		return 0;
	}
}
