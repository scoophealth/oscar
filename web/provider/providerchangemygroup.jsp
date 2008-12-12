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
  
  //String curProvider_no, curOperator_no;
  //curProvider_no = request.getParameter("provider_no");
  //curOperator_no = (String) session.getAttribute("user");
  String oldGroup_no = request.getParameter("mygroup_no")==null?".":request.getParameter("mygroup_no");
%>
<%@ page import="java.util.*,java.sql.*"
	errorPage="../provider/errorpage.jsp"%>
<jsp:useBean id="groupBean" class="oscar.AppointmentMainBean"
	scope="page" />

<%@ include file="../admin/dbconnection.jsp"%>
<%
  String [][] dbQueries=new String[][] {
    {"searchmygroupno", "select mygroup_no from mygroup group by mygroup_no order by mygroup_no"}, 
    {"searchmygroupall", "select * from mygroup order by mygroup_no"}, 
  };
  String[][] responseTargets=new String[][] {  };
  groupBean.doConfigure(dbParams,dbQueries,responseTargets);
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<html:html locale="true">
<head>
<title><bean:message key="provider.providerchangemygroup.title" /></title>
</head>
<meta http-equiv="Cache-Control" content="no-cache">

<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database
function setfocus() {
  this.focus();
  //document.UPDATEPRE.start_hour.focus();
}
// stop javascript -->
</script>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME="UPDATEPRE" METHOD="post" ACTION="providercontrol.jsp">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="provider.providerchangemygroup.msgTitle" /></font></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td><bean:message
			key="provider.providerchangemygroup.msgChangeGroup" />:</TD>
		<TD align="right"><select name="mygroup_no">
			<% ResultSet rsgroup = groupBean.queryResults("searchmygroupno");
 	 while (rsgroup.next()) { 
%>
			<option value="<%=rsgroup.getString("mygroup_no")%>"
				<%=oldGroup_no.equals(rsgroup.getString("mygroup_no"))?"selected":""%>><%=rsgroup.getString("mygroup_no")%></option>
			<%
 	 }
%>
		</select> &nbsp;<INPUT TYPE="submit"
			VALUE="<bean:message key="provider.providerchangemygroup.btnChange"/>">
		<INPUT TYPE="RESET"
			VALUE="<bean:message key="provider.providerchangemygroup.btnCancel"/>"
			onClick="window.close();"></td>
	</tr>
</TABLE>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#C4D9E7">
				<td ALIGN="center"><font face="arial"> <bean:message
					key="provider.providerchangemygroup.msgGroup" /></font></td>
				<td ALIGN="center"><font face="arial"> <bean:message
					key="provider.providerchangemygroup.msgName" /></font></td>
			</tr>
			<%
   rsgroup = null;
   boolean bNewNo=false;
   String oldNo="";
   rsgroup = groupBean.queryResults("searchmygroupall");
   while (rsgroup.next()) { 
     if(!(rsgroup.getString("mygroup_no").equals(oldNo)) ) {
       bNewNo=bNewNo?false:true; oldNo=rsgroup.getString("mygroup_no");
       //System.out.println(oldNo);
     }
%>
			<tr BGCOLOR="<%=bNewNo?"white":"ivory"%>">
				<td ALIGN="center"><font face="arial"> <%=rsgroup.getString("mygroup_no")%></font></td>
				<td><font face="arial"> &nbsp;<%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></font>
				</td>
			</tr>
			<%
   }
   groupBean.closePstmtConn();
%>
			<INPUT TYPE="hidden" NAME="start_hour"
				VALUE='<%=(String) session.getAttribute("starthour")%>'>
			<INPUT TYPE="hidden" NAME="end_hour"
				VALUE='<%=(String) session.getAttribute("endhour")%>'>
			<INPUT TYPE="hidden" NAME="every_min"
				VALUE='<%=(String) session.getAttribute("everymin")%>'>
			<INPUT TYPE="hidden" NAME="provider_no"
				VALUE='<%=(String) session.getAttribute("user")%>'>
			<caisi:isModuleLoad moduleName="ticklerplus">
				<INPUT TYPE="hidden" NAME="new_tickler_warning_window"
					VALUE='<%=(String) session.getAttribute("newticklerwarningwindow")%>'>
			</caisi:isModuleLoad>
			<INPUT TYPE="hidden" NAME="color_template" VALUE='deepblue'>
			<INPUT TYPE="hidden" NAME="dboperation" VALUE='updatepreference'>
			<INPUT TYPE="hidden" NAME="displaymode" VALUE='updatepreference'>

		</table>

		</td>
	</tr>
</table>
</center>

<table width="100%" BGCOLOR="#486ebd">
	<tr>
		<TD align="center"><INPUT TYPE="button"
			VALUE="<bean:message key="provider.providerchangemygroup.btnCancel"/>"
			onClick="window.close();"></TD>
	</tr>
</TABLE>
<INPUT TYPE="hidden" NAME="color_template" VALUE='deepblue'> <INPUT
	TYPE="hidden" NAME="dboperation" VALUE='updatepreference'> <INPUT
	TYPE="hidden" NAME="displaymode" VALUE='updatepreference'></FORM>


</body>
</html:html>
