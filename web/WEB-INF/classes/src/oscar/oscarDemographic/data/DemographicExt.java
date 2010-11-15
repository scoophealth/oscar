/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 *  Jason Gallagher
 * 
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of DemographicExt
 *
 *
 * DemographicExt.java
 *
 * Created on March 30, 2005, 3:16 PM
 */
/*
 create table demographicExt (
id int (10) not null auto_increment primary key,
demographic_no int (10),
provider_no varchar(6),
key_val char (10),
value text,
date_time datetime,
hidden char(1) default 0);
 
 
create table demographicKeyValue (
id int (10) not null auto_increment primary key,
provider_no varchar (6),
key_val char(10),
key_desc text,
date_time datetime,
hidden char(1) default 0
);

 
 **/

package oscar.oscarDemographic.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

/**
 * This class is used to interface with the DemographicExt table.
 *
 * Its intent is to Extend the different fields Clinics would want to store.
 * @author Jay Gallagher
 */
public class DemographicExt {
   
   
   public DemographicExt() {
   }
   
   public boolean hasKey(String demo, String key){
      boolean haskey = true;
      if (null == getValueForDemoKey(demo,key)){
         haskey = false;
      }
      return haskey;      
   }
   
   public String getValueForDemoKey(String demo, String key){
      String retval  = null;
      try {
            
            ResultSet rs;
            String sql = "select value from demographicExt where demographic_no = ? and key_val = ? order by id desc  limit 1";
            
            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            
            PreparedStatement pstmt = conn.prepareStatement(sql);            
            pstmt.setString(1,demo ); 
            pstmt.setString(2,key);                                                              
            rs = pstmt.executeQuery();
            
            if(rs.next()){
               retval = oscar.Misc.getString(rs, "value");
            }
                                      
            pstmt.close();
            
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
      return retval;
   }
   
   
   
   
   public Hashtable getAllValuesForDemo(String demo){
      Hashtable retval =  new Hashtable();
      try {
            
            ResultSet rs;
                                    
            String sql = "select key_val, value from demographicExt where demographic_no = ? ";
            
            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            
            PreparedStatement pstmt = conn.prepareStatement(sql);            
            pstmt.setString(1,demo ); 
                                                                         
            rs = pstmt.executeQuery();
            
            while(rs.next()){
               String key = oscar.Misc.getString(rs, "key_val");
               String val = oscar.Misc.getString(rs, "value");
               
               MiscUtils.getLogger().debug("for hash Key "+key+" value "+val);
               if(key != null && val != null){
                  retval.put(key,val);
               }
            }
                                      
            pstmt.close();
            
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
      MiscUtils.getLogger().debug("size of hash "+retval.size());
      return retval;
      
   }
   
   
   ArrayList hashtable2ArrayList(Hashtable h){
      Enumeration e= h.keys();
      ArrayList arr = new ArrayList();
      while(e.hasMoreElements()){
         String key = (String) e.nextElement();
         String val = (String) h.get(key);
         String[] sArr = new String[] {key,val};
         arr.add(sArr);
         MiscUtils.getLogger().debug("has2Arr "+key+" val "+val);
      }
      
      return  arr;
   }
   
   public ArrayList getListOfValuesForDemo(String demo){
      return hashtable2ArrayList(getAllValuesForDemo(demo));      
   }
   /**
    * This Method is used to add a key value pair for a patient
    * @param providerNo providers Number entering the key value pair
    * @param demo Demographic number of the patient that the  key/value  pair is for
    * @param key The key ie "cellphone"
    * @param value The value for this key
    */
   public void addKey(String providerNo, String demo,String key, String value){
      try {
                                      
            String sql = "insert into demographicExt (provider_no,demographic_no,key_val,value,date_time) values (?,?,?,?,now())";
            
            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,providerNo);              
            pstmt.setString(2,demo ); 
            pstmt.setString(3,key);  
            pstmt.setString(4,value);                             
                    
            pstmt.executeUpdate();
                                      
            pstmt.close();
            
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
      /*
       insert into demographicExt (demographic_no,key_val,key_desc,value,date_time) values ('1','test','test for this','this value2',now());
      
      */      
   }
   
   
   public void addKey(String providerNo, String demo,String key, String newValue,String oldValue){
      MiscUtils.getLogger().debug("String providerNo "+providerNo+" String demo "+demo+" String key "+key+" String newValue "+newValue+" String oldValue"+oldValue);
      if ( oldValue == null ){ oldValue = ""; }
      if (newValue != null && !oldValue.equalsIgnoreCase(newValue)){
         
         try {
                       
            String sql = "insert into demographicExt (provider_no,demographic_no,key_val,value,date_time) values (?,?,?,?,now())";
            
            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,providerNo);              
            pstmt.setString(2,demo ); 
            pstmt.setString(3,key);  
            pstmt.setString(4,newValue);                             
                    
            pstmt.executeUpdate();
                                      
            pstmt.close();
            
         } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
      }
      /*
       insert into demographicExt (demographic_no,key_val,key_desc,value,date_time) values ('1','test','test for this','this value2',now());
      
      */      
   }
   
}
