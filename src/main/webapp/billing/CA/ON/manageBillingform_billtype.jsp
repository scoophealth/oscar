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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.CtlBillingType" %>
<%@ page import="org.oscarehr.common.dao.CtlBillingTypeDao" %>

<%
	CtlBillingTypeDao ctlBillingTypeDao = SpringUtils.getBean(CtlBillingTypeDao.class);
%>


<%
String type_id = "", type_name="", billtype="no";
type_id = request.getParameter("type_id");
type_name = request.getParameter("type_name");

for(CtlBillingType cbt:ctlBillingTypeDao.findByServiceType(type_id)) {
	billtype = cbt.getBillType();
}

%>

<table width=95%>
	<tr>
		<td class="black" width="15%"><%=type_id%></td>
		<td class="black" height="30"><%=type_name%></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td class="white">
		<p>&nbsp;<br>
		<bean:message key="billing.manageBillingform_add.formDefaultBillType" />
		:<br>
		<input type="hidden" name="bill_servicetype" value="<%=type_id%>">
		<input type="hidden" name="billtype_old" value="<%=billtype%>">
		<select name="billtype_new">
			<option value="no" <%=billtype.equals("no")?"selected":""%>>--
			no --</option>
			<option value="ODP" <%=billtype.equals("ODP")?"selected":""%>>Bill
			OHIP</option>
			<option value="WCB" <%=billtype.equals("WCB")?"selected":""%>>WSIB</option>
			<option value="NOT" <%=billtype.equals("NOT")?"selected":""%>>Do
			Not Bill</option>
			<option value="IFH" <%=billtype.equals("IFH")?"selected":""%>>IFH</option>
			<option value="PAT" <%=billtype.equals("PAT")?"selected":""%>>3rd
			Party</option>
			<option value="OCF" <%=billtype.equals("OCF")?"selected":""%>>-OCF</option>
			<option value="ODS" <%=billtype.equals("ODS")?"selected":""%>>-ODSP</option>
			<option value="CPP" <%=billtype.equals("CPP")?"selected":""%>>-CPP</option>
			<option value="STD" <%=billtype.equals("STD")?"selected":""%>>-STD/LTD</option>
		</select> <input type="button" value="Change"
			onclick="manageBillType(bill_servicetype.value, billtype_old.value, billtype_new.value);"><br>
		</p>
		<p><input type="button" value="Delete Billing Form"
			onclick="onUnbilled('dbManageBillingform_delete.jsp?servicetype=<%=type_id%>');"></p>
		<p><input type="button" value="Cancel"
			onclick="showManageType(false);"></p>
		</td>
	</tr>
</table>
