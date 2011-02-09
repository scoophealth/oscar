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
