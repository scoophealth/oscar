/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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

package oscar.form.study.hsfo2.pageUtil;

import java.util.Calendar;
import java.util.Date;

public class ConvertUtil
{
  public static int toInt( String sValue )
  {
    if( sValue == null || sValue.length() == 0 )
      return 0;
    try
    {
      return Integer.parseInt( sValue );
    }
    catch( NumberFormatException e )
    {
      return 0;
    }
  }
  
  public static Integer toInteger( String sValue )
  {
    if( sValue == null || sValue.length() == 0 )
      return null;
    try
    {
      return Integer.parseInt( sValue );
    }
    catch( NumberFormatException e )
    {
      return null;
    }
  }
  
  public static double toDouble( String sValue )
  {
    if( sValue == null || sValue.length() == 0 )
      return 0;
    try
    {
      return Double.parseDouble( sValue );
    }
    catch( NumberFormatException e )
    {
      return 0;
    }
  }
  
  public static boolean toBoolean( String sValue )
  {
    return "yes".equalsIgnoreCase( sValue ) || "true".equalsIgnoreCase( sValue );
  }
  
  public static Calendar dateToCalendar( Date date )
  {
    if( date == null )
      return null;
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTime( date );
    return calendar;
  }
  
  public static String toUpperCase( String str )
  {
    if( str == null || str.length() == 0 )
      return str;
    return str.toUpperCase();
  }

  public static String toLowerCase( String str )
  {
    if( str == null || str.length() == 0 )
      return str;
    return str.toLowerCase();
  }

}
