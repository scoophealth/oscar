/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.util.MiscUtils;

import com.quatro.common.KeyConstants;

public class MyDateFormat {
	//private int aDateTime;
	public MyDateFormat() {
		//this.aDateTime = d;
	}

	public static int getDaysDiff(Calendar start, Calendar end){
		  if(start==null || end==null) return 0;
		  long days = (end.getTimeInMillis() - start.getTimeInMillis())/(24*60*60*1000);
		  return (int)days;
	}

	public static String formatMonthOrDay(String value) {
	   String str2= "0" + value;
	   return str2.substring(str2.length()-2, str2.length());
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
//from 2001-01-01 12:00:00 to 2001-01-01
	public static String getMyStandardDate(java.lang.String aDate)
	{
		if (aDate == null) return "";
		if (aDate.indexOf(' ')<0)
		{
			return aDate;
		}
		else
		{
			return aDate.substring(0,aDate.indexOf(' '));
		}
	}

	public static String getMyStandardDate(java.util.Date aDate)
	{
		if (aDate == null) return "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    return formatter.format(aDate);
	}

	public static String getStandardDate(Calendar cal) {
		if (cal == null) return "";
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
	    return formatter.format(cal.getTime());
	}

	public static String getStandardDateTime(Calendar cal) {
		if(cal==null) return "";
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    return formatter.format(cal.getTime());
	}

	//from 2001, 2, 2 to 2001-02-02
	public static String getMysqlStandardDate(int year,int month, int day) {
		return (year+ "-"+ getDigitalXX(month)+ "-" +getDigitalXX(day));
	}
	//from 2001, 2, 2 to 20010202
	public static String getStandardDate(int year,int month, int day) {
		return (year+ getDigitalXX(month) +getDigitalXX(day));
	}

	// from 2001-02-02 to 2
	public static int getDayFromStandardDate(String aDate) {
		try {
			aDate = getMyStandardDate(aDate);
			return Integer.parseInt(aDate.trim().substring(aDate.trim().lastIndexOf('-') + 1));
		} catch (Exception e) {
			// bad string doesn't parse properly
			return (0);
		}
	}

	public static int getMonthFromStandardDate(String aDate) {
		try {
			aDate = getMyStandardDate(aDate);
			return Integer.parseInt(aDate.trim().substring(aDate.trim().indexOf('-') + 1, aDate.trim().lastIndexOf('-')));
		} catch (Exception e) {
			// bad string doesn't parse properly
			return (0);
		}
	}

	public static int getYearFromStandardDate(String aDate) {
		try {
			aDate = getMyStandardDate(aDate);
			return Integer.parseInt(aDate.trim().substring(0, aDate.trim().indexOf('-')));
		} catch (Exception e) {
			// bad string doesn't parse properly
			return (0);
		}
	}

	public static int getHourFromStandardTime(String aTime) {
		int i = aTime.indexOf(' ');
		if(i>=0) aTime = aTime.substring(i+1);
		return Integer.parseInt(aTime.substring(0, 2));
	}

	//from 8:20pm to 20:20:00, 9:9am to 09:09:00, 8:20 to 08:20:00
	public static String getTimeXX_XX_XX(String aXX_XXampm) {
		String temp="\\N"; //mySQL = null
		int hour=0;

		aXX_XXampm=aXX_XXampm.trim().toLowerCase();
		int i1=aXX_XXampm.indexOf(58); //":" a s c i i is 58
		if(i1>0) {
			hour=Integer.parseInt(aXX_XXampm.substring(0,i1).trim());
			temp=aXX_XXampm.substring(i1+1); //temp xxam or xx
			//t2=aXX_XXampm.indexOf(58);
			if(aXX_XXampm.endsWith("am")) {
				temp=temp.substring(0,temp.length()-2).trim();

				temp=getDigitalXX(hour)+":"+getDigitalXX(Integer.parseInt(temp))+":00";
			} else if(aXX_XXampm.endsWith("pm")) {
				temp=temp.substring(0,temp.length()-2).trim();
				//get rid of 12pm
				temp=getDigitalXX(hour==12?12:(hour+12))+":"+getDigitalXX(Integer.parseInt(temp))+":00";
			} else {
				temp=temp.trim();

				temp=getDigitalXX(hour)+":"+getDigitalXX(Integer.parseInt(temp))+":00";
			}
		}
		return temp;
	}

	// Convert date to yyyy-mm-dd, used to display screen values
    public static String getSysDateString(java.util.Date pDate){
	   SimpleDateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd");
	   return formatter.format(pDate);
    }

	public static java.sql.Date getSysDate(String pDate)
    {
		pDate=StringUtils.trimToNull(pDate);

        if (pDate == null) return null;

        if ("TODAY".equals(pDate.toUpperCase())) return new java.sql.Date(new Date().getTime());
        try
        {
        	char sep = '-';
        	boolean bnosep = false;
        	int idx = pDate.indexOf(sep);
        	if (idx < 0) {
        		sep='/';
        		idx= pDate.indexOf(sep);
        	}
        	bnosep = idx < 0;
        	int day, month, year;
        	if(bnosep) {
                year = Integer.parseInt(pDate.substring(0, 4));
                month = Integer.parseInt(pDate.substring(4, 6));
                day= Integer.parseInt(pDate.substring(6, 8));
        	}
        	else
        	{
        		year = Integer.parseInt(pDate.substring(0,idx));
        		int idx1 = pDate.indexOf(sep,idx+1);
        		month = Integer.parseInt(pDate.substring(idx+1,idx1));
        		idx = idx1;
        		idx1 = pDate.indexOf(' ');
        		if(idx1<0) idx1 = pDate.length();
        		day = Integer.parseInt(pDate.substring(idx+1,idx1));
        	}
        	if(month>0){
        		month = month - 1;
        	}
            GregorianCalendar cal = new GregorianCalendar(year, month, day,0,0,0);
            return new java.sql.Date(cal.getTime().getTime());
        }
            catch (Exception e)
            {
                MiscUtils.getLogger().debug("Invalid Date - the input date is in wrong format or out of range");
                return null;
            }
	}

	public static java.sql.Date getSysTime(String pDate)
    {
		pDate=StringUtils.trimToNull(pDate);

        if (pDate == null) return null;

        if(pDate.indexOf(":") == -1) {
        	return null;
        }

        String parts[] = pDate.split(":");

        if(parts.length != 2) {
        	return null;
        }

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(parts[1]));

        return new java.sql.Date(c.getTime().getTime());
	}

	public static java.sql.Date dayEnd(String pDate){
		if (pDate == null || "".equals(pDate)) return null;
        try
        {
        	char sep = '-';
        	boolean bnosep = false;
        	int idx = pDate.indexOf(sep);
        	if (idx < 0) {
        		sep='/';
        		idx= pDate.indexOf(sep);
        	}
        	bnosep = idx < 0;
        	int day, month, year;
        	if(bnosep) {
                year = Integer.parseInt(pDate.substring(0, 4));
                month = Integer.parseInt(pDate.substring(4, 6));
                day= Integer.parseInt(pDate.substring(6, 8));
        	}
        	else
        	{
        		year = Integer.parseInt(pDate.substring(0,idx));
        		int idx1 = pDate.indexOf(sep,idx+1);
        		month = Integer.parseInt(pDate.substring(idx+1,idx1));
        		idx = idx1;
        		idx1 = pDate.indexOf(' ');
        		if(idx1<0) idx1 = pDate.length();
        		day = Integer.parseInt(pDate.substring(idx+1,idx1));
        	}
        	if(month>0){
        		month = month - 1;
        	}
            GregorianCalendar cal = new GregorianCalendar(year, month, day,23,59,59);
            return new java.sql.Date(cal.getTime().getTime());
        }
            catch (Exception e)
            {
                MiscUtils.getLogger().debug("Invalid Date - the input date is in wrong format or out of range");
                return null;
            }
	}

    //yyyy-mm-dd hh:mm:ss
	public static Calendar getCalendarwithTime(String pDate){
	   pDate = pDate.replace('-','/');
       SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
       try{
         Date date = formatter.parse(pDate);
         Calendar cal=Calendar.getInstance();
         cal.setTime(date);
         return cal;
       }catch(Exception e){
    	 return null;
       }
    }

	public static boolean isBefore(Calendar cal1, Calendar cal2){
       String str1= getStandardDate(cal1);
       String str2= getStandardDate(cal2);

       return str1.compareTo(str2)<0;
	}

	public static Calendar getCalendar(java.util.Date date){
		Calendar cal= Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	public static Calendar getCalendar(String pDate,String dateFormat){
		if (pDate == null || "".equals(pDate)) return null;
		 GregorianCalendar cal = null;
		int day, month, year;
		if(KeyConstants.DATE_YYYYMMDD.equals(dateFormat.toUpperCase()) ||
				KeyConstants.DATE_YYYYMMDDHHMM.equals(dateFormat.toUpperCase()))
			return getCalendar(pDate);
		else if(KeyConstants.DATE_DDMMYYYY.equals(dateFormat.toUpperCase()))
		{
			 year = Integer.parseInt(pDate.substring(4, 8));
             month = Integer.parseInt(pDate.substring(2, 4));
             day= Integer.parseInt(pDate.substring(0, 2));
             if(month>0){
         		month = month - 1;
         	}
              cal=new GregorianCalendar(year, month, day);
		}
		else if(KeyConstants.DATE_MMDDYYYY.equals(dateFormat.toUpperCase()))
		{
			 year = Integer.parseInt(pDate.substring(4, 8));
             day = Integer.parseInt(pDate.substring(2, 4));
             month= Integer.parseInt(pDate.substring(0, 2));
             if(month>0){
         		month = month - 1;
         	}
              cal=new GregorianCalendar(year, month, day);
		}
		return cal;
	}
	public static Calendar getCalendar(String pDate)
    {
		//date format yyyymmddHHMM or yyyymmdd or yyyy/mm/dd or yyyy-mm-dd
        if (pDate == null || "".equals(pDate)) return null;
        if ("TODAY".equals(pDate.toUpperCase())) return Calendar.getInstance();
        int len =pDate.length();
        try
        {
        	char sep = '-';
        	boolean bnosep = false;
        	int idx = pDate.indexOf(sep);
        	if (idx < 0) {
        		sep='/';
        		idx= pDate.indexOf(sep);
        	}
        	bnosep = idx < 0;
        	int day, month, year;
        	int hour=0,min=0;
        	if(bnosep && len==8) {
                year = Integer.parseInt(pDate.substring(0, 4));
                month = Integer.parseInt(pDate.substring(4, 6));
                day= Integer.parseInt(pDate.substring(6, 8));
        	}
        	else if(bnosep && len==12){
        		year = Integer.parseInt(pDate.substring(0, 4));
                month = Integer.parseInt(pDate.substring(4, 6));
                day= Integer.parseInt(pDate.substring(6, 8));
                hour =Integer.parseInt(pDate.substring(8, 10));
                min =Integer.parseInt(pDate.substring(10, 12));
        	}
        	else
        	{
        		year = Integer.parseInt(pDate.substring(0,idx));
        		int idx1 = pDate.indexOf(sep,idx+1);
        		month = Integer.parseInt(pDate.substring(idx+1,idx1));
        		idx = idx1;
        		idx1 = pDate.indexOf(' ');
        		if(idx1<0) idx1 = pDate.length();
        		day = Integer.parseInt(pDate.substring(idx+1,idx1));
        	}
        	if(month>0){
        		month = month - 1;
        	}
            GregorianCalendar cal = null;
            if(hour>0) cal=new GregorianCalendar(year, month, day,hour,min);
            else cal=new GregorianCalendar(year, month, day);
            return cal;
        }
        catch (Exception e)
        {
            MiscUtils.getLogger().debug("Invalid Date - the input date is in wrong format or out of range");
            return null;
        }
    }

	public static java.sql.Date getSysDateEX(String pDate, int days)
    {
        if (pDate == null || "".equals(pDate)) return null;
        if ("TODAY".equals(pDate.toUpperCase())) return new java.sql.Date(new Date().getTime());
        try
        {
        	char sep = '-';
        	boolean bnosep = false;
        	int idx = pDate.indexOf(sep);
        	if (idx < 0) {
        		sep='/';
        		idx= pDate.indexOf(sep);
        	}
        	bnosep = idx < 0;
        	int day, month, year;
        	if(bnosep) {
                year = Integer.parseInt(pDate.substring(0, 4));
                month = Integer.parseInt(pDate.substring(4, 6));
                day= Integer.parseInt(pDate.substring(6, 8));
        	}
        	else
        	{
        		year = Integer.parseInt(pDate.substring(0,idx));
        		int idx1 = pDate.indexOf(sep,idx+1);
        		month = Integer.parseInt(pDate.substring(idx+1,idx1));
        		idx = idx1;
        		idx1 = pDate.indexOf(' ');
        		if(idx1<0) idx1 = pDate.length();
        		day = Integer.parseInt(pDate.substring(idx+1,idx1));
        	}
        	if(month>0){
        		month = month - 1;
        	}
            GregorianCalendar cal = new GregorianCalendar(year, month, day);
            cal.add(Calendar.DAY_OF_YEAR, days);
            return new java.sql.Date(cal.getTime().getTime());
        }
        catch (Exception e)
        {
            MiscUtils.getLogger().debug("Invalid Date - the input date is in wrong format or out of range");
            return null;
        }
    }

    public static java.sql.Date getCurrentDate()
    {
      GregorianCalendar cal = new GregorianCalendar();
      return new java.sql.Date(cal.getTime().getTime());
    }

	//from  20:20:00to 08:20pm,  09:09:00 to 09:09am, or 20:20 to 08:20pm
	public static String getTimeXX_XXampm(String aXX_XX_XX) {
		String temp=null; //mySQL = null
		int hour=0;

		aXX_XX_XX=aXX_XX_XX.trim().toLowerCase();
		int i1=aXX_XX_XX.indexOf(58); //":" a s c i i is 58
		if(i1>0) {
			hour=Integer.parseInt(aXX_XX_XX.substring(0,i1).trim());
			temp=aXX_XX_XX.substring(i1+1).trim(); //temp XX:XX or x:xx
			temp=getTimeXXampm(hour)+":"+temp.substring(0,2)+ getTimeAMPM(hour) ;
		}
		return temp;
	}
	 public static Calendar getDayStart(Calendar pDate){
	    	int year =pDate.get(Calendar.YEAR);
	    	int month = pDate.get(Calendar.MONTH);
	    	int day = pDate.get(Calendar.DATE);
	    	return new GregorianCalendar(year,month,day,0,0,0);
	    }
    public static Calendar getDayEnd(Calendar pDate){
    	int year =pDate.get(Calendar.YEAR);
    	int month = pDate.get(Calendar.MONTH);
    	int day = pDate.get(Calendar.DATE);
    	return new GregorianCalendar(year,month,day,23,59,59);
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

	public static String formatMonthDay(String pValue) {
		if(pValue==null) return null;

		if(pValue.length()==1){
		  return "0" + pValue;
		}else{
		  return pValue;
		}
	}

}
