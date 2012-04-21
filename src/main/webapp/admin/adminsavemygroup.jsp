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

<%
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.htm");
%>
<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat" errorPage="../errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.common.model.MyGroupPrimaryKey" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.adminsavemygroup.title" /></font></th>
	</tr>
</table>
<%
	int rowsAffected=0;
	String[] nums = request.getParameterValues("data");

	if (nums != null){
    	for (String datano: nums){
			MyGroup myGroup = new MyGroup();
			myGroup.setId(new MyGroupPrimaryKey());
			myGroup.getId().setMyGroupNo(request.getParameter("mygroup_no"));
			myGroup.getId().setProviderNo(request.getParameter("provider_no"+datano));
			myGroup.setFirstName(request.getParameter("first_name"+datano));
			myGroup.setLastName(request.getParameter("last_name"+datano));
			if(myGroupDao.find(myGroup.getId()) == null) {
				myGroupDao.persist(myGroup);
			}
			rowsAffected++;
    	}
	}

  if (rowsAffected > 0) {
%>
<p>
<h1><bean:message key="admin.adminsavemygroup.msgAdditionSuccess" /></h1>
</p>
<script LANGUAGE="JavaScript">
      self.close();
     	//self.opener.refresh();
</script> <%
  }  else {
%>
<p>
<h1><bean:message key="admin.adminsavemygroup.msgAdditionFailure" /></h1>
</p>
<%
  }
%>
<p></p>
<hr width="90%"></hr>
<form><input type="button"
	value="<bean:message key="global.btnClose"/>" onClick="window.close()">
</form>
</center>
</body>
</html:html>
