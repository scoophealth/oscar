<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbTicker.jsp" %>
<% 

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String module="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="", docpriority="", docassigned="";
module_id = request.getParameter("demographic_no");
doccreator = request.getParameter("user_no");
docdate = request.getParameter("xml_appointment_date");
docfilename =request.getParameter("textarea");
docpriority =request.getParameter("priority");
docassigned =request.getParameter("task_assigned_to");
             
             


    String[] param =new String[8];
	  param[0]=module_id;
	  param[1]=docfilename;
	  param[2]="A";
	  param[3]=nowDate;
	  param[4]=docdate;
	  param[5]=doccreator;
	  param[6]=docpriority;
	  param[7]=docassigned;

 int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_tickler");
	           
	    
	    



if (rowsAffected == 1){
%>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script>
<%}%>