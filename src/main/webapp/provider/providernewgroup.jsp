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

<%@ page import="java.util.*,java.sql.*" errorPage="../provider/errorpage.jsp"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.common.model.MyGroupPrimaryKey" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
    ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="provider.providernewgroup.title" /></title>
<script language="javascript">
<!-- start javascript
function setfocus() {
  this.focus();
  document.UPDATEPRE.mygroup_no.focus();
  document.UPDATEPRE.mygroup_no.select();
}
function checkForm() {
	if (UPDATEPRE.mygroup_no.value == "") {
		alert("No Group No.!");
		UPDATEPRE.mygroup_no.focus();
		return false;
	}
	return true;
}
// stop javascript -->
</script>
</head>

<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<%
  if ("Delete".equals(request.getParameter("submit_form")) ) { //delete the group member
    int rowsAffected=0;
    String[] param = new String[2];

  	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
  		StringBuffer strbuf=new StringBuffer(e.nextElement().toString());
  		if (strbuf.toString().indexOf("displaymode")!=-1 || strbuf.toString().indexOf("submit_form")!=-1) continue;
    	param[0]=request.getParameter(strbuf.toString());
	    param[1]=strbuf.toString().substring( param[0].length() );
	    myGroupDao.deleteGroupMember(param[0],param[1]);
      	rowsAffected = 1;
    }
    out.println("<script language='JavaScript'>self.close();</script>");
  }
%>


<FORM NAME="UPDATEPRE" METHOD="post" ACTION="providercontrol.jsp"
	onSubmit="return checkForm();">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="provider.providernewgroup.msgTitle" /></font></th>
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
					key="provider.providernewgroup.msgGroupNo" /></font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="text" name="mygroup_no" size="10" maxlength="10"> <font
					size="-2"><bean:message
					key="provider.providernewgroup.msgMaxChars" /></font></td>
			</tr>
<%
   int i=0;
   for(Provider p : providerDao.getActiveProviders()) {
     i++;
%>
			<tr BGCOLOR="#C4D9E7">
				<td><font face="arial"> &nbsp;<%=p.getLastName()%>,
				<%=p.getFirstName()%></font></td>
				<td ALIGN="center"><font face="arial"> </font> <input
					type="checkbox" name="data<%=i%>" value="<%=i%>"> <input
					type="hidden" name="provider_no<%=i%>"
					value="<%=p.getProviderNo()%>"> <INPUT
					TYPE="hidden" NAME="last_name<%=i%>"
					VALUE='<%=p.getLastName()%>'> <INPUT
					TYPE="hidden" NAME="first_name<%=i%>"
					VALUE='<%=p.getFirstName()%>'></td>
			</tr>
<%
   }
%>
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
			value="<bean:message key="provider.providernewgroup.btnSave"/>">
		<INPUT TYPE="RESET"
			VALUE="<bean:message key="provider.providernewgroup.btnExit"/>"
			onClick="window.close();"></TD>
	</tr>
</TABLE>

</FORM>

<div align="center"><font size="1" face="Verdana" color="#0000FF"><B></B></font></div>

</body>
</html:html>
