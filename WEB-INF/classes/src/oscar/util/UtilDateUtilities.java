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

    public static String calcAge(Date date)    {
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        int i = gregoriancalendar.get(1);
        int j = gregoriancalendar.get(2) + 1;
        int k = gregoriancalendar.get(5);
        GregorianCalendar gregoriancalendar1 = new GregorianCalendar();
        gregoriancalendar1.setTime(date);
        int l = gregoriancalendar1.get(1);
        int i1 = gregoriancalendar1.get(2) + 1;
        int j1 = gregoriancalendar1.get(5);
        String s = "";
        int k1 = 0;
        k1 = i - l;
        s = k1 + " years";
        if(j > i1 || j == i1 && k >= j1)        {
            k1 = i - l;
            s = k1 + " years";
        } else        {
            k1 = i - l - 1;
            s = k1 + " years";
        }
        if(k1 <= 2)        {
            int i2 = i - l;
            int l1;
            if(i2 == 2)
                l1 = (gregoriancalendar1.getActualMaximum(6) - gregoriancalendar1.get(6)) + gregoriancalendar.get(6) + 365;
            else
            if(i2 == 1)
                l1 = (gregoriancalendar1.getActualMaximum(6) - gregoriancalendar1.get(6)) + gregoriancalendar.get(6);
            else
                l1 = gregoriancalendar.get(6) - gregoriancalendar1.get(6);
            if(l1 / 7 > 9)
                s = l1 / 30 + " months";
            else
            if(l1 >= 14)
                s = l1 / 7 + " weeks";
            else
                s = l1 + " days";
        }
        return s;
    }

    private static String defaultPattern = "yyyy-MM-dd";
//    private static String dateTimePattern = "yyyy-MM-dd HH:mm:ss"; timeStampPattern = "yyyyMMddHHmmss";
    private static Locale defaultLocale=Locale.CANADA;

}
