<%--

    Copyright (c) 2005, 2009 IBM Corporation and others.
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

    Contributors:
        <Quatro Group Software Systems inc.>  <OSCAR Team>

--%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.pmm" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.pmm");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="com.quatro.common.KeyConstants" %>
<table width="100%" height="100%" cellpadding="0px" cellspacing="0px">
	<tr>
		<th class="pageTitle" align="center"><span
			id="_ctl0_phBody_lblTitle" align="left">Lookup Table &nbsp;-&nbsp;<bean:write name="lookupCodeListForm" property="tableDef.description"/></span></th>
	</tr>
	<tr>
		<td align="left" class="buttonBar2">
		<logic:notEqual value="true" name="lookupCodeListForm" property="tableDef.readonly">
			<%
				String securityRole = "" + session.getAttribute("userrole") + "," + session.getAttribute("user");
			%>
			<security:oscarSec roleName="<%=securityRole%>" objectName="<%=KeyConstants.FUN_ADMIN_LOOKUP %>" rights="<%=KeyConstants.ACCESS_WRITE%>">
			<html:link  action="/Lookup/LookupCodeEdit.do" paramName="lookupCodeListForm" paramProperty="tableDef.tableId" paramId="id">
			<img src="../images/New16.png" border="0"/> Add</html:link>&nbsp;|&nbsp;
			</security:oscarSec>
		</logic:notEqual>
		<html:link action="/Lookup/LookupTableList.do"> <img src="../images/Back16.png" border="0"/> Back to Lookup Fields</html:link>
</td>

	</tr>
	<tr>
		<td align="left"></td>
	</tr>
	<tr>
		<td height="100%">
		<div
			style="color: Black; background-color: White; border-width: 1px; border-style: Ridge;
                    height: 100%; width: 100%; overflow: auto;" id="scrollBar">


		



<table>
<tr>
     <th>Category: </th>
     <th align="left"><bean:write name="lookupCodeListForm" property="tableDef.moduleName" /></th>
</tr>
<tr>
     <th>Field: </th>
     <th alighn="left"><bean:write name="lookupCodeListForm" property="tableDef.description"/></th>
</tr>
<tr><td colspan="2">&nbsp;</td>
</tr>
<tr>
	<th>ID</th><th>Description</th>
	<logic:equal name="lookupCodeListForm" property="tableDef.hasActive" value="true">
	<th>Active</th>
	</logic:equal>
	<logic:equal name="lookupCodeListForm" property="tableDef.hasDisplayOrder" value="true">
	<th>Display Order</th>
	</logic:equal>
</tr>

<logic:iterate id="lkCode" name="lookupCodeListForm" property="codes">
<tr>
	<td>
		<html:link action="/Lookup/LookupCodeEdit.do" paramId="id" paramProperty="codeId" paramName="lkCode">
			<bean:write name="lkCode" property="code" /> 
 		</html:link>
 	</td>
 	<td>
		<html:link action="/Lookup/LookupCodeEdit.do" paramId="id" paramProperty="codeId" paramName="lkCode">
 			<bean:write name="lkCode" property="description" />
	 	</html:link>
 	</td>
	<logic:equal name="lookupCodeListForm" property="tableDef.hasActive" value="true">
 	<td>
 		<logic:equal value="true" name="lkCode" property="active">
 			Yes
 		</logic:equal>
 		<logic:equal value="false" name="lkCode" property="active">
 			No
 		</logic:equal>
 	</td>
 	</logic:equal>
 	<logic:equal name="lookupCodeListForm" property="tableDef.hasDisplayOrder" value="true">
 	<td>
 		<bean:write name="lkCode" property="orderByIndex" />
 	</td>
 	</logic:equal>
</tr> 	
	</logic:iterate>

<tr><td colspan="2">

</td>

</table>


		</div>
		</td>
	</tr>
</table>
