package bean;
import java.sql.*;
/*
 * $RCSfile: AbstractApplication.java,v1.0 $
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *
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
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,USA.
 *
 * Tom Zhu
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */

public class ReturnPage {
     
 

  public ReturnPage() {
  }
 
  public String getAppointmentHtml(String appointment_date,String start_time,String end_time,String name,String reason,String location ) {
  
     String ReturnString = "";
	ReturnString = ReturnString + "<tr><td>Appointment from: </td><td>&nbsp;&nbsp;&nbsp;to :</td><td ></td></tr>";
        ReturnString = ReturnString + "<tr><td> "+start_time+" </td><td>"+end_time+"</td></tr>";
        ReturnString = ReturnString + "<tr><td colspan=\"2\">"+name+"</td></tr><tr><td colspan=\"2\"><textarea rows=\"3\" cols=\"33\">";
        ReturnString = ReturnString + reason+"</textarea></td></tr><tr><td colspan=\"2\">"+location+"</td></tr>";
   return ReturnString ;
  }


}

