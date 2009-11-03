// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License.
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * as published by the Free Software Foundation; either version 2
// * of the License, or (at your option) any later version. *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
// * along with this program; if not, write to the Free Software
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
// *
// * <OSCAR TEAM>
// * This software was written for the
// * Department of Family Medicine
// * McMaster Unviersity
// * Hamilton
// * Ontario, Canada
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar;

import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oscar.oscarDB.DBPreparedHandler;

public class dbBillingData {
  private String username="";
  private String password="";
  DBPreparedHandler accessDB=null;
  private String db_service_code=null;
  private String service_code=null;
  private String description=null;
  private String value=null;
  private String percentage=null;
  private MessageDigest md;
  Properties  oscarVariables= null;


  public dbBillingData() {}

  public void setService_code(String value) {
    service_code=value;
    // System.out.println("Service Code =" + value);
  }
    public void setVariables(Properties variables) {
    this.oscarVariables=variables;

  }
  public String[] ejbLoad() {
    getService_code();
    System.out.println("Service Code from db=" + db_service_code);
    if(db_service_code==null) return null; // the user is not in security table

    if(service_code.equals(db_service_code)) { // login successfully
     	String[] strAuth = new String [4];
    	strAuth[0] = db_service_code;
    	strAuth[1] = description;
    	strAuth[2] = value;
    	strAuth[3] = percentage;
      return strAuth;
    } else { // login failed
      return null;
    }
  }

	private void getService_code() { // if failed, username will be null
		String[] temp = new String[4];

		accessDB = new DBPreparedHandler();
		String strSQL = "select service_code, description, value, percentage from billingservice where service_code = '" + service_code + "'";
		// System.out.println("SQL=" + strSQL);

		temp = searchDB(strSQL, "service_code", "description", "value", "percentage"); // use StringBuffer later
		if (temp != null) {
			db_service_code = temp[0];
			description = temp[1];
			value = temp[2];
			percentage = temp[3];
		} else { // no this user in the table
			return;
		}
	}

  private String[] searchDB(String dbSQL, String str1, String str2, String str3, String str4) {
  	String [] temp=new String[4];
    ResultSet rs=null;
    try {
      rs = accessDB.queryResults(dbSQL);
      while (rs.next()) {
        temp[0] = accessDB.getString(rs,str1);
        temp[1] = accessDB.getString(rs,str2);
        temp[2] = accessDB.getString(rs,str3);
        temp[3] = accessDB.getString(rs,str4);
      }
      accessDB.closePstmt();
      return temp;
    } catch (SQLException e) {;}
    return null;
  }



}
