<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%
if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
%>

<%@ page
	import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.CtlBillingType" %>
<%@ page import="org.oscarehr.common.dao.CtlBillingTypeDao" %>
<%
	CtlBillingTypeDao ctlBillingTypeDao = SpringUtils.getBean(CtlBillingTypeDao.class);
%>
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
    CtlBillingType cbt = new CtlBillingType();
    cbt.setId(servicetype);
    cbt.setBillType(billtype);
    ctlBillingTypeDao.persist(cbt);
} else {
    param[0]=billtype;
    param[1]=servicetype;
    int recordAffected = apptMainBean.queryExecuteUpdate(param,"update_ctlbilltype");
}
%>

<script LANGUAGE="JavaScript">
    opener.document.serviceform.submit();
    self.close();
</script>
