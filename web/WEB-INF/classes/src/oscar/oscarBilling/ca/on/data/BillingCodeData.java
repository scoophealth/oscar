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
 * Ontario, Canada   Creates a new instance of BillingCodeData
 *
 *
 * BillingCodeData.java
 *
 * Created on October 6, 2005, 10:33 AM
 */

package oscar.oscarBilling.ca.on.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Jay Gallagher
 */
public class BillingCodeData {
   
   public List search(String str){
      ArrayList list = new ArrayList();
      String sql = "select * from billingservice where service_code like '"+str+"' or description like '%"+str+"%' ";
      try {
         
         ResultSet rs = DBHandler.GetSQL(sql);
         while (rs.next()){            
             list.add(fillCodeDataHashtable(rs));
         }
         rs.close();
      } catch (SQLException e) {
         MiscUtils.getLogger().error("Error", e);
      }      
      return list;
   }
   
   public Hashtable fillCodeDataHashtable(ResultSet rs) throws SQLException{
       MiscUtils.getLogger().debug("fillCode " + c(rs.getString("service_code")) + " " + c(rs.getString("value")));
      Hashtable h = new Hashtable();
       h.put("service_compositecode", c(rs.getString("service_compositecode")));
       h.put("service_code", c(rs.getString("service_code")));
       h.put("description", c(rs.getString("description")));
       h.put("value", c(rs.getString("value")));
       h.put("percentage", c(rs.getString("percentage")));
       h.put("billingservice_date", c(rs.getString("billingservice_date")));
       h.put("specialty", c(rs.getString("specialty")));
       h.put("region", c(rs.getString("region")));
       h.put("anaesthesia", c(rs.getString("anaesthesia")));             
      return h;
   }

   String c(String str){
      return ( str == null ) ? "" : str;    
   }
   
   public Hashtable searchBillingCode(String str){
      Hashtable h = null;
      int count = 0;
      String sql = "select * from billingservice b where b.service_code like '"+str+"%' and b.billingservice_date = (select max(b2.billingservice_date) from billingservice b2 where b2.service_code = b.service_code and b2.billingservice_date <= now())";
      try {
         
         ResultSet rs = DBHandler.GetSQL(sql);
         while (rs.next()){
             count++;
             h = fillCodeDataHashtable(rs);
         }
         MiscUtils.getLogger().debug(count+" for "+str);
         if (h != null){               
            h.put("count",""+count);
         }
         rs.close();
      } catch (SQLException e) {
         MiscUtils.getLogger().error("Error", e);
      }        
      return h;
   }
   
   
   ///
   public int searchNumBillingCode(String str){      
      int count = 0;
      String sql = "select count(*) as coun from billingservice b where b.service_code like '"+str+"%' and b.billingservice_date = (select max(b2.billingservice_date) from billingservice b2 where b2.service_code = b.service_code and b2.billingservice_date <= now())";
      try {
         
         ResultSet rs = DBHandler.GetSQL(sql);
         while (rs.next()){
             count = rs.getInt("coun");
         }                  
         rs.close();
      } catch (SQLException e) {
         MiscUtils.getLogger().error("Error", e);
      }        
      return count;
   }
   
   
   public void lastLetterofEachBillingCode(){
      
      String sql = "select service_code from billingservice ";
      try {
         
         ResultSet rs = DBHandler.GetSQL(sql);
         while (rs.next()){
            String service_code = rs.getString("service_code");
            MiscUtils.getLogger().debug(service_code.charAt(service_code.length()-1));
         }
         rs.close();
      } catch (SQLException e) {
         MiscUtils.getLogger().error("Error", e);
      }            
   }
   
   
   public BillingCodeData() {
   }
   
   public boolean editBillingCodeDesc(String desc, String val, String codeId){
      boolean retval = true;
      try{
         

         String str = "update billingservice set "+
                      "description = '"+StringEscapeUtils.escapeSql(desc) +"', "+
                      "value       = '"+StringEscapeUtils.escapeSql(val)  +"' "+
                      "where billingservice_no = '"+StringEscapeUtils.escapeSql(codeId)+"'";
         DBHandler.RunSQL(str);
      }catch(Exception e1){MiscUtils.getLogger().error("Error", e1);
      }
      return retval;
   }
   
  
   public boolean editBillingCode(String val, String codeId){
      boolean retval = true;
      try{
         

         String str = "update billingservice set "+                      
                      "value =                   '"+StringEscapeUtils.escapeSql(val)  +"' "+
                      "where billingservice_no = '"+StringEscapeUtils.escapeSql(codeId)+"'";
         MiscUtils.getLogger().debug(str);
         DBHandler.RunSQL(str);
         MiscUtils.getLogger().debug("NOW updated");
      }catch(Exception e1){MiscUtils.getLogger().error("Error", e1);
      }
      return retval;
   }
   
   
   
   public boolean editBillingCodeByServiceCode(String val, String codeId, String date){
      boolean retval = true;
      try{
         

         String str = "update billingservice set "+                      
                      "value =                   '"+StringEscapeUtils.escapeSql(val)  +"' "+
                      "where service_code = '"+StringEscapeUtils.escapeSql(codeId)+"' and billingservice_date = '" + date + "'";
         MiscUtils.getLogger().debug(str);
         DBHandler.RunSQL(str);
         MiscUtils.getLogger().debug("NOW updated");
      }catch(Exception e1){MiscUtils.getLogger().error("Error", e1);
      }
      return retval;
   }
   
   public boolean insertBillingCode(String value, String code, String date, String description, String termDate) {
      boolean retval = true;
      try{
         

         String str = "insert into billingservice (service_compositecode,service_code,description,value,percentage,billingservice_date,specialty,region,anaesthesia,termination_date) Values("+
                      "''," +                      
                      "'"+StringEscapeUtils.escapeSql(code)+"'," +
                      "'"+StringEscapeUtils.escapeSql(description) + "'," + 
                      "'"+StringEscapeUtils.escapeSql(value)  +"',"+
                      "''," +
                      "'"+StringEscapeUtils.escapeSql(date) + "'," + 
                      "''," +
                      "'ON'," +
                      "'00'," + 
                      "'" + StringEscapeUtils.escapeSql(termDate) + "')";
         MiscUtils.getLogger().debug(str);
         DBHandler.RunSQL(str);
         MiscUtils.getLogger().debug("NOW updated");
      }catch(Exception e1){MiscUtils.getLogger().error("Error", e1);
      }
      return retval;
   }
}
