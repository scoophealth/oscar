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