/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.util;

import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone; 

import com.quatro.common.KeyConstants;

public class CurrentDateTime {

  private static boolean checkIfNowAtDST(String iTimezoneName){ 
   	boolean isDST = false;
   	TimeZone iTimezone = TimeZone.getTimeZone(iTimezoneName);
    SimpleTimeZone stz = new SimpleTimeZone(iTimezone.getRawOffset(),
   		iTimezoneName, Calendar.MARCH, 8, -Calendar.SUNDAY,
        7200000, Calendar.NOVEMBER, 1, -Calendar.SUNDAY,
        7200000, 3600000); 
    if(stz.inDaylightTime(new Date())){
      	isDST = true;
    } 

    return isDST;
  } 

  public static Calendar getCurrentDateTime(){
	  long offset = 0L;
	  boolean isNowatDST = checkIfNowAtDST(KeyConstants.DEFAULT_TIME_ZONE);
	  TimeZone iTimeZone = TimeZone.getTimeZone(KeyConstants.DEFAULT_TIME_ZONE);
	  Calendar iCalendar = Calendar.getInstance(iTimeZone);
	  if(isNowatDST){
		  iCalendar.add(Calendar.HOUR_OF_DAY, 1);
	  }
	   return iCalendar;
  }
  
}
