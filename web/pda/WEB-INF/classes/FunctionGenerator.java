package bean;
import java.sql.*;
import java.lang.reflect.Array;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class FunctionGenerator {
    private static Logger logger=MiscUtils.getLogger(); 

  public FunctionGenerator() {
  }

  public String[] getAppointmentInMonth(int provider_no,String [] in_array){

     String start_date = in_array[0];
     int in_array_length = in_array.length;
     String end_date = in_array[in_array_length-1];

     String [] returnArray = new String[in_array_length]; 
     String [] dateArray = new String[in_array_length]; 
     String [] dateArray_have ;
     String dateString =",";

//     String getQuery = " select name from appointment where provider_no = "+ provider_no +" and appointment_date between '"+start_date+"' and '"+end_date+"' order by appointment_date DESC " ;
     String getQuery = " select * from appointment where provider_no = "+ provider_no +" and appointment_date between '"+start_date+"' and '"+end_date+"' order by appointment_date DESC " ;
         ResultSet RS;
                  try{
                    int i=0;
                    DBConnect dbc= new DBConnect();
                    RS = dbc.executeQuery(getQuery);
                        while(RS.next()){
                    	 i++;
                    	}
                       dateArray_have = new String[i]; 
                    
                    RS.beforeFirst();
                        int j=0;
                        while(RS.next()){
                          dateArray_have[j]= RS.getString("appointment_date");
                          dateString = dateString +","+ dateArray_have[j];
                          j++;
                        }	 
                    RS.close();
                     
                    } catch(Exception ex) {
                    logger.error(" select name from appointment  : " + ex.getMessage());
                    }

          for (int k =0; k<in_array_length; k++){
                if (dateString.indexOf(in_array[k])<0){
               	      returnArray[k]="";
               	}else{
               	      returnArray[k]=in_array[k];
               	}	
          	
          }     


    return returnArray ;
  }

  public String[] getMonthCalendar(String start_date,String end_date){
     int days = new Integer(end_date.substring(8)).intValue()- new Integer(start_date.substring(8)).intValue()+1;
     int start_no = new Integer(start_date.substring(8)).intValue();
     String[] returnArray = new String[days];

        for(int i=0;i<days;i++){
     	    if (start_no+i<=9){ 
//     	   returnArray[i]= start_date.substring(0,8)+ (i<=9)?"0"+new Integer(i).toString():new Integer(i).toString() ;
       	         returnArray[i]= start_date.substring(0,8)+ "0"+new Integer(start_no+i).toString();
            }else{
                 returnArray[i]= start_date.substring(0,8)+ new Integer(start_no+i).toString();
         
            }
    	}

      return  returnArray;
  }

// replace all ":" with "%3A" so that the http header can accept 
  public String getHttpHeaderString(String in_string){
      String returnString = in_string;
        while (returnString.indexOf(":")>0){
          String temp = returnString.substring(0,returnString.indexOf(":"))+"%3A"+ returnString.substring(returnString.indexOf(":")+1); 
          returnString = temp ;
        } 
        while (returnString.indexOf(",")>0){
          String temp_1 = returnString.substring(0,returnString.indexOf(","))+"%2C"+ returnString.substring(returnString.indexOf(",")+1); 
          returnString = temp_1 ;
        } 

      return returnString;
  }

  public int getAge(String in_year,String in_month){
          int age =  0;
	  GregorianCalendar now=new GregorianCalendar();
	  int curYear = now.get(Calendar.YEAR);
	  int curMonth = (now.get(Calendar.MONTH)+1);
      if(new Integer(in_month).intValue()-curMonth>5){
      	age = curYear - new Integer(in_year).intValue()-1;
      }else if(curMonth-new Integer(in_month).intValue()>5){
      	age = curYear - new Integer(in_year).intValue()+1;
      }else{
      	age = curYear - new Integer(in_year).intValue();
      }
      return age;
  }


  public String getXMLout(String in_string,String s_start,String s_end){
      String returnString = "";
      int start_length = s_start.length();
    
        if (!(in_string.indexOf(s_start)<0)){
           returnString =  in_string.substring(in_string.indexOf(s_start)+start_length,in_string.indexOf(s_end)); 
        } 
      return returnString;
  }


  public String[] getAppointmentTodayArray(String [] timeTable,String appointMent){
     int length = Array.getLength(timeTable);
     String [] returnArray = new String[length];
        int flag =0; 
        int i=0;
        while(i<length){
          if (flag ==0){ 
           int timeTable_hour = new Integer(timeTable[i].substring(0,2)).intValue();
           int timeTable_mini = new Integer(timeTable[i].substring(3,5)).intValue();

           int appointMent_hour = new Integer(appointMent.substring(0,2)).intValue();
           int appointMent_mini = new Integer(appointMent.substring(3,5)).intValue();
           
           
            if(timeTable_hour<appointMent_hour ){
                returnArray[i] = timeTable[i];
                   i++;
            }else if((timeTable_hour==appointMent_hour)&&((appointMent_mini-timeTable_mini)>=15)){
                returnArray[i] = timeTable[i];
                   i++; 
            }else{
                returnArray[i] = appointMent;
                flag=1;;
                   i++;
            }

           }else{
                returnArray[i] = timeTable[i];
                   i++;
           }
       	}
     return  returnArray;
  }

  public String[] getTry(String [] timeTable,String []appointMent){
     int length = Array.getLength(timeTable);
     int appointMent_length = Array.getLength(appointMent);
     String [] returnArray = new String[length];
     String [] returnArrayPoistion = new String[appointMent_length];
 
      for(int j=0;j<appointMent_length;j++){
        for(int i=0;i<length;i++){
 
           int timeTable_hour = new Integer(timeTable[i].substring(0,2)).intValue();
           int timeTable_mini = new Integer(timeTable[i].substring(3,5)).intValue();

           int appointMent_hour = new Integer(appointMent[j].substring(0,2)).intValue();
           int appointMent_mini = new Integer(appointMent[j].substring(3,5)).intValue();
            
            if(timeTable_hour<appointMent_hour ){
                returnArray[i] = timeTable[i];
                    
            }else if((timeTable_hour==appointMent_hour)&&((appointMent_mini-timeTable_mini)>=15)){
                returnArray[i] = timeTable[i];
                   
            }else{
                returnArray[i] = appointMent[j];
//                returnArrayPoistion[j] = timeTable[i] +"-"+new Integer(i).toString()+"-"+appointMent[j];
                returnArrayPoistion[j] = new Integer(i).toString();
                break;
            }
 
       	}
      }

     return  returnArrayPoistion;
  }

}