/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 * @deprecated 2013-12-09 use org.oscarehr.util.DateUtils instead
 */
public class DateUtils {


  private static Logger cat = MiscUtils.getLogger();

  private static SimpleDateFormat sdf;

  private static String formatDate = "dd/MM/yyyy";
  public final static String intakeADelimiter = "/";
  
//##########################################################################
  
  public static SimpleDateFormat getDateFormatter() {

    if (sdf == null) {

      sdf = new SimpleDateFormat(formatDate);

    }

    return sdf;

  }
//##########################################################################

  public static void setDateFormatter(String pattern) {

    sdf = new SimpleDateFormat(pattern);

  }
//##########################################################################

  public static String getDate()//e.g. Sept 23, 2005 
  {

    Date date = new Date();

    return DateFormat.getDateInstance().format(date);

  }
//##########################################################################

  public static String getDate(Date date) 
  {

    SimpleDateFormat sdf = new SimpleDateFormat();

    return sdf.format(date);

  }

//###############################################################################
  public static String getCurrentDateOnlyStr(String delimiter)//2005/09/23
  {
  	int year = Calendar.getInstance().get(Calendar.YEAR);
  	int month = Calendar.getInstance().get(Calendar.MONTH) + 1; //!!! add 1  to month for correction!
  	int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//  	int hour = Calendar.getInstance().get(Calendar.HOUR);
//  	int minute = Calendar.getInstance().get(Calendar.MINUTE);
//    int am_pm = Calendar.getInstance().get(Calendar.AM_PM);
    String monthStr = String.valueOf(month);
    String dayStr = String.valueOf(day);
//    String hourStr = String.valueOf(hour);
//  	String minuteStr = String.valueOf(minute);
//    String am_pmStr = String.valueOf(am_pm);

    if(monthStr.length() <= 1)
    {
        monthStr = "0" + monthStr;
    }
    if(dayStr.length() <= 1)
    {
        dayStr = "0" + dayStr;
    }
/*    
    if(hourStr.length() <= 1)
    {
        hourStr = "0" + hourStr;
    }
    if(minuteStr.length() <= 1)
    {
        minuteStr = "0" + minuteStr;
    }
*/
    String dateStr = year + delimiter + monthStr + delimiter + dayStr;

      return dateStr;  
  }

//###############################################################################
  public static String[] getYearMonthDayParams(String dateStr, String delimiter)//2005/09/23 --> 2005,09,03
  {
	  if( dateStr == null  ||  dateStr.equals("")  ||
		  delimiter == null  ||  delimiter.equals(""))
	  {
		  return null;
	  }
	  
	  String[] dateParams = new String[3];
	  dateParams = Utility.separateStrComponentsIntoStrArray(dateStr,delimiter);
	  
	  
	  if(dateParams[0].length() <= 1)
	  {
		  dateParams[0] = "000" + dateParams[0];
	  }

	  if(dateParams[1].length() <= 1)
	  {
		  dateParams[1] = "0" + dateParams[1];
	  }
	  if(dateParams[2].length() <= 1)
	  {
		  dateParams[2] = "0" + dateParams[2];
	  }


      return dateParams;  
  }


//########################################################################  
  public static String getDateTime() {

    Date date = new Date();

    return DateFormat.getDateTimeInstance().format(date);

  }

  public static String formatDate(String date, String format,
                                  String formatAtual) 
  {

    try 
    {

      setDateFormatter(formatAtual);

      Date data = getDateFormatter().parse(date);

      cat.debug("[DateUtils] - formatDate: data formatada: " +

                getDateFormatter().format(data));

      setDateFormatter(format);

      return getDateFormatter().format(data);

    }
    catch(ParseException e) 
    {

      cat.error("[DateUtils] - formatDate: ", e);

    }

    return "";

  }
//##########################################################################

  public static String formatDate(String date, String format) {

    try {

      SimpleDateFormat sdf = new SimpleDateFormat();

      Date data = sdf.parse(date);

      cat.debug("[DateUtils] - formatDate: data formatada: " +

                sdf.format(data));

      setDateFormatter(format);

      return getDateFormatter().format(data);

    }
    catch (ParseException e) {

      cat.error("[DateUtils] - formatDate: ", e);

    }

    return "";

  }
//##########################################################################

  public static String sumDate(String format, String pSum) {

    int iSum = new Integer(pSum).intValue();

    cat.debug("[DateUtils] - sumDate: iSum = " + iSum);

    Calendar calendar = new GregorianCalendar();

    Date now = new Date();

    calendar.setTime(now);

    calendar.add(Calendar.DATE, iSum);

    Date data = calendar.getTime();

    setDateFormatter(format);

    return getDateFormatter().format(data);

  }
//##########################################################################

  public String NextDay(int day, int month, int year) {

    boolean leapyear;

    switch (month) {

      // the months with 31 days without december

      case 1:

      case 3:

      case 5:

      case 7:

      case 8:

      case 10:

        if (day < 31) {

          day++;

        }
        else {

          day = 1;

          month++;

        }

        break;

      case 12:

        if (day < 31) {

          day++;

        }
        else {

          day = 1;

          month = 1;

          year++;

        }

        break;

      case 2:

        if (day < 28) {

          day++;

        }
        else {

          if ( ( (year % 4 == 0) && ! (year % 100 == 0)) || (year % 400 == 0)) {

            leapyear = true;

          }
          else {

            leapyear = false;

            // in a leapyear 29 days

          }
          if (leapyear == true) {

            if (day == 28) {

              day++;

            }
            else {

              day = 1;

              month++;

            }
          }

          else {

            day = 1;

            month++;

          }

        }

        break;

        // these are the other month 4 6 9 11

      default:

        if (day < 30) {

          day++;

        }
        else {

          day = 1;

          month++;

        }

    } // switch

    String nextDay = year + "-" + month + "-" + day;

    return nextDay;

  }
//##########################################################################
  public String NextDay(int day, int month, int year, int numDays) {

   

    int modValue = 28;

    while (numDays > 0) {

      int curNumDays = numDays % modValue;

      if (curNumDays == 0) {

        curNumDays = modValue;

      }

      switch (month) {

        // the months with 31 days without december

        case 1:

        case 3:

        case 5:

        case 7:

        case 8:

        case 10:

          if (day + curNumDays < 31) {

            day = day + curNumDays;

          }
          else if ( ( (day + curNumDays) % 31) == 0) {

            day = 31;

          }

          else {

            day = ( (day + curNumDays) % 31);

            month++;

          }

          break;

        case 12:

          if (day + curNumDays < 31) {

            day = day + curNumDays;

          }
          else if ( ( (day + curNumDays) % 31) == 0) {

            day = 31;

          }

          else {

            day = ( (day + curNumDays) % 31);

            month = 1;

            year++;

          }

          break;

        case 2:

          if ( ( (year % 4 == 0) && ! (year % 100 == 0)) || (year % 400 == 0)) {

            if (day + curNumDays < 29) {

              day = day + curNumDays;

            }
            else if ( ( (day + curNumDays) % 29) == 0) {

              day = 29;

            }

            else {

              day = ( (day + curNumDays) % 29);

              month++;

            }

          }

          else {

            if (day + curNumDays < 28) {

              day = day + curNumDays;

            }
            else if ( ( (day + curNumDays) % 28) == 0) {

              day = 28;

            }

            else {

              day = ( (day + curNumDays) % 28);

              month++;

            }

          }

          break;

          // these are the other month 4 6 9 11

        default:

          if (day + curNumDays < 30) {

            day = day + curNumDays;

          }
          else if ( ( (day + curNumDays) % 30) == 0) {

            day = 30;

          }

          else {

            day = ( (day + curNumDays) % 30);

            month++;

          }

      } // switch

      numDays = numDays - curNumDays;

    }

    String nextDay = year + "-" + month + "-" + day;
    return nextDay;

  }
//################################################################################
  /**
   *Gets the difference between two dates, in days.
   *Takes two dates represented in milliseconds and returns the difference in days
   */
  public static int getDifDays(long greater, long lesser) 
  {
    double x = (greater - lesser) / 86400000;
    int ret = (int) x;
    return ret;
  }
//################################################################################
  public static String getTrimmedDateStr(String dateTimeStr, String delimiter) 
  {//2005-11-09 19:02:41.0  ==>  2005/11/09
	  String trimmedDateStr = "";
	  
	  if(dateTimeStr != null  &&  dateTimeStr.length() < 10)
	  {
		  return "";
	  }
  
	  trimmedDateStr = dateTimeStr.substring(0,4) + delimiter +
	  				   dateTimeStr.substring(5,7) + delimiter +
	  				   dateTimeStr.substring(8,10);
	  
	  return trimmedDateStr;
  }
  
  
//################################################################################
public static int compareDateForIntakeB(String currDate, String enteredDate)
{
  	if( currDate == null  ||  currDate.length() <= 0  ||
  		enteredDate	== null  ||  enteredDate.length() <= 0 )
  	{
  		return -1;
  	}
  	
  	int intDiff = -1;
  	try
  	{
  		String[] currDateParams = DateUtils.getYearMonthDayParams(currDate, intakeADelimiter);
  		String[] enteredDateParams = DateUtils.getYearMonthDayParams(enteredDate, intakeADelimiter);
  		java.util.Date currDateObj = null;
  		java.util.Date enteredDateObj = null;
      	
      	if(currDateParams != null && currDateParams[0]!= null && currDateParams[1]!= null && currDateParams[2]!= null)
      	{
      		currDateObj = UtilDateUtilities.calcDate(
      			       currDateParams[0], currDateParams[1], currDateParams[2]);
      	}
      
      	if(enteredDateParams != null && enteredDateParams[0] != null && enteredDateParams[1] != null && enteredDateParams[2] !=null)
      	{
      		enteredDateObj = UtilDateUtilities.calcDate(
      			enteredDateParams[0], enteredDateParams[1], enteredDateParams[2]);
      	}
    
  		intDiff = DateUtils.getDifDays( currDateObj.getTime(), enteredDateObj.getTime());
  	}
  	catch(Exception ex1)
  	{
  		return -1;
  	}
    
  	return intDiff;
}
  
//########################################################################  

}
