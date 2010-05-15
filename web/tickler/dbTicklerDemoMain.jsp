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
<%@ include file="../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbTicker.jsp"%>
<%
 String demoview = request.getParameter("demoview")==null?"all":request.getParameter("demoview") ;
 String parentAjaxId = request.getParameter("parentAjaxId")==null?"":request.getParameter("parentAjaxId");
 String updateParent = request.getParameter("updateParent")==null?"false":request.getParameter("updateParent");
 
String[] param = new String[2];
String[] temp = request.getParameterValues("checkbox");
if (temp== null){
%>
<jsp:forward page='ticklerDemoMain.jsp'>
	<jsp:param name="demoview" value='<%=demoview%>' />
	<jsp:param name="parentAjaxId" value="<%=parentAjaxId%>" />
	<jsp:param name="updateParent" value="<%=updateParent%>" />
</jsp:forward>
<%}else{
		//temp=e.nextElement().toString();
		

for (int i=0; i<temp.length; i++){


param[0] = request.getParameter("submit_form").substring(0,1);
param[1] = temp[i];	
int rowsAffected = apptMainBean.queryExecuteUpdate(param,"update_tickler");

} //end for



}

apptMainBean.closePstmtConn();

%>
<jsp:forward page='ticklerDemoMain.jsp'>
	<jsp:param name="demoview" value='<%=demoview%>' />
	<jsp:param name="parentAjaxId" value="<%=parentAjaxId%>" />
	<jsp:param name="updateParent" value="<%=updateParent%>" />
</jsp:forward>
