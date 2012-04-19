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


package org.oscarehr.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import oscar.oscarDB.DBHandler;

public class AppointmentUtil {
	private AppointmentUtil() {}
	
	public static String getNextAppointment(String demographicNo) {
		Date nextApptDate = null;
	       if (demographicNo != null && !demographicNo.equalsIgnoreCase("") && !demographicNo.equalsIgnoreCase("null")){
	           try {
	              String sql = "select * from appointment where demographic_no = '"+demographicNo+"' and status not like '%C%' and CONCAT(IFNULL(appointment_date, ''), ' ', IFNULL(start_time, '')) >= now() order by appointment_date";
	              ResultSet rs = DBHandler.GetSQL(sql);
	              if (rs.next()) {
	                 nextApptDate = rs.getDate("appointment_date");
	              }
	              rs.close();
	           }catch(SQLException e)        {
	              MiscUtils.getLogger().error("error",e);
	           } 
	       }    
	       String s = "(none)";
	       try{
	          if ( nextApptDate != null ){    
	             Format formatter = new SimpleDateFormat("yyyy-MM-dd");
	             s = formatter.format(nextApptDate);
	          }         
	       }catch(Exception p) {
	    	   MiscUtils.getLogger().error("error",p);
	       }
	       
	       return s;
	}
}
