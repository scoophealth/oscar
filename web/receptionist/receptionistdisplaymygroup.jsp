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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ page import="java.util.*,java.sql.*"
	errorPage="../provider/errorpage.jsp"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="receptionist.receptionistdisplaymygroup.title" /></title>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="javascript">
<!--

// stop -->
</script>
</head>

<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<FORM NAME="UPDATEPRE" METHOD="post" ACTION="receptionistcontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#CCCCFF">
		<th align=CENTER NOWRAP><bean:message
			key="receptionist.receptionistdisplaymygroup.msgTitle" /></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF" ALIGN="center">
				<th colspan="2"><bean:message
					key="receptionist.receptionistdisplaymygroup.msgGroupNo" /></th>
				<th><bean:message
					key="receptionist.receptionistdisplaymygroup.msgProviderName" /></th>
				<th><bean:message
					key="receptionist.receptionistdisplaymygroup.msgOrder" /></th>
			</tr>
<%
	boolean bNewNo = false;
	String oldNo = "";
	List<Map> resultList = oscarSuperManager.find("receptionistDao", "searchmygroupall", new Object[] {});
	for (Map group : resultList) {
		String groupNo = String.valueOf(group.get("mygroup_no"));
		bNewNo = bNewNo?false:true;
%>
			<tr BGCOLOR="<%=bNewNo?"white":"#EEEEFF"%>">
				<td width="10%" align="center"><input type="checkbox"
					name="<%=groupNo + group.get("provider_no")%>"
					value="<%=groupNo%>"></td>
				<td ALIGN="center"><%=groupNo%></td>
				<td>&nbsp; <%=group.get("last_name") + ", " + group.get("first_name")%>
				</td>
				<td ALIGN="center"><INPUT TYPE="text"
					name="__vieworder<%=groupNo + group.get("provider_no")%>"
					VALUE="<%=group.get("vieworder")==null?"":group.get("vieworder")%>"
					SIZE="3" maxlength="2"></td>
			</tr>
<%
	}
%>
			<INPUT TYPE="hidden" NAME="displaymode" VALUE='newgroup'>

		</table>

		</td>
	</tr>
</table>
</center>

<table width="100%">
	<tr bgcolor="#CCCCFF">
		<TD align="center"><input type="hidden" name="submit_form"
			value=""> <INPUT TYPE="button"
			VALUE="<bean:message key="receptionist.receptionistdisplaymygroup.btnUpdate"/>"
			SIZE="7"
			onclick="document.forms['UPDATEPRE'].submit_form.value='Update'; document.forms['UPDATEPRE'].submit();">
		<INPUT TYPE="button"
			VALUE="<bean:message key="receptionist.receptionistdisplaymygroup.btnDelete"/>"
			SIZE="7"
			onclick="document.forms['UPDATEPRE'].submit_form.value='Delete'; document.forms['UPDATEPRE'].submit();">
		<INPUT TYPE="button"
			VALUE="<bean:message key="receptionist.receptionistdisplaymygroup.btnNew"/>"
			SIZE="7"
			onclick="document.forms['UPDATEPRE'].submit_form.value='New Group/Add a Member'; document.forms['UPDATEPRE'].submit();">
		<INPUT TYPE="RESET"
			VALUE="<bean:message key="receptionist.receptionistdisplaymygroup.btnExit"/>"
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

</body>
</html:html>
