<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<!--
 ../oscarSurveillance/CheckSurveillance.do?demographicNo=2&proceed=..%2FoscarEncounter%2FIncomingEncounter.do%3FproviderNo%3D999998%26appointmentNo%3D0%26demographicNo%3D2%26curProviderNo%3D999998%26reason%3D%26encType%3Dface%2Bto%2Bface%2Bencounter%2Bwith%2Bclient%26userName%3Ddoctor%2Boscardoc%26curDate%3D2010-3-3%26appointmentDate%3D2010-3-3%26startTime%3D14%3A49%26status%3DT%26source%3Dcm

-->
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Demographic demographic = null;

	String strProgramId = request.getParameter("programId");
	int programId = 0;
	try {
		programId = Integer.parseInt(strProgramId);
	} catch(NumberFormatException e) {}

	int clientId = 0;
	if(programId == 0) {
		//error has occured
	} else {
		demographic = org.oscarehr.PMmodule.web.CreateAnonymousClientAction.generatePEClient(loggedInInfo.getLoggedInProviderNo(),programId);
	}

	Provider provider = loggedInInfo.getLoggedInProvider();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String curDate = sdf.format(new java.util.Date());
	java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("kk:mm");
	String curTime = sdf2.format(new java.util.Date());

	response.sendRedirect("../oscarSurveillance/CheckSurveillance.do?demographicNo="+demographic.getDemographicNo()+"&proceed=..%2FoscarEncounter%2FIncomingEncounter.do%3FproviderNo%3D"+provider.getProviderNo()+"%26appointmentNo%3D0%26demographicNo%3D"+demographic.getDemographicNo()+"%26curProviderNo%3D"+provider.getProviderNo()+"%26reason%3D%26encType%3DTelephone%2BEncounter%2Bwith%2Bclient%26userName%3D"+provider.getFormattedName()+"%26curDate%3D"+curDate+"%26appointmentDate%3D"+curDate+"%26startTime%3D"+curTime+"%26status%3DT%26source%3Dcm");

%>