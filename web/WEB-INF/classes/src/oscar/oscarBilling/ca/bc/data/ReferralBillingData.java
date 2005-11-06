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

import java.sql.*;
import java.util.*;
import oscar.oscarDB.*;

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
      Hashtable h = new Hashtable();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(" select * from billingreferral where referral_no = '"+billingNum+"' ");         
         if(rs.next()){
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
         rs.close();          
         db.CloseConn();                    
      }catch (Exception e){
         e.printStackTrace();        
      }
      return h;
   }
   
   
   public String getReferralDocName(String billingNum){
      String retval= "";
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(" select * from billingreferral where referral_no = '"+billingNum+"' ");         
         if(rs.next()){
            retval = rs.getString("last_name")+", "+rs.getString("first_name");            
         }
         rs.close();          
         db.CloseConn();                    
      }catch (Exception e){
         e.printStackTrace();        
      }
      return retval;
   }
   
}