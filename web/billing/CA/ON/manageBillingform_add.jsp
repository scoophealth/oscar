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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
--%>

<form name="servicetypeform" method="post">
<table width="80%" border="0" height="285">
<tr>
	<td width="60%" valign="top">

	<table width="100%" border="0">
	<tr> 
		<th colspan="2" class="white"><bean:message key="billing.manageBillingform_add.msgAdd"/></th>
	</tr>
	<tr> 
		<td width="26%" class="white"><bean:message key="billing.manageBillingform_add.formServiceID"/>:</td>
		<td width="74%" class="white">
		<input type="text" name="typeid" maxlength="3">
		</td>
	</tr>
	<tr> 
		<td class="white"><bean:message key="billing.manageBillingform_add.formServiceName"/>:</td>
		<td class="white">
		<input type="text" name="type" value='<bean:message key="billing.manageBillingform_add.formServiceName"/>'>
		</td>
	</tr>
	<tr> 
		<td class="white"><bean:message key="billing.manageBillingform_add.formGroup1Name"/>:</td>
		<td class="white">
		<input type="text" name="group1" value='<bean:message key="billing.manageBillingform_add.formGroup1Name"/>'>
		</td>
	</tr>
	<tr> 
		<td class="white"><bean:message key="billing.manageBillingform_add.formGroup2Name"/>:</td>
		<td class="white">
		<input type="text" name="group2" value='<bean:message key="billing.manageBillingform_add.formGroup2Name"/>'>
		</td>
	</tr>
	<tr> 
		<td class="white"><bean:message key="billing.manageBillingform_add.formGroup3Name"/>:</td>
		<td class="white">
		<input type="text" name="group3" value='<bean:message key="billing.manageBillingform_add.formGroup3Name"/>'>
		</td>
	</tr>
	<tr> 
		<td class="white">
		<input type="button" name="addForm" value="<bean:message key="billing.manageBillingform_add.btnAdd"/>" onClick="valid(this.form)">
		</td>
		<td class="white">&nbsp;</td>
	</tr>
	</table>

	</td>
	<td bgcolor="#336699" valign="top"> 

	<table width="100%" border="0">
	<tr> 
		<th colspan="2" valign="top" bgcolor="black"><bean:message key="billing.manageBillingform_add.msgDeleteType"/></th>
	</tr>

<% 
	ResultSet rs=null ;
	ResultSet rs2=null ;

	rs = apptMainBean.queryResults("%", "search_ctlbillservice");
	int rCount = 0;
	boolean bodd=false;

	if(rs==null) {
		out.println("failed!!!"); 
	} else {
		while (rs.next()) {
%>

	<tr> 
		<td width="30%" valign="top" class="black"> 
		<a href=# onClick='onUnbilled("dbManageBillingform_delete.jsp?servicetype=<%=rs.getString("servicetype")%>");return false;' title="Delete Billing Form"><%=rs.getString("servicetype")%></a></td>
		<td class="black">
		<a href=# onClick='onUnbilled("dbManageBillingform_delete.jsp?servicetype=<%=rs.getString("servicetype")%>");return false;' title="Delete Billing Form"><%=rs.getString("servicetype_name")%></a></td>
	</tr>
<%
	}
}
%>

	</table>

	</td>
</tr>
</table>
</form>
