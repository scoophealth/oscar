package oscar.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import oscar.oscarDB.DBPreparedHandler;

public class LoginCheckLoginBean {
  private String username="";
  private String password=""; 
  private String pin=""; 
  private String ip=""; 
  DBPreparedHandler accessDB=null;

  private String userid=null; //who is logining? provider_no
  private String userpassword=null; //your password in the table
  private String userpin=null; //your password in the table
  
  private String firstname=null; 
  private String lastname=null; 
  private String profession=null; 
  Properties  oscarVariables= null;
  private MessageDigest md; 
  
  public LoginCheckLoginBean() {}
  
  public void ini(String user_name, String password, String pin1, String ip1, Properties variables) {
    setUsername(user_name) ;
    setPassword(password) ;
    setPin(pin1) ;
    setIp(ip1) ;
    setVariables(variables) ;
  }

  public void setUsername(String user_name) {
    this.username=user_name;
  }
  public void setPassword(String password) {
    this.password=password.replace(' ','\b'); //no white space to be allowed in the password
  }
  public void setPin(String pin1) {
    this.pin=pin1.replace(' ','\b'); 
  }
  public void setIp(String ip1) {
    this.ip=ip1;
  }
  public void setVariables(Properties variables) {
    this.oscarVariables=variables;
		try {
			md = MessageDigest.getInstance("SHA"); //may get from prop file
		} catch (NoSuchAlgorithmException foo) {}
  }
  
  public String[] authenticate() {
  	getUserID();
    if(userpassword==null) return null; // the user is not in security table
    if( isWAN() && (!pin.equals(userpin) || pin.length()<3) ) return null;
    //System.out.println("wan!!!"+pin+" 111 "+userpin) ;
    
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
  	String [] temp=new String[4];
    try {
      accessDB = new DBPreparedHandler(oscarVariables.getProperty("db_driver"),oscarVariables.getProperty("db_uri")+oscarVariables.getProperty("db_name")+"?user="+oscarVariables.getProperty("db_username")+"&password="+oscarVariables.getProperty("db_password"),oscarVariables.getProperty("db_username"),oscarVariables.getProperty("db_password") );
      String strSQL="select password, provider_no, user_name, pin from security where user_name = '" + username+"'";
      temp = searchDB(strSQL, "password", "provider_no", "user_name", "pin" ); //use StringBuffer later
      if(temp[0]!=null) { //password is in the table
        userpassword=temp[0];
        userid=temp[1];
        userpin = temp[3];
      } else { //no this user in the table
        accessDB.closeConn();
       	return;
      }

      if(!userid.equals("")) { //find the detail of the user
        strSQL="select first_name, last_name, provider_type from provider where provider_no = '" + userid +"'";
        temp = searchDB(strSQL, "first_name", "last_name", "provider_type", "provider_type" ); //use StringBuffer later
        firstname = temp[0];
        lastname = temp[1];
        profession = temp[2];
      }
      if (!(profession.equals("receptionist")||profession.equals("doctor")) ) // *****be careful here
        accessDB.closeConn();
    }catch (SQLException e) {return;}
  }

  private String[] searchDB(String dbSQL, String str1, String str2, String str3, String str4) {
  	String [] temp=new String[4];
    ResultSet rs=null;
    try {
      rs = accessDB.queryResults(dbSQL);
      while (rs.next()) {
        temp[0] = rs.getString(str1);
        temp[1] = rs.getString(str2);
        temp[2] = rs.getString(str3);
        temp[3] = rs.getString(str4);
      }
      accessDB.closePstmt();
      return temp;
      //accessDB.closeConn();
    } catch (SQLException e) {;}
    return null;
  }

  public String[] getPreferences() {
  	String [] temp=new String[] {"8","18","15","a"};
    ResultSet rs=null;
    try {
      String strSQL="select start_hour, end_hour, every_min, mygroup_no from preference where provider_no = '" + userid+"'";
      rs = accessDB.queryResults(strSQL);
      while (rs.next()) {
        temp[0] = rs.getString("start_hour");
        temp[1] = rs.getString("end_hour");
        temp[2] = rs.getString("every_min");
        temp[3] = rs.getString("mygroup_no");
      }
      //accessDB.closePstmt();
      accessDB.closeConn();

      if(temp[0]==null  ) { //no preference for the useid
        temp[0] = "8"; //default value
        temp[1] = "18";
        temp[2] = "15";
        temp[3] = "a";
      }
      return temp;

    }catch (SQLException e) {;}
    return temp; //always get the default values
  }
  
  public boolean isWAN() {
  	boolean bWAN = true;
    if(ip.startsWith(oscarVariables.getProperty("login_local_ip")) ) bWAN = false ;
    return bWAN; 
  }

}
