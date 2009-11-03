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
 * Ontario, Canada   Creates a new instance of DemographicSets
 *
 *
 * DemographicSets.java
 *
 * Created on May 30, 2005, 1:58 PM
 */

package oscar.oscarReport.data;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Jay Gallagher
 *
   create table demographicSets (
      demographic_no int(10),
      set_name varchar(20),
      eligibility char(1)
   );
 */

public class DemographicSets {
   
   
   public DemographicSets() {
   }
   
   public void addDemographicSet(String setName, String demographic){      
       ArrayList list = new ArrayList();
       list.add(demographic);
       addDemographicSet(setName,list);
   }
   
   public void addDemographicSet(String setName, ArrayList demoList){      
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              
         for (int i = 0; i < demoList.size(); i++){                            
            String demographicNo = (String) demoList.get(i);
            db.RunSQL("insert into demographicSets (set_name,demographic_no) values ('"+StringEscapeUtils.escapeSql(setName)+"','"+demographicNo+"')");
         }
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
   }
   
   
   public ArrayList getDemographicSet(String setName){      
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select * from demographicSets where archive != '1' and set_name = '"+StringEscapeUtils.escapeSql(setName)+"'");          
         
         while (rs.next()){
            retval.add(db.getString(rs,"demographic_no"));            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   public ArrayList getDemographicSet(ArrayList setNames) {
       ArrayList retval = new ArrayList();
       StringBuffer strNames = new StringBuffer();
       
       for( int idx = 0; idx < setNames.size(); ++idx ) {
           strNames.append("'" + StringEscapeUtils.escapeSql((String)setNames.get(idx)) + "'");
           if( idx < (setNames.size()-1)) {
               strNames.append(",");
           }
       }
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select * from demographicSets where  archive != '1' and set_name in (" + strNames.toString() + ") group by demographic_no");          
         
         while (rs.next()){
            retval.add(db.getString(rs,"demographic_no"));            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   public ArrayList getDemographicSetExt(String setName){      
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select * from demographicSets  where archive != '1' and  set_name = '"+StringEscapeUtils.escapeSql(setName)+"'");          
         
         while (rs.next()){
            Hashtable h = new Hashtable();
            h.put("demographic_no",db.getString(rs,"demographic_no"));            
            String el = db.getString(rs,"eligibility");
            if (el == null || el.equalsIgnoreCase("null")){
               el = "0";
            }
            h.put("eligibility",el);
            retval.add(h);            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   
   public ArrayList getIneligibleDemographicSet(String setName){      
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select * from demographicSets where set_name = '"+StringEscapeUtils.escapeSql(setName)+"' and eligibility = '1' ");          
         
         while (rs.next()){
            retval.add(db.getString(rs,"demographic_no"));            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   public ArrayList getEligibleDemographicSet(String setName){      
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select * from demographicSets where set_name = '"+StringEscapeUtils.escapeSql(setName)+"' and eligibility = '0' ");          
         
         while (rs.next()){
            retval.add(db.getString(rs,"demographic_no"));            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   
   public ArrayList setDemographicIneligible(String setName,String demoNo){      
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         db.RunSQL("update demographicSets set eligibility = '1' where set_name = '"+StringEscapeUtils.escapeSql(setName)+"' and demographic_no = '"+demoNo+"'");
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   public ArrayList setDemographicDelete(String setName,String demoNo){      
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         db.RunSQL("update demographicSets set archive = '1' where set_name = '"+StringEscapeUtils.escapeSql(setName)+"' and demographic_no = '"+demoNo+"'");
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   
   public ArrayList getDemographicSets(String demoNo) {
       ArrayList retval = new ArrayList();
       try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select distinct set_name from demographicSets where archive = '1' and demographic_no = " + demoNo);
         
         while (rs.next()){
            retval.add(db.getString(rs,"set_name"));            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
   
   
   public ArrayList getDemographicSets(){
      ArrayList retval = new ArrayList();
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL("select distinct set_name from demographicSets ");
         
         while (rs.next()){
            retval.add(db.getString(rs,"set_name"));            
         }
         rs.close();
      }catch (java.sql.SQLException e){ System.out.println(e.getMessage()); }            
      return retval;
   }
}
