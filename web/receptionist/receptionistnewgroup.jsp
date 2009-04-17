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
	key="receptionist.receptionistnewgroup.title" /></title>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<script language="javascript">
<!-- start javascript 
function setfocus() {
  this.focus();
  document.UPDATEPRE.mygroup_no.focus();
  document.UPDATEPRE.mygroup_no.select();
}
function checkForm() {
	if (UPDATEPRE.mygroup_no.value == "") {
		alert("<bean:message key="receptionist.receptionistnewgroup.msgAlert"/>");
		UPDATEPRE.mygroup_no.focus();
		return false;
	}
	return true;
}
// stop javascript -->
</script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<%
  int rowsAffected=0;

  if ("Update".equals(request.getParameter("submit_form")) ) { //update the group member 
    String[] param = new String[3];

  	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
  		StringBuffer strbuf = new StringBuffer(e.nextElement().toString());
  		if (strbuf.toString().indexOf("displaymode")!=-1 || strbuf.toString().indexOf("submit_form")!=-1) continue;
    	param[1] = request.getParameter(strbuf.toString()); //mygroup_no
    	if (param[1].equals("") || strbuf.toString().startsWith("__vieworder")) continue;
		param[2] = strbuf.toString().substring(param[1].length()); 
    	param[0] = request.getParameter("__vieworder" + param[1] + param[2]);
    	rowsAffected = oscarSuperManager.update("receptionistDao", "upgradegroupmember", param);
    }
    out.println("<script language='JavaScript'>self.close();</script>");
    return;
  }

  if ("Delete".equals(request.getParameter("submit_form"))) { //delete the group member 
    String[] param = new String[2];

  	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
  		StringBuffer strbuf = new StringBuffer(e.nextElement().toString());
  		if (strbuf.toString().indexOf("displaymode")!=-1 || strbuf.toString().indexOf("submit_form")!=-1) continue;
    	param[0] = request.getParameter(strbuf.toString());
		param[1] = strbuf.toString().substring(param[0].length() ); 
    	rowsAffected = oscarSuperManager.update("receptionistDao", "deletegroupmember", param);
    }
    out.println("<script language='JavaScript'>self.close();</script>");
    return;
  }
%>


<FORM NAME="UPDATEPRE" METHOD="post" ACTION="receptionistcontrol.jsp"
	onSubmit="return checkForm();">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="receptionist.receptionistnewgroup.msgTitle" /></font></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF">
				<td ALIGN="center"><font face="arial"> <bean:message
					key="receptionist.receptionistnewgroup.msgGroupNo" /></font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="text" name="mygroup_no" size="10" maxlength="10"> <font
					size="-2"><bean:message
					key="receptionist.receptionistnewgroup.msgMaxChar" /></font></td>
			</tr>
<%
	int i=0;
	List<Map> resultList = oscarSuperManager.find("receptionistDao", "searchprovider", new Object[] {});
	for (Map provider : resultList) {
		i++;
%>
			<tr BGCOLOR="#C4D9E7">
				<td><font face="arial"> &nbsp;<%=provider.get("last_name")%>,
				<%=provider.get("first_name")%></font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="checkbox" name="data<%=i%>" value="<%=i%>"> <input
					type="hidden" name="provider_no<%=i%>"
					value="<%=provider.get("provider_no")%>"> <INPUT
					TYPE="hidden" NAME="last_name<%=i%>"
					VALUE='<%=provider.get("last_name")%>'> <INPUT
					TYPE="hidden" NAME="first_name<%=i%>"
					VALUE='<%=provider.get("first_name")%>'></td>
			</tr>
<%
	}
%>
			<INPUT TYPE="hidden" NAME="dboperation" VALUE='savemygroup'>
			<INPUT TYPE="hidden" NAME="displaymode" VALUE='savemygroup'>

		</table>

		</td>
	</tr>
</table>
</center>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><input type="hidden" name="Submit"
			value=" Save "> <input type="submit"
			value="<bean:message key="receptionist.receptionistnewgroup.btnSave"/>">
		<INPUT TYPE="RESET"
			VALUE="<bean:message key="receptionist.receptionistnewgroup.btnExit"/>"
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

<div align="center"><font size="1" face="Verdana" color="#0000FF"><B></B></font></div>

</body>
</html:html>
