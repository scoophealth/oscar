<%--  
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
--%>
<%      
if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
%>

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>

<%
String typeid = request.getParameter("typeid");
String type = request.getParameter("type");
int rowsAffected0 = apptMainBean.queryExecuteUpdate(typeid,"delete_ctlbillservice");	   

String[] group = new String[4];
int rowsAffected = -100;

for(int j=1;j<4;j++){
	group[j] = request.getParameter("group"+j);

	for (int i=0; i<20; i++){
		if(request.getParameter("group"+j+"_service"+i).length() !=0){
			String[] param =new String[7];
			param[0]=type;
			param[1]=typeid;
			param[2]=request.getParameter("group"+j+"_service"+i);
			param[3]=group[j];
			param[4]="Group"+j;
			param[5]="A";
			param[6]=request.getParameter("group"+j+"_service"+i+"_order");
			rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_ctlbillservice");
		}
	}
}

response.sendRedirect("manageBillingform.jsp"); 
%>