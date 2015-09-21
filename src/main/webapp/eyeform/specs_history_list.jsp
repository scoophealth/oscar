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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page
	import="oscar.oscarEncounter.pageUtil.*,oscar.oscarEncounter.data.*,java.util.List,org.oscarehr.eyeform.model.EyeformSpecsHistory"%>

<%
String demo = request.getParameter("demographicNo");
String proNo = (String) session.getAttribute("user");
oscar.oscarDemographic.data.DemographicData demoData=null;
org.oscarehr.common.model.Demographic demographic=null;

oscar.oscarProvider.data.ProviderData pdata = new oscar.oscarProvider.data.ProviderData(proNo);
String team = pdata.getTeam();

if (demo != null ){ 
    demoData = new oscar.oscarDemographic.data.DemographicData();
    demographic = demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demo);    
}
else
    response.sendRedirect("../error.jsp");


List<EyeformSpecsHistory> specs = (List<EyeformSpecsHistory>)request.getAttribute("specs");
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Specs History
</title>
<html:base />

<!--META HTTP-EQUIV="Refresh" CONTENT="20;"-->

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />





</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}

</script>

<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF" onload="window.focus()">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Specs History</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td class="Header" NOWRAP>Specs History&nbsp;&nbsp;
				<%=demographic.getLastName() %>, <%=demographic.getFirstName()%> <%=demographic.getSex()%>
				<%=demographic.getAge()%></td>
				<td></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
			
			
		</td>
		<td class="MainTableRightColumn">
	
				<table border="0" width="80%" cellspacing="1">
					<tr>
						<th align="left" class="VCRheads">Type</th>
						<th align="left" class="VCRheads">Data</th>
						<th align="left" class="VCRheads">Date</th>									
					</tr>
					<%  
                                    for (int i = 0; i < specs.size(); i++){
                                    String type      = (String) specs.get(i).getType();     
                                    String date = specs.get(i).getDateStr();
                                    String formula = specs.get(i).toString();
                                %>
					<tr>
						<td ><%=type %></td>
						<td ><%=formula %></td>
						<td ><%=date%></td>											
					</tr>
					<%}%>
				</table>			
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
