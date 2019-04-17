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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.managers.DemographicManager"%>
<%@page import="org.oscarehr.common.service.myoscar.MyOscarMedicalDataManagerUtils"%>
<%@page import="org.oscarehr.util.WebUtils"%>
<%@page import="org.oscarehr.util.LocaleUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	DemographicManager demographicManager=(DemographicManager)SpringUtils.getBean("demographicManager");

	String medicalDataType = request.getParameter("medicalDataType");
	String parentPage = request.getParameter("parentPage");
	String demographicNoString=request.getParameter("demographicId");
	
	String sendDataPath = "send_medicaldata_to_myoscar_action.jsp?"
							+ "medicalDataType=" + medicalDataType + "&"
							+ "parentPage=" + parentPage + "&"
							+ "demographicId=" + demographicNoString;

	try
	{
		if(demographicManager.getPhrVerificationLevelByDemographicId(loggedInInfo, Integer.parseInt(demographicNoString))){
			response.sendRedirect(sendDataPath);
		}
	}
	catch (Exception e)
	{
		MiscUtils.getLogger().error("error", e);
		WebUtils.addErrorMessage(session, LocaleUtils.getMessage(request,"UnexpectedError"));
		response.sendRedirect(parentPage);
	}
%>

<html>
<head>
	<title>
		<%=LocaleUtils.getMessage(request, "ConfirmSending")%>&nbsp;<%=LocaleUtils.getMessage(request, medicalDataType)%>&nbsp;<%=LocaleUtils.getMessage(request, "ToPHR")%>
	</title>
</head>
<body>
	<p>
	<h3><%=LocaleUtils.getMessage(request, "WarningNotVerified")%></h3>
	<h3><%=LocaleUtils.getMessage(request, "AreYouSureYouWantToSend")%>&nbsp;<%=LocaleUtils.getMessage(request, medicalDataType)%>&nbsp;<%=LocaleUtils.getMessage(request, "ToPHR")%>?</h3>
	<p>
	<input type="button" value="Yes" onclick="window.location.href='<%=sendDataPath%>'" />
	<p>
	<input type="button" value="No" onclick="window.location.href='<%=parentPage%>'" />
</body>
</html>
