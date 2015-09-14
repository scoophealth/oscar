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


<%@ page import="java.sql.*, java.util.*, oscar.MyDateFormat" errorPage="../errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.common.model.MyGroupPrimaryKey" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
%>

<!DOCTYPE html>
<html:html locale="true">
<head>

<script>
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">


</head>

<body onload="start()">

<br>
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

    <div class="alert alert-success">
 		<bean:message key="admin.adminsavemygroup.msgAdditionSuccess" />
    </div>
 <%
  }  else {
%>
	<div class="alert alert-error">
		<bean:message key="admin.adminsavemygroup.msgAdditionFailure" />
	</div>
<%
  }
%>


<a href="admindisplaymygroup.jsp" class="btn btn-primary">View Group List</a>

<a href="adminnewgroup.jsp" class="btn"><bean:message key="admin.admindisplaymygroup.btnSubmit2"/></a>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>

<script>
$( document ).ready(function() {	
    parent.parent.resizeIframe($('html').height());	
});
</script>

</body>
</html:html>
