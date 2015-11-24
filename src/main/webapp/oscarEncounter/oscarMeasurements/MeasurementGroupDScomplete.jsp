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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.measurements" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_admin.measurements");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.managers.MeasurementManager"%>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%
String groupName = session.getAttribute( "groupName").toString();
String groupId = null;

String propKey = null;
String propValue = null;
String valueDisplay = null;
String dsHTML = null;

String state = null;

MeasurementManager measurementManager = SpringUtils.getBean(MeasurementManager.class);

if(groupName!=null){

groupId = measurementManager.findGroupId(groupName);
propKey = "mgroup.ds.html."+groupId;



if(request.getParameter("add")!=null){
	dsHTML = request.getParameter("htmlName");
	measurementManager.addMeasurementGroupDS(groupName, dsHTML);
	
	state = "added";
}else if(request.getParameter("remove")!=null){
	
	measurementManager.removeMeasurementGroupDS(propKey);
	state = "removed";
}

}//if groupName
%>
<!DOCTYPE html>
<html>
<head>

		<title><bean:message key="oscarEncounter.Measurements.msgEditMeasurementGroup" /> - complete</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet" media="screen">

</head>

<body>
<div class="container">

<h3><bean:message key="oscarEncounter.Measurements.msgEditMeasurementGroup" /> - Modify Decision Support for Group </h3>
    
    <div class="alert alert-success alert-block">
      <h4>Success!</h4>
      You have successfully <%=state%> the decision support!
      
      <div style="width:100%;text-align:right;margin-top:10px"><a class="btn" onclick="window.close();">Done</a>  <a href="SetupGroupList.do" class="btn btn-info">Modify another group</a></div>
      
    </div>

</div><!-- container -->						
						
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

</body>
</html>
