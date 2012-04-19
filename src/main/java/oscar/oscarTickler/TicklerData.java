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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

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
   
   public ResultSet listTickler(String demographic_no, String status, String beginDate, String endDate) throws SQLException {
       String sql = "select t.tickler_no,t.message,t.service_date, t.update_date from tickler t where t.status='A' and TO_DAYS(t.service_date) >=TO_DAYS('" + beginDate + "') and TO_DAYS(t.service_date)<=TO_DAYS('" + endDate + "') and t.demographic_no = " + demographic_no + " order by t.service_date desc";
       
       ResultSet rs = DBHandler.GetSQL(sql);
       
       return rs;
   }
   
   public void addTickler(String demographic_no,String message,String status,String service_date,String creator,String priority,String task_assigned_to){
            
      String date = service_date;
      if ( date != null && !date.equals("now()")){          //Just a hack for now.
         date = "'"+StringEscapeUtils.escapeSql(service_date)+"'";
      }
      
      String sql = "insert into tickler (demographic_no, message, status, update_date, service_date, creator, priority, task_assigned_to) "
                  +" values "
                  +"('"+StringEscapeUtils.escapeSql(demographic_no)+"', "
                  +" '"+StringEscapeUtils.escapeSql(message)+"', "
                  +" '"+StringEscapeUtils.escapeSql(status)+"', "
                  //+" '"+StringEscapeUtils.escapeSql(demographic_no)+"', "
                  +" now(), "                    
                  +" "+date+", "
                  +" '"+StringEscapeUtils.escapeSql(creator)+"', "
                  +" '"+StringEscapeUtils.escapeSql(priority)+"', "
                  +" '"+StringEscapeUtils.escapeSql(task_assigned_to)+"')";  

      try {         
         
            DBHandler.RunSQL(sql);         
      } catch (SQLException e) {         
         MiscUtils.getLogger().error("Error", e);
         MiscUtils.getLogger().error("Error", e);
      }      
   }
   
   public boolean hasTickler(String demographic,String task_assigned_to,String message){
      boolean hastickler = false;
      try {         
         
         String sql = "select * from tickler  where demographic_no = '"+StringEscapeUtils.escapeSql(demographic)+"' "
                     +" and task_assigned_to = '"+StringEscapeUtils.escapeSql(task_assigned_to)+"' "
                     +" and message = '"+StringEscapeUtils.escapeSql(message)+"'";

         ResultSet rs = DBHandler.GetSQL(sql);
         if (rs.next()){
            hastickler = true;
         }
         rs.close();         
      } catch (SQLException e) {
         MiscUtils.getLogger().error("Error", e);
         MiscUtils.getLogger().error("Error", e);
      }      
      return hastickler;
   }
   
}
