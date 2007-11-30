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
 * Ontario, Canada   Creates a new instance of PreventionData
 *
 *
 * PreventionData.java
 *
 * Created on May 25, 2005, 10:16 PM
 */

package oscar.oscarPrevention;

import java.sql.*;
import java.util.*;
import oscar.oscarDB.*;
import oscar.oscarDemographic.data.*;
import oscar.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oscar.oscarProvider.data.ProviderData;

/**
 *
 * @author Jay Gallagher
 */
public class PreventionData {
    private static Log log = LogFactory.getLog(PreventionData.class);
   /*
  create table preventions(
      id int(10) NOT NULL auto_increment primary key,  
      demographic_no int(10) NOT NULL default '0',
      creation_date datetime,
      prevention_date date,
      provider_no varchar(6) NOT NULL default '',
      provider_name varchar(255),
      prevention_type varchar(20),
      next_date date,
      never char(1) default '0',
      deleted char(1) default '0',
      refused char(1) default '0',
      creator varchar(6) default NULL
  )
      
  need to add next_date
  
  create table preventionsExt(
      id int(10) NOT NULL auto_increment primary key,  
      prevention_id int(10),
      keyval varchar(20),
      val text           
  )  
    
  */ 
    
   public PreventionData() {
   }
   
   public void insertPreventionData(String creator, String demoNo, String date, String providerNo, String providerName, String preventionType,String refused,String nextDate,String neverWarn,ArrayList list){
      String sql = null;
       try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         sql = "Insert into preventions (creator,demographic_no,prevention_date,provider_no,provider_name,prevention_type,refused,creation_date,next_date,never) values "
         + "('"+creator+"','"+demoNo+"','"+date+"','"+providerNo+"','"+providerName+"','"+preventionType+"','"+refused+"',now(),'"+nextDate+"','"+neverWarn+"')";            
         log.debug(sql);
         db.RunSQL(sql);
         rs = db.GetSQL("select Last_insert_id()");
         int insertId = -1;
         if (rs.next()){
             insertId = rs.getInt(1);
         }         
         if (insertId != -1){
            for (int i = 0; i < list.size(); i++){
               Hashtable h = (Hashtable) list.get(i);
               String key = null;
	       if (h.keys().hasMoreElements()) key = (String) h.keys().nextElement();
               if (key != null && h.get(key) != null ){
                  String val = (String) h.get(key);
                  addPreventionKeyValue(""+insertId,key,val);
               }
            }
         }         
         db.CloseConn();            
      } catch (SQLException e) { 
         log.error(e.getMessage(),e);
         log.error(sql);
      }
   }
   
      
   public void addPreventionKeyValue(String preventionId, String keyval, String val){
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         sql = "Insert into preventionsExt (prevention_id,keyval,val) values "
         + "('"+preventionId+"','"+keyval+"','"+StringEscapeUtils.escapeSql(val)+"')";            
         log.debug(sql);            
         db.RunSQL(sql);                                                
         db.CloseConn();            
      } catch (SQLException e) {
         log.error(e.getMessage(),e);
         log.error(sql);
      }
   }
   
   public Hashtable getPreventionKeyValues(String preventionId){
      Hashtable h = new Hashtable();
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         sql = "select * from  preventionsExt where prevention_id = '"+preventionId+"'";         
         log.debug(sql);            
         rs = db.GetSQL(sql);                                                
         while(rs.next()){
            String key = rs.getString("keyval");
            String val = rs.getString("val");
            if (key != null && val != null){
               h.put(key,val);
            }
         }
         db.CloseConn();            
      } catch (SQLException e) {
         log.error(e.getMessage(),e);
         log.error(sql);
      }
      return h;
   }
   
   
   public void deletePreventionData(String id){
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         sql = "update preventions set deleted = '1' where id = '"+id+"' ";  //TODO: logg this in the Deletion record table or generic logging table
         log.debug(sql);
         db.RunSQL(sql);                                       
         db.CloseConn();            
      } catch (SQLException e) {
         log.error(e.getMessage(),e);
         log.error(sql);
      }
   }
   
   public void setNextPreventionDate(String date,String id){
      String sql = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         sql = "update preventions set next_date = '"+date+"' where id = '"+id+"' ";  
         log.debug(sql);
         db.RunSQL(sql);                                       
         db.CloseConn();            
      } catch (SQLException e) {
         log.error(e.getMessage(),e);
         log.error(sql);
      }
   }
   
   public String getProviderName(Hashtable hash){
       String name = "";
       if (hash != null){
           String proNum = (String) hash.get("provider_no");
           if (proNum == null || proNum.equals("-1")){
              name = (String) hash.get("provider_name");
           }else{
              name = ProviderData.getProviderName(proNum);
           }
       }
       return name;
   }
   
   public void updatetPreventionData(String id ,String creator, String demoNo, String date, String providerNo, String providerName, String preventionType,String refused,String nextDate,String neverWarn,ArrayList list){
      deletePreventionData( id);
      insertPreventionData(creator,demoNo, date, providerNo, providerName, preventionType,refused,nextDate,neverWarn,list);
   }
   
   public ArrayList getPreventionData(String demoNo){
      return getPreventionData("%",demoNo);
   }
   
   
   public ArrayList getPreventionDataFromExt(String extKey, String extVal){
      ArrayList list = new ArrayList();
      String sql = null;   
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs;
         sql = "Select prevention_id from preventionsExt where  keyval = '"+extKey+"' and val = '"+extVal+"'" ;            
         log.debug(sql); 
         rs = db.GetSQL(sql);
         while (rs.next()){     
           Hashtable hash = getPreventionById(rs.getString("prevention_id"));
           if(hash.get("deleted") != null && ( (String) hash.get("deleted")).equals("0")   ){
              list.add(hash);    
           }    
         }        
         db.CloseConn();            
       } catch (SQLException e) {
           log.error(e.getMessage(),e);
           log.error(sql);
       }
       return list;
   }
   
   
   
   
   
   
   ////////
   /**
    *Method to get a list of (demographic #, prevention dates, and key values)  of a certain type <injectionTppe> from a start Date to an end Date with a Ext key value
    *EG get all Rh injection's product #, from 2006-12-12 to 2006-12-18
    *
    */
   public ArrayList getExtValues(String injectionType,java.util.Date startDate,java.util.Date endDate,String keyVal){
   String sql = "select demographic_no, prevention_date ,val from preventions, preventionsExt where preventions.id = preventionsExt.prevention_id  and prevention_type = ? and prevention_date >= ? and prevention_date <= ? and preventionsExt.keyval = ? and preventions.deleted = '0' and preventions.refused = '0' order by prevention_date";
     
        ArrayList list = new ArrayList();
        DBHandler dbhandler = null;
        try {
            //log.debug("e-DATE: "+date);
            dbhandler = new DBHandler(DBHandler.OSCAR_DATA);
            
            PreparedStatement pstmt = dbhandler.GetConnection().prepareStatement(sql);
            pstmt.setString(1,injectionType);
            pstmt.setDate(2,new java.sql.Date(startDate.getTime()));
            pstmt.setDate(3,new java.sql.Date(endDate.getTime()));
            pstmt.setString(4,keyVal);
            
            ResultSet rs = pstmt.executeQuery();
         
            while (rs.next()){
                Hashtable h = new Hashtable();
                 h.put("demographic_no", rs.getString("demographic_no"));
                 h.put("val",rs.getString("val"));
                 h.put("prevention_date",rs.getDate("prevention_date"));
                list.add(h);
            }
          
            rs.close();
            pstmt.close();
            
        }catch(Exception e){
            log.error(e.getMessage(),e);
            log.error(sql);
        }
        return list;
   }
   ////////
   
   
   
   public ArrayList getPreventionData(String preventionType,String demoNo){
      ArrayList list = new ArrayList();
      String sql = null;
      DemographicData dd = new DemographicData();
      java.util.Date dob = dd.getDemographicDOB(demoNo);
      
      try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            sql = "Select * from preventions where  demographic_no = '"+demoNo+"' and prevention_type like '"+preventionType+"' and deleted != 1 order by prevention_date" ;            
            log.debug(sql);            
            rs = db.GetSQL(sql);
            while (rs.next()){
               Hashtable h = new Hashtable();
               h.put("id", rs.getString("id"));               
               h.put("refused",rs.getString("refused"));
               h.put("type",rs.getString("prevention_type"));
               log.debug("id set to "+rs.getString("id"));
               h.put("provider_no",rs.getString("provider_no")); 
               h.put("provider_name",rs.getString("provider_name"));          
               h.put("prevention_date",blankIfNull(rs.getString("prevention_date")));
               java.util.Date date = null;
               
               try{               
                   date = rs.getDate("prevention_date");
                  h.put("prevention_date_asDate",date);
               }catch(Exception pe){}
               String age = "N/A";   
               if(date!= null){                               
                  age = UtilDateUtilities.calcAgeAtDate(dob,date); 
               }
               h.put("age",age);
               list.add(h);
            }
            
            db.CloseConn();            
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            log.error(sql);
        }
      return list;
   }
   
   private static String blankIfNull(String s){
       if ( s == null)
           return "";
       return s;
   }
   
   
   public String getPreventionComment(String id){
       log.debug("Calling getPreventionComment "+id);
       String comment = null;
       String sql = null;
       try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            sql = "Select val from preventionsExt where  prevention_id = '"+id+"' and keyval = 'comments' " ;            
            log.debug(sql);            
            rs = db.GetSQL(sql);
            if (rs.next()){
               comment = rs.getString("val");
               if ( comment != null && comment.trim().length() == 0){
                  comment = null;
               }
            }            
            db.CloseConn();            
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            log.error(sql);
        }
       return comment;
   }
   
   public Prevention getPrevention(String demoNo){
      DemographicData dd = new DemographicData();
      java.util.Date dob = dd.getDemographicDOB(demoNo);
      String sex = dd.getDemographicSex(demoNo);
      String sql = null;
      Prevention p = new Prevention(sex,dob);
      
      try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            sql = "Select * from preventions where  demographic_no = '"+demoNo+"'  and deleted != 1 order by prevention_type,prevention_date" ;            
            log.debug(sql);            
            rs = db.GetSQL(sql);
            while (rs.next()){
               PreventionItem pi = new PreventionItem( rs.getString("prevention_type"),rs.getDate("prevention_date"),rs.getString("never"), rs.getDate("next_date") ) ;
               p.addPreventionItem(pi,rs.getString("prevention_type"));
            }
            db.CloseConn();            
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            log.error(sql);
        }
      return p;
   }
   
   public Hashtable  getPreventionById(String id){
      String sql = null;
      Hashtable h = null;
      try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            sql = "Select * from preventions where  id = '"+id+"' " ;            
            log.debug(sql);            
            rs = db.GetSQL(sql);
            if (rs.next()){
               h = new Hashtable();
               log.debug("preventionType"+rs.getString("prevention_type"));
               addToHashIfNotNull(h,"id", rs.getString("id"));
               addToHashIfNotNull(h,"provider_no",rs.getString("provider_no"));
               addToHashIfNotNull(h,"demographicNo", rs.getString("demographic_no"));
               addToHashIfNotNull(h,"creationDate", rs.getString("creation_date"));
               addToHashIfNotNull(h,"preventionDate", rs.getString("prevention_date"));
               addToHashIfNotNull(h,"prevention_date_asDate",rs.getDate("prevention_date"));
               //log.debug(rs.getDate("prevention_date"));
               addToHashIfNotNull(h,"providerName", rs.getString("provider_name"));
               addToHashIfNotNull(h,"preventionType", rs.getString("prevention_type"));
               addToHashIfNotNull(h,"deleted", rs.getString("deleted"));
               addToHashIfNotNull(h,"refused", rs.getString("refused"));  
               addToHashIfNotNull(h,"next_date", rs.getString("next_date"));
               addToHashIfNotNull(h,"never",rs.getString("never"));
               
	       String refused = " completed";
	       switch (Integer.parseInt(rs.getString("refused"))) {
		   case 1: refused = " refused"; break;
		   case 2: refused = " ineligible"; break;
	       }
	       String provider = ProviderData.getProviderName(rs.getString("provider_no"));
	       String summary = "Prevention " + rs.getString("prevention_type") + " provided by " + provider + " on " + rs.getString("prevention_date") +"\n";
	       Hashtable ext = getPreventionKeyValues(rs.getString("id"));
	       if (ext.containsKey("result")) {
		   summary += "Result: " + ext.get("result");
		   if (!ext.get("reason").equals("")) summary += "\nReason: " + ext.get("reason");
	       } else {
		   if (!ext.get("location").equals("")) {
		       addToHashIfNotNull(h, "location", (String)ext.get("location"));
		       summary += "Location: " + ext.get("location") + "\n";
		   }
		   if (!ext.get("route").equals("")) {
		       addToHashIfNotNull(h, "route", (String)ext.get("route"));
		       summary += "Route: " + ext.get("route") + "\n";
		   }
		   if (!ext.get("lot").equals("")) {
		       addToHashIfNotNull(h, "lot", (String)ext.get("lot"));
		       summary += "Lot: " + ext.get("lot") + "\n";
		   }
		   if (!ext.get("manufacture").equals("")) {
		       addToHashIfNotNull(h, "manufacture", (String)ext.get("manufacture"));
		       summary += "Manufacturer: " + ext.get("manufacture");
		   }
	       }
	       addToHashIfNotNull(h,"summary", summary);
               log.debug("1"+h.get("preventionType")+" "+h.size());
               log.debug("id"+h.get("id"));
            }
            
            db.CloseConn();            
        } catch (SQLException e) {
            log.error(e.getMessage(),e);
            log.error(sql);
        }
      return h;
   }
   
   private void addToHashIfNotNull(Hashtable h,String key,String val){
      if (val != null && !val.equalsIgnoreCase("null")){
         h.put(key,val);
      }
   }
   
   private void addToHashIfNotNull(Hashtable h,String key,java.util.Date val){
      if (val != null ){
         h.put(key,val);
      }
   }
}

