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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class RscheduleBean {
	
  public String provider_no = "";
  public String sdate = "";
  public String edate = "";
  public String available = "";
  public String day_of_week = "";
  public String avail_hourB = ""; 
  public String avail_hour = "";
  public String creator = "";
  public String active = "A";
  private String weekdaytag[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
  private String sitedaytag[] = {"A7","A1","A2","A3","A4","A5","A6"};

  public RscheduleBean() {}
  public RscheduleBean( String provider_no1, String sdate1,String edate1,String bAvailable1, String day_of_week1, String avail_hourB1, String avail_hour1, String creator1) {
    provider_no = provider_no1;
    sdate = sdate1;
    edate = edate1;
    available = bAvailable1;
    day_of_week = day_of_week1;
    avail_hourB = avail_hourB1;
    avail_hour = avail_hour1;
    creator = creator1;
	}
  public void setRscheduleBean(String provider_no1, String sdate1,String edate1, String bAvailable1, String day_of_week1, String avail_hourB1, String avail_hour1, String creator1) { 
    provider_no = provider_no1;
    sdate = sdate1;
    edate = edate1;
    available = bAvailable1;
    day_of_week = day_of_week1;
    avail_hourB = avail_hourB1;
    avail_hour = avail_hour1;
    creator = creator1;
  }  
  public void setRscheduleBean(String provider_no1, String sdate1,String edate1, String bAvailable1, String day_of_week1, String day_of_week2 ,String avail_hourB1, String avail_hour1, String creator1) { 
    provider_no = provider_no1;
    sdate = sdate1;
    edate = edate1;
    available = bAvailable1;
    day_of_week = day_of_week1+"|"+day_of_week2;
    avail_hourB = avail_hourB1;
    avail_hour = avail_hour1;
    creator = creator1;
  }  
  public void clear() { 
    provider_no = "";
    sdate = "";
    edate = "";
    available = "";
    day_of_week = "";
    avail_hourB = "";
    avail_hour = "";
    creator = "";
  }  
  public boolean getEvenWeek(GregorianCalendar aDate) { 
  	int sWeek = (new GregorianCalendar(MyDateFormat.getYearFromStandardDate(this.sdate), MyDateFormat.getMonthFromStandardDate(this.sdate)-1,MyDateFormat.getDayFromStandardDate(this.sdate))).get(Calendar.WEEK_OF_YEAR);
  	int curWeek = aDate.get(Calendar.WEEK_OF_YEAR);
    return ((curWeek-sWeek)%2==0?true:false);      
  }
  private boolean getEvenWeek(GregorianCalendar sDate, GregorianCalendar aDate) { 
  	int sWeek = sDate.get(Calendar.WEEK_OF_YEAR);
  	int curWeek = aDate.get(Calendar.WEEK_OF_YEAR);
    return ((curWeek-sWeek)%2==0?true:false);      
  }

  public boolean getDateAvail(GregorianCalendar aDate) { 
  	String aVailable = null, aDOW = null;
  	if(this.available.compareTo("A")==0) {
  	  aVailable = "A";
  	  if(getEvenWeek(new GregorianCalendar(MyDateFormat.getYearFromStandardDate(this.sdate), MyDateFormat.getMonthFromStandardDate(this.sdate)-1,MyDateFormat.getDayFromStandardDate(this.sdate)), aDate) ) {
  	    aDOW = this.day_of_week.substring(0,this.day_of_week.indexOf("|") );
  		} else aDOW = this.day_of_week.substring(this.day_of_week.indexOf("|")+1 );

    } else {
  	  aVailable = this.available;
  	  aDOW = this.day_of_week;
  	}
    return (getSingleDateAvail(aDate, aVailable, aDOW));
  }
  
  public boolean getSingleDateAvail(GregorianCalendar aDate, String aVailable, String aDOW) { 
    boolean bAvail= (aVailable.compareTo("1")==0 || aVailable.compareTo("A")==0)?false:true;
  	boolean bAvailableTemp = (aVailable.compareTo("0")==0)?false:true;
  	
  	//check if it is unavailable, then break
    StringTokenizer st = new StringTokenizer(aDOW);
    while (st.hasMoreTokens() ) {
      if( st.nextToken().compareTo(""+ aDate.get(Calendar.DAY_OF_WEEK) )==0 ) { //prompt the number, from 0?

    	  bAvail = bAvailableTemp;
    	  break;
      }
    }
/*
    //check if it is a special day of month, then modify the status
    st = new StringTokenizer(this.avail_hourB);
    while (st.hasMoreTokens() ) {
      if( st.nextToken().compareTo(""+ aDate.get(Calendar.avail_hourB) )==0 ) { 
    	  bAvail = bAvailableTemp;
    	  break;
      }
    }
*/    
    return bAvail;
  }
  public boolean getDateAvail(String aDate) { 
    return (getDateAvail(new GregorianCalendar(MyDateFormat.getYearFromStandardDate(aDate), MyDateFormat.getMonthFromStandardDate(aDate)-1,MyDateFormat.getDayFromStandardDate(aDate))));
  }
    
  public String getDateAvailHour(GregorianCalendar aDate) { 
  	String val = "";
    if(provider_no!="") {
      int j = aDate.get(Calendar.DAY_OF_WEEK)-1;
	    int i = j==7?0:j;
  	  if(this.available.compareTo("A")==0) {
  	    if(getEvenWeek(new GregorianCalendar(MyDateFormat.getYearFromStandardDate(this.sdate), MyDateFormat.getMonthFromStandardDate(this.sdate)-1,MyDateFormat.getDayFromStandardDate(this.sdate)), aDate) ) {
  	      val = SxmlMisc.getXmlContent(avail_hour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") ; 
  		  } else val = SxmlMisc.getXmlContent(avail_hourB, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") ; 
  	  } else val = SxmlMisc.getXmlContent(avail_hour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") ; 
    } 
    return val;
  }
  public String getSiteAvail(GregorianCalendar aDate) { 
	  	String val = "";
	    if(provider_no!="") {
	      int j = aDate.get(Calendar.DAY_OF_WEEK)-1;
		    int i = j==7?0:j;
	  	  if(this.available.compareTo("A")==0) {
	  	    if(getEvenWeek(new GregorianCalendar(MyDateFormat.getYearFromStandardDate(this.sdate), MyDateFormat.getMonthFromStandardDate(this.sdate)-1,MyDateFormat.getDayFromStandardDate(this.sdate)), aDate) ) {
	  	      val = SxmlMisc.getXmlContent(avail_hour, ("<"+sitedaytag[i]+">"),"</"+sitedaytag[i]+">") ; 
	  		  } else val = SxmlMisc.getXmlContent(avail_hourB, ("<"+sitedaytag[i]+">"),"</"+sitedaytag[i]+">") ; 
	  	  } else val = SxmlMisc.getXmlContent(avail_hour, ("<"+sitedaytag[i]+">"),"</"+sitedaytag[i]+">") ; 
	    } 
	    return val;
	  }
  public String getDateAvailHour(String aDate) { 
    return (getDateAvailHour(new GregorianCalendar(MyDateFormat.getYearFromStandardDate(aDate), MyDateFormat.getMonthFromStandardDate(aDate)-1,MyDateFormat.getDayFromStandardDate(aDate))));
  }
  public String getAvailHour(GregorianCalendar aDate) { 
  	String val = "";
  	  if(this.available.compareTo("A")==0) {
  	    if(getEvenWeek(aDate) ) {
  	      val = avail_hour ; 
  		  } else val = avail_hourB ; 
  	  } else val = avail_hour ; 
    return val;
  }
  
}
