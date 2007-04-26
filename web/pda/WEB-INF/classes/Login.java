  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar;
import java.sql.*;
import java.util.*;
import java.security.*;

public class Login {
  private String username="";
  private String password=""; 
  DBHandler accessDB=null;

  private String userid=null; //who is logining? provider_no
  private String userpassword=null; //your password in the table
  
  private String firstname=null; 
  private String lastname=null; 
  private String profession=null; 
  Properties  oscarVariables= null;
  private MessageDigest md; 
  
  public Login() {}
  
  public void setUsername(String user_name) {
    this.username=user_name;
  }
  public void setPassword(String password) {
    this.password=password.replace(' ','\b'); //no white space to be allowed in the password
  }
  public void setVariables(Properties variables) {
    this.oscarVariables=variables;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException foo) {
		}
  }
  
  public String[] authenticate() {
  	getUserID();
    if(userpassword==null) return null; // the user is not in security table
    
    StringBuffer sbTemp = new StringBuffer();
    byte[] btTypeInPasswd= md.digest(password.getBytes());
    for(int i=0; i<btTypeInPasswd.length; i++) sbTemp = sbTemp.append(btTypeInPasswd[i]);
    password = sbTemp.toString();

    if(userpassword.length()<20) {
      sbTemp = new StringBuffer();
      byte[] btDBPasswd= md.digest(userpassword.getBytes());
      for(int i=0; i<btDBPasswd.length; i++) sbTemp = sbTemp.append(btDBPasswd[i]);
      userpassword = sbTemp.toString();
    }

    if(password.equals(userpassword)) { // login successfully
     	String[] strAuth = new String [4];
    	strAuth[0] = userid;
    	strAuth[1] = firstname;
    	strAuth[2] = lastname;
    	strAuth[3] = profession;
      return strAuth;
    } else { // login failed
      return null;
    }
  }

  private void getUserID() { //if failed, username will be null
  	String [] temp=new String[3];
    try {
      accessDB = new DBHandler(oscarVariables.getProperty("db_driver"),oscarVariables.getProperty("db_uri")+oscarVariables.getProperty("db_name")+"?user="+oscarVariables.getProperty("db_username")+"&password="+oscarVariables.getProperty("db_password"),oscarVariables.getProperty("db_username"),oscarVariables.getProperty("db_password"), null,null);
      String strSQL="select password, provider_no, user_name from security where user_name = '" + username+"'";
      temp = searchDB(strSQL, "password", "provider_no", "user_name" ); //use StringBuffer later
      if(temp[0]!=null) { //password is in the table
        userpassword=temp[0];
        userid=temp[1];
      } else { //no this user in the table
        accessDB.closeConn();
       	return;
      }

      if(!userid.equals("")) { //find the detail of the user
        strSQL="select first_name, last_name, provider_type from provider where provider_no = '" + userid +"'";
        temp = searchDB(strSQL, "first_name", "last_name", "provider_type" ); //use StringBuffer later
        firstname = temp[0];
        lastname = temp[1];
        profession = temp[2];
      }
      if (!(profession.equals("receptionist")||profession.equals("doctor")) ) // *****be careful here
        accessDB.closeConn();
    }catch (SQLException e) {return;}
  }

  private String[] searchDB(String dbSQL, String str1, String str2, String str3) {
  	String [] temp=new String[3];
    ResultSet rs=null;
    try {
      rs = accessDB.queryResults(dbSQL);
      while (rs.next()) {
        temp[0] = rs.getString(str1);
        temp[1] = rs.getString(str2);
        temp[2] = rs.getString(str3);
      }
      accessDB.closePstmt();
      return temp;
      //accessDB.closeConn();
    } catch (SQLException e) {;}
    return null;
  }

  public String[] getPreferences() {
	  String [] temp=null;
  if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
    	temp=new String[] {"8","18","15","a","disabled"};
   }else temp=new String[] {"8","18","15","a"};
    ResultSet rs=null;
    try {
      String strSQL="select start_hour, end_hour, every_min, mygroup_no from preference where provider_no = '" + userid+"'";
      rs = accessDB.queryResults(strSQL);
      while (rs.next()) {
        temp[0] = rs.getString("start_hour");
        temp[1] = rs.getString("end_hour");
        temp[2] = rs.getString("every_min");
        temp[3] = rs.getString("mygroup_no");
        if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
            temp[4] = rs.getString("new_tickler_warning_window");
        }
      }
      //accessDB.closePstmt();
      accessDB.closeConn();

      if(temp[0]==null  ) { //no preference for the useid
        temp[0] = "8"; //default value
        temp[1] = "18";
        temp[2] = "15";
        temp[3] = "a";
        if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
           temp[4] = "disabled";
        }
      }
      return temp;

    }catch (SQLException e) {;}
    return temp; //always get the default values
  }
  
}
