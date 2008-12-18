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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
  
%>
<%@ page import="java.util.*,java.sql.*,java.util.ResourceBundle"
	errorPage="../provider/errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.adminnewgroup.title" /></title>
</head>
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<script language="javascript">
<%-- start javascript ---- check to see if it is really empty in database --%>

function setfocus() {
  this.focus();
  document.UPDATEPRE.mygroup_no.focus();
  document.UPDATEPRE.mygroup_no.select();
}

function validate() {
  group = document.UPDATEPRE.mygroup_no.value;
  
  if (group.length <=0 || group <= " ") {
     alert("<bean:message key="admin.adminNewGroup.msgGroupIsRequired"/>");
     
     return false;
  }else{
  	
  	var checked=false;
  	var checkboxes = document.getElementsByName("data");
	var x=0;
		for(x=0;x<checkboxes.length;x++) {
			if(checkboxes[x].checked==true) {
				checked=true;
			}
		}
		if(checked==false) {
			alert('You must choose a provider');
			return false;
		}
		return true;
  	}	
}
</script>

<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<%
  ResourceBundle properties = ResourceBundle.getBundle("oscarResources", request.getLocale());
  
  if(request.getParameter("submit").equals(properties.getString("admin.admindisplaymygroup.btnSubmit1")) ) { //delete the group member 
    int rowsAffected=0;
    String[] param =new String[2];
    StringBuffer strbuf=new StringBuffer();

  	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    strbuf=new StringBuffer(e.nextElement().toString());
  		if( strbuf.toString().indexOf("displaymode")!=-1 || strbuf.toString().indexOf("submit")!=-1) continue;
      param[0]=request.getParameter(strbuf.toString());
	    //param[1]=strbuf.toString().substring( strbuf.toString().indexOf(param[0])+1 ); 
	    param[1]=strbuf.toString().substring( param[0].length() ); 
      rowsAffected = apptMainBean.queryExecuteUpdate(param,"deletegroupmember");
    }
    out.println("<script language='JavaScript'>self.close();</script>");
  }
%>

<FORM NAME="UPDATEPRE" METHOD="post" ACTION="admincontrol.jsp"
	onsubmit="return validate();">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.adminnewgroup.description" /></font></th>
	</tr>
</table>

<center>
<table border="0" cellpadding="0" cellspacing="0" width="80%">
	<tr>
		<td width="100%">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr BGCOLOR="#CCFFFF">
				<td ALIGN="center"><font face="arial"><bean:message
					key="admin.adminmygroup.formGroupNo" /></font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="text" name="mygroup_no" size="10" maxlength="10"> <font
					size="-2">(Max. 10 chars.)</font></td>
			</tr>
			<%
   ResultSet rsgroup = null;
   int i=0;
   rsgroup = apptMainBean.queryResults("searchproviderall");
   while (rsgroup.next()) { 
     i++;
%>
			<tr BGCOLOR="<%=i%2==0?"ivory":"white"%>">
				<td>&nbsp; <%=rsgroup.getString("last_name")%>, <%=rsgroup.getString("first_name")%></td>
				<td ALIGN="center"><input type="checkbox" name="data"
					value="<%=i%>"> <input type="hidden"
					name="provider_no<%=i%>"
					value="<%=rsgroup.getString("provider_no")%>"> <INPUT
					TYPE="hidden" NAME="last_name<%=i%>"
					VALUE='<%=rsgroup.getString("last_name")%>'> <INPUT
					TYPE="hidden" NAME="first_name<%=i%>"
					VALUE='<%=rsgroup.getString("first_name")%>'></td>
			</tr>
			<%
   }
   apptMainBean.closePstmtConn();
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
		<TD align="center"><input type="submit" name="Submit"
			value="<bean:message key="admin.adminnewgroup.btnSubmit"/>">
		<INPUT TYPE="RESET" VALUE="<bean:message key="global.btnClose"/>"
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

<div align="center"><font size="1" face="Verdana" color="#0000FF"><B></B></font></div>

</body>
</html:html>
