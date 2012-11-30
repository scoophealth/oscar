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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.common.model.MyGroupPrimaryKey" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>


<%
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
%>

<%
    String curProvider_no = (String) session.getAttribute("user");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isSiteAccessPrivacy=false;
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>



<%@ page import="java.util.*,java.sql.*" errorPage="../provider/errorpage.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.admindisplaymygroup.title" /></title>
</head>
<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database

//function upCaseCtrl(ctrl) {
//	ctrl.value = ctrl.value.toUpperCase();
//}

// stop javascript -->
</script>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="UPDATEPRE" METHOD="post" ACTION="adminnewgroup.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.admindisplaymygroup.description" /></font></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF">
				<td ALIGN="center" colspan="2"><font face="arial"><bean:message
					key="admin.adminmygroup.formGroupNo" /></font></td>
				<td ALIGN="center"><font face="arial"><bean:message
					key="admin.admindisplaymygroup.formProviderName" /></font></td>
			</tr>
			
			
<%

String oldNumber="";
boolean toggleLine=false;

List<MyGroup> groupList = myGroupDao.findAll();
Collections.sort(groupList, MyGroup.MyGroupNoComparator);

if(isSiteAccessPrivacy) {
	groupList = myGroupDao.getProviderGroups(curProvider_no);
}


for(MyGroup myGroup : groupList) {

	if(!myGroup.getId().getMyGroupNo().equals(oldNumber)) {
		toggleLine = !toggleLine;
		oldNumber = myGroup.getId().getMyGroupNo();
	}
%>
			<tr BGCOLOR="<%=toggleLine?"white":"ivory"%>">
				<td width="10%" align="center"><input type="checkbox"
					name="<%=myGroup.getId().getMyGroupNo() + myGroup.getId().getProviderNo()%>"
					value="<%=myGroup.getId().getMyGroupNo()%>"></td>
				<td ALIGN="center"><font face="arial"> <%=myGroup.getId().getMyGroupNo()%></font></td>
				<td ALIGN="center"><font face="arial"> <%=myGroup.getLastName()+","+ myGroup.getFirstName()%></font>
				</td>
			</tr>
<%
   }
%>
		
		</table>

		</td>
	</tr>
</table>
</center>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><INPUT TYPE="submit" name="submit"
			VALUE="<bean:message key="admin.admindisplaymygroup.btnSubmit1"/>"
			SIZE="7"> <INPUT TYPE="submit" name="submit"
			VALUE="<bean:message key="admin.admindisplaymygroup.btnSubmit2"/>"
			SIZE="7"> <INPUT TYPE="RESET"
			VALUE='<bean:message key="global.btnClose"/>'
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

</body>
</html:html>
