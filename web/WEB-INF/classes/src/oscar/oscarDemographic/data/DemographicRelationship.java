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
 * Ontario, Canada   Creates a new instance of DemographicRelationship
 *
 *
 * DemographicRelationship.java
 *
 * Created on June 12, 2005, 6:16 PM
 */

package oscar.oscarDemographic.data;

import java.sql.*;
import java.util.*;
import oscar.oscarDB.*;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicRelationship {
   
   /*
   CREATE TABLE relationships (
   id int(10) NOT NULL auto_increment,
   demographic_no int(10) NOT NULL default '0',
   relation_demographic_no int(10) NOT NULL default '0',
   relation varchar(20),
   creation_date datetime default NULL,  
   creator varchar(6) NOT NULL default '', 
   sub_decision_maker char(1) default '0', 
   emergency_contact char(1) default '0',
   notes text,
   deleted char(1) default '0',
   PRIMARY KEY  (id)
   )    
   */
   
   public DemographicRelationship() {
   }
   
   public void addDemographicRelationship(String demographic,String linkingDemographic,String relationship,boolean sdm,boolean emergency,String notes,String providerNo){
       String sdmStr = "0";
       String eContact = "0";
       if (sdm){
          sdmStr = "1";
       }
       if(emergency){
          eContact = "1";
       }
       
       try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "insert into relationships (demographic_no,relation_demographic_no,relation,sub_decision_maker,emergency_contact,notes,creator,creation_date) values "
            + "('"+demographic+"','"+linkingDemographic+"','"+StringEscapeUtils.escapeSql(relationship)+"','"+sdmStr+"','"+eContact+"','"+StringEscapeUtils.escapeSql(notes)+"','"+providerNo+"',now())";
            db.RunSQL(sql);                                                
            db.CloseConn();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }       
   }
   
   public void deleteDemographicRelationship(String id){
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String sql = "update relationships  set deleted = '1' where  id = '"+id+"'";
         db.RunSQL(sql);                                                
         db.CloseConn();
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }       
   }
   
   
   public ArrayList getDemographicRelationships(String demographic){
      ArrayList list = new ArrayList();
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         String sql = "select * from relationships where demographic_no = '"+demographic+"' and deleted != '1'";
         rs = db.GetSQL(sql);
         while(rs.next()){
            Hashtable h = new Hashtable();
            h.put("id", rs.getString("id"));
            h.put("demographic_no", rs.getString("relation_demographic_no"));
            h.put("relation", rs.getString("relation"));
            h.put("sub_decision_maker", rs.getString("sub_decision_maker"));
	    h.put("emergency_contact", rs.getString("emergency_contact"));
            h.put("notes", rs.getString("notes"));
            list.add(h);
         }
         db.CloseConn();            
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }       
      return list; 
   }
   
   
   public String getSDM(String demographic){
      String sdm = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         String sql = "select * from relationships where demographic_no = '"+demographic+"' and deleted != '1' and sub_decision_maker = '1'";
         rs = db.GetSQL(sql);
         if(rs.next()){            
            sdm = rs.getString("relation_demographic_no");
         }
         db.CloseConn();            
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }       
      return sdm; 
   }
   
   
   
   public ArrayList getDemographicRelationshipsWithNamePhone(String demographic_no){                 
      ArrayList list = new ArrayList();
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         String sql = "select * from relationships where demographic_no = '"+demographic_no+"' and deleted != '1'";
         rs = db.GetSQL(sql);
         while(rs.next()){
            Hashtable h = new Hashtable();
            String demo = rs.getString("relation_demographic_no");
            DemographicData dd = new DemographicData();
            DemographicData.Demographic demographic = dd.getDemographic(demo);    
            h.put("lastName", demographic.getLastName());
            h.put("firstName", demographic.getFirstName());
            h.put("phone", demographic.getPhone());
            h.put("demographicNo", demo);
            h.put("relation", rs.getString("relation"));
            
            h.put("subDecisionMaker", booleanConverter(rs.getString("sub_decision_maker")));
            h.put("emergencyContact", booleanConverter(rs.getString("emergency_contact")));
            h.put("notes", rs.getString("notes"));
            h.put("age",demographic.getAge());
            list.add(h);
         }
         db.CloseConn();            
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }       
      return list; 
   }

   
   private Boolean booleanConverter(String s){      
      boolean isTrue = false;
      if (s != null && s.equals("1")){
         isTrue = true;
      }            
      return new Boolean(isTrue);
   }
   
}




