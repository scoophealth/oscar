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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.consult" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.consult");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.ResourceBundle"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<jsp:useBean id="displayServiceUtil" scope="request"
	class="oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConDisplayServiceUtil" />
<%
displayServiceUtil.estSpecialistVector();
String serviceId = (String) request.getAttribute("serviceId");
String serviceDesc = displayServiceUtil.getServiceDesc(serviceId);
%>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.oscarConsultationRequest.config.DisplayService.title" />
</title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}
</script>
<link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
<body class="BodyStyle" vlink="#0000FF">
<html:errors />
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn">Consultation</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td class="Header"><bean:message
					key="oscarEncounter.oscarConsultationRequest.config.DisplayService.title" />
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="vertical-align: top">
		<td class="MainTableLeftColumn">
		<%oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar titlebar = new oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConTitlebar();
                    out.print(titlebar.estBar(request));
                 %>
		</td>
		<td class="MainTableRightColumn">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
				<td><bean:message
					key="oscarEncounter.oscarConsultationRequest.config.DisplayService.msgCheckOff"
					arg0="<%=serviceDesc %>" /></td>
			</tr>
			<tr>
				<td><html:form
					action="/oscarEncounter/UpdateServiceSpecialists">
					<input type="hidden" name="serviceId" value="<%=serviceId %>">
					<input type="submit"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.config.DisplayService.btnUpdateServices"/>">
					<div class="ChooseRecipientsBox1">
					<table>
						<tr>
							<th>&nbsp;</th>
							<th><bean:message
								key="oscarEncounter.oscarConsultationRequest.config.DisplayService.specialist" />
							</th>
							<th><bean:message
								key="oscarEncounter.oscarConsultationRequest.config.DisplayService.address" />
							</th>
							<th><bean:message
								key="oscarEncounter.oscarConsultationRequest.config.DisplayService.phone" />
							</th>
							<th><bean:message
								key="oscarEncounter.oscarConsultationRequest.config.DisplayService.fax" />
							</th>

						</tr>
						<tr>
							<td><!--<div class="ChooseRecipientsBox1">--> <%
                                 java.util.Vector  specialistInField = displayServiceUtil.getSpecialistInField(serviceId);
                                 for(int i=0;i < displayServiceUtil.specIdVec.size(); i++){
                                 String  specId     = displayServiceUtil.specIdVec.elementAt(i);
                                 String  fName      = displayServiceUtil.fNameVec.elementAt(i);
                                 String  lName      = displayServiceUtil.lNameVec.elementAt(i);
                                 String  proLetters = displayServiceUtil.proLettersVec.elementAt(i);
                                 String  address    = displayServiceUtil.addressVec.elementAt(i);
                                 String  phone      = displayServiceUtil.phoneVec.elementAt(i);
                                 String  fax        = displayServiceUtil.faxVec.elementAt(i);

                              %>
							
						<tr>
							<td>
							<%if (specialistInField.contains(specId)){ %> <input type=checkbox
								name="specialists" value=<%=specId%> checked> <%}else{%>
							<input type=checkbox name="specialists" value=<%=specId%>>
							<%}%>
							</td>
							<td>
							<% out.print(lName+" "+fName + (proLetters == null ? "" : " " + proLetters)); %>
							</td>
							<td><%=address %></td>
							<td><%=phone%></td>
							<td><%=fax%></td>
						</tr>
						<% }%>
						<!--</div>-->
						</td>
						</tr>
					</table>
					</div>
				</html:form></td>
			</tr>
			<!----End new rows here-->

			<tr height="100%">
				<td></td>
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
