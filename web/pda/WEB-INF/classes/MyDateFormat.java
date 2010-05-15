  
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar;
import java.lang.*;
import java.util.*;

public class MyDateFormat {
	//private int aDateTime;
	public MyDateFormat() {
		//this.aDateTime = d;
	}
	
	// from 8 (int) to 08 (String), 19 to 19
	public static String getDigitalXX(int d) {
		return (d>9?(""+d):("0"+d));
	}
	
	//from 18 (int) to 06 (pm), 16 to 04 (String)
	public static String getTimeXXampm(int hour) {
		return (hour>12?(getDigitalXX(hour-12)):getDigitalXX(hour));
	}
	
	//from 10 to am, 18 to pm
	public static String getTimeAMPM(int hour) {
		return (hour<12?"am":"pm");
	}
	//from 2001, 2, 2 to 2001-02-02
	public static String getMysqlStandardDate(int year,int month, int day) {
		return (year+ "-"+ getDigitalXX(month)+ "-" +getDigitalXX(day));
	}
	//from 2001, 2, 2 to 20010202
	public static String getStandardDate(int year,int month, int day) {
		return (year+ getDigitalXX(month) +getDigitalXX(day));
	}
	//from 2001-02-02 to 2	
	public static int getDayFromStandardDate(String aDate) {
		return Integer.parseInt(aDate.trim().substring(aDate.trim().lastIndexOf('-')+1));
	}
	public static int getMonthFromStandardDate(String aDate) {
		return Integer.parseInt(aDate.trim().substring(aDate.trim().indexOf('-')+1, aDate.trim().lastIndexOf('-')));
	}
	public static int getYearFromStandardDate(String aDate) {
		return Integer.parseInt(aDate.trim().substring(0,aDate.trim().indexOf('-'))); 
	}
	public static int getHourFromStandardTime(String aTime) {
		return Integer.parseInt(aTime.substring(0, 2));
	}
	
	//from 8:20pm to 20:20:00, 9:9am to 09:09:00, 8:20 to 08:20:00
	public static String getTimeXX_XX_XX(String aXX_XXampm) {
		String temp="\\N"; //mySQL = null
		int hour=0, min=0;
		
		aXX_XXampm=aXX_XXampm.trim().toLowerCase();
		int i1=aXX_XXampm.indexOf(58); //":" ascii is 58
		if(i1>0) {
			hour=Integer.parseInt(aXX_XXampm.substring(0,i1).trim());
			temp=aXX_XXampm.substring(i1+1); //temp xxam or xx
			//t2=aXX_XXampm.indexOf(58);
			if(aXX_XXampm.endsWith("am")) {
				temp=temp.substring(0,temp.length()-2).trim();
				//System.out.println(hour+" :"+temp);
				temp=getDigitalXX(hour)+":"+getDigitalXX(Integer.parseInt(temp))+":00";
			} else if(aXX_XXampm.endsWith("pm")) {
				temp=temp.substring(0,temp.length()-2).trim();
				//get rid of 12pm
				temp=getDigitalXX(hour==12?12:(hour+12))+":"+getDigitalXX(Integer.parseInt(temp))+":00";
			} else {
				temp=temp.trim();
				//System.out.println(hour+" :"+temp);
				temp=getDigitalXX(hour)+":"+getDigitalXX(Integer.parseInt(temp))+":00";
			}
		}
		return temp;
	}

	//from  20:20:00to 08:20pm,  09:09:00 to 09:09am, or 20:20 to 08:20pm
	public static String getTimeXX_XXampm(String aXX_XX_XX) {
		String temp=null; //mySQL = null
		int hour=0, min=0;
		
		aXX_XX_XX=aXX_XX_XX.trim().toLowerCase();
		int i1=aXX_XX_XX.indexOf(58); //":" ascii is 58
		if(i1>0) {
			hour=Integer.parseInt(aXX_XX_XX.substring(0,i1).trim());
			temp=aXX_XX_XX.substring(i1+1).trim(); //temp XX:XX or x:xx
			temp=getTimeXXampm(hour)+":"+temp.substring(0,2)+ getTimeAMPM(hour) ;
		}
		return temp;
	}

	public static int getAge(int year, int month, int date) {
  	GregorianCalendar now=new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    int age=0;

 	  if( curMonth>month || (curMonth==month && curDay >= date) ) { 
 		  age = curYear-year; 
   	} else {
 		  age = curYear-year -1; 
 		}
    return age;
	}
	public static int getAge(String year1, String month1, String date1) {
		int year = Integer.parseInt(year1);
		int month = Integer.parseInt(month1);
		int date = Integer.parseInt(date1);
    int age= MyDateFormat.getAge(year, month, date);
    return age;
	}
	
}