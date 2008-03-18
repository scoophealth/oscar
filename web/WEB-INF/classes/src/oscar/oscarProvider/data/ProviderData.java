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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

/*
 * ProviderData.java
 *
 * Created on August 19, 2004, 2:15 PM
 */

package oscar.oscarProvider.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author  Jay Gallagher
 * Used to access data in provider table
 */
public class ProviderData {
/*+-------------------+-------------+------+-----+---------+-------+
 | Field             | Type        | Null | Key | Default | Extra |
 +-------------------+-------------+------+-----+---------+-------+
 | provider_no       | varchar(6)  |      | PRI |         |       |
 | last_name         | varchar(30) |      |     |         |       |
 | first_name        | varchar(30) |      |     |         |       |
 | provider_type     | varchar(15) |      |     |         |       |
 | specialty         | varchar(20) |      |     |         |       |
 | team              | varchar(20) | YES  |     |         |       |
 | sex               | char(1)     |      |     |         |       |
 | dob               | date        | YES  |     | NULL    |       |
 | address           | varchar(40) | YES  |     | NULL    |       |
 | phone             | varchar(20) | YES  |     | NULL    |       |
 | work_phone        | varchar(50) | YES  |     | NULL    |       |
 | ohip_no           | varchar(20) | YES  |     | NULL    |       |
 | rma_no            | varchar(20) | YES  |     | NULL    |       |
 | billing_no        | varchar(20) | YES  |     | NULL    |       |
 | hso_no            | varchar(10) | YES  |     | NULL    |       |
 | status            | char(1)     | YES  |     | NULL    |       |
 | comments          | text        | YES  |     | NULL    |       |
 | provider_activity | char(3)     | YES  |     | NULL    |       |
 +-------------------+-------------+------+-----+---------+-------+
*/
   
   String provider_no;
   String last_name;
   String first_name;
   String provider_type;
   String specialty;
   String team;
   String sex;
   String dob;
   String address;
   String phone;
   String work_phone;
   String ohip_no;
   String rma_no;
   String billing_no;
   String hso_no;
   String status;
   String comments;
   String provider_activity;
   
   /** Creates a new instance of ProviderData */
   public ProviderData() {            
   }
   
   public ProviderData(String providerNo) {            
      getProvider(providerNo);
   }
   
   public List getProviderListWithInsuranceNo(){
       return getProviderListWithInsuranceNo("%");
   }
   
   public List getProviderListWithInsuranceNo(String insurerNo){
        ArrayList list = new ArrayList();
        try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql = "select * from provider where provider_type='doctor' and ohip_no like '"+insurerNo+"' and ohip_no != '' order by last_name";

                rs = db.GetSQL(sql);
                while  (rs.next()) {
                   list.add(db.getString(rs,"provider_no"));
                }

                rs.close();
                db.CloseConn();
                               
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }         
        return list;
   }
   
  
   
   public void getProvider(String providerNo){
          try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql = "SELECT * FROM provider WHERE provider_no = '" + providerNo +"'";

                rs = db.GetSQL(sql);

                if (rs.next()) {
                   this.provider_no = db.getString(rs,"provider_no");
                   this.last_name = db.getString(rs,"last_name");
                   this.first_name = db.getString(rs,"first_name");
                   this.provider_type = db.getString(rs,"provider_type"); 
                   this.specialty = db.getString(rs,"specialty");
                   this.team = db.getString(rs,"team");   String sex;
                   this.dob= db.getString(rs,"dob");
                   this.address= db.getString(rs,"address");
                   this.phone= db.getString(rs,"phone");
                   this.work_phone= db.getString(rs,"work_phone");
                   this.ohip_no= db.getString(rs,"ohip_no");
                   this.rma_no= db.getString(rs,"rma_no");
                   this.billing_no= db.getString(rs,"billing_no");
                   this.hso_no= db.getString(rs,"hso_no");
                   this.status= db.getString(rs,"status");
                   this.comments= db.getString(rs,"comments");
                   this.provider_activity= db.getString(rs,"provider_activity");
                }

                rs.close();
                db.CloseConn();
                               
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }            
      
   }
   
   /**
    * Getter for property provider_no.
    * @return Value of property provider_no.
    */
   public java.lang.String getProvider_no() {
      return provider_no;
   }
   
   /**
    * Setter for property provider_no.
    * @param provider_no New value of property provider_no.
    */
   public void setProvider_no(java.lang.String provider_no) {
      this.provider_no = provider_no;
   }
   
   /**
    * Getter for property last_name.
    * @return Value of property last_name.
    */
   public java.lang.String getLast_name() {
      return last_name;
   }
   
   /**
    * Setter for property last_name.
    * @param last_name New value of property last_name.
    */
   public void setLast_name(java.lang.String last_name) {
      this.last_name = last_name;
   }
   
   /**
    * Getter for property first_name.
    * @return Value of property first_name.
    */
   public java.lang.String getFirst_name() {
      return first_name;
   }
   
   /**
    * Setter for property first_name.
    * @param first_name New value of property first_name.
    */
   public void setFirst_name(java.lang.String first_name) {
      this.first_name = first_name;
   }
   
   /**
    * Getter for property provider_type.
    * @return Value of property provider_type.
    */
   public java.lang.String getProvider_type() {
      return provider_type;
   }
   
   /**
    * Setter for property provider_type.
    * @param provider_type New value of property provider_type.
    */
   public void setProvider_type(java.lang.String provider_type) {
      this.provider_type = provider_type;
   }
   
   /**
    * Getter for property specialty.
    * @return Value of property specialty.
    */
   public java.lang.String getSpecialty() {
      return specialty;
   }
   
   /**
    * Setter for property specialty.
    * @param specialty New value of property specialty.
    */
   public void setSpecialty(java.lang.String specialty) {
      this.specialty = specialty;
   }
   
   /**
    * Getter for property team.
    * @return Value of property team.
    */
   public java.lang.String getTeam() {
      return team;
   }
   
   /**
    * Setter for property team.
    * @param team New value of property team.
    */
   public void setTeam(java.lang.String team) {
      this.team = team;
   }
   
   /**
    * Getter for property sex.
    * @return Value of property sex.
    */
   public java.lang.String getSex() {
      return sex;
   }
   
   /**
    * Setter for property sex.
    * @param sex New value of property sex.
    */
   public void setSex(java.lang.String sex) {
      this.sex = sex;
   }
   
   /**
    * Getter for property dob.
    * @return Value of property dob.
    */
   public java.lang.String getDob() {
      return dob;
   }
   
   /**
    * Setter for property dob.
    * @param dob New value of property dob.
    */
   public void setDob(java.lang.String dob) {
      this.dob = dob;
   }
   
   /**
    * Getter for property address.
    * @return Value of property address.
    */
   public java.lang.String getAddress() {
      return address;
   }
   
   /**
    * Setter for property address.
    * @param address New value of property address.
    */
   public void setAddress(java.lang.String address) {
      this.address = address;
   }
   
   /**
    * Getter for property phone.
    * @return Value of property phone.
    */
   public java.lang.String getPhone() {
      return phone;
   }
   
   /**
    * Setter for property phone.
    * @param phone New value of property phone.
    */
   public void setPhone(java.lang.String phone) {
      this.phone = phone;
   }
   
   /**
    * Getter for property work_phone.
    * @return Value of property work_phone.
    */
   public java.lang.String getWork_phone() {
      return work_phone;
   }
   
   /**
    * Setter for property work_phone.
    * @param work_phone New value of property work_phone.
    */
   public void setWork_phone(java.lang.String work_phone) {
      this.work_phone = work_phone;
   }
   
   /**
    * Getter for property ohip_no.
    * @return Value of property ohip_no.
    */
   public java.lang.String getOhip_no() {
      return ohip_no;
   }
   
   /**
    * Setter for property ohip_no.
    * @param ohip_no New value of property ohip_no.
    */
   public void setOhip_no(java.lang.String ohip_no) {
      this.ohip_no = ohip_no;
   }
   
   /**
    * Getter for property rma_no.
    * @return Value of property rma_no.
    */
   public java.lang.String getRma_no() {
      return rma_no;
   }
   
   /**
    * Setter for property rma_no.
    * @param rma_no New value of property rma_no.
    */
   public void setRma_no(java.lang.String rma_no) {
      this.rma_no = rma_no;
   }
   
   /**
    * Getter for property billing_no.
    * @return Value of property billing_no.
    */
   public java.lang.String getBilling_no() {
      return billing_no;
   }
   
   /**
    * Setter for property billing_no.
    * @param billing_no New value of property billing_no.
    */
   public void setBilling_no(java.lang.String billing_no) {
      this.billing_no = billing_no;
   }
   
   /**
    * Getter for property hso_no.
    * @return Value of property hso_no.
    */
   public java.lang.String getHso_no() {
      return hso_no;
   }
   
   /**
    * Setter for property hso_no.
    * @param hso_no New value of property hso_no.
    */
   public void setHso_no(java.lang.String hso_no) {
      this.hso_no = hso_no;
   }
   
   /**
    * Getter for property status.
    * @return Value of property status.
    */
   public java.lang.String getStatus() {
      return status;
   }
   
   /**
    * Setter for property status.
    * @param status New value of property status.
    */
   public void setStatus(java.lang.String status) {
      this.status = status;
   }
   
   /**
    * Getter for property comments.
    * @return Value of property comments.
    */
   public java.lang.String getComments() {
      return comments;
   }
   
   /**
    * Setter for property comments.
    * @param comments New value of property comments.
    */
   public void setComments(java.lang.String comments) {
      this.comments = comments;
   }
   
   /**
    * Getter for property provider_activity.
    * @return Value of property provider_activity.
    */
   public java.lang.String getProvider_activity() {
      return provider_activity;
   }
   
   /**
    * Setter for property provider_activity.
    * @param provider_activity New value of property provider_activity.
    */
   public void setProvider_activity(java.lang.String provider_activity) {
      this.provider_activity = provider_activity;
   }

   //TODO: Add a cache of providers
   public static ArrayList getProviderList (boolean inactive) {
        try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ArrayList result = new ArrayList();
            String active = " and status = '1' ";
            if (inactive){
               active = "";
            }
            
            String sql = "select provider_no, first_name, last_name from provider where provider_type='doctor' "+active+" order by last_name , first_name";
            ResultSet rs = db.GetSQL(sql);            
            while ( rs.next() ) {
                Hashtable provider = new Hashtable();
                provider.put("providerNo",db.getString(rs,"provider_no"));
                provider.put("firstName",db.getString(rs,"first_name"));
                provider.put("lastName",db.getString(rs,"last_name"));
                result.add(provider);
            }
            db.CloseConn();            
            return result;
        }catch(Exception e){
            System.out.println("exception in ProviderData:"+e);
            return null;
        }        
    }
   
   public static ArrayList getProviderList () {
      return getProviderList (false); 
   }
   
              
    public static String getProviderName(String providerNo) {
           try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
                                    
            String sql = "select first_name, last_name from provider where provider_no='"+providerNo+"'";
            ResultSet rs = db.GetSQL(sql);            
            db.CloseConn();            
            if ( rs.next() ) {            
                return ( db.getString(rs,"first_name") + " " + db.getString(rs,"last_name") );            
            } else {                            
                return "";
            }
        }catch(Exception e){
            System.out.println("exception in ProviderData:"+e);
            return null;
        }        
    }
    
    public String getMyOscarId() {
         if (myOscarId == null) this.initMyOscarId();
         return myOscarId;
    }
 
    public void initMyOscarId() {
         ProviderMyOscarIdData myOscar = new ProviderMyOscarIdData(this.getProvider_no());
         this.myOscarId = myOscar.getMyOscarId();
    }
    private String myOscarId = null;
    
    public String getDefaultBillingView(String providerNo){
        String defaultView = null;
         try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select default_servicetype from preference where provider_no='" + providerNo + "'";
            ResultSet rs = db.GetSQL(sql);   
               if (rs.next() && db.getString(rs,"default_servicetype")!=null) {
		   defaultView = db.getString(rs,"default_servicetype");
               } 
         }catch (Exception e){
             e.printStackTrace();
         }
         return defaultView;
    }
    
    public int addProvider(String providerNo, String firstName, String lastName, String ohipNo) throws SQLException {
	String add_record_string = "insert into provider values (?,?,?,'doctor','','','','','','','',?,'','','','1','','')";
	int key = 0;
	
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	Connection conn = db.GetConnection();
	PreparedStatement add_record = conn.prepareStatement(add_record_string);
	
	add_record.setString(1, providerNo);
	add_record.setString(2, lastName);
	add_record.setString(3, firstName);
	add_record.setString(4, ohipNo);
	
	add_record.executeUpdate();
	ResultSet rs = add_record.getGeneratedKeys();
	if(rs.next()) key = rs.getInt(1);
	add_record.close();
	rs.close();
	db.CloseConn();
	
	return key;
    }
    
    public String getNewExtProviderNo() throws SQLException {
	String providerNo = null;
	
	String sql = "select max(provider_no) from provider where provider_no like 'E%'";
	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	ResultSet rs = db.GetSQL(sql);
	if (rs.next()) providerNo = db.getString(rs,1);
	rs.close();
	db.CloseConn();
	
	if (providerNo!=null) {
	    int lastPN = Integer.parseInt(providerNo.substring(1)) + 1;
	    providerNo = "E" + fillUp(String.valueOf(lastPN), 5, '0');
	} else {
	    providerNo = "E00001";
	}
	return providerNo;
    }
    
    public String getProviderNoByOhip(String OhipNO) throws SQLException {
	String providerNo = null;
	if (OhipNO!=null) {
	    String sql = "select provider_no from provider where ohip_no = '" + OhipNO + "'";
	    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	    ResultSet rs = db.GetSQL(sql);
	    if (rs.next()) providerNo = db.getString(rs,"provider_no");
	    rs.close();
	}
	return providerNo;
    }
    
     String fillUp(String in, int size, char fill) {
	size -= in.length();
	for (int i=0; i<size; i++) in = fill + in;
	return in;
    }    
}
