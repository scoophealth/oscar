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

package org.oscarehr.PMmodule.utility;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

public class MigrateStaffAssignments {
	private static  final Logger log = MiscUtils.getLogger();
    protected int nurseRoleId = 0;
    protected int doctorRoleId = 0;
    
    public MigrateStaffAssignments() {
    }
    
    public void run() throws Exception {
    	nurseRoleId = (int)this.getRoleId("Nurse");
    	doctorRoleId = (int)this.getRoleId("Doctor");
    	
    	Statement stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
    	stmt.execute("SELECT * FROM provider_role_program");
    	ResultSet rs = stmt.getResultSet();
    	
    	while(rs.next()) {
    		long providerNo = rs.getInt("provider_no");
    		long programId = rs.getInt("program_id");
    		long group_id = rs.getInt("group_id");
    		if(programExists(programId) && providerExists(providerNo)) {

    			this.addProgramProvider(programId,providerNo,group_id);
    		}
    
    		
    	}
    	rs.close();

    }
    
    public boolean programExists(long programId) throws Exception {
    	Statement stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
    	stmt.execute("SELECT count(*) as num FROM program where program_id =" + programId);
    	ResultSet rs = stmt.getResultSet();
    	rs.next();
    	long num = rs.getInt("num");
    	if(num >0) {
    		return true;
    	}
		return false;
    }

    public boolean providerExists(long providerNo) throws Exception {
    	Statement stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
    	stmt.execute("SELECT count(*) as num FROM provider where provider_no =" + providerNo);
    	ResultSet rs = stmt.getResultSet();
    	rs.next();
    	long num = rs.getInt("num");
    	if(num >0) {
    		return true;
    	}
		return false;
    }
    
    public long getRoleId(String name) throws Exception {
    	Statement stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
    	stmt.execute("SELECT * FROM caisi_role where name = '"+ name + "'");
    	ResultSet rs = stmt.getResultSet();
    	if(rs.next()) {
    		return rs.getInt("role_id");
    	} else {
    		throw new Exception("no Nurse role defined");
    	}
    }
    
    public void addProgramProvider(long programId, long providerNo, long groupNo) throws Exception {
    	Statement stmt = DbConnectionFilter.getThreadLocalDbConnection().createStatement();
    	long roleId = doctorRoleId;
    	if(groupNo == 9) {
    		roleId = nurseRoleId;
    	}
    	stmt.executeUpdate("insert into program_provider (program_id,provider_no,role_id,team_id) values (" + programId + "," + providerNo + "," + roleId + "," + "0)");
    }
    
	public static void main(String args[]) throws Exception {
		new MigrateStaffAssignments().run();
	}
}
