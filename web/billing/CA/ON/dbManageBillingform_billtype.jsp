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
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jsp"%>

<%
String servicetype="", billtype="", billtype_old="";
String[] param = new String[2];

servicetype = request.getParameter("servicetype");
billtype = request.getParameter("billtype");
billtype_old = request.getParameter("billtype_old");

if (billtype.equals("no")) {
    int recordAffected = apptMainBean.queryExecuteUpdate(servicetype,"delete_ctlbilltype");
}
else if (billtype_old.equals("no")) {
    param[0]=servicetype;
    param[1]=billtype;
    int recordAffected = apptMainBean.queryExecuteUpdate(param,"save_ctlbilltype");
} else {
    param[0]=billtype;
    param[1]=servicetype;
    int recordAffected = apptMainBean.queryExecuteUpdate(param,"update_ctlbilltype");
}
apptMainBean.closePstmtConn();
%>

<script LANGUAGE="JavaScript">
    opener.document.serviceform.submit();
    self.close();
</script>
