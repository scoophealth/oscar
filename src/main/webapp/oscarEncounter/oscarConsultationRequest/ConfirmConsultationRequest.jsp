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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>



<%@page import="org.oscarehr.util.WebUtils"%><html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.title" />
</title>
<html:base />

</head>
<script language="javascript">
function BackToOscar() {
       window.close();
}

function finishPage(secs){
    setTimeout("window.close()",secs*500);    
    //window.opener.location.reload();    
}

</script>


<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="finishPage(5);">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Consultation</td>
		<td class="MainTableTopRowRightColumn"></td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn" width="10%">&nbsp;</td>
		<td class="MainTableRightColumn">
		<table width="100%" height="100%">
			<tr>
				<td><bean:message
					key="oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgConsReq" />
				<%
                            String type = request.getParameter("transType") == null ? (String) request.getAttribute("transType") : request.getParameter("transType");
                            if (type.equals("1")){ %> <bean:message
					key="oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgUpdated" />
				<% }else if (type.equals("2")){ %> <bean:message
					key="oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgCreated" />
				<% } %>
				
				<%=WebUtilsOld.popInfoMessagesAsHtml(session)%>
				</td>
			</tr>
			<tr>
				<td><bean:message
					key="oscarEncounter.oscarConsultationRequest.ConfirmConsultationRequest.msgClose5Sec" />
				</td>
			</tr>
			<tr>
				<td><a href="javascript: BackToOscar();"> <bean:message
					key="global.btnClose" /> </a></td>
			</tr>
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
