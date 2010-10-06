/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of ReferralBilling
 *
 *
 * ReferralBilling.java
 *
 * Created on November 5, 2005, 7:21 PM
 */

package oscar.oscarBilling.ca.bc.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *+--------------------+-------------+------+-----+---------+----------------+
  | Field              | Type        | Null | Key | Default | Extra          |
  +--------------------+-------------+------+-----+---------+----------------+
  | billingreferral_no | int(10)     |      | PRI | NULL    | auto_increment |
  | referral_no        | varchar(5)  |      | MUL | 00000   |                |
  | last_name          | varchar(20) | YES  |     | NULL    |                |
  | first_name         | varchar(20) | YES  |     | NULL    |                |
  | specialty          | varchar(8)  | YES  |     | NULL    |                |
  | address1           | varchar(25) | YES  |     | NULL    |                |
  | address2           | varchar(25) | YES  |     | NULL    |                |
  | city               | varchar(15) | YES  |     | NULL    |                |
  | province           | varchar(20) | YES  |     | NULL    |                |
  | postal             | varchar(10) | YES  |     | NULL    |                |
  | phone              | varchar(16) | YES  |     | NULL    |                |
  | fax                | varchar(16) | YES  |     | NULL    |                |
  +--------------------+-------------+------+-----+---------+----------------+

 *
 * @author Jay Gallagher
 */
public class ReferralBillingData {
      
   public ReferralBillingData() {
      
   }
   
   
   public void addReferralDoc(){      
      //TODO:
   }
   
   public Hashtable getReferralDoc(String billingNum){
      String sql = " select * from billingreferral where referral_no = '"+billingNum+"' ";
      return runSQL(sql);
   }
   
   
   public Hashtable getReferralbyId(String billingNum){
      String sql = " select * from billingreferral where billingreferral_no = '"+billingNum+"' ";
      return runSQL(sql);
   }
   
   public ArrayList searchReferralDocByLastName(String lastname, String limit ){
      String sql = " select * from billingreferral where last_name like  '"+StringEscapeUtils.escapeSql(lastname)+"%' limit "+StringEscapeUtils.escapeSql(limit);
      return runSQLMulti(sql);
   }
   
   public ArrayList searchReferralDocByLastName(String lastname ){
      String sql = " select * from billingreferral where last_name like  '"+StringEscapeUtils.escapeSql(lastname)+"%' ";
      return runSQLMulti(sql);
   }
   
   
   
   private Hashtable runSQL(String sql){
      Hashtable h = new Hashtable();
      try{
         
         ResultSet rs = DBHandler.GetSQL(sql);         
         if(rs.next()){
            fillHash(rs,h);
         }
         rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      return h;
   }
   
   private ArrayList runSQLMulti(String sql){
      ArrayList l = new ArrayList();
      try{
         
         ResultSet rs = DBHandler.GetSQL(sql);         
         while(rs.next()){
            l.add(fillHash(rs));
         }
         rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      return l;
   }
   
   
   private void fillHash(ResultSet rs, Hashtable h) throws Exception{
      h.put("billingreferral_no",rs.getString("billingreferral_no"));
      h.put("referral_no",rs.getString("referral_no"));
      h.put("last_name",rs.getString("last_name"));
      h.put("first_name",rs.getString("first_name"));
      h.put("specialty",rs.getString("specialty"));
      h.put("address1",rs.getString("address1"));
      h.put("address2",rs.getString("address2"));
      h.put("city",rs.getString("city"));
      h.put("province",rs.getString("province"));
      h.put("postal",rs.getString("postal"));
      h.put("phone",rs.getString("phone"));
      h.put("fax",rs.getString("fax"));
   }
   
   private Hashtable fillHash(ResultSet rs) throws Exception{
      Hashtable h = new Hashtable();
      h.put("billingreferral_no",rs.getString("billingreferral_no"));
      h.put("referral_no",rs.getString("referral_no"));
      h.put("last_name",rs.getString("last_name"));
      h.put("first_name",rs.getString("first_name"));
      h.put("specialty",rs.getString("specialty"));
      h.put("address1",rs.getString("address1"));
      h.put("address2",rs.getString("address2"));
      h.put("city",rs.getString("city"));
      h.put("province",rs.getString("province"));
      h.put("postal",rs.getString("postal"));
      h.put("phone",rs.getString("phone"));
      h.put("fax",rs.getString("fax"));
      return h;
   }
   
   public String getReferralDocName(String billingNum){
      String retval= "";
      try{
         
         ResultSet rs = DBHandler.GetSQL(" select * from billingreferral where referral_no = '"+billingNum+"' ");         
         if(rs.next()){
            retval = rs.getString("last_name")+", "+rs.getString("first_name");            
         }
         rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      return retval;
   }
   
   
   public int getNumberOfRecordsUsingBillingNumber(String billingNum){
      int ret = 0;
      try{
         
         ResultSet rs = DBHandler.GetSQL(" select count(*) as coun from billingreferral where referral_no = '"+billingNum+"' ");         
         if(rs.next()){
            ret = rs.getInt("coun");
         }
         rs.close();                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
      return ret;
   }
   
   
   
   public void insertIntoBillingReferral(String referral_no,String last_name,String first_name, String specialty,String  address1,String address2,
   String city,String province,String postal,String phone,String fax){
      try{
         
         String sql = "insert into billingreferral  (referral_no,last_name,first_name,specialty,address1,address2,city,province,postal,phone,fax) values "
         + "('"+StringEscapeUtils.escapeSql(referral_no)+"',"  
         + " '"+StringEscapeUtils.escapeSql(last_name)+"', "  
         + " '"+StringEscapeUtils.escapeSql(first_name)+"', "  
         + " '"+StringEscapeUtils.escapeSql(specialty)+"', "  
         + " '"+StringEscapeUtils.escapeSql(address1)+"', "  
         + " '"+StringEscapeUtils.escapeSql(address2)+"', "  
         + " '"+StringEscapeUtils.escapeSql(city)+"', "  
         + " '"+StringEscapeUtils.escapeSql(province)+"', "  
         + " '"+StringEscapeUtils.escapeSql(postal)+"', "  
         + " '"+StringEscapeUtils.escapeSql(phone)+"', "  
         + " '"+StringEscapeUtils.escapeSql(fax)+"' "           
         + ")";          
         
         DBHandler.RunSQL(sql);                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
   }
   
   //TODO 9 - 12 on nov 11
   //1 validate that billing number being added isn't already in use.
   //2 validate that a last name has been entered.
   //3 a way to delete a record
   //4 display more than ten records and a way to search that
   //?3validate phone nums?
   //
   
   public void updateBillingReferral(String billingreferral_no, String referral_no,String last_name,String first_name, String specialty,String  address1,String address2,
   String city,String province,String postal,String phone,String fax){
      try{
         
         String sql = "update billingreferral set "
         + "referral_no = '"+StringEscapeUtils.escapeSql(referral_no)+"',"  
         + "last_name = '"+StringEscapeUtils.escapeSql(last_name)+"', "  
         + "first_name = '"+StringEscapeUtils.escapeSql(first_name)+"', "  
         + "specialty ='"+StringEscapeUtils.escapeSql(specialty)+"', "  
         + "address1 = '"+StringEscapeUtils.escapeSql(address1)+"', "  
         + "address2 = '"+StringEscapeUtils.escapeSql(address2)+"', "  
         + "city = '"+StringEscapeUtils.escapeSql(city)+"', "  
         + "province = '"+StringEscapeUtils.escapeSql(province)+"', "  
         + "postal = '"+StringEscapeUtils.escapeSql(postal)+"', "  
         + "phone = '"+StringEscapeUtils.escapeSql(phone)+"', "  
         + "fax = '"+StringEscapeUtils.escapeSql(fax)+"' "           
         + " where billingreferral_no = '"+billingreferral_no+"'";          
         
         DBHandler.RunSQL(sql);                    
      }catch (Exception e){
         MiscUtils.getLogger().error("Error", e);        
      }
   }
   
}