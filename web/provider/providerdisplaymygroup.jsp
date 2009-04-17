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

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.htm");
%>
<%@ page import="java.util.*,java.sql.*"
	errorPage="../provider/errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="provider.providerdisplaymygroup.title" /></title>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database

//function upCaseCtrl(ctrl) {
//	ctrl.value = ctrl.value.toUpperCase();
//}

// stop javascript -->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="UPDATEPRE" METHOD="post" ACTION="providercontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="provider.providerdisplaymygroup.msgTitle" /></font></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF">
				<td ALIGN="center" colspan="2"><font face="arial"> <bean:message
					key="provider.providerdisplaymygroup.msgGroupNo" /></font></td>
				<td ALIGN="center"><font face="arial"> <bean:message
					key="provider.providerdisplaymygroup.msgProvider" /></font></td>
			</tr>
<%
   boolean bNewNo=false;
   String oldNo="";
   List<Map> resultList = oscarSuperManager.find("providerDao", "searchmygroupall", new Object[] {});
   for (Map group : resultList) {
	 String groupNo = String.valueOf(group.get("mygroup_no"));
     if(!(groupNo.equals(oldNo)) ) {
       bNewNo=bNewNo?false:true; oldNo=groupNo;
       //System.out.println(oldNo);
     }
%>
			<tr BGCOLOR="<%=bNewNo?"white":"ivory"%>">
				<td width="10%" align="center"><input type="checkbox"
					name="<%=groupNo+group.get("provider_no")%>"
					value="<%=groupNo%>"></td>
				<td ALIGN="center"><font face="arial"> <%=groupNo%></font></td>
				<td ALIGN="center"><font face="arial"> <%=group.get("last_name")+", "+group.get("first_name")%></font>
				</td>
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

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><input type="hidden" name="submit_form"
			value=""> <INPUT TYPE="submit"
			VALUE="<bean:message key="provider.providerdisplaymygroup.btnDelete"/>"
			SIZE="7"
			onclick="document.forms['UPDATEPRE'].submit_form.value='Delete'; document.forms['UPDATEPRE'].submit();">
		<INPUT TYPE="submit"
			VALUE="<bean:message key="provider.providerdisplaymygroup.btnNew"/>"
			SIZE="7"
			onclick="document.forms['UPDATEPRE'].submit_form.value='New Group/Add a Member'; document.forms['UPDATEPRE'].submit();">
		<INPUT TYPE="RESET"
			VALUE="<bean:message key="provider.providerdisplaymygroup.btnClose"/>"
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

</body>
</html:html>
