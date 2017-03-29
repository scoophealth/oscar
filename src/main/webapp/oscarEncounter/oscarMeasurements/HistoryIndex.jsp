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

<%@page import="org.oscarehr.util.WebUtilsOld"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ page import="oscar.oscarEncounter.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.pageUtil.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.bean.*"%>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.data.*"%>
<%@ page import="java.util.Vector"%>
<%
    EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
    MeasurementMapConfig measurementMapConfig = new MeasurementMapConfig();
%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_measurement" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_measurement");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.Index.oldMeasurements" />
</title>
<html:base />

</head>


<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<style type="text/css" media="print">
    .noprint {
        display: none;
    }
    
</style>
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();">
<html:errors />
<%=WebUtilsOld.popErrorAndInfoMessagesAsHtml(session)%>

<div style="display:inline-block; text-align:center">
	<bean:message key="oscarEncounter.oscarMeasurements.oldmesurementindex"/>
	
	<table>
		<tr>
			<th align="left" class="Header" width="20"><bean:message
				key="oscarEncounter.oscarMeasurements.displayHistory.headingType" />
			</th>
			<th align="left" class="Header" width="200"><bean:message key="oscarEncounter.oscarMeasurements.typedescription"/></th>
			<th align="left" class="Header" width="50"></th>
		</tr>
		<logic:present name="measurementsData">
			<logic:iterate id="data" name="measurementsData"
	                 property="measurementsDataVector" type="oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean" indexId="ctr">
				<tr class="data">                                                  
	                         <td width="20"><bean:write name="data" property="type" /></td>
					<td width="200"><bean:write name="data"
						property="typeDescription" /></td>
					<td width="50"><a href="#"
						name='<bean:message key="oscarEncounter.Index.oldMeasurements"/>'
						onClick="popupPage(300,800,'SetupDisplayHistory.do?type=<bean:write name="data" property="type" />'); return false;">more...</a></td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>
	
	<input type="button" name="Button" value="<bean:message key="global.btnPrint"/>" onClick="window.print()">
	<input type="button" name="Button" value="<bean:message key="global.btnClose"/>" onClick="window.close()">
	<logic:present name="type">
		<input type="hidden" name="type" value="<bean:write name="type" />" />
	</logic:present>
	
	<%
		if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user")))
		{
			MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
			boolean enabledMyOscarButton=MyOscarUtils.isMyOscarSendButtonEnabled(myOscarLoggedInInfo, Integer.valueOf(bean.getDemographicNo()));

			String sendDataPath = request.getContextPath() + "/phr/send_medicaldata_to_myoscar.jsp?"
					+ "demographicId=" + bean.getDemographicNo() + "&"
					+ "medicalDataType=Measurements" + "&"
					+ "parentPage=" + request.getRequestURI(); 
			%>
				<input type="button" name="Button" <%=WebUtils.getDisabledString(enabledMyOscarButton)%> value="<%=LocaleUtils.getMessage(request, "SendToPHR")%>" onclick="document.location.href='<%=sendDataPath%>'">
			<%
		}
	%>
									             	
</div>

</body>
</html:html>
