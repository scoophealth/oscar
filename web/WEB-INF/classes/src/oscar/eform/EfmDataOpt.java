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
package oscar.eform ; 

import java.sql.*;
import java.util.* ;
import oscar.oscarDB.* ;
import oscar.util.* ;
import oscar.* ;
//import bean.* ;

public class EfmDataOpt {

  public void save_eform(String file_name,String htmlString ){
    String form_date = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd" );
    String form_time = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
    String query = "insert into eform (file_name,form_date,form_time,form_html) values ('" + UtilMisc.charEscape(file_name, '\'') + "','" +form_date +"','"+form_time+"','"+ UtilMisc.charEscape(htmlString, '\'') +"')";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(query);
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("efm.executeQuery: " + ex.getMessage());
    }
  }

  public void save_eform(String form_name, String file_name, String subject, String htmlString ){
    String form_date = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd" );
    String form_time = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
    String query = "insert into eform (form_name, file_name,form_date,form_time,subject, form_html) values ('" + UtilMisc.charEscape(form_name, '\'') + "','" + UtilMisc.charEscape(file_name, '\'') +"','" +form_date +"','"+form_time+"','"+ UtilMisc.charEscape(subject, '\'')+"','"+ UtilMisc.charEscape(htmlString, '\'') +"')";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(query);
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("efm.executeQuery: " + ex.getMessage());
    }
  }

  public void save_eform_data (int demographic_no,int fid,String form_name,String subject,String form_provider,String form_data){
    String form_date = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd" );
    String form_time = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "HH:mm:ss");
    String query = "insert into eform_data (demographic_no,fid,form_name,subject,form_date,form_time,form_provider,form_data) values ("+demographic_no+","+fid+",'"+ UtilMisc.charEscape(form_name, '\'') +"','"+UtilMisc.charEscape(subject, '\'')+"','"+form_date +"','"+form_time +"','"+form_provider+"','"+UtilMisc.charEscape(form_data, '\'')+"')";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(query);
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("efm.insert into eform_data : " + ex.getMessage());
    }
  }

  public String getXML(int demographic_no,String tag){
    String temp = ""; 
    String query = "select * from demographicaccessory where demographic_no = "+ demographic_no;
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(query) ;
      if (rs.next()){
        temp = rs.getString("content");
        temp = SxmlMisc.getXmlContent(temp,tag);
        db.CloseConn();
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println(": getXML" + ex.getMessage());
    }                   
    return temp;
  }    

  public String[] getEChartAcc(int demographic_no, String[] field){
    String[] temp = new String[field.length]; 
    StringBuffer fields = new StringBuffer(field[0]);
    for (int i=1; i<field.length; i++) {
      fields = fields.append(",").append(field[i] );
    }
    
    String query = "select " + fields.toString() + " from eChart where demographicNo = "+ demographic_no + " order by timeStamp desc limit 1";
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(query) ;
      if (rs.next()){
        for (int i=0; i<field.length; i++) {
          temp[i] = rs.getString(field[i]);
        }
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println(": getEChart" + ex.getMessage());
    }                   
    return temp;
  }    

  public void update_eform_data_status(int  fdid, int d){
    String query = "UPDATE eform_data set status = " +d+ " where fdid= "+fdid ;
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(query);
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("eform_data : " + ex.getMessage());
    }
  }

  public void update_eform_status(int fid, int d){
    String query = "UPDATE eform set status = " +d+ " where fid= "+fid ;
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      db.RunSQL(query);
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("eform : " + ex.getMessage());
    }
  }

  public String getLabel(int demographic_no){
    String temp = ""; 
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select * from demographic where demographic_no = "+ demographic_no) ;

      if (rs.next()){
        temp = rs.getString("last_name") + ","+ rs.getString("first_name") +"\n"
                + rs.getString("address")+"\n"
                + rs.getString("city") +","+rs.getString("province")+ ","+ rs.getString("postal")+ "\n"
                + "Home:"+ rs.getString("phone")+"\n"
                + rs.getString("date_of_birth")+"/"+ rs.getString("month_of_birth")+"/"+ rs.getString("year_of_birth")+"("+ rs.getString("sex")+")" + "\n"
                + rs.getString("hin");
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }                   
    return temp;
  }

  public String getPatientName(int demographic_no){
    String temp = ""; 
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select * from demographic where demographic_no = "+ demographic_no) ;

      if (rs.next()){
        temp = rs.getString("last_name") + ","+ rs.getString("first_name");
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }                   
    return temp;
  }

  public String getFormString(int fid){
    String temp = new String(); 
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select form_html from eform where fid = "+ fid) ;

      if (rs.next()){
        temp = rs.getString("form_html");
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("aq.select form_html: " + ex.getMessage());
    }
    return temp;
  }

  public int getFID(int fdid){
    int temp = 0 ; 
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select fid from eform_data where fdid = "+ fdid) ;
      if (rs.next()){
        temp = rs.getInt("fid");
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
       System.err.println("aq.select form_html: " + ex.getMessage());
    }                   
    return temp;
  }

  public String getFormDataString(int fdid){
    String temp = new String(); 
    try {
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL("select form_data from eform_data where fdid = "+ fdid) ;
      if (rs.next()){
        temp = rs.getString("form_data");
        rs.close();
      }
      db.CloseConn();
    } catch(Exception ex) {
      System.err.println("aq.select form_html: " + ex.getMessage());
    }                   
    return temp;
  } 
}
