// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package bean;
import java.sql.*;
import java.util.* ;

public class DBOperator {

  public DBOperator() {
  }

  public void save_eForms(String file_name,String htmlString ){
    ResultSet rs = null;
    String form_date = new Utility().getToday();
    String form_time = new Utility().getTime();
    String query = "insert into eforms (file_name,form_date,form_time,form_html) values ('" + file_name + "','" +form_date +"','"+form_time+"','"+htmlString+"')";
    try {
         DBConnect dbc= new DBConnect();
         dbc.executeUpdate(query);
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }
  }

  public void save_eForms(String form_name,String file_name,String subject,String htmlString ){
    ResultSet rs = null;
    String form_date = new Utility().getToday();
    String form_time = new Utility().getTime();
    String query = "insert into eforms (form_name,file_name,form_date,form_time,subject,form_html) values ('"+ form_name + "','" + file_name + "','" +form_date +"','"+form_time+"','"+subject+"','" +htmlString+"')";
    try {
         DBConnect dbc= new DBConnect();
         dbc.executeUpdate(query);
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }
  }
  public void save_eForms_data (int demographic_no,int fid,String form_name,String subject,String form_date,String form_provider,String form_data){
    String form_time = new Utility().getTime();
    String query = "insert into eforms_data (demographic_no,fid,form_name,subject,form_date,form_time,form_provider,form_data) values ("+demographic_no+","+fid+",'"+ form_name +"','"+subject+"','"+form_date +"','"+form_time +"','"+form_provider+"','"+form_data+"')";
    try {
         DBConnect dbc= new DBConnect();
         dbc.executeUpdate(query);
    } catch(Exception ex) {
       System.err.println("aq.insert into eForms_data : " + ex.getMessage());
    }
  }
 
  public void edit_eForms_data (int fdid,int demographic_no,String form_name,String subject,String form_date,String form_provider,String form_data){
    String form_time = new Utility().getTime();
    String query = "UPDATE eforms_data set demographic_no="+demographic_no+", form_name='"+form_name+"', subject='"+subject+"', form_date='"+form_date+"', form_time='"+form_time+"', form_provider='"+form_provider+"', form_data='"+form_data+"'  where fdid="+fdid;
    try {
         DBConnect dbc= new DBConnect();
         dbc.executeUpdate(query);
    } catch(Exception ex) {
       System.err.println("aq.insert into eForms_data : " + ex.getMessage());
    }
  }

  public void save_collect_data (String in_data){
    String collect_date = new Utility().getToday();
    String collect_data = new Utility().moveSingleQuote(in_data);
    String query = "insert into collect_data (collect_date,collect_data) values ('"+collect_date +"','"+collect_data+"')";
    try {
         DBConnect dbc= new DBConnect();
         dbc.executeUpdate(query);
    } catch(Exception ex) {
       System.err.println("aq.insert into eForms_data : " + ex.getMessage());
    }
  }


public String getLabel(int demographic_no){
   String temp = ""; 
   String query = "select * from demographic where demographic_no = "+ demographic_no;
    try {
         DBConnect dbc= new DBConnect();
         ResultSet rs = dbc.executeQuery(query);
         if (rs.next()){
            temp = rs.getString("last_name") + ","+ rs.getString("first_name") +"\n"
                + rs.getString("address")+"\n"
                + rs.getString("city") +","+rs.getString("province")+ ","+ rs.getString("postal")+ "\n"
                + "Home:"+ rs.getString("phone")+"\n"
                + rs.getString("date_of_birth")+"/"+ rs.getString("month_of_birth")+"/"+ rs.getString("year_of_birth")+"("+ rs.getString("sex")+")" + "\n"
                + rs.getString("hin");
            rs.close();
         }
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: " + ex.getMessage());
    }                   
    return temp;
}

public String getXML(int demographic_no,String tag){
   String temp = ""; 
   String query = "select * from demographicaccessory where demographic_no = "+ demographic_no;
    try {
         DBConnect dbc= new DBConnect();
         ResultSet rs = dbc.executeQuery(query);
         if (rs.next()){
           temp = rs.getString("content");
           temp = (new FunctionGenerator()).getXMLout(temp,tag,"</"+tag.substring(1,tag.length()));
           rs.close();
         }
    } catch(Exception ex) {
       System.err.println("aq.executeQuery: getXML" + ex.getMessage());
    }                   
    return temp;
}    

public String getFormString(int fid){
   String temp = new String(); 
   String query = "select form_html from eforms where fid = "+ fid;
    try {
         DBConnect dbc= new DBConnect();
         ResultSet rs = dbc.executeQuery(query);
         if (rs.next()){
           temp = rs.getString("form_html");
           rs.close();
         }
    } catch(Exception ex) {
       System.err.println("aq.select form_html: " + ex.getMessage());
    }                   
    return temp;
}

public String getFormDataString(int fdid){
   String temp = new String(); 
   String query = "select form_data from eforms_data where fdid = "+ fdid;
    try {
         DBConnect dbc= new DBConnect();
         ResultSet rs = dbc.executeQuery(query);
         if (rs.next()){
           temp = rs.getString("form_data");
           rs.close();
         }
    } catch(Exception ex) {
       System.err.println("aq.select form_html: " + ex.getMessage());
    }                   
    return temp;
}

public int getFID(int fdid){
   int temp = 0 ; 
   String query = "select fid from eforms_data where fdid = "+ fdid;
    try {
         DBConnect dbc= new DBConnect();
         ResultSet rs = dbc.executeQuery(query);
         if (rs.next()){
           temp = rs.getInt("fid");
           rs.close();
         }
    } catch(Exception ex) {
       System.err.println("aq.select form_html: " + ex.getMessage());
    }                   
    return temp;
}

}
