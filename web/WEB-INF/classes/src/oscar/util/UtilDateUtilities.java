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
package oscar.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class UtilDateUtilities {

    public static Date StringToDate(String s)    {
        return StringToDate(s, defaultPattern, defaultLocale);
    }
    public static Date StringToDate(String s, Locale locale)    {
        return StringToDate(s, defaultPattern, locale);
    }
    public static Date StringToDate(String s, String spattern)    {
        return StringToDate(s, spattern, defaultLocale);
    }
    public static Date StringToDate(String s, String spattern, Locale locale)    {
        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(spattern, locale);
            return simpledateformat.parse(s);
        }
        catch(Exception exception) {
            return null;
        }
    }

    public static String DateToString(Date date)    {
        return DateToString(date, defaultPattern, defaultLocale);
    }
    public static String DateToString(Date date, Locale locale)    {
        return DateToString(date, defaultPattern, locale);
    }
    public static String DateToString(Date date, String spattern)    {
        return DateToString(date, spattern, defaultLocale);
    }
    public static String DateToString(Date date, String spattern, Locale locale)    {
        if(date != null) {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(spattern, locale);
            return simpledateformat.format(date);
        } else {
            return "";
        }
    }

    public static Date now()    {    //Today()    
        return GregorianCalendar.getInstance().getTime();
    }
    public static Date  Today()  {       
        return GregorianCalendar.getInstance().getTime();
    }

    public static Date calcDate(String s, String s1, String s2)    {
        int i = Integer.parseInt(s);
        int j = Integer.parseInt(s1) - 1;
        int k = Integer.parseInt(s2);
        GregorianCalendar gregoriancalendar = new GregorianCalendar(i, j, k);
        return gregoriancalendar.getTime();
    }

    public static String calcAge(Date DOB)    {
        GregorianCalendar now = new GregorianCalendar();
        int curYear = now.get(Calendar.YEAR);
        int curMonth = now.get(Calendar.MONTH) + 1;
        int curDay = now.get(Calendar.DAY_OF_MONTH);
        
        GregorianCalendar birthDate = new GregorianCalendar();
        birthDate.setTime(DOB);
        int birthYear = birthDate.get(Calendar.YEAR);
        int birthMonth = birthDate.get(Calendar.MONTH) + 1;
        int birthDay = birthDate.get(5);
                
        int ageInYears = curYear - birthYear;
        String result = ageInYears + " years";
        
        if (curMonth > birthMonth || curMonth == birthMonth && curDay >= birthDay)        {
            ageInYears = curYear - birthYear;
            result = ageInYears + " years";
        } else        {
            ageInYears = curYear - birthYear - 1;
            result = ageInYears + " years";
        }
        if (ageInYears < 2)        {
            int yearDiff = curYear - birthYear;
            int ageInDays;
            if (yearDiff == 2) {
                ageInDays = (birthDate.getActualMaximum(Calendar.DAY_OF_YEAR) - birthDate.get(Calendar.DAY_OF_YEAR)) + now.get(Calendar.DAY_OF_YEAR) + 365;
            } else if (yearDiff == 1) {
                ageInDays = (birthDate.getActualMaximum(Calendar.DAY_OF_YEAR) - birthDate.get(Calendar.DAY_OF_YEAR)) + now.get(Calendar.DAY_OF_YEAR);
            } else {
                ageInDays = now.get(Calendar.DAY_OF_YEAR) - birthDate.get(Calendar.DAY_OF_YEAR);
            }
            if (ageInDays / 7 > 9) {
                result = ageInDays / 30 + " months";
            } else if (ageInDays >= 14) {
                result = ageInDays / 7 + " weeks";
            } else {
                result = ageInDays + " days";
            }
        }
        return result;
    }

    public static int calcAge(String year_of_birth, String month_of_birth, String date_of_birth)    {
      GregorianCalendar now = new GregorianCalendar();
      int curYear = now.get(Calendar.YEAR);
      int curMonth = (now.get(Calendar.MONTH)+1);
      int curDay = now.get(Calendar.DAY_OF_MONTH);
      int age=0;

    	if(curMonth>Integer.parseInt(month_of_birth) ) {
    		age=curYear-Integer.parseInt(year_of_birth);
    	} else {
    		if(curMonth==Integer.parseInt(month_of_birth) && curDay>Integer.parseInt(date_of_birth)) {
    			age=curYear-Integer.parseInt(year_of_birth);
    		} else {
    			age=curYear-Integer.parseInt(year_of_birth)-1; 
    		}
    	}	
      return age;
    }

    private static String defaultPattern = "yyyy-MM-dd";
//    private static String dateTimePattern = "yyyy-MM-dd HH:mm:ss"; timeStampPattern = "yyyyMMddHHmmss";
    private static Locale defaultLocale=Locale.CANADA;

}
