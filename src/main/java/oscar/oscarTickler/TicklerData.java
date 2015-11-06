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


package oscar.oscarTickler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 *
 * @author Jay Gallagher
 */
public class TicklerData {
   
   public static final String ACTIVE  = "A";
   public static final String COMPLETED = "C";
   public static final String DELETED = "D";
      
   public static final String HIGH = "High";
   public static final String NORMAL = "Normal";
   public static final String LOW = "Low";
   
   private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
   
   public TicklerData() {
   }
   
   public List<Tickler> listTickler(LoggedInInfo loggedInInfo,String demographic_no, String beginDate, String endDate) {
	   return ticklerManager.listTicklers(loggedInInfo,Integer.parseInt(demographic_no), ConversionUtils.fromDateString(beginDate), ConversionUtils.fromDateString(endDate));
   }
   
   public void addTickler(LoggedInInfo loggedInInfo,String demographic_no,String message,String status,String service_date,String creator,String priority,String task_assigned_to){
            
      String date = service_date;
      if ( date != null && !date.equals("now()")){          //Just a hack for now.
         date = "'"+StringEscapeUtils.escapeSql(service_date)+"'";
      }
      
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      	Tickler t = new Tickler();
		t.setDemographicNo(Integer.parseInt(demographic_no));
		t.setMessage(message);
		t.setStatusAsChar(status.toCharArray()[0]);
		
		try {
			t.setServiceDate(formatter.parse(service_date));
		}catch(ParseException e) {
			MiscUtils.getLogger().error("Error",e);
			t.setServiceDate(new Date());
		}
		t.setCreator(creator);
		t.setPriorityAsString(priority);
		t.setTaskAssignedTo(task_assigned_to);
		
		ticklerManager.addTickler(loggedInInfo,t);
   }
   
   public boolean hasTickler(String demographic,String task_assigned_to,String message){
	   return ticklerManager.hasTickler(demographic, task_assigned_to, message); 
   }
   
}
