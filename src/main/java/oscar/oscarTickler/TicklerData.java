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
import org.caisi.dao.TicklerDAO;
import org.caisi.model.Tickler;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author Jay Gallagher
 */
public class TicklerData {
   
   public static String ACTIVE  = "A";
   public static String COMPLETED = "C";
   public static String DELETED = "D";
      
   public static String HIGH = "High";
   public static String NORMAL = "Normal";
   public static String LOW = "Low";
   
   public TicklerData() {
   }
   
   public List<Tickler> listTickler(String demographic_no, String beginDate, String endDate) {
	   TicklerDAO dao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
	   return dao.listTicklers(demographic_no, beginDate, endDate);
   }
   
   public void addTickler(String demographic_no,String message,String status,String service_date,String creator,String priority,String task_assigned_to){
            
      String date = service_date;
      if ( date != null && !date.equals("now()")){          //Just a hack for now.
         date = "'"+StringEscapeUtils.escapeSql(service_date)+"'";
      }
      
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      	Tickler t = new Tickler();
		t.setDemographic_no(demographic_no);
		t.setMessage(message);
		t.setStatus(status.toCharArray()[0]);
		t.setUpdate_date(new Date());
		try {
			t.setService_date(formatter.parse(service_date));
		}catch(ParseException e) {
			MiscUtils.getLogger().error("Error",e);
			t.setService_date(new Date());
		}
		t.setCreator(creator);
		t.setPriority(priority);
		t.setTask_assigned_to(task_assigned_to);
		
		TicklerDAO dao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
		dao.saveTickler(t);
   }
   
   public boolean hasTickler(String demographic,String task_assigned_to,String message){
      TicklerDAO dao = (TicklerDAO)SpringUtils.getBean("ticklerDAOT");
      List<Tickler> ticklers = dao.findByDemographicIdTaskAssignedToAndMessage(demographic, task_assigned_to, message);
      return !ticklers.isEmpty();
   }
   
}
