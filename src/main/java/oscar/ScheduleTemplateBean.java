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


public class ScheduleTemplateBean {
	
  private String providerNo = "";
  private String name = "";
  private String summary = "";
  private String timecode = "";
  private int step = 0;

  public ScheduleTemplateBean() {}
  public void setScheduleTemplateBean( String provider_no1, String name1,String summary1,String timecode1 ) {
    providerNo = provider_no1;
    name = name1;
    summary = summary1;
    timecode = timecode1;
    step = timecode1.length()>0?24*60/timecode1.length():0;
	}
  public void setProviderNo(String provider_no1 ) { 
    providerNo = provider_no1;
  }  
  public void setName(String name1 ) { 
    name = name1;
  }  
  public void setSummary(String summary1 ) { 
    summary = summary1;
  }  
  public void setTimecode(String timecode1 ) { 
    timecode = timecode1;
  }  

  public String getProviderNo() { 
    return (providerNo);
  }  
  public String getName() { 
    return(name);
  }  
  public String getSummary() { 
    return(summary);
  }  
  public String getTimecode() { 
    return(timecode);
  }  
  public int getStep() { 
    return(step);
  }  
  public char getTimecodeCharAt(int i) { 
    return(timecode.charAt(i));
  }  

}
