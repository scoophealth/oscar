package bean;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;

/*
 * $RCSfile: AbstractApplication.java,v1.0 $
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * Tom Zhu
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */

public class DBQuery {
     private int wrongInVolume = 0 ;
     private String sVolume = "";
     ResultSet RS = null;
     private int[] in_correct = new int[6] ;

  public DBQuery() {
  }

  public ResultSet getAppointmentDate(int provider_no){
     String getQuery = " select distinct appointment_date from appointment where provider_no = "+ provider_no +" order by appointment_date DESC " ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select distinct appointment_date from appointment : " + ex.getMessage());
                    }
    return RS;
  }

  public ResultSet getAppointmentDate(int provider_no,int month,int year){
     String getQuery = " select distinct appointment_date from appointment where provider_no = "+ provider_no +" and instr('appointment_date','2001-05')>0  order by appointment_date DESC " ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select distinct appointment_date from appointment : " + ex.getMessage());
                    }
    return RS;
  } 
  public ResultSet getAppointmentSubDetail(int provider_no,String appointment_date){
     String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date='"+appointment_date+"' order by start_time";
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from appointment : " + ex.getMessage());
                    }
    return RS;
  }

  public int getAppointmentId(int provider_no,String appointment_date){
     String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date='"+appointment_date+"'";
         ResultSet RS = null;
         int i = 1;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    RS.next();
                    i=RS.getInt("appointment_no");
                    } catch(Exception ex) {
                    System.err.println(" select * from appointment : " + ex.getMessage());
                    }
    return i;
  } 
  public ResultSet getAppointmentDetail(int appointment_no){
     String getQuery = " select * from appointment where appointment_no = "+ appointment_no ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from appointment : " + ex.getMessage());
                    }
    return RS;
  }

  public int getAppointmentNo(String appointment_date,int provider_no){
     String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date = '"+appointment_date+"'" ;
         ResultSet RS = null;
         int returnNo = 0;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                       while (RS.next()){
                       returnNo++;
                       }
                       RS.close();
                    } catch(Exception ex) {
                    System.err.println("select vid from tvolume where volume =: " + ex.getMessage());
                    }
    return returnNo ;
  }

  public String [] getAppointmentNo(String[] appointment_date,int provider_no){
     int num = appointment_date.length;
     String[] returnNo = new String [num];
     
     for (int i=0;i<num;i++){
       if (appointment_date[i].length()>3){
         String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date = '"+appointment_date[i]+"'" ;
         ResultSet RS = null;
                  try{
                    int k = 0;
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                       while (RS.next()){
                       k++;
                       }
                       returnNo[i]= new Integer(k).toString(); 
                       RS.close();
                    } catch(Exception ex) {
                    System.err.println("select vid from tvolume where volume =: " + ex.getMessage());
                    }
       }else{
             returnNo[i]=""; 
       }
     }
    return returnNo ;
  }
  
  public ResultSet getAppointmentTime(int p_no){
     String getQuery = " select * from appointment where provider_no = "+ p_no +" order by appointment_date,start_time " ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from appointment : " + ex.getMessage());
                    }
    return RS;
  }

  public String getProvider(int provider_no){
     String getQuery = " select * from provider where provider_no = "+ provider_no ;
         String providerName = "";
                try{
                    DBConnect dbc= new DBConnect();
                    ResultSet RS = dbc.executeQuery(getQuery);
                       while (RS.next()){
                         providerName = RS.getString("last_name")+","+ RS.getString("first_name");
                       }
                       RS.close();

                    } catch(Exception ex) {
                    System.err.println("select vid from tvolume where volume =: " + ex.getMessage());
                    }

    return providerName ;
  }


  public String haveAppointmentTodayOrNot(int provider_no){
      
     Date today = new Date();      
     String todayStrig =  today.getYear()+1900 +"-"+ today.getMonth()+1 +"-"+ today.getDate() ;
 
     String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date ='"+ todayStrig +"'";
         String providerName = "";
                try{
                    DBConnect dbc= new DBConnect();
                    ResultSet RS = dbc.executeQuery(getQuery);
                       if (RS.next()){
                            
                       }else{
                           todayStrig = "0";
                       }
                       RS.close();
                    } catch(Exception ex) {
                    System.err.println("select * from appointment where provider_no =: " + ex.getMessage());
                    }
 
    
     return todayStrig ;
  }

  public String haveAppointmentOnThatDayOrNot(int provider_no,String DayString){
 
     String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date ='"+ DayString +"'";
         String providerName = "";
                try{
                    DBConnect dbc= new DBConnect();
                    ResultSet RS = dbc.executeQuery(getQuery);
                       if (RS.next()){
                            
                       }else{
                           DayString = "0";
                       }
                       RS.close();
                    } catch(Exception ex) {
                    System.err.println("select * from appointment where provider_no =: " + ex.getMessage());
                    }
 
    
     return DayString ;
  }
  public int deleteAppointment(int appointment_no){
     String getUpdate = " delete from appointment where appointment_no = "+ appointment_no;
         int returnVid = 1;
                  try{
                    DBConnect dbc= new DBConnect();
                    dbc.executeUpdate(getUpdate);
                    } catch(Exception ex) {
                      returnVid = 0;
                    System.err.println("select vid from tvolume where volume =: " + ex.getMessage());
                    }
    return returnVid ;
  }

 
  public ResultSet getSearchName(String keyword){
     String getQuery="";
     String temp_first="";
     String temp_last="";

       if(keyword==""){
           getQuery = " select * from demographic order by last_name" ;
       }
       if(keyword.indexOf(",")<0){
           getQuery = " select * from demographic where last_name like '"+keyword+"%' or first_name like '"+keyword+"%' order by last_name" ;
       }else{
       	   temp_last=keyword.substring(0,keyword.indexOf(","));
       	   temp_first=keyword.substring(keyword.indexOf(",")+1);
           getQuery = " select * from demographic where last_name like '"+temp_last+"%' and first_name like '"+temp_first+"%' order by last_name" ;
       }
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  }

  public ResultSet getSearchPhone(String keyword){
         String getQuery = " select * from demographic where phone like '%"+keyword+"%' order by last_name" ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  }
 
  public ResultSet getSearchDob(String keyword){
     String getQuery="";
     String temp_year="";
     String temp_month="";
     String temp_date="";
     String temp="";

       temp_year = keyword.substring(0,keyword.indexOf("-")) ;
       temp = keyword.substring(keyword.indexOf("-")+1) ;
       temp_month= temp.substring(0,temp.indexOf("-")) ;
       temp_date= temp.substring(temp.indexOf("-")+1) ;

         getQuery = " select * from demographic where year_of_birth like '%"+temp_year+"%' and month_of_birth like '%"+temp_month+"%' and date_of_birth like '%"+temp_date+"%' order by last_name" ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  }

  public ResultSet getSearchAddress(String keyword){
         String getQuery = " select * from demographic where address like '%"+keyword+"%' order by last_name" ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  }
 
  public ResultSet getSearchHin(String keyword){
         String getQuery = " select * from demographic where hin like '%"+keyword+"%' order by last_name" ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  } 
 
  public ResultSet getSearchChartNo(String keyword){
         String getQuery = " select * from demographic where chart_no like '%"+keyword+"%' order by last_name" ;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  }   


  public ResultSet getSearchByOrder(String keyword){
         String getQuery = " select * from demographic order by "+ keyword;
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                    } catch(Exception ex) {
                    System.err.println(" select * from demographic : " + ex.getMessage());
                    }
    return RS;
  }   
  
     
/*
  public ResultSet getUsers(){
     String getMaxVid = "  select * from tusers ";
         ResultSet RS = null;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getMaxVid);
                    } catch(Exception ex) {
                    System.err.println("select * from tusers: " + ex.getMessage());
                    }
    return RS;
  }

 

  public int getVid(String in_volume){
     String getMaxVid = " select vid from tvolume where volume = '"+ in_volume+"'";
         ResultSet RS = null;
         int returnVid = 1;
                  try{
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getMaxVid);
                    RS.last();
                    returnVid = RS.getInt("vid");
                    } catch(Exception ex) {
                    System.err.println("select vid from tvolume where volume =: " + ex.getMessage());
                    }
    return returnVid ;
  }
  
  public void updateUser(int in_mailme,String in_cfpcNo,int in_eOrNot,int in_pOrNot,int in_pay,String in_payDate){
    String  updateUser = " update tusers set mailme ='"+in_mailme+"' ,cfpcNo ='"+in_cfpcNo+"',"
                       +" eOrNot="+in_eOrNot+", pOrNot="+in_pOrNot+", pay="+in_pay+", payDate='"+in_payDate+"'";
                try{
                    DBConnect dbc= new DBConnect();
                     dbc.executeUpdate(updateUser);
                    } catch(Exception ex) {
                    System.err.println("update tusers set mailme: " + ex.getMessage());
                    }

  }
 
   public void makeBackupTables(){
   String makeBackupTables = "BACKUP TABLE tadmin,tusers,tanswers,tquestions,treference,tsubtopic,ttopic,tvolume  TO '/back'";
                try{
                    DBConnect dbc= new DBConnect();
                     dbc.executeUpdate(makeBackupTables);
                    } catch(Exception ex) {
                    System.err.println("makeBackupTables : " + ex.getMessage());
                    }
   }

   public void makeRestoreTables(){
   String makeRestoreTables = "RESTORE TABLE tadmin,tusers,tanswers,tquestions,treference,tsubtopic,ttopic,tvolume  FROM '/back'";
                try{
                    DBConnect dbc= new DBConnect();
                     dbc.executeUpdate(makeRestoreTables);
                    } catch(Exception ex) {
                    System.err.println("makeRestoreTables : " + ex.getMessage());
                    }
   }

*/


}
