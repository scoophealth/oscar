<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@page import="org.oscarehr.util.WebUtilsOld"%>
<%@page import="oscar.eform.actions.FaxAction"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@page import="org.oscarehr.util.WebUtils,org.oscarehr.util.MiscUtils"%>
<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Rich Text Letter Fax
</title>
<html:base />

</head>
<script language="javascript">
function BackToOscar() {
       window.close();
}

function finishPage(secs){
    setTimeout("window.close()",secs*500);
}

</script>


<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="finishPage(5);">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">E From</td>
		<td class="MainTableTopRowRightColumn"></td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn" width="10%">&nbsp;</td>
		<td class="MainTableRightColumn">
		<table width="100%" height="100%">
			<tr>
				<td>
				<%
					
				String id  = (String)request.getAttribute("fdid");
				String[] s = request.getParameterValues("faxRecipients");
				String providerId = request.getParameter("providerId");
				FaxAction bean=new FaxAction(request);
				try { 
					bean.faxForms(s,id,providerId);
					%>
					Fax has been sent successfully.	
					<%
				}
				catch (Exception e) {
					MiscUtils.getLogger().error("",e);
					
					%>
					An error occurred sending the fax, please contact an administrator.
					<%
				}
				%>
				
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

