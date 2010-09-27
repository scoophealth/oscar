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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%


String group1="",group2="", group3="";
String typeid = "", type="";

typeid = request.getParameter("typeid");
type = request.getParameter("type");
group1 = request.getParameter("group1");
group2 = request.getParameter("group2");
group3 = request.getParameter("group3");

%>

<%
if (type.compareTo("") == 0 || group1.compareTo("") == 0 || group2.compareTo("") == 0 || group3.compareTo("") == 0) {
 String errormsg = "Error: Type Description, Groups Descrption  must be entered.";

%>
<jsp:forward page='../dms/errorpage.jsp'>
	<jsp:param name="msg" value='<%=errormsg%>' />
	<jsp:param name="type" value='<%=type%>' />
	<jsp:param name="typeid" value='<%=typeid%>' />
	<jsp:param name="group1" value='<%=group1%>' />
	<jsp:param name="group2" value='<%=group2%>' />
	<jsp:param name="group2" value='<%=group3%>' />
</jsp:forward>
<%

}
else {  

             
             


    String[] param =new String[7];
	  param[0]=type;
	  param[1]=typeid;
	  param[2]="A007A";
	  param[3]=group1;
	  param[4]="Group1";
	  param[5]="A";
	  param[6]="1";
	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_ctlbillservice");

    String[] param0 =new String[7];
	  param0[0]=type;
	  param0[1]=typeid;
	  param0[2]="A007A";
	  param0[3]=group2;
	  param0[4]="Group2";
	  param0[5]="A";
	  param0[6]="1";
	  int rowsAffected0 = apptMainBean.queryExecuteUpdate(param0,"save_ctlbillservice");	    
    String[] param00 =new String[7];
	  param00[0]=type;
	  param00[1]=typeid; 
	  param00[2]="A007A";
	  param00[3]=group3;
	  param00[4]="Group3";
	  param00[5]="A";
	  param00[6]="1";
	  int rowsAffected00 = apptMainBean.queryExecuteUpdate(param00,"save_ctlbillservice");
	       
	       
	       
	       String[] param3 =new String[3];
		 	          	    	
		 	          	    	   
		 	          	    	  param3[0]=typeid;
		 	          	    	  param3[1]="000";
		 	          	    	  param3[2]="A";
		 	          	    	  
		 	          
		 	     
		 	           
	        int   recordAffected = apptMainBean.queryExecuteUpdate(param3,"save_ctldiagcode");
	           
	    
	    




%>
<% response.sendRedirect("manageBillingform.jsp"); %>
<%
}


%>
