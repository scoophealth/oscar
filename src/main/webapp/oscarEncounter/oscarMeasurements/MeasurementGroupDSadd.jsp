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
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%
String groupName = session.getAttribute( "groupName").toString();
String groupId = null;
String propKey = null;

MeasurementManager measurementManager = SpringUtils.getBean(MeasurementManager.class);

%>
<!DOCTYPE html>
<html>
<head>

		<title><bean:message key="oscarEncounter.Measurements.msgEditMeasurementGroup" /> - <%=groupName%></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet" media="screen">

</head>

<body>
<div class="container">

<h3><bean:message key="oscarEncounter.Measurements.msgEditMeasurementGroup" /> - Add Decision Support to <em class="text-info"><%=groupName%></em> Group </h3>
<p><em>The following listed decision support files are available for both the flowsheets and Health Tracker. Make a selection and press "add" to make that decision support available on the <strong><%=groupName%></strong>  measurement group.</em></p>


<form action="MeasurementGroupDScomplete.jsp" method="post" name="formAdd">
         <select name="htmlName">
                 <option value="-1">Select Decision Support</option>
<%
List<String> dsHtmlList = measurementManager.getFlowsheetDsHTML();
for (String file : dsHtmlList) {%>

                 <option value="<%=file%>"><%=file.substring(0, file.lastIndexOf('.'))%></option>
                 
<%}%>
		 </select> 	
		 
		 <button type="submit" name="add" class="btn btn-primary" style="margin-top:-10px">Add</button>	
		 <a href="SetupGroupList.do" class="btn" style="margin-top:-10px">Cancel</a>	
</form>

</div><!-- container -->						
						
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

</body>
</html>
