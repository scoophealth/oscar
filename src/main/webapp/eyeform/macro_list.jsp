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

<%@ page import="org.oscarehr.eyeform.model.Macro" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<html>
<head>
<title>Macros</title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/eyeform/displaytag.css" type="text/css">
<style type="text/css">
.boldRow {
	color:red;
}
.commonRow{
	color:black;
}
span.h5 {
  margin-top: 1px;
  border-bottom: 1px solid #000;
  width: 90%;
  font-weight: bold;
  list-style-type: none;
  padding: 2px 2px 2px 2px;
  color: black;
  background-color: #69c;
  font-family: Trebuchet MS, Lucida Sans Unicode, Arial, sans-serif;
  font-size: 10pt;
  text-decoration: none;
  display: block;
  clear: both;
  white-space: nowrap;	

}
</style>
<script>
function editMacro(id) {
	location.href='<%=request.getContextPath()%>/eyeform/Macro.do?method=form&macro.id='+id;	
}
</script>
</head>
<body>

<form name="inputForm" method="post" action="Macro.do">
	<input type="hidden" name="method" value="editMacro">
	<input type="hidden" name="editTarget" value="">
	<h1>Eyeform Macros</h1>
	
	<table style="border:0px">
	<tr>
	<td>
	<input type="button" value="Add" onclick="this.form.method.value='addMacro';this.form.submit();">
	<input type="button" value="Delete" onclick="if(confirm('Are you sure to delete the selected macros?')){this.form.method.value='deleteMacro';this.form.submit();}">
	<input type="button" value="Close" onclick="window.opener.location.reload();window.close();return false;">
	</td>
	</tr>
	</table>

	<display:table name="requestScope.macros" requestURI="Macro.do?method=list" defaultsort="2" sort="list" defaultorder="ascending" id="macro" pagesize="0">
		<%
			Macro m = (Macro)pageContext.getAttribute("macro");												
		%>	
		<display:column style="width:20px;">
		<input type="checkbox" name="selected_id" value="<c:out value="${macro.id}"/>">
		</display:column>
		<display:column title="#" property="displayOrder" sortable="true" style="width:20px;">
		</display:column>		
		<display:column title="Label" sortable="true" style="width:120px;">
		<a href="javascript:void(0)" onclick="editMacro(<c:out value="${macro.id}"/>);"><c:out value="${macro.label}" /></a>
		</display:column>
		<display:column property="impression" sortable="true" title="Append impression with" style="width:250px;"/>
		<display:column title="Followup in" style="width:200px;">			
			<%=getFollowupWeb(m) %>
		</display:column>
		<display:column title="Test Bookings" style="width:80px" >
			<pre><c:out value="${macro.testRecords}" /></pre>
		</display:column>		
		<display:column title="Tickler" >
			<%=getTicklerWeb(m)	%>			
		</display:column>
		<display:column title="Bill" style="width:40px" >
			<%=getBillingWeb(m)%>
		</display:column>		
	</display:table>
	
</form>

</body>
</html>
<%!
public String getFollowupWeb(Macro macro) {
	if(macro.getFollowupNo()>0) {
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		Provider provider = providerDao.getProvider(macro.getFollowupDoctorId());
		if(provider!=null)
			return macro.getFollowupNo() + " " + macro.getFollowupUnit() + " with Dr. " + provider.getFormattedName();
	}
	return new String();
}

public String getTicklerWeb(Macro macro) {
	if(macro.getTicklerRecipient()!=null && macro.getTicklerRecipient().length()>0) {
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		Provider provider = providerDao.getProvider(macro.getTicklerRecipient());
		if(provider!=null)
		 	return provider.getFormattedName();
	}
	return new String();
}

public String getBillingWeb(Macro macro) {
	if(macro.getBillingCodes()!=null && macro.getBillingCodes().trim().length()>0) {
		return "YES";
	}
	return "NO";
}
%>
