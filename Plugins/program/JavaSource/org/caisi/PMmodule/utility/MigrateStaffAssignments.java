package org.caisi.PMmodule.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MigrateStaffAssignments {
	protected final Log log = LogFactory.getLog(getClass());
    protected ApplicationContext ctx = null;
    protected DataSource ds;
    protected Connection conn;
    protected int nurseRoleId = 0;
    protected int doctorRoleId = 0;
    
    public MigrateStaffAssignments() throws Exception {
    	String[] paths = {"/WEB-INF/applicationContext.xml"};
    	ctx = new ClassPathXmlApplicationContext(paths);
    	ds = (DataSource)ctx.getBean("dataSource");
    	conn = ds.getConnection();
    }
    
    public void run() throws Exception {
    	nurseRoleId = (int)this.getRoleId("Nurse");
    	doctorRoleId = (int)this.getRoleId("Doctor");
    	
    	Statement stmt = conn.createStatement();
    	stmt.execute("SELECT * FROM provider_role_program");
    	ResultSet rs = stmt.getResultSet();
    	int count = 0;
    	while(rs.next()) {
    		long providerNo = rs.getInt("provider_no");
    		long programId = rs.getInt("program_id");
    		long group_id = rs.getInt("group_id");
    		if(programExists(programId) && providerExists(providerNo)) {
    			//System.out.println(providerNo + "," +programId + "," + group_id);
    			this.addProgramProvider(programId,providerNo,group_id);
    		}
    
    		count++;
    	}
    	rs.close();
    	//System.out.println("# of entries = " + count);
    }
    
    public boolean programExists(long programId) throws Exception {
    	Statement stmt = conn.createStatement();
    	stmt.execute("SELECT count(*) as num FROM program where program_id =" + programId);
    	ResultSet rs = stmt.getResultSet();
    	rs.next();
    	long num = rs.getInt("num");
    	if(num >0) {
    		return true;
    	}
    	System.out.println("program doesn't exist: " + programId);
		return false;
    }

    public boolean providerExists(long providerNo) throws Exception {
    	Statement stmt = conn.createStatement();
    	stmt.execute("SELECT count(*) as num FROM provider where provider_no =" + providerNo);
    	ResultSet rs = stmt.getResultSet();
    	rs.next();
    	long num = rs.getInt("num");
    	if(num >0) {
    		return true;
    	}
    	System.out.println("provider doesn't exist: " + providerNo);
		return false;
    }
    
    public long getRoleId(String name) throws Exception {
    	Statement stmt = conn.createStatement();
    	stmt.execute("SELECT * FROM caisi_role where name = '"+ name + "'");
    	ResultSet rs = stmt.getResultSet();
    	if(rs.next()) {
    		return rs.getInt("role_id");
    	} else {
    		throw new Exception("no Nurse role defined");
    	}
    }
    
    public void addProgramProvider(long programId, long providerNo, long groupNo) throws Exception {
    	Statement stmt = conn.createStatement();
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
